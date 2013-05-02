/**
 * 
 */
package de.xwic.appkit.webbase.toolkit.table.renderer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.jwic.base.Control;
import de.jwic.base.Field;
import de.jwic.base.IControlRenderer;
import de.jwic.base.ImageRef;
import de.jwic.base.JWicRuntime;
import de.jwic.base.RenderContext;
import de.jwic.controls.tableviewer.DefaultTableRenderer;
import de.jwic.controls.tableviewer.ITableLabelProvider;
import de.jwic.controls.tableviewer.ITableRenderer;
import de.jwic.controls.tableviewer.RowContext;
import de.jwic.controls.tableviewer.TableColumn;
import de.jwic.controls.tableviewer.TableModel;
import de.jwic.controls.tableviewer.TableViewer;
import de.jwic.data.IContentProvider;

/**
 * Provides more generic interface to the cell rendering.
 * 
 * @see DefaultTableRenderer
 * 
 * @author Oleksiy Samokhvalov
 *
 */
public class SimpleTableRenderer implements ITableRenderer, Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = -8147038951482625490L;
	
	/** default icon used for sort-up image */
	public final static ImageRef ICON_SORTUP = new ImageRef("/ecolib/tblviewer/sortup.gif");
	/** default icon used for sort-down image */
	public final static ImageRef ICON_SORTDOWN = new ImageRef("/ecolib/tblviewer/sortdn.gif"); 
	/** default icon used for expand image */
	public final static ImageRef ICON_EXPAND = new ImageRef("/ecolib/treeviewer/expand.png");
	/** default icon used for collapse image */
	public final static ImageRef ICON_COLLAPSE = new ImageRef("/ecolib/treeviewer/collapse.png"); 
	/** default icon used for indention */
	public final static ImageRef ICON_CLEAR = new ImageRef("/ecolib/tblviewer/clear.gif"); 

	protected transient Log log = LogFactory.getLog(getClass()); 
	
	private boolean expandLinkSpacing = true;
	private int expandIconWidth = 19;
	private int expandIconHeight = 16;
	
	/* (non-Javadoc)
	 * @see de.jwic.ecolib.tableviewer.ITableRenderer#renderTable(de.jwic.base.RenderContext, de.jwic.ecolib.tableviewer.TableViewer, de.jwic.ecolib.tableviewer.TableModel)
	 */
	@SuppressWarnings("unchecked")
	public void renderTable(RenderContext renderContext, TableViewer viewer, TableModel model, ITableLabelProvider labelProvider) {
		
		PrintWriter writer = renderContext.getWriter();
		
		IContentProvider contentProvider = model.getContentProvider();
		
		int tblWidth = 0;
		for (Iterator it = model.getColumnIterator(); it.hasNext(); ) {
			TableColumn tc = (TableColumn)it.next();
			if (!tc.isVisible()) {
				continue;
			}
			tblWidth += tc.getWidth();
		}
		
		// Add resizer div 
		if (viewer.isResizeableColumns()) {
			writer.print("<DIV id=\"tblViewResizer_" + viewer.getControlID() + "\" class=\"tblViewResizer\" ");
			/*writer.print("onMouseUp=\"tblViewer_resizeColumnDone()\"" +
					" onMouseMove=\"tblViewer_resizeColumMove()\"");*/
			writer.print(" style=\"height: " + (viewer.getHeight() != 0 ? viewer.getHeight() : 20) + "px\"");
			writer.println("></DIV>");
		}
		
		// render main table.
		writer.print("<table cellspacing=\"0\" cellpadding=\"0\" class=\"" + viewer.getCssClass() + "\"");
		if (viewer.isFillWidth()) {
			writer.print(" width=\"100%\"");
		} else {
			if (viewer.getWidth() != 0) {
				writer.print(" width=\"" + viewer.getWidth() + "\"");
			}
			// FLI: the table does not need a height...
			/*if (viewer.getHeight() != 0) {
				writer.print(" height=\"" + viewer.getHeight() + "\"");
			}*/
		}
		writer.println(">");

		
		writer.print("<tr><td>");
		
		// render data table.
		String divHeight = viewer.getHeight() != 0 ? 
				(viewer.isShowStatusBar() ? viewer.getHeight() - 18 : viewer.getHeight() ) + "px" 
				: "100%";
		String divWidth = viewer.getWidth() != 0 ? viewer.getWidth() + "px" : "100%";
		
		// main table content DIV
		writer.print("<div class=\"tblContent\"");
		writer.print(" id=\"tblContent_" + viewer.getControlID() + "\"");
		writer.print(" style=\"height: " + divHeight + "; width: " + divWidth + "; overflow: hidden\">");
		
		if (viewer.isScrollable() && viewer.isShowHeader()) {
			int tmpWidth = viewer.getWidth() != 0 ? viewer.getWidth() : 300;
			writer.print("<DIV id=\"tblViewHead_" + viewer.getControlID() + "\"");
			writer.print("style=\"width: " + tmpWidth + "px; ");
//			writer.print("height: 20px;");
			writer.print("overflow: hidden;");
			writer.print("\">");
		}
		
		// create required table attributes.
		StringBuffer sbTblSelAttrs = new StringBuffer();
		switch (model.getSelectionMode()) {
		case TableModel.SELECTION_SINGLE: {
			String clearKey = model.getFirstSelectedKey();
			if (clearKey == null) {
				clearKey = "";
			}
			sbTblSelAttrs.append(" tbvSelKey=\"" + clearKey + "\"");
			sbTblSelAttrs.append(" tbvSelMode=\"single\"");
			break;
		}
		case TableModel.SELECTION_MULTI: {
			sbTblSelAttrs.append(" tbvSelKey=\"\"");
			sbTblSelAttrs.append(" tbvSelMode=\"multi\"");
			break;
		}
		default: {
			sbTblSelAttrs.append(" tbvSelKey=\"\"");
			sbTblSelAttrs.append(" tbvSelMode=\"none\"");
		}
		}
		
		if (viewer.isShowHeader() || !viewer.isScrollable()) {
		
			writer.print("<table");
			writer.print(" tbvctrlid=\"" + viewer.getControlID() + "\"");
			writer.print(" id=\"tblViewData_" + viewer.getControlID() + "\"");
			writer.print(" class=\"tblData\" cellspacing=\"0\" cellpadding=\"0\" ");
			if (viewer.isScrollable()) {
				// must add a width attribute, otherwise table-layout: fixed isnt working on Mozilla
				writer.print(" width=\"" + tblWidth + "\" ");
			}
			writer.print(sbTblSelAttrs);
			writer.println(">");
		}
		
		// render HEADER columns
		if (viewer.isShowHeader()) {
			renderHeader(writer, model, viewer, labelProvider);
		}		
		if (viewer.isScrollable()) {
			// if scrollable, seperate the data table from the header
			// and render it within its own, scrollable DIV. Will only
			// look proper if the columns have a fixed width.
			
			int dataHeight = viewer.getHeight() != 0 ? viewer.getHeight() - (viewer.isShowHeader() ? 20 : 0): 200;
			if (viewer.isShowStatusBar()) {
				dataHeight -= 18;
			}
			int dataWidth = viewer.getWidth() != 0 ? viewer.getWidth() : 300;
			if (viewer.isShowHeader()) {
				writer.println("</TABLE></DIV>");
			}
			writer.print("<DIV onscroll=\"tblViewer_handleScroll(event, '" + viewer.getControlID() + "')\" style=\"");
			writer.print("width: " + dataWidth + "px; height: " + dataHeight + "px; overflow: auto;");
			writer.print("\" id=\"tblViewDataLayer_" + viewer.getControlID() + "\"");
			writer.println(">");
			writer.print("<table");
			writer.print(" tbvctrlid=\"" + viewer.getControlID() + "\"");
			writer.print(" id=\"tblViewDataTbl_" + viewer.getControlID() + "\"");
			writer.print(" class=\"tblData\" cellspacing=\"0\" cellpadding=\"0\" width=\"" + tblWidth + "\"");			
			writer.print(sbTblSelAttrs);
			writer.println(">");
		}
		
		// render table BODY
		writer.println("<TBODY>");
		try {
			int count = renderRows(0, false, writer, contentProvider.getContentIterator(model.getRange()), viewer, labelProvider);
			model.setLastRowRenderCount(count);
		} catch (Exception e) {
			writer.println("Error reading data from ContentProvider: " + e);
			log.error("Error reading data from ContentProvider", e);
		}
		
		writer.println("</TBODY>");
		writer.println("</table>");
		if (viewer.isScrollable()) {
			writer.println("</DIV>");
		}
		writer.println("</div></td></tr>");
		
		// render STATUS BAR
		if (viewer.isShowStatusBar()) {
			writer.println("<tr><td>");
			// render context
			Control sb = viewer.getControl("statusBar");
			IControlRenderer renderer = JWicRuntime.getRenderer(sb.getRendererId());
			renderer.renderControl(sb, renderContext);
			writer.print("</td></tr>");
		}
		
		writer.println("</table>");
		
		if (viewer.isScrollable()) {
			// add scroll fields
			Field fldLeft = viewer.getField("left");
			Field fldTop = viewer.getField("top");
			writer.println("<INPUT TYPE=\"HIDDEN\" NAME=\"" + fldLeft.getId() + "\" VALUE=\"" + fldLeft.getValue() + "\">");
			writer.println("<INPUT TYPE=\"HIDDEN\" NAME=\"" + fldTop.getId() + "\" VALUE=\"" + fldTop.getValue() + "\">");

			writer.println("<script language=\"javascript\">");
			writer.println("window.setTimeout(\"JWic.fixScrolling('" + viewer.getControlID() + "', 'tblViewDataLayer_" + viewer.getControlID() + "');\", 0);");
			writer.println("</script>");
		}
		
	}
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	protected void renderHeader(PrintWriter writer, TableModel model, TableViewer viewer, ITableLabelProvider labelProvider) {
		
		writer.println("<THEAD>");
		
		writer.println("<tr>");
		for (Iterator itC = model.getColumnIterator(); itC.hasNext(); ) {
			TableColumn column = (TableColumn)itC.next();
			
			if (!column.isVisible()) {
				continue;
			}

			IHtmlContentRenderer renderer = ((ITableRendererProvider)labelProvider).getHeaderRenderer(column);
			renderer.render(writer);
		}
		
		// if the width is fixed, we must render an empty column at the end so that the
		// browser will not adjust the columns width
		if (viewer.getWidth() != 0) {
			if (viewer.isScrollable()) {
				writer.println("<TH width=\"" + viewer.getWidth() + "\">&nbsp;</TH>");
			} else {
				writer.println("<TH>&nbsp;</TH>");
			}
		}
		
		writer.println("</tr>");
		writer.println("</THEAD>");
		
	}

	/**
	 * @param writer
	 * @param rootIterator
	 */
	@SuppressWarnings("unchecked")
	private int renderRows(int level, boolean hasNext, PrintWriter writer, Iterator it, TableViewer viewer, ITableLabelProvider labelProvider) {
		
		TableModel model = viewer.getModel();
		IContentProvider contentProvider = model.getContentProvider();
		
		int count = 0;
		while(it.hasNext()) {
			Object row = it.next();
			count++;
			String key = contentProvider.getUniqueKey(row) ;
			boolean expanded = model.isExpanded(key);
			writer.print("<tr");
			String rowCssClass = "";
			if (!(it.hasNext() || hasNext || contentProvider.hasChildren(row))) {
				rowCssClass="lastRow";
			}

			// handle selection
			writer.print(" tbvRowKey=\"" + key + "\"");
			if (model.getSelectionMode() != TableModel.SELECTION_NONE) {
				if (viewer.isEnabled()) {
					writer.print(" onClick=\"tblViewer_ClickRow(this, event)\"");
				}
				if (model.isSelected(key)) {
					rowCssClass = rowCssClass + "selected";
				}
			}
			if (rowCssClass.length() != 0) {
				writer.print(" class=\"" + rowCssClass + "\"");
			}
			writer.println(">");
			for (Iterator itC = model.getColumnIterator(); itC.hasNext(); ) {
				TableColumn column = (TableColumn)itC.next();
				if (!column.isVisible()) {
					continue;
				}

				IHtmlContentRenderer renderer = ((ITableRendererProvider)labelProvider).getCellRenderer(row, column, new RowContext(expanded, level));
				renderer.render(writer);
			}
			// if its a fixed width, must render an empty column that fills up the space.
			if (viewer.getWidth() != 0) {
				writer.println("<TD>&nbsp;</TD>");
			}
			writer.println(" </tr>");
			
			if (contentProvider.hasChildren(row) && expanded) {
				renderRows(level + 1, hasNext || it.hasNext(), writer, contentProvider.getChildren(row), viewer, labelProvider);
			}
			
		}
		return count;
	}
	/**
	 * @return the expandLinkSpacing
	 */
	public boolean isExpandLinkSpacing() {
		return expandLinkSpacing;
	}
	
	/**
	 * @param expandLinkSpacing the expandLinkSpacing to set
	 */
	public void setExpandLinkSpacing(boolean expandLinkSpacing) {
		this.expandLinkSpacing = expandLinkSpacing;
	}
	
	/**
	 * @param expandIconHeight the expandIconHeight to set
	 */
	public void setExpandIconHeight(int expandIconHeight) {
		this.expandIconHeight = expandIconHeight;
	}
	
	/**
	 * @return the expandIconHeight
	 */
	public int getExpandIconHeight() {
		return expandIconHeight;
	}
	
	/**
	 * @param expandIconWidth the expandIconWidth to set
	 */
	public void setExpandIconWidth(int expandIconWidth) {
		this.expandIconWidth = expandIconWidth;
	}
	
	/**
	 * @return the expandIconWidth
	 */
	public int getExpandIconWidth() {
		return expandIconWidth;
	}
	/**
	 * Get new logger after deserialization.
	 * @param s
	 * @throws IOException
	 */
	private void readObject(ObjectInputStream s) throws IOException  {
		try {
			s.defaultReadObject();
		} catch (ClassNotFoundException e) {
			throw new IOException("ClassNotFound in readObject");
		}
		log = LogFactory.getLog(getClass());
	}

}
