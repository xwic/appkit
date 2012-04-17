/*
 * Copyright (c) 2009 Network Appliance, Inc.
 * All rights reserved.
 */

package de.xwic.appkit.webbase.table;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.xwic.appkit.core.config.Bundle;
import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.config.ConfigurationManager;
import de.xwic.appkit.core.config.list.ListColumn;
import de.xwic.appkit.core.config.list.ListSetup;
import de.xwic.appkit.core.config.model.Property;
import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.daos.IMitarbeiterDAO;
import de.xwic.appkit.core.model.daos.IUserViewConfigurationDAO;
import de.xwic.appkit.core.model.entities.IMitarbeiter;
import de.xwic.appkit.core.model.entities.IUserViewConfiguration;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.core.model.queries.QueryElement;
import de.xwic.appkit.webbase.core.Platform;
import de.xwic.appkit.webbase.prefstore.IPreferenceStore;
import de.xwic.appkit.webbase.table.Column.Sort;
import de.xwic.appkit.webbase.table.ColumnsConfigurationUtil.ColumnConfigurationWrapper;

/**
 * Model for the EntityTable.
 * @author lippisch
 */
public class EntityTableModel {

	private enum EventType {
		COLUMN_SORT_CHANGE, 
		COLUMN_FILTER_CHANGE,
		COLUMN_REORDER,
		BEFORE_USER_CONFIGURATION_CHANGE,
		USER_CONFIGURATION_CHANGE,
		USER_CONFIGURATION_DELETED,
		NEW_USER_CONFIGURATION_CREATED
	}
	
	private final static Log log = LogFactory.getLog(EntityTableModel.class);
	
	private final static String PREF_STORE_KEY = "de.xwic.appkit.webbase.UserViewConfiguration";
	
	private final Class<? extends IEntity> entityClazz;
	private ListSetup listSetup;
	private String viewId;
	private EntityTableConfiguration configuration;

	private List<Column> columns;
	private Map<String, Column> idMapColumns;
	private Map<String, Integer> propertyDataIndex = new HashMap<String, Integer>();
	private Bundle bundle;
	
	private PropertyQuery query = null;
	private PropertyQuery baseFilter = null;
	private PropertyQuery customQuickFilter = null;
	
	private CurrentConfigurationWrapper currentConfig;
	
	private List<IEntityTableListener> listeners = new ArrayList<IEntityTableListener>();

	private IPreferenceStore userConfigPrefStore;
	
	/**
	 * @param configuration
	 * @throws ConfigurationException
	 */
	public EntityTableModel(EntityTableConfiguration configuration) throws ConfigurationException {
		this.entityClazz = configuration.getEntityClass();
		
		// first load all the columns from the list setup
		
		this.baseFilter = configuration.getBaseFilter();
		this.viewId = configuration.getViewId();	
		this.configuration = configuration;
		
		try {
			initModel(false);
		} catch (ConfigurationException e) {
			log.error(e);
			throw (e);
		}
		
		// then search for a user configuration and, if found, apply it to the columns
		
		userConfigPrefStore = Platform.getContextPreferenceProvider().getPreferenceStore(PREF_STORE_KEY);
		try {
			int userConfigId = Integer.parseInt(userConfigPrefStore.getString(viewId, "-1"));
			if (userConfigId > 0) {
				applyUserViewConfiguration(userConfigId);		
			}
		} catch (Exception ex) {
		}
		
		// sort to apply user sorting
		Collections.sort(columns);
	}

