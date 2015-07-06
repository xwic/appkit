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
package de.xwic.appkit.webbase.toolkit.table.renderer;

import de.jwic.controls.tableviewer.CellLabel;
import de.jwic.controls.tableviewer.TableColumn;
import de.jwic.controls.tableviewer.TableViewer;

/**
 * @author Oleksiy Samokhvalov
 *
 */
public class TableCellInfo {
	private Object row;
	private TableColumn column;
	private CellLabel label;
	private TableViewer viewer;
	private int level;
	
	/**
	 * @param row
	 * @param column
	 * @param label
	 * @param viewer
	 * @param level
	 */
	public TableCellInfo(Object row, TableColumn column, CellLabel label, TableViewer viewer, int level) {
		this.row = row;
		this.column = column;
		this.label = label;
		this.viewer = viewer;
		this.level = level;
	}

	/**
	 * @return the row
	 */
	public Object getRow() {
		return row;
	}

	/**
	 * @return the column
	 */
	public TableColumn getColumn() {
		return column;
	}

	/**
	 * @return the label
	 */
	public CellLabel getLabel() {
		return label;
	}

	/**
	 * @return the viewer
	 */
	public TableViewer getViewer() {
		return viewer;
	}

	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

}
