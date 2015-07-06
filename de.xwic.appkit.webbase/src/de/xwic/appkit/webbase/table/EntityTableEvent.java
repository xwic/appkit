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