	/**
	 * @throws ConfigurationException
	 */
	public void initModel(boolean fireEvent) throws ConfigurationException {
		listSetup = ConfigurationManager.getUserProfile().getListSetup(entityClazz.getName(), configuration.getListId());
		bundle = listSetup.getEntityDescriptor().getDomain().getBundle(configuration.getLocale().getLanguage());
		
		currentConfig = null;
		
		PropertyQuery defaultFilter = configuration.getDefaultFilter();
		
		columns = new ArrayList<Column>();
		idMapColumns = new HashMap<String, Column>();
		int c = 0;
		for (ListColumn lc : listSetup.getColumns()) {
			Column col = new Column(configuration.getLocale(), lc, entityClazz);
			col.setColumnOrder(c++);
			columns.add(col);
			idMapColumns.put(col.getId(), col);

			String title = null;
			if (null != bundle) {
				title = bundle.getString(col.getTitleId());
			}

			if (null == title || "!!".equals(title)) {
				title = getResString(col.getListColumn().getFinalProperty());
			}
			col.setTitle(title);
			
			if(defaultFilter != null ){
				if (defaultFilter.getSortField() != null && lc.getPropertyId().equals(defaultFilter.getSortField())) {
					col.setSortState(defaultFilter.getSortDirection() == PropertyQuery.SORT_DIRECTION_DOWN ? Sort.DOWN : Sort.UP);
					defaultFilter.setSortField(null);
				}
				
				if (defaultFilter.size() > 0) {
					for (QueryElement qe : defaultFilter.getElements()) {
						String propName;
						if (qe.getSubQuery() != null && !qe.getSubQuery().getElements().isEmpty()) {
							QueryElement q = qe.getSubQuery().getElements().get(0);
							propName = q.getPropertyName();
						} else {
							propName = qe.getPropertyName();
						}
						if (propName != null) {
							int index = propName.lastIndexOf(".id"); // taking out any ".id"
							if (index > -1) {
								propName = propName.substring(0, index);
							}
							
							if(propName.equals(lc.getPropertyId())){
								col.setFilter(qe);
							}
						}
					}	
				}
			}
		}
		
		if (fireEvent) {
			buildQuery();
			fireEvent(EventType.USER_CONFIGURATION_CHANGE, new EntityTableEvent(this));
		}
	}
	
	/**
	 * @param key
	 * @return the resource String from the bundle
	 */
	public String getResString(Property property) {
		String key = "";
		if (null != property) {
			key = property.getEntityDescriptor().getClassname() + "." + property.getName();
		}
		if (null != bundle) {
			if (!"".equals(key)) {
				return bundle.getString(key);
			}
		} else {
			return "!" + key + "!";
		}

		return "";
	}

