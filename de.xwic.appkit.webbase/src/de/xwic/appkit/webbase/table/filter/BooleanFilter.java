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

import de.jwic.base.IControlContainer;
import de.jwic.controls.RadioGroup;
import de.jwic.events.ElementSelectedEvent;
import de.jwic.events.ElementSelectedListener;
import de.xwic.appkit.core.model.queries.QueryElement;
import de.xwic.appkit.webbase.table.Column;

/**
 * @author lippisch
 */
public class BooleanFilter extends AbstractFilterControl {

	private RadioGroup rgFilter;
	private ElementSelectedListener listener;
	
	/**
	 * @param container
	 * @param name
	 */
	public BooleanFilter(IControlContainer container, String name) {
		super(container, name);
		
		listener = new ElementSelectedListener() {
			@Override
			public void elementSelected(ElementSelectedEvent event) {
				notifyListeners();
			}
		};
		
		rgFilter = new RadioGroup(this, "filter");
		rgFilter.addElement("<i>All</i>", "all");
		rgFilter.addElement("True", "true");
		rgFilter.addElement("False", "false");
		rgFilter.setColumns(3);

		rgFilter.setChangeNotification(true);
		rgFilter.addElementSelectedListener(listener);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.table.filter.AbstractFilterControl#getPreferredHeight()
	 */
	@Override
	public int getPreferredHeight() {
		return 40;
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.table.filter.AbstractFilterControl#getPreferredWidth()
	 */
	@Override
	public int getPreferredWidth() {
		
		return 220;
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.table.filter.AbstractFilterControl#getQueryElement()
	 */
	@Override
	public QueryElement getQueryElement() {
		if (rgFilter.getSelectedKey().equals("true")) {
			return new QueryElement(column.getListColumn().getPropertyId(), QueryElement.EQUALS, Boolean.TRUE);
		} else if (rgFilter.getSelectedKey().equals("false")) {
				return new QueryElement(column.getListColumn().getPropertyId(), QueryElement.EQUALS, Boolean.FALSE);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.table.filter.AbstractFilterControl#initialize(de.xwic.appkit.webbase.table.Column, de.xwic.appkit.core.model.queries.QueryElement)
	 */
	@Override
	public void initialize(Column column, QueryElement queryElement) {
		this.column = column;
		
		rgFilter.removeElementSelectedListener(listener);
		
		if (queryElement == null) {
			rgFilter.setSelectedKey("all");
		} else if (queryElement.getValue() == Boolean.TRUE) {
			rgFilter.setSelectedKey("true");
		} else {
			rgFilter.setSelectedKey("false");
		}
		
		rgFilter.addElementSelectedListener(listener);
	}

}
