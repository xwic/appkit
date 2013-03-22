/*
 * Copyright (c) 2009 Network Appliance, Inc.
 * All rights reserved.
 */

package de.xwic.appkit.webbase.table;

/**
 * Contains details about event objects.
 * @author lippisch
 */
public class EntityTableEvent {
	
	private Object source = null;
	private Column column = null;
	
	private boolean clearedFilters = false;
	private boolean columnFilterSetFromQuickFilter;
	
	/**
	 * @param source
	 */
	public EntityTableEvent(Object source) {
		super();
		this.source = source;
	}
	
	/**
	 * @param source
	 * @param clearedFilters
	 */
	public EntityTableEvent(Object source, boolean clearedFilters, boolean columnFilterSetFromQuickFilter) {
		super();
		this.source = source;
		this.clearedFilters = clearedFilters;
		this.columnFilterSetFromQuickFilter = columnFilterSetFromQuickFilter;
	}

	/**
	 * @param source
	 * @param column
	 */
	public EntityTableEvent(Object source, Column column) {
		super();
		this.source = source;
		this.column = column;
	}

	/**
	 * @return the source
	 */
	public Object getSource() {
		return source;
	}

	/**
	 * @return the column
	 */
	public Column getColumn() {
		return column;
	}

	/**
	 * @return
	 */
	public boolean isClearedFilters() {
		return clearedFilters;
	}
	
	/**
	 * @return
	 */
	public boolean isColumnFilterSetFromQuickFilter() {
		return columnFilterSetFromQuickFilter;
	}

}
