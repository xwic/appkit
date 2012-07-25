/*
 * Copyright (c) 2009 Network Appliance, Inc.
 * All rights reserved.
 */

package de.xwic.appkit.webbase.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.xwic.appkit.core.config.Bundle;
import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.config.ConfigurationManager;
import de.xwic.appkit.core.config.list.ListColumn;
import de.xwic.appkit.core.config.list.ListSetup;
import de.xwic.appkit.core.config.model.Property;
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
		USER_CONFIGURATION_CHANGED,
		NEW_USER_CONFIGURATION_CREATED,
		USER_CONFIGURATION_DIRTY_CHANGED,		
		USER_CONFIGURATION_DELETED
	}
	
	private final static Log log = LogFactory.getLog(EntityTableModel.class);
	
	public final static String USER_CUSTOMIZED_NAME = "User Customized";
	public final static String DEFAULT_PROFILE_NAME = "Default Profile";
	
	private final Class<? extends IEntity> entityClazz;
	private ListSetup listSetup;
	private String viewId;
	private EntityTableConfiguration entityTableConfiguration;

	private List<Column> columns;
	private Map<String, Column> idMapColumns;
	private Map<String, Integer> propertyDataIndex = new HashMap<String, Integer>();
	private Bundle bundle;
	
	private PropertyQuery query = null;
	private PropertyQuery baseFilter = null;
	private PropertyQuery customQuickFilter = null;
	
	private List<IEntityTableListener> listeners = new ArrayList<IEntityTableListener>();

	private IUserViewConfiguration mainConfig;
	private IUserViewConfigurationDAO userConfigDao = (IUserViewConfigurationDAO) DAOSystem.getDAO(IUserViewConfigurationDAO.class);
	private boolean userConfigDirty = false;
	private IMitarbeiter currentUser;
	private int newMaxRows;
	
	/**
	 * @param configuration
	 * @throws ConfigurationException
	 */
	public EntityTableModel(EntityTableConfiguration configuration) throws ConfigurationException {
		this.entityClazz = configuration.getEntityClass();
		
		this.baseFilter = configuration.getBaseFilter();
		this.viewId = configuration.getViewId();	
		this.entityTableConfiguration = configuration;
		
		this.currentUser = ((IMitarbeiterDAO) DAOSystem.getDAO(IMitarbeiterDAO.class)).getByCurrentUser();
		
		try {
			initModel(false);
		} catch (ConfigurationException e) {
			log.error(e);
			throw (e);
		}
		
		initUserConfig();
		
		// sort to apply user sorting
		Collections.sort(columns);
	}

	/**
	 * @param fireConfigChangedEvent
	 * @throws ConfigurationException
	 */
	public void initModel(boolean fireConfigChangedEvent) throws ConfigurationException {
		listSetup = ConfigurationManager.getUserProfile().getListSetup(entityClazz.getName(), entityTableConfiguration.getListId());
		bundle = listSetup.getEntityDescriptor().getDomain().getBundle(entityTableConfiguration.getLocale().getLanguage());
		
		PropertyQuery defaultFilter = entityTableConfiguration.getDefaultFilter();
		
		columns = new ArrayList<Column>();
		idMapColumns = new HashMap<String, Column>();
		int c = 0;
		for (ListColumn lc : listSetup.getColumns()) {
			Column col = new Column(entityTableConfiguration.getLocale(), lc, entityClazz);
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
		
		if (fireConfigChangedEvent) {
			buildQuery();
			fireEvent(EventType.USER_CONFIGURATION_CHANGED, new EntityTableEvent(this));
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
				setConfigDirty(true);
				break;
			case COLUMN_FILTER_CHANGE:
				listener.columnFiltered(event);
				break;
			case COLUMN_REORDER:
				listener.columnsReordered(event);
				setConfigDirty(true);
				break;
			case USER_CONFIGURATION_CHANGED:
				listener.userConfigurationChanged(event);
				break;
			case NEW_USER_CONFIGURATION_CREATED:
				listener.newUserConfigurationCreated(event);
				setConfigDirty(true);
				break;
			case USER_CONFIGURATION_DIRTY_CHANGED:
				listener.userConfigurationDirtyChanged(event);
				break;
			case USER_CONFIGURATION_DELETED:
				// nothing
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
	 * @return
	 */
	public IMitarbeiter getCurrentUser() {
		return currentUser;
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
		return entityTableConfiguration.getDefaultFilter();
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
	 * 
	 */
	private void initUserConfig() {
		
		mainConfig = userConfigDao.getMainConfigurationForView(currentUser, entityClazz.getName(), viewId);
		
		// the preference store is no longer used for view configurations, but since we can't migrate everything at once,
		// the migration will be done one view at a time, when the user accesses it
		
		if (mainConfig == null) {
			
			// look in the preference store, maybe the user created a config for this view
			
			try {
				IPreferenceStore userConfigPrefStore = Platform.getContextPreferenceProvider().getPreferenceStore("de.xwic.appkit.webbase.UserViewConfiguration");
				int userConfigId = Integer.parseInt(userConfigPrefStore.getString(viewId, "-1"));
				if (userConfigId > 0) {
					IUserViewConfiguration uvc = (IUserViewConfiguration) userConfigDao.getEntity(userConfigId);
					if (uvc != null) {
						mainConfig = createNewConfig(DEFAULT_PROFILE_NAME);
						mainConfig.setMainConfiguration(true);
						mainConfig.setRelatedConfiguration(uvc);
						
						transferDataToMainConfig(uvc);
					}
				}
			} catch (Exception ex) {
				log.error(ex.getMessage(), ex);
			}	
			
		}
		
		// if no config exists, we create one which will be the main
		if (mainConfig == null) {
			createDefaultMainConfig();
			// no need to apply it again, as it contains the default values
		} else {
			applyConfig(mainConfig);
		}
	}
	
	/**
	 * 
	 */
	private void createDefaultMainConfig() {
		mainConfig = createNewConfig(DEFAULT_PROFILE_NAME);
		mainConfig.setMainConfiguration(true);
		mainConfig.setRelatedConfiguration(null);
		
		storeCurrentData(mainConfig, true);
	}
	
	/**
	 * @param name 
	 * @return
	 */
	private IUserViewConfiguration createNewConfig(String name) {
		IUserViewConfiguration uvc = (IUserViewConfiguration) userConfigDao.createEntity();
		
		uvc.setOwner(currentUser);
		uvc.setClassName(entityClazz.getName());
		uvc.setViewId(viewId);			
		uvc.setListSetupId(listSetup.getListId());
		uvc.setName(name);
		
		return uvc;
	}
	
	/**
	 * @param existentUvc
	 */
	private void transferDataToMainConfig(IUserViewConfiguration existentUvc) {
		
		mainConfig = (IUserViewConfiguration) userConfigDao.getEntity(mainConfig.getId());
		
		mainConfig.setName(existentUvc.getName());
		mainConfig.setDescription(existentUvc.getDescription());
		//newUvc.setPublic(currentUvc.isPublic()); not the Public flag!!
		mainConfig.setColumnsConfiguration(existentUvc.getColumnsConfiguration());
		mainConfig.setSortField(existentUvc.getSortField());
		mainConfig.setSortDirection(existentUvc.getSortDirection());
		mainConfig.setMaxRows(existentUvc.getMaxRows());
		
		mainConfig.setRelatedConfiguration(existentUvc);
		
		userConfigDao.update(mainConfig);
	}
	
	/**
	 * @param uvc
	 * @param updateDb
	 */
	private void storeCurrentData(IUserViewConfiguration uvc, boolean updateDb) {
		
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
		if (!columnsConfiguration.equals(uvc.getColumnsConfiguration())) {
			uvc.setColumnsConfiguration(columnsConfiguration);
			changed = true;
		}
		
		if (!sortField.equals(uvc.getSortField())) {
			uvc.setSortField(sortField);
			changed = true;
		}
		
		if (!sortDirection.equals(uvc.getSortDirection())) {
			uvc.setSortDirection(sortDirection);
			changed = true;
		}
		
		if (newMaxRows != uvc.getMaxRows()) {
			uvc.setMaxRows(newMaxRows);
			changed = true;
		}
		
		if (updateDb && changed) {
			userConfigDao.update(uvc);
		}
	}
	
	/**
	 * 
	 */
	private void deleteMainConfig() {
		
		if (mainConfig == null) {
			return;
		}
		
		IUserViewConfiguration related = mainConfig.getRelatedConfiguration();
		
		userConfigDao.delete(mainConfig);
		
		if (related != null) {
			userConfigDao.delete(related);
		}
	}
	
	/**
	 * 
	 */
	public void resetConfig() {
		try {							
			initModel(true);
			deleteMainConfig();
			
			newMaxRows = 0;
			
			createDefaultMainConfig();
			
			fireEvent(EventType.USER_CONFIGURATION_CHANGED, new EntityTableEvent(this));
		} catch (ConfigurationException e) {
			throw new RuntimeException("Can not create EntityTable: " + e, e);
		}
	}
	
	/**
	 * @return
	 */
	public IUserViewConfiguration createConfigWithCurrentSettings() {
		IUserViewConfiguration uvc = createNewConfig(mainConfig.getName());
		storeCurrentData(uvc, false);
		
		return uvc;		
	}
	
	/**
	 * @param userConfig 
	 * 
	 */
	public IUserViewConfiguration updateConfig(IUserViewConfiguration config, String name, String description, boolean isPublic) {
		
		if (config.getId() > 0) {
			// refresh the entity, unless it's a new one
			config = (IUserViewConfiguration) userConfigDao.getEntity(config.getId());
		}
		
		config.setName(name);
		config.setDescription(description);
		config.setPublic(isPublic);
		
		userConfigDao.update(config);
		
		if (getCurrentConfigId() == config.getId()) {
			
			mainConfig = (IUserViewConfiguration) userConfigDao.getEntity(mainConfig.getId());

			// check again, maybe it was updated in the meantime (two browser windows?)
			if (mainConfig.getRelatedConfiguration() != null && mainConfig.getRelatedConfiguration().getId() == config.getId()) {
				mainConfig.setName(name);
				mainConfig.setDescription(description);
				
				userConfigDao.update(mainConfig);
				
				// fire the event to trigger the button title change
				fireEvent(EventType.USER_CONFIGURATION_DIRTY_CHANGED, new EntityTableEvent(this));
			}
		}
		
		return config;
	}
	
	/**
	 * 
	 */
	public void saveCurrentDataToMainConfig() {
		mainConfig = (IUserViewConfiguration) userConfigDao.getEntity(mainConfig.getId());
		storeCurrentData(mainConfig, true);
	}
	
	/**
	 * 
	 */
	public void updateRelatedConfig(boolean undoChanges) {
		
		if (mainConfig.getRelatedConfiguration() == null) {
			
			// if we're still on the default config, reset it all			
			if (undoChanges) {
				resetConfig();
			}
			
			return;
		}
			
		IUserViewConfiguration relatedConfig = (IUserViewConfiguration) userConfigDao.getEntity(mainConfig.getRelatedConfiguration().getId());
		
		if (!undoChanges) {
			storeCurrentData(relatedConfig, true);
		}
		
		transferDataToMainConfig(relatedConfig);
		
		if (undoChanges) {
			// if we undid the changes, we need to re-apply the config
			applyConfig(mainConfig);
		}
		
		setConfigDirty(false);
	}
	
	/**
	 * @param name
	 * @param currentId
	 * @return
	 */
	public boolean configNameExists(String name, int currentId) {
		return userConfigDao.configNameExists(currentUser, entityClazz.getName(), viewId, name, currentId);
	}
	
	/**
	 * @param userConfigId
	 */
	public void deleteConfig(int id) {
		if (id > 0) {
			IEntity uvc = userConfigDao.getEntity(id);
			if (uvc != null) {
				userConfigDao.delete(uvc);
			}
		}
	}

	/**
	 * @param userConfig
	 */
	public void applyConfig(IUserViewConfiguration userConfig) {
		
		if (!userConfig.isMainConfiguration()) {
			// applying a configuration actually means copying its values to the main configuration 
			
			transferDataToMainConfig(userConfig);
			applyConfig(mainConfig);
			
		} else {
			
			ColumnsConfigurationUtil util = new ColumnsConfigurationUtil(userConfig);
			
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
			
			newMaxRows = userConfig.getMaxRows();
			
			buildQuery();
			
			fireEvent(EventType.USER_CONFIGURATION_CHANGED, new EntityTableEvent(this));
		}
	}
	
	/**
	 * @return
	 */
	public boolean isCurrentConfigDirty() {
		
		// if the user has changed something or this is a modified default config
		
		if (userConfigDirty || (mainConfig.getRelatedConfiguration() == null && mainConfig.getName().equals(USER_CUSTOMIZED_NAME)) ) {
			return true;
		}
		
		IUserViewConfiguration relatedConfig = mainConfig.getRelatedConfiguration();
		
		if (relatedConfig != null) {
			if (mainConfig.getMaxRows() != relatedConfig.getMaxRows()) {
				return true;
			}
			
			if (mainConfig.getSortField() != null && !mainConfig.getSortField().equals(relatedConfig.getSortField())) {
				return true;
			}
			
			if (mainConfig.getSortDirection() != null && !mainConfig.getSortDirection().equals(relatedConfig.getSortDirection())) {
				return true;
			}
			
			if (mainConfig.getColumnsConfiguration() != null && !mainConfig.getColumnsConfiguration().equals(relatedConfig.getColumnsConfiguration())) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * @param dirty
	 */
	public void setConfigDirty(boolean dirty) {
		
		userConfigDirty = dirty;
		
		if (dirty && mainConfig.getName().equals(DEFAULT_PROFILE_NAME)) {
			mainConfig.setName(USER_CUSTOMIZED_NAME);
			userConfigDao.update(mainConfig);
		}
		
		fireEvent(EventType.USER_CONFIGURATION_DIRTY_CHANGED, new EntityTableEvent(this));
	}

	/**
	 * @return the maxRows
	 */
	public int getMaxRows() {
		return mainConfig.getMaxRows() < 0 ? 0 : mainConfig.getMaxRows();
	}
	
	/**
	 * @param newMaxRows
	 */
	public void setNewMaxRows(int newMaxRows) {
		this.newMaxRows = newMaxRows;
		setConfigDirty(true);
	}
	
	/**
	 * @return
	 */
	public String getCurrentConfigName() {
		return mainConfig.getName();
	}
	
	/**
	 * @return
	 */
	public int getCurrentConfigId() {
		return mainConfig.getRelatedConfiguration() != null ? mainConfig.getRelatedConfiguration().getId() : mainConfig.getId();
	}

	/**
	 * @return
	 */
	public boolean isDefaultConfig() {
		return mainConfig.getName().equals(USER_CUSTOMIZED_NAME);
	}
}