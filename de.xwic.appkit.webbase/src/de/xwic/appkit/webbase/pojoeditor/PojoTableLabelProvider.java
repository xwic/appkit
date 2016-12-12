/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 * 
 * de.xwic.appkit.webbase.pojoeditor.PojoTableLabelProvider 
 */

package de.xwic.appkit.webbase.pojoeditor;

import java.lang.reflect.Field;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.jwic.controls.tableviewer.CellLabel;
import de.jwic.controls.tableviewer.ITableLabelProvider;
import de.jwic.controls.tableviewer.RowContext;
import de.jwic.controls.tableviewer.TableColumn;

/**
 * @author Andrei Pat
 *
 */
public class PojoTableLabelProvider implements ITableLabelProvider {

	private static final Log log = LogFactory.getLog(PojoTableLabelProvider.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jwic.controls.tableviewer.ITableLabelProvider#getCellLabel(java.lang.Object, de.jwic.controls.tableviewer.TableColumn,
	 * de.jwic.controls.tableviewer.RowContext)
	 */
	@Override
	public CellLabel getCellLabel(Object row, TableColumn column, RowContext rowContext) {
		try {
			Field f = (Field) column.getUserObject();
			f.setAccessible(true);
			Object val = f.get(row);
			String stringValue;
			if (val == null) {
				stringValue = "";
			} else {
				stringValue = String.valueOf(f.get(row));
			}
			return new CellLabel(stringValue);
		} catch (Exception e) {
			log.error(e);
			return null;
		}
	}
}
