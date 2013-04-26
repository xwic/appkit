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
