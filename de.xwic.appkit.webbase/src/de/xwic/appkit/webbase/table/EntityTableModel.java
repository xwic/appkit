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
import de.xwic.appkit.core.model.entities.IMitarbeiter;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.core.model.queries.QueryElement;
import de.xwic.appkit.core.util.Equals;
import de.xwic.appkit.webbase.entityviewer.config.UserConfigHandler;
import de.xwic.appkit.webbase.table.Column.Sort;

/**
 * Model for the EntityTable.
 * @author lippisch
 */
public class EntityTableModel {

	public enum EventType {
		COLUMN_SORT_CHANGE, 
		COLUMN_FILTER_CHANGE,
		COLUMN_REORDER,
		USER_CONFIGURATION_CHANGED,
		USER_CONFIGURATION_DIRTY_CHANGED,		
		USER_CONFIGURATION_DELETED
	}
	
	private final static Log log = LogFactory.getLog(EntityTableModel.class);
	
	private final Class<? extends IEntity> entityClazz;
	private ListSetup listSetup;
	private String viewId;
	private EntityTableConfiguration entityTableConfiguration;

	private List<Column> columns;
	private Map<String, Column> idMapColumns;
	private Map<String, Integer> propertyDataIndex = new HashMap<String, Integer>();
	private Bundle bundle;
	private IMitarbeiter currentUser;
	
	private PropertyQuery query = null;
	private PropertyQuery baseFilter = null;
	private PropertyQuery customQuickFilter = null;
	
	private List<IEntityTableListener> listeners = new ArrayList<IEntityTableListener>();
	
	private UserConfigHandler userConfigHandler;
	
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
		
		userConfigHandler = new UserConfigHandler(this);
		
		try {
			initModel(false);
		} catch (ConfigurationException e) {
			log.error(e);
			throw (e);
		}
	}

	/**
	 * @param fireConfigChangedEvent
	 * @throws ConfigurationException
	 */
	public void initModel(boolean fireConfigChangedEvent) throws ConfigurationException {
		listSetup = ConfigurationManager.getUserProfile().getListSetup(entityClazz.getName(), entityTableConfiguration.getListId());
		bundle = listSetup.getEntityDescriptor().getDomain().getBundle(entityTableConfiguration.getLocale().getLanguage());
		
		columns = new ArrayList<Column>();
		idMapColumns = new HashMap<String, Column>();
		int c = 0;
		
		for (ListColumn lc : listSetup.getColumns()) {
			Column col = new Column(entityTableConfiguration.getTimeZone(), entityTableConfiguration.getLocale(), entityTableConfiguration.getDateFormat(), entityTableConfiguration.getTimeFormat(), lc, entityClazz);
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
		}
		
		applyDefaultFilter();

		buildQuery();
		
		if (fireConfigChangedEvent) {
			fireEvent(EventType.USER_CONFIGURATION_CHANGED, new EntityTableEvent(this));
		}
	}
	
	/**
	 * 
	 */
	public void applyDefaultFilter() {
		
		PropertyQuery defaultFilter = entityTableConfiguration.getDefaultFilter();
		
		if(defaultFilter == null ){
			return;
		}
		
		boolean sorted = false;
		
		for (Column col : getColumns()) {
			
			if (!sorted && defaultFilter.getSortField() != null && col.getId().equals(defaultFilter.getSortField())) {
				col.setSortState(defaultFilter.getSortDirection() == PropertyQuery.SORT_DIRECTION_DOWN ? Sort.DOWN : Sort.UP);
				sorted = true;
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
						
						if(propName.equals(col.getId())){
							col.setFilter(qe);
						}
					}
				}	
			}
			
			// fire the event so that any listening quick filter controls updates themselves
			fireEvent(EventType.COLUMN_FILTER_CHANGE, new EntityTableEvent(this, col));
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
	public void fireEvent(EventType type, EntityTableEvent event) {
		
		IEntityTableListener[] lst = new IEntityTableListener[listeners.size()];
		lst = listeners.toArray(lst);

		if (type == EventType.COLUMN_SORT_CHANGE || type == EventType.COLUMN_REORDER) {
			userConfigHandler.setConfigDirty(true);
			userConfigHandler.saveCurrentDataToMainConfig();
		}
		
		// if column is null, means the event came from a quickfilter, in which case we don't update the config
		if (type == EventType.COLUMN_FILTER_CHANGE && 
				(event.getColumn() != null || event.isClearedFilters() || event.isColumnFilterSetFromQuickFilter())) {
			userConfigHandler.setConfigDirty(true);
			userConfigHandler.saveCurrentDataToMainConfig();
		}
		
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
			case USER_CONFIGURATION_CHANGED:
				listener.userConfigurationChanged(event);
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
	 * @return the bundle
	 */
	public Bundle getBundle() {
		return bundle;
	}

	/**
	 * @return
	 */
	public IMitarbeiter getCurrentUser() {
		return currentUser;
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
	public void buildQuery() {
		
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
			
			// if we have no sorting yet but the base filter has a sorting, apply it
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
		
		boolean rebuild = false;
		
		for (Column col : columns) {
			if (col.getFilter() != null) {
				col.setFilter(null);
				rebuild = true;
			}
		}

		if (customQuickFilter != null) {
			customQuickFilter = null;
			rebuild = true;
		}
		
		if (rebuild) {
			buildQuery();		
			fireEvent(EventType.COLUMN_FILTER_CHANGE, new EntityTableEvent(this, true, false));
		}
	}

	/**
	 * @param filtersMap
	 */
	public void applyQuickFilters(Map<String, QueryElement> filtersMap) {
		// here we will add all filters that don't specify a column
		PropertyQuery customQuickFilter = new PropertyQuery();
		
		boolean columnFilterSetFromQuickFilter = false;
		
		for (Entry<String, QueryElement> entry : filtersMap.entrySet()) {
			String columnId = entry.getKey();
			QueryElement queryElement  = entry.getValue();
				
			if (columnId != null && idMapColumns.containsKey(columnId)) {
				Column col = idMapColumns.get(columnId);
				
				if (!Equals.equal(col.getFilter(), queryElement)) {
					col.setFilter(queryElement);
					columnFilterSetFromQuickFilter = true;
				}
			} else {
				// Note FLI: Not every queryElement has a subQuery - so add the whole element...
				if (queryElement != null) {
					customQuickFilter.addQueryElement(queryElement);
				}
			}
		}
		
		this.customQuickFilter = customQuickFilter;
		
		buildQuery();
		fireEvent(EventType.COLUMN_FILTER_CHANGE, new EntityTableEvent(this, false, columnFilterSetFromQuickFilter));
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

	/**
	 * @return
	 */
	public PropertyQuery getCustomQuickFilter() {
		return customQuickFilter;
	}
	
	/**
	 * @param customQuickFilter
	 */
	public void setCustomQuickFilter(PropertyQuery customQuickFilter) {
		this.customQuickFilter = customQuickFilter;
	}
	
	/**
	 * @return
	 */
	public UserConfigHandler getUserConfigHandler() {
		return userConfigHandler;
	}
}