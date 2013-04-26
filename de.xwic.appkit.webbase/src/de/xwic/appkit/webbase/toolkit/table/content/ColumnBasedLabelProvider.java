/**
 * 
 */
package de.xwic.appkit.webbase.toolkit.table.content;

import de.jwic.controls.tableviewer.CellLabel;
import de.jwic.controls.tableviewer.ITableLabelProvider;
import de.jwic.controls.tableviewer.RowContext;
import de.jwic.controls.tableviewer.TableColumn;

/**
 * @author Oleksiy Samokhvalov
 *
 */
@SuppressWarnings("serial")
public class ColumnBasedLabelProvider implements ITableLabelProvider{

	/* (non-Javadoc)
	 * @see de.jwic.ecolib.tableviewer.ITableLabelProvider#getCellLabel(java.lang.Object, de.jwic.ecolib.tableviewer.TableColumn, de.jwic.ecolib.tableviewer.RowContext)
	 */
	public CellLabel getCellLabel(Object row, TableColumn column, RowContext rowContext) {
		return ((FormattingTableColumn)column).getCellLabel(row, rowContext);
	}

}