	/**
	 * Add a listener.
	 * @param listener
	 */
	public synchronized void addEntityTableListener(IEntityTableListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * Remove a listener.
	 * @param listener
	 * @return true if the listener was found and removed
	 */
	public synchronized boolean removeEntityTableListener(IEntityTableListener listener) {
		return listeners.remove(listener);
	}
	
	/**
	 * Fire the event.
	 * @param type
	 * @param event
	 */
	private void fireEvent(EventType type, EntityTableEvent event) {
		
		IEntityTableListener[] lst = new IEntityTableListener[listeners.size()];
		lst = listeners.toArray(lst);
		
		for (IEntityTableListener listener : lst) {
			
			switch (type) {
			case COLUMN_SORT_CHANGE:
				listener.columnSorted(event);
				break;
			case COLUMN_FILTER_CHANGE:
				listener.columnFiltered(event);
				break;
			case COLUMN_REORDER:
				listener.columnsReordered(event);
				break;
			case BEFORE_USER_CONFIGURATION_CHANGE:
				listener.beforeUserConfigurationChanged(event);
				break;
			case USER_CONFIGURATION_CHANGE:
				listener.userConfigurationChanged(event);
				break;
			case NEW_USER_CONFIGURATION_CREATED:
				listener.newUserConfigurationCreated(event);
				break;
			}
		}
	}
	
	/**
	 * @return the columns
	 */
	public List<Column> getColumns() {
		return columns;
	}
	
	/**
	 * Returns the column with the specified ID.
	 * @param id
	 * @return
	 */
	public Column getColumn(String id) {
		return idMapColumns.get(id);
	}

	/**
	 * @return the listSetupId
	 */
	public ListSetup getListSetup() {
		return listSetup;
	}

	/**
	 * @return the entityClazz
	 */
	public Class<? extends IEntity> getEntityClass() {
		return entityClazz;
	}
	
	/**
	 * @return
	 */
	public String getViewId() {
		return viewId;
	}

	/**
	 * @return the bundle
	 */
	public Bundle getBundle() {
		return bundle;
	}

	/**
	 * @return
	 */
	public EntityQuery getQuery() {
		if (query == null) {
			buildQuery();
		}
		return query;
	}

	/**
	 * 
	 */
	private void buildQuery() {
		
		PropertyQuery q = new PropertyQuery();
		PropertyQuery userFilter = new PropertyQuery();
		
		// build column list

		Set<String> properties = new HashSet<String>();
		for (Column col : columns) {
			ListColumn listColumn = col.getListColumn();
			String prop = listColumn.getPropertyId();
			String[] propList = col.getColumnLabelProvider().getRequiredProperties(listColumn, prop);
			if (col.isVisible()) {
				col.setPropertyIds(propList);
				for (String p : propList) {
					if (!p.startsWith("#")) {
						properties.add(p);
					}
				}
			}
			
			if (col.getFilter() != null) {
				// quick filters have priority, so if the column specifies a filter that is already specified 
				// by the quickfilter, we remove it from the column
				boolean quickFilterExists = false;
				if (customQuickFilter != null && customQuickFilter.size() > 0) {
					for (QueryElement qe : customQuickFilter.getElements()) {
						String propName = qe.getPropertyName();
						if (propName != null) {
							int index = propName.indexOf(".");
							if (index > -1) {
								propName = propName.substring(0, index);
							}

							if (propName.equals(listColumn.getPropertyId())) {
								quickFilterExists = true;
								break;
							}
						}
					}
				}
				
				if (quickFilterExists) {
					col.setFilter(null);
				} else {
					userFilter.addQueryElement(col.getFilter());
				}
				
				
			}
			
			// Apply Sorting
			if (col.getSortState() != Sort.NONE && propList.length > 0 && !propList[0].startsWith("#")) {
				Property property = listColumn.getFinalProperty();
				if (property.isPicklistEntry()) {
					q.setSortField(prop + ".sortIndex");
				} else {
					q.setSortField(propList[0]);
				}
				q.setSortDirection(col.getSortState() == Sort.UP ? PropertyQuery.SORT_DIRECTION_UP : PropertyQuery.SORT_DIRECTION_DOWN);
			}
		}
		
		List<String> selectCols = new ArrayList<String>();
		propertyDataIndex.clear();
		
		int idx = 0;
		for (String p : properties) {
			selectCols.add(p);
			propertyDataIndex.put(p, idx++);
		}
		
		q.setColumns(selectCols);
		
		if (baseFilter != null && baseFilter.size() > 0) {
			q.addSubQuery(baseFilter);
			
			// if we have no filters yet but the base filter has a filter, apply it
			if ((q.getSortField() == null || q.getSortField().isEmpty()) 
					&& baseFilter.getSortField() != null && !baseFilter.getSortField().isEmpty()) {
				q.setSortField(baseFilter.getSortField());
				q.setSortDirection(baseFilter.getSortDirection());
			}
		}
		if (userFilter.size() > 0) {
			q.addSubQuery(userFilter);
		}
		if (customQuickFilter != null && customQuickFilter.size() > 0) {
			q.addSubQuery(customQuickFilter);
		}
		
		query = q;
	}

	/**
	 * Returns the data index for the given property.
	 * @param propertyId
	 * @return
	 */
	public Integer getPropertyDataIndex(String propertyId) {
		return propertyDataIndex.get(propertyId);
	}
	
	/**
	 * @return the baseFilter
	 */
	public PropertyQuery getBaseFilter() {
		return baseFilter;
	}

	/**
	 * @param baseFilter the baseFilter to set
	 */
	public void setBaseFilter(PropertyQuery baseFilter) {
		this.baseFilter = baseFilter;
	}

	/**
	 * @return
	 */
	public PropertyQuery getDefaultFilter() {
		return configuration.getDefaultFilter();
	}
	
	/**
	 * Change sorting for that column
	 * @param column
	 * @param down
	 */
	public void sortColumn(Column column, Sort sort) {
		for (Column col : columns) {
			if (col == column) {
				col.setSortState(sort);
			} else if (col.getSortState() != Column.Sort.NONE) {
				col.setSortState(Column.Sort.NONE);
			}
		}
		buildQuery();
		fireEvent(EventType.COLUMN_SORT_CHANGE, new EntityTableEvent(this, column));
	}

	/**
	 * 
	 */
	public void clearFilters() {
		for (Column col : columns) {
			col.setFilter(null);
			customQuickFilter = null;
		}
		buildQuery();
		fireEvent(EventType.COLUMN_FILTER_CHANGE, new EntityTableEvent(this, null));
	}

	/**
	 * @param filtersMap
	 */
	public void applyQuickFilters(Map<String, QueryElement> filtersMap) {
		// here we will add all filters that don't specify a column
		PropertyQuery customQuickFilter = new PropertyQuery();
		
		for (Entry<String, QueryElement> entry : filtersMap.entrySet()) {
			String columnId = entry.getKey();
			QueryElement queryElement  = entry.getValue();
				
			if (columnId != null && idMapColumns.containsKey(columnId)) {
				Column col = idMapColumns.get(columnId);
				col.setFilter(queryElement);
			} else {
				// Note FLI: Not every queryElement has a subQuery - so add the whole element...
				if (queryElement != null) {
					customQuickFilter.addQueryElement(queryElement);
				}
			}
		}
		
		this.customQuickFilter = customQuickFilter;
		
		buildQuery();
		fireEvent(EventType.COLUMN_FILTER_CHANGE, new EntityTableEvent(this, null));
	}
	
	/**
	 * @param column
	 * @param queryElement
	 */
	public void updateFilter(Column column, QueryElement queryElement) {
		column.setFilter(queryElement);
		buildQuery();
		fireEvent(EventType.COLUMN_FILTER_CHANGE, new EntityTableEvent(this, column));
	}
	
	
	/**
	 * Needs to be invoked after the columns order or visibility has changed.
	 */
	public void applyColumnReorder() {
		
		Collections.sort(columns);
		buildQuery();
		fireEvent(EventType.COLUMN_REORDER, new EntityTableEvent(this));
		
	}
	
	// *********** USER CONFIGURATION METHODS ***********
	
	/**
	 * @return the maxRows
	 */
	public int getMaxRows() {
		int maxRows = currentConfig != null ? currentConfig.maxRows : 0; 
		return maxRows < 0 ? 0 : maxRows;
	}
	
	/**
	 * @return
	 */
	public int getCurrentUserConfigurationId() {
		return currentConfig != null ? currentConfig.id : -1;
	}
	
	/**
	 * @return
	 */
	public String getCurrentUserConfigurationName() {
		return currentConfig != null ? currentConfig.name : "default";
	}
	
	/**
	 * @return
	 */
	public IUserViewConfiguration cloneUserViewConfiguration(int userConfigId) {
		DAO dao = DAOSystem.getDAO(IUserViewConfigurationDAO.class);
		
		IUserViewConfiguration newUvc = createNewUserViewConfiguration(false);		
		IUserViewConfiguration existentUvc = (IUserViewConfiguration) dao.getEntity(userConfigId);
		
		String name = existentUvc.getName(); 
		
		List<IUserViewConfiguration> allUVCs = getMyUserConfigurationsForCurrentView(); 
		
		boolean exists = true;
		int index = 1;
		String tempName = name;
		while (exists) {
			boolean inWhile = false;
			for (IUserViewConfiguration uvc : allUVCs) {
				if (uvc.getName().equals(tempName)) {
					tempName = name + "_" + index;
					index++;
					inWhile = true;
					break;
				}
			}

			exists = inWhile;
		}
		
		name = tempName;
		
		newUvc.setName(name);		
		newUvc.setDescription(existentUvc.getDescription());
		//newUvc.setPublic(currentUvc.isPublic()); don't clone the Public flag!!
		newUvc.setColumnsConfiguration(existentUvc.getColumnsConfiguration());
		newUvc.setSortField(existentUvc.getSortField());
		newUvc.setSortDirection(existentUvc.getSortDirection());
		newUvc.setMaxRows(existentUvc.getMaxRows());			
		
		return newUvc;
	}

	/**
	 * @param name
	 * @param currentId
	 * @return
	 */
	public boolean nameAlreadyExists(String name, int currentId) {
		PropertyQuery pq = new PropertyQuery();
		pq.addEquals("owner", ((IMitarbeiterDAO) DAOSystem.getDAO(IMitarbeiterDAO.class)).getByCurrentUser());
		pq.addEquals("className", entityClazz.getName());
		pq.addEquals("viewId", viewId);
		pq.addEquals("name", name);
		pq.addNotEquals("id", currentId);

		return !DAOSystem.getDAO(IUserViewConfigurationDAO.class).getEntities(null, pq).isEmpty();
	}
	
	/**
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<IUserViewConfiguration> getMyUserConfigurationsForCurrentView() {
		PropertyQuery pq = new PropertyQuery();
		pq.addEquals("owner", ((IMitarbeiterDAO) DAOSystem.getDAO(IMitarbeiterDAO.class)).getByCurrentUser());
		pq.addEquals("className", entityClazz.getName());
		pq.addEquals("viewId", viewId);
		
		return DAOSystem.getDAO(IUserViewConfigurationDAO.class).getEntities(null, pq);
	}
	
	/**
	 * @param firstUserConfig
	 * @return
	 */
	public IUserViewConfiguration createNewUserViewConfiguration(boolean firstUserConfig) {
		DAO dao = DAOSystem.getDAO(IUserViewConfigurationDAO.class);
		
		IUserViewConfiguration uvc = (IUserViewConfiguration) dao.createEntity();
		
		uvc.setOwner(((IMitarbeiterDAO) DAOSystem.getDAO(IMitarbeiterDAO.class)).getByCurrentUser());
		uvc.setClassName(entityClazz.getName());
		uvc.setViewId(viewId);			
		uvc.setListSetupId(listSetup.getListId());
		uvc.setName(listSetup.getListId());
		
		if (firstUserConfig) {
			dao.update(uvc);
			currentConfig = new CurrentConfigurationWrapper(uvc);
			updateUserConfigInStore(uvc.getId());
			
			fireEvent(EventType.NEW_USER_CONFIGURATION_CREATED, new EntityTableEvent(this));
		}
		
		return uvc;
	}
	
	/**
	 * @param userConfigId
	 */
	public void deleteUserViewConfiguration(IUserViewConfiguration userConfig) {
		if (userConfig.getId() > 0) {
			DAOSystem.getDAO(IUserViewConfigurationDAO.class).softDelete(userConfig);
		}
	}
	
	/**
	 * @param userConfigId
	 */
	public void applyUserViewConfiguration(int userConfigId) {
		if (currentConfig != null && currentConfig.id == userConfigId) {
			return;
		}
		
		IUserViewConfiguration uvc = (IUserViewConfiguration) DAOSystem.getDAO(IUserViewConfigurationDAO.class).getEntity(userConfigId);
		
		if (uvc != null && !uvc.isDeleted()) {
			applyUserViewConfiguration(uvc);
		}
	}
	
	/**
	 * @param userConfiguration
	 */
	public void applyUserViewConfiguration(IUserViewConfiguration userConfiguration) {
		fireEvent(EventType.BEFORE_USER_CONFIGURATION_CHANGE, new EntityTableEvent(this));
		
		ColumnsConfigurationUtil util = new ColumnsConfigurationUtil(userConfiguration);
		
		for (Column col : columns) {
			ColumnConfigurationWrapper colConfig = util.getColumnConfiguration(col.getId());
			// if it's null, it means it's a new column, which did not exist
			// when the user configuration was created. We display it by
			// default, so the users see it exists, they can take it out after
			if (colConfig == null) {
				col.setVisible(true);
				continue;
			}
			
			col.setWidth(colConfig.size);
			col.setVisible(colConfig.visible);
			col.setColumnOrder(colConfig.index);
			
			if (col.getId().equals(util.getSortField())) {
				col.setSortState(util.getSortDirection());
			} else {
				col.setSortState(Sort.NONE);
			}
		}
		
		buildQuery();
		
		this.currentConfig = new CurrentConfigurationWrapper(userConfiguration);
		
		// if it's a public profile that doesn't belong to the current user, don't put it in the preference store
		if (isCurrentConfigurationMine()) {
			updateUserConfigInStore(userConfiguration.getId());
		}
		
		fireEvent(EventType.USER_CONFIGURATION_CHANGE, new EntityTableEvent(this));
	}
	

	/**
	 * @param newMaxRows
	 */
	public void storeUserViewConfiguration(int newMaxRows) {
		DAO dao = DAOSystem.getDAO(IUserViewConfigurationDAO.class);
		
		IUserViewConfiguration uvc = null;

		boolean isNew = false;
		
		if (currentConfig != null) {
			// if it's a public profile that doesn't belong to the current user, don't save it
			if (!isCurrentConfigurationMine()) {
				return;
			}
			
			uvc = (IUserViewConfiguration) dao.getEntity(currentConfig.id);
		} else {
			uvc = createNewUserViewConfiguration(false);
			isNew = true;
		}

		// just in case.. paranoid?
		if (uvc == null) {
			return;
		}
		
		boolean changed = false;
		String sortField = "";
		String sortDirection = "";		
		StringBuilder sbColumnsConfiguration = new StringBuilder();
		
		for (Column col : columns) {
			if (sbColumnsConfiguration.length() > 0) {
				sbColumnsConfiguration.append(";");
			}
			
			sbColumnsConfiguration.append(col.getId()).append(",");
			sbColumnsConfiguration.append(col.getWidth()).append(",");
			sbColumnsConfiguration.append(col.isVisible()).append(",");
			sbColumnsConfiguration.append(col.getColumnOrder());
			
			if (col.getSortState() != Sort.NONE) {
				sortField = col.getId();
				sortDirection = col.getSortState().name();
			}
		}
		
		String columnsConfiguration = sbColumnsConfiguration.toString();
		if (isNew || !columnsConfiguration.equals(uvc.getColumnsConfiguration())) {
			uvc.setColumnsConfiguration(columnsConfiguration);
			changed = true;
		}
		
		if (isNew || !sortField.equals(uvc.getSortField())) {
			uvc.setSortField(sortField);
			changed = true;
		}
		
		if (isNew || !sortDirection.equals(uvc.getSortDirection())) {
			uvc.setSortDirection(sortDirection);
			changed = true;
		}
		
		if (isNew || newMaxRows != uvc.getMaxRows()) {
			uvc.setMaxRows(newMaxRows);
			changed = true;
		}
		
		if (changed) {
			dao.update(uvc);
		}
		
		if (isNew) {
			updateUserConfigInStore(uvc.getId());
		}
	}
	
	/**
	 * @param uvc
	 * @return
	 */
	public boolean isCurrentConfigurationMine() {
		if (currentConfig != null) {
			// if it's a public profile that doesn't belong to the current user, don't save it
			IMitarbeiter mit = ((IMitarbeiterDAO) DAOSystem.getDAO(IMitarbeiterDAO.class)).getByCurrentUser();
			if (currentConfig.id > 0 && currentConfig.isPublic && currentConfig.ownerId != mit.getId()) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * 
	 */
	private void updateUserConfigInStore(int userConfigurationId) {
		// if it's different from what we have in the user's store, update it
		String oldUserConfig = userConfigPrefStore.getString(viewId, "");
		if (!String.valueOf(userConfigurationId).equals(oldUserConfig)) {
			userConfigPrefStore.setValue(viewId, String.valueOf(userConfigurationId));
			try {
				userConfigPrefStore.flush();
			} catch (IOException e) {
				throw new RuntimeException("Error while saving preferences: " + e.getMessage(), e);
			}
		}
	}
	
	/**
	 * @author Adrian Ionescu
	 */
	private class CurrentConfigurationWrapper {

		private int id;
		private int maxRows;
		private String name;
		private boolean isPublic;
		private int ownerId;
		
		/**
		 * @param config
		 */
		public CurrentConfigurationWrapper(IUserViewConfiguration config) {
			id = config.getId();
			maxRows = config.getMaxRows();
			name = config.getName();
			isPublic = config.isPublic();
			ownerId = config.getOwner().getId();
		}
	}
}
