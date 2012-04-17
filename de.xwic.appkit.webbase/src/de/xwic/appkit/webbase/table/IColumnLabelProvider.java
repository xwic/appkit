/*
 * Copyright (c) 2009 Network Appliance, Inc.
 * All rights reserved.
 */

package de.xwic.appkit.webbase.table;

import java.util.Locale;

import de.jwic.ecolib.tableviewer.CellLabel;
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
	 * @param locale
	 * @param column
	 */
	public void initialize(Locale locale, Column column);
	
	/**
	 * Create the cell from the given row data object.
	 * @param row
	 * @param column
	 * @return
	 */
	public CellLabel getCellLabel(RowData row);
	
}
