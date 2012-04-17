/**
 * 
 */
package de.xwic.appkit.webbase.toolkit.table.renderer;

import java.io.PrintWriter;

import de.jwic.ecolib.tableviewer.DefaultTableRenderer;
import de.jwic.ecolib.tableviewer.IContentProvider;
import de.jwic.ecolib.tableviewer.TableModel;

/**
 * @author Oleksiy Samokhvalov
 * 
 */
public class DefaultCellRenderer implements IHtmlContentRenderer {

	private TableCellInfo cellInfo;

	private int expandIconWidth = 19;
	private int expandIconHeight = 16;
	private boolean expandLinkSpacing = true;
	
	

	/**
	 * @param cellInfo
	 * @param cellRendererProvider
	 */
	public DefaultCellRenderer(TableCellInfo cellInfo) {
		this.cellInfo = cellInfo;
	}



	/* (non-Javadoc)
	 * @see de.pol.netapp.amis.planning.forecasting.table.IHtmlContentRenderer#render(java.io.PrintWriter)
	 */
	public void render(PrintWriter writer) {
		TableModel model = cellInfo.getViewer().getModel();
		IContentProvider contentProvider = model.getContentProvider();
		String key = contentProvider.getUniqueKey(cellInfo.getRow()) ;
		boolean expanded = model.isExpanded(key);
		
		
		writer.print("<td");
		if (cellInfo.getColumn().getWidth() > 0) {
			writer.print(" width=\"" + cellInfo.getColumn().getWidth() + "\"");
		}
		if (cellInfo.getLabel().cssClass != null) {
			writer.print(" class=\"" + cellInfo.getLabel().cssClass + "\"");
		}
        if (cellInfo.getLabel().toolTip != null && cellInfo.getLabel().toolTip.length() > 0) {
            writer.print(" title=\"" + cellInfo.getLabel().toolTip + "\"");
        }

		writer.print(">");
		
		boolean innerTable = cellInfo.getColumn().getIndex() == cellInfo.getViewer().getExpandableColumn() || 
								cellInfo.getLabel().image != null && cellInfo.getLabel().text != null;
		
		if (innerTable) {
			writer.print("<table class=\"inner\" cellspacing=0 cellpadding=0 border=0><tr><td>");
		}
		// handle exp/collapse
		if (cellInfo.getColumn().getIndex() == cellInfo.getViewer().getExpandableColumn()) {
			// indention
			for (int i = 0; i < cellInfo.getLevel(); i++) {
				writer.print(DefaultTableRenderer.ICON_CLEAR.toImgTag(expandIconWidth, expandIconHeight));
			}
			if (contentProvider.hasChildren(cellInfo.getRow())) {
				if (cellInfo.getViewer().isEnabled()) {
					writer.print("<a href=\"#\" onClick=\"return ");
					writer.print(expanded ? "trV_Collapse(event)" : "trV_Expand(event)");
					writer.print("\";return false;\">");
					writer.print((expanded ? DefaultTableRenderer.ICON_COLLAPSE : DefaultTableRenderer.ICON_EXPAND).toImgTag(expandIconWidth, expandIconHeight));
					writer.print("</A>");
				} else {
					writer.print((expanded ? DefaultTableRenderer.ICON_COLLAPSE : DefaultTableRenderer.ICON_EXPAND).toImgTag(expandIconWidth, expandIconHeight));
				}
			} else {
				if (expandLinkSpacing) {
					writer.print(DefaultTableRenderer.ICON_CLEAR.toImgTag(expandIconWidth, expandIconHeight));
				}
			}
			writer.print("</td><td class=\"content\">");
		}
		
		// print cell value
		if (cellInfo.getLabel().image != null) {
			writer.print(cellInfo.getLabel().image.toImgTag());
			if (cellInfo.getLabel().text != null) {
				writer.print("</td><td class=\"content\">");
			}
		}
		if (cellInfo.getLabel().text != null) {
			writer.print("<NOBR>");
			if (cellInfo.getLabel().text.length() == 0 && cellInfo.getLabel().image == null) {
				writer.print("&nbsp;");
			} else {
				writer.print(cellInfo.getLabel().text);
			}
			writer.print("</NOBR>");
		}

		if (innerTable) {
			writer.print("</td></tr></table>");
		}				
		writer.println("</td>");
	}

}
