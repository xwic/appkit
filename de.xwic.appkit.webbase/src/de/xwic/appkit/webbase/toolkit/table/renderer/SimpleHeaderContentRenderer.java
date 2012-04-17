/**
 * 
 */
package de.xwic.appkit.webbase.toolkit.table.renderer;

import java.io.PrintWriter;

import de.jwic.ecolib.tableviewer.TableColumn;

/**
 * @author Oleksiy Samokhvalov
 *
 */
public class SimpleHeaderContentRenderer implements IHtmlContentRenderer{

	private TableColumn column;
	
	/**
	 * @param column
	 */
	public SimpleHeaderContentRenderer(TableColumn column) {
		this.column = column;
	}



	/* (non-Javadoc)
	 * @see de.pol.netapp.amis.planning.forecasting.table.IHtmlContentRenderer#render(java.io.PrintWriter)
	 */
	public void render(PrintWriter writer) {
		writer.print("<NOBR>");
		if (column.getImage() != null) {
			writer.print("<IMG SRC=\"" + column.getImage().getPath() + "\" border=0/>");
		}
		writer.print(column.getTitle());
		writer.print("</NOBR>");
	}

}
