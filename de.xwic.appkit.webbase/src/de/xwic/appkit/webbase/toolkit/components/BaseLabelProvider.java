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
/**
 * 
 */
package de.xwic.appkit.webbase.toolkit.components;

import de.jwic.controls.tableviewer.CellLabel;
import de.jwic.controls.tableviewer.RowContext;
import de.jwic.controls.tableviewer.TableColumn;
import de.xwic.appkit.core.config.model.Property;
import de.xwic.appkit.webbase.toolkit.util.ImageLibrary;
import de.xwic.appkit.webbase.viewer.base.PropertyLabelProvider;
import de.xwic.appkit.webbase.viewer.columns.TableColumnInfo;

/**
 * @author Ronny Pfretzschner
 *
 */
public class BaseLabelProvider extends PropertyLabelProvider {

	
	@Override
	public CellLabel getCellLabel(Object row, TableColumn column,
			RowContext rowContext) {
		CellLabel label = super.getCellLabel(row, column, rowContext);
		
		int index = column.getIndex();
		Object obj = columnInfoList.get(index);
		
		if (obj instanceof TableColumnInfo) {
			TableColumnInfo info = (TableColumnInfo) obj;
			Property prop = info.getColumn().getFinalProperty();

			if (prop.getDescriptor().getPropertyType().equals(boolean.class)) {
				Boolean val;
				try {
					val = (Boolean) info.getData(row);
				} catch (Exception e) {
					label.text = e.toString();
					return label;
				}
				
				if (val != null && val.booleanValue()) {
					label.image = ImageLibrary.ICON_CHECKED;
				} else {
					label.image = ImageLibrary.ICON_UNCHECKED;
				}
				label.text = "";
			}
		}
		
		return label;
	}
}
