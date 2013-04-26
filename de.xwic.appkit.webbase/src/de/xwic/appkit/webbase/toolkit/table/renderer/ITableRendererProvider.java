/**
 * 
 */
package de.xwic.appkit.webbase.toolkit.table.renderer;

import de.jwic.controls.tableviewer.ITableLabelProvider;
import de.jwic.controls.tableviewer.RowContext;
import de.jwic.controls.tableviewer.TableColumn;

/**
 * @author Oleksiy Samokhvalov
 *
 */
public interface ITableRendererProvider extends ITableLabelProvider {
	/**
	 * @param row
	 * @param column
	 * @param rowContext
	 * @return a HTML content renderer for the specified cell.
	 */
	IHtmlContentRenderer getCellRenderer(Object row, TableColumn column, RowContext rowContext);
	
	/**
	 * @param column
	 * @return the header renderer for the specified column.
	 */
	IHtmlContentRenderer getHeaderRenderer(TableColumn column);
}
