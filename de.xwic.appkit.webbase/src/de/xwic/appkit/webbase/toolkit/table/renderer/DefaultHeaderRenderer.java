/**
 * 
 */
package de.xwic.appkit.webbase.toolkit.table.renderer;

import java.io.PrintWriter;

import de.jwic.base.ImageRef;
import de.jwic.base.JWicRuntime;
import de.jwic.controls.tableviewer.DefaultTableRenderer;
import de.jwic.controls.tableviewer.TableColumn;
import de.jwic.controls.tableviewer.TableViewer;

/**
 * @author Oleksiy Samokhvalov
 *
 */
public class DefaultHeaderRenderer implements IHtmlContentRenderer {

	private TableViewer viewer;
	private TableColumn column;
	
	private String tblvWebPath = JWicRuntime.getJWicRuntime().getContextPath() + "/ecolib/tblviewer/";
	
	private IHtmlRendererProvider contentRendererProvider;
	
	/**
	 * @param viewer
	 * @param column
	 */
	public DefaultHeaderRenderer(TableViewer viewer, TableColumn column, IHtmlRendererProvider contentRendererProvider) {
		this.viewer = viewer;
		this.column = column;
		this.contentRendererProvider = contentRendererProvider;
	}



	/* (non-Javadoc)
	 * @see de.pol.netapp.amis.planning.forecasting.table.IHtmlContentRenderer#render(java.io.PrintWriter)
	 */	
	public void render(PrintWriter writer) {
		boolean isResizable = viewer.isResizeableColumns() && viewer.isEnabled();
		boolean isColSelectable = viewer.isSelectableColumns() && viewer.isEnabled();

		
		if (isResizable && column.getWidth() == 0) {
			// must set a default width if resizeable columns is activated
			column.setWidth(150);
		}
		
		writer.print("<th");
		int innerWidth = 0;
		if (column.getWidth() > 0) {
			writer.print(" width=\"" + column.getWidth() + "\"");
			innerWidth = column.getWidth() - (viewer.isResizeableColumns() ? 5 : 0);
			innerWidth = innerWidth - (column.getSortIcon() != TableColumn.SORT_ICON_NONE ? 8 : 0);
			if (innerWidth < 3) {
				innerWidth = 3;
			}
		}
		writer.print(" colIdx=\"" + column.getIndex() + "\"");
		
        //header tooltip
        if (column.getToolTip() != null && column.getToolTip().length() > 0) {
            writer.print(" title=\"" + column.getToolTip() + "\"");
        }
	
		writer.println(">");
		// create cell table
		writer.print("<TABLE class=\"tbvColHeader\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"><TR>");
		writer.print("<TD class=\"tbvColHeadCell\" width=\"" + innerWidth + "\"");
		if (isColSelectable) {
			writer.print(" onClick=\"JWic.fireAction('" + viewer.getControlID() + "', 'columnSelection', '" + column.getIndex() + "')\"");
			writer.print(" onMouseDown=\"tblViewer_pushColumn(" + column.getIndex() + ", '" + viewer.getControlID() + "')\"");
			writer.print(" onMouseUp=\"tblViewer_releaseColumn()\"");
			writer.print(" onMouseOut=\"tblViewer_releaseColumn()\"");
		}
		writer.print(">");
		contentRendererProvider.getRenderer(column).render(writer);
		writer.print("</TD>");
		if (column.getSortIcon() != TableColumn.SORT_ICON_NONE) {
			ImageRef imgSort = null;
			switch (column.getSortIcon()) {
			case TableColumn.SORT_ICON_UP:
				imgSort = DefaultTableRenderer.ICON_SORTUP;
				break;
			case TableColumn.SORT_ICON_DOWN:
				imgSort = DefaultTableRenderer.ICON_SORTDOWN;
				break;
			}
			if (imgSort != null) {
				writer.print("<TD class=\"tbvColHeadCell\" width=\"8\">");
				writer.print("<IMG SRC=\"" + imgSort.getPath() + "\" border=0>");
				writer.print("</TD>");
			}
		}
		if (isResizable) {
			writer.print("<TD class=\"tbvColHeadCellPoint\" width=\"3\"><IMG SRC=\"" + tblvWebPath + "resizer.gif\" width=\"3\" height=\"13\"");
			writer.print(" colIdx=\"" + column.getIndex() + "\"");
			writer.print(" onMouseDown=\"tblViewer_resizeColumn(event, '" + viewer.getControlID() + "')\" class=\"tblResize\" border=0>");
			writer.print("</TD>");
		}
		writer.print("</TR></TABLE>");
		writer.println("</th>");
	}

}
