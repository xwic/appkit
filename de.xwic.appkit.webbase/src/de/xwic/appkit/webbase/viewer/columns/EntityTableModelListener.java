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
package de.xwic.appkit.webbase.viewer.columns;

import de.jwic.controls.tableviewer.TableColumn;
import de.jwic.controls.tableviewer.TableModel;
import de.jwic.controls.tableviewer.TableModelAdapter;
import de.jwic.controls.tableviewer.TableModelEvent;
import de.xwic.appkit.webbase.utils.UserProfileWrapper;
import de.xwic.appkit.webbase.viewer.IEnhancedTableViewer;

/**
 * This class handles the event when a column is selected. 
 * @author Aron Cotrau
 */
public class EntityTableModelListener extends TableModelAdapter {

	private IEnhancedTableViewer enhancedTable = null;
	
	/**
	 * c-tor
	 * @param entityTable
	 * @param contentProvider
	 */
	public EntityTableModelListener(IEnhancedTableViewer entityTable) {
		this.enhancedTable = entityTable;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.jwic.ecolib.tableviewer.TableModelAdapter#columnSelected(de.jwic.ecolib.tableviewer.TableModelEvent)
	 */
	public void columnSelected(TableModelEvent event) {
		handleSorting(event.getTableColumn());
	}

	/**
	 * Change the sort icon.
	 * 
	 * @param tableColumn
	 */
	public void handleSorting(TableColumn tableColumn) {
		enhancedTable.handleSorting(tableColumn);
	}
	
	@Override
	public void rangeUpdated(TableModelEvent event) {
		TableModel tableModel = (TableModel)event.getEventSource();
		UserProfileWrapper userListProfile = enhancedTable.getUserListProfile();
		
		if (null != userListProfile) {
			userListProfile.setMaxRows(tableModel.getMaxLines());
		}
	}
}
