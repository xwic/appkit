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
package de.xwic.appkit.webbase.viewer.base;

import java.util.List;

import de.jwic.controls.tableviewer.CellLabel;
import de.jwic.controls.tableviewer.ITableLabelProvider;
import de.jwic.controls.tableviewer.RowContext;
import de.jwic.controls.tableviewer.TableColumn;
import de.xwic.appkit.webbase.viewer.columns.TableColumnInfo;

/**
 * Defines the LabelProvider for the tables.
 * @author Aron Cotrau
 */
public class PropertyLabelProvider implements ITableLabelProvider {

	protected List<TableColumnInfo> columnInfoList = null;
	
	/**
	 * sets the list that must contain <code>TableColumnInfo</code> objects
	 * @param list
	 */
	public void setPropertiesList(List<TableColumnInfo> list) {
		this.columnInfoList = list;
	}

	public CellLabel getCellLabel(Object row, TableColumn column, RowContext rowContext) {
		CellLabel cellLabel = new CellLabel();
		int index = column.getIndex();
		Object obj = columnInfoList.get(index);
		
		if (obj instanceof TableColumnInfo) {
			TableColumnInfo ci = (TableColumnInfo) obj;
			cellLabel.text = ci.getText(row);
			cellLabel.object = ci.getData(row);
		}
		
		return cellLabel;
	}
}
