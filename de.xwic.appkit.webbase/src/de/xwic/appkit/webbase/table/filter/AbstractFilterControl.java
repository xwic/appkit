/*******************************************************************************
 * Copyright 2015 xWic group (http://www.xwic.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *  
 *******************************************************************************/

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
