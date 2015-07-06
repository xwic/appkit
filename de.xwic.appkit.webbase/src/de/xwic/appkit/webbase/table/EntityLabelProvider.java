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

import de.jwic.controls.tableviewer.CellLabel;
import de.jwic.controls.tableviewer.ITableLabelProvider;
import de.jwic.controls.tableviewer.RowContext;
import de.jwic.controls.tableviewer.TableColumn;

/**
 * @author lippisch
 */
public class EntityLabelProvider implements ITableLabelProvider {

	/**
	 * @param model
	 */
	public EntityLabelProvider() {
		
	}

	/* (non-Javadoc)
	 * @see de.jwic.ecolib.tableviewer.ITableLabelProvider#getCellLabel(java.lang.Object, de.jwic.ecolib.tableviewer.TableColumn, de.jwic.ecolib.tableviewer.RowContext)
	 */
	@Override
	public CellLabel getCellLabel(Object oRow, TableColumn tableColumn, RowContext rowContext) {
		RowData row = (RowData)oRow;
		Column column = (Column)tableColumn.getUserObject();
		return column.getColumnLabelProvider().getCellLabel(row);
	}

}
