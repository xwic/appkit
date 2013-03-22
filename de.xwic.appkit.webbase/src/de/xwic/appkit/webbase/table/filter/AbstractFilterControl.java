/*
 * Copyright (c) 2009 Network Appliance, Inc.
 * All rights reserved.
 */

package de.xwic.appkit.webbase.table.filter;

import java.util.ArrayList;
import java.util.List;

import de.jwic.base.ControlContainer;
import de.jwic.base.Event;
import de.jwic.base.IControlContainer;
import de.xwic.appkit.core.model.queries.QueryElement;
import de.xwic.appkit.webbase.table.Column;

/**
 * @author lippisch
 */
public abstract class AbstractFilterControl extends ControlContainer {

	private List<IFilterControlListener> listeners;
	protected Column column;
	
	/**
	 * @param container
	 * @param name
	 */
	public AbstractFilterControl(IControlContainer container, String name) {
		super(container, name);
		
		listeners = new ArrayList<IFilterControlListener>();
	}

	/**
	 * Initialize the filter with the given element.
	 * @param queryElement
	 */
	public abstract void initialize(Column column, QueryElement queryElement);
	
	/**
	 * Returns the QueryElement that represents the filter options.
	 * @return
	 */
	public abstract QueryElement getQueryElement();
	
	/**
	 * Returns the preferred height for this filter.
	 * @return
	 */
	public int getPreferredHeight() {
		return 60;
	}
	
	/**
	 * Returns the preferred width for this filter
	 * @return
	 */
	public int getPreferredWidth() {
		return 264;
	}
	
	/**
	 * @param listener
	 */
	public void addListener(IFilterControlListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	/**
	 * @param listener
	 */
	public void removeListener(IFilterControlListener listener) {
		if (listeners.contains(listener)) {
			listeners.remove(listener);
		}
	}
	
	/**
	 * 
	 */
	protected void notifyListeners() {
		for (IFilterControlListener listener : listeners) {
			listener.applyFilter(new Event(this));
		}
	}
}
