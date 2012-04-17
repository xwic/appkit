/**
 * 
 */
package de.xwic.appkit.webbase.toolkit.table.content;

import de.jwic.ecolib.tableviewer.CellLabel;
import de.jwic.ecolib.tableviewer.ITableLabelProvider;
import de.jwic.ecolib.tableviewer.RowContext;
import de.jwic.ecolib.tableviewer.TableColumn;

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
