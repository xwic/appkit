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
