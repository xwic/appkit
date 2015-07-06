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

import java.util.Locale;
import java.util.TimeZone;

import org.apache.poi.ss.usermodel.Cell;

import de.jwic.controls.tableviewer.CellLabel;
import de.xwic.appkit.core.config.list.ListColumn;

/**
 * @author lippisch
 */
public interface IColumnLabelProvider {

	/**
	 * Returns a list of properties needed to handle the specified property.
	 * @param listColumn 
	 * @param propertyId
	 * @return
	 */
	public String[] getRequiredProperties(ListColumn listColumn, String propertyId);
	
	/**
	 * Initialize this column label provider.
	 * @param timeZone
	 * @param locale
	 * @param dateFormat
	 * @param timeFormat
	 * @param column
	 */
	public void initialize(TimeZone timeZone, Locale locale, String dateFormat, String timeFormat, Column column);
	
	/**
	 * Create the cell from the given row data object.
	 * @param row
	 * @param column
	 * @return
	 */
	public CellLabel getCellLabel(RowData row);
	
	
	/**
	 * Customize cell value properties during excel export.
	 * @param cell
	 * @param label
	 * @return - true to continue immediately with next row instead setting value on cell.
	 */
	public boolean renderExcelCell(Cell cell, CellLabel label);
	
}
