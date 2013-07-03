/*
 * Copyright (c) 2009 Network Appliance, Inc.
 * All rights reserved.
 */

package de.xwic.appkit.webbase.table;

import java.util.Locale;
import java.util.TimeZone;

import de.xwic.appkit.core.config.list.ListColumn;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.queries.QueryElement;

/**
 * Specifies a column in the EntityTable.
 * @author lippisch
 */
public class Column implements Comparable<Column> {

	public enum Sort {
		NONE,
		UP,
		DOWN
	}
	
	private final ListColumn listColumn;
	private String id;
	
	private int width;
	private boolean visible = true;
	private String title = null;
	private String titleId;
	
	private int columnOrder = 0;
	
	private IColumnLabelProvider columnLabelProvider = null;
	private QueryElement filter = null;
	private Sort sortState = Sort.NONE;
	
	private String[] propertyIds = null;
	private final Class<? extends IEntity> entityClazz;
	
	/**
	 * @param entityClazz 
	 * @param lc
	 */
	@SuppressWarnings("unchecked")
	public Column(TimeZone timeZone, Locale locale, String dateFormat, String timeFormat, ListColumn listColumn, Class<? extends IEntity> entityClazz) {
		this.listColumn = listColumn;
		this.entityClazz = entityClazz;
		id = listColumn.getPropertyId();
		
		width = listColumn.getDefaultWidth();
		visible = listColumn.isDefaultVisible();
		titleId = listColumn.getTitleId();
		
		if (listColumn.getCustomLabelProviderClass() == null || listColumn.getCustomLabelProviderClass().isEmpty()) {
			if (listColumn.getFinalProperty().isEntity()) {
				// if an entity is specified, we will lookup for a renderer extension that handles this type
				columnLabelProvider = EntityTableExtensionHelper.getEntityColumnLabelProvider(listColumn.getFinalProperty().getEntityType());
			}
			if (columnLabelProvider == null) {
				columnLabelProvider = new DefaultColumnLabelProvider();
			}
		} else {
			try {
				Class<IColumnLabelProvider> clazz = (Class<IColumnLabelProvider>) Class.forName(listColumn.getCustomLabelProviderClass());
				columnLabelProvider = clazz.newInstance();
			} catch (Exception e) {
				throw new RuntimeException("Error creating column label provider " + listColumn.getCustomLabelProviderClass() + ": " + e, e);
			} 
		}
		columnLabelProvider.initialize(timeZone, locale, dateFormat, timeFormat, this);
		
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return the visible
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * @param visible the visible to set
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * @return the titleId
	 */
	public String getTitleId() {
		return titleId;
	}

	/**
	 * @param titleId the titleId to set
	 */
	public void setTitleId(String titleId) {
		this.titleId = titleId;
	}

	/**
	 * @return the columnOrder
	 */
	public int getColumnOrder() {
		return columnOrder;
	}

	/**
	 * @param columnOrder the columnOrder to set
	 */
	public void setColumnOrder(int columnOrder) {
		this.columnOrder = columnOrder;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Column o) {
		return (columnOrder<o.columnOrder ? -1 : (columnOrder==o.columnOrder ? 0 : 1));
	}

	/**
	 * @return the listColumn
	 */
	public ListColumn getListColumn() {
		return listColumn;
	}

	/**
	 * @return the columnLabelProvider
	 */
	public IColumnLabelProvider getColumnLabelProvider() {
		return columnLabelProvider;
	}

	/**
	 * @return the filter
	 */
	public QueryElement getFilter() {
		return filter;
	}

	/**
	 * @param filter the filter to set
	 */
	public void setFilter(QueryElement filter) {
		this.filter = filter;
	}

	/**
	 * @return the sortState
	 */
	public Sort getSortState() {
		return sortState;
	}

	/**
	 * @param sortState the sortState to set
	 */
	public void setSortState(Sort sortState) {
		this.sortState = sortState;
	}

	/**
	 * Returns the property IDs this column is represented by. This is usually the 
	 * property as defined in the list setup, but may be changed by the LabelProvider
	 * if i.e. the property represents an entity.
	 * @return the propertyIds
	 */
	public String[] getPropertyIds() {
		return propertyIds;
	}

	/**
	 * @param propertyIds the propertyIds to set
	 */
	public void setPropertyIds(String[] propertyIds) {
		this.propertyIds = propertyIds;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the entityClazz
	 */
	public Class<? extends IEntity> getEntityClass() {
		return entityClazz;
	}
	
}
