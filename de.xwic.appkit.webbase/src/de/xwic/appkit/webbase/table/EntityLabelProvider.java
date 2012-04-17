/*
 * Copyright (c) 2009 Network Appliance, Inc.
 * All rights reserved.
 */

package de.xwic.appkit.webbase.table;

import de.jwic.ecolib.tableviewer.CellLabel;
import de.jwic.ecolib.tableviewer.ITableLabelProvider;
import de.jwic.ecolib.tableviewer.RowContext;
import de.jwic.ecolib.tableviewer.TableColumn;

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
