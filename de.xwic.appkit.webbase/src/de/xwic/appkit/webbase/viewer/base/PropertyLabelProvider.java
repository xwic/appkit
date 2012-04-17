/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.webbase.test.DAOLabelProvider
 * Created on Mar 20, 2007 by Aron Cotrau
 *
 */
package de.xwic.appkit.webbase.viewer.base;

import java.util.List;

import de.jwic.ecolib.tableviewer.CellLabel;
import de.jwic.ecolib.tableviewer.ITableLabelProvider;
import de.jwic.ecolib.tableviewer.RowContext;
import de.jwic.ecolib.tableviewer.TableColumn;
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
