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
package de.xwic.appkit.webbase.toolkit.table.content;

import de.jwic.controls.tableviewer.CellLabel;
import de.jwic.controls.tableviewer.RowContext;
import de.jwic.controls.tableviewer.TableColumn;

/**
 * @author Oleksiy Samokhvalov
 * 
 */
@SuppressWarnings("serial")
public abstract class FormattingTableColumn extends TableColumn {

	/**
	 * Default constructor.
	 */
	public FormattingTableColumn(String title, int width) {
		super(title, width);
	}

	/**
	 * Returns a cell label for the current column.
	 * 
	 * @param row
	 * @param rowContext
	 * @return
	 */
	public abstract CellLabel getCellLabel(Object row, RowContext rowContext);
}
