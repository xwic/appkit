/*
 * Copyright 2005-2007 jWic group (http://www.jwic.de)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * de.jwic.ecolib.tableviewer.export.ExcelExportControl
 * Created on Apr 3, 2007
 * $Id: ExcelExportControl.java,v 1.13 2011/09/08 11:25:16 adrianionescu12 Exp $
 */
package de.xwic.appkit.webbase.controls.export;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import de.jwic.base.IControlContainer;
import de.jwic.base.IResourceControl;
import de.jwic.controls.Button;
import de.jwic.controls.tableviewer.CellLabel;
import de.jwic.controls.tableviewer.RowContext;
import de.jwic.controls.tableviewer.TableColumn;
import de.jwic.controls.tableviewer.TableModel;
import de.jwic.controls.tableviewer.TableViewer;
import de.jwic.data.IContentProvider;
import de.jwic.data.Range;
import de.xwic.appkit.webbase.utils.MimeTypeUtil;

/**
 * This control defines the ExcelExport button.
 * 
 * @author Aron Cotrau
 */
public class ExcelExportControl extends Button implements IResourceControl {
	private static final long serialVersionUID = 1L;
	private boolean showDownload = false;
	private boolean plain = false;

	private TableViewer tableViewer = null;

	/**
	 * @param container
	 * @param tableViewer
	 * @throws IllegalArgumentException
	 *             if tableViewer is <code>null</code>
	 */
	public ExcelExportControl(IControlContainer container, TableViewer tableViewer) {
		this(container, null, tableViewer);
	}

	/**
	 * @param container
	 * @param name
	 * @param tableViewer
	 * @throws IllegalArgumentException
	 *             if tableViewer is <code>null</code>
	 */
	public ExcelExportControl(IControlContainer container, String name, TableViewer tableViewer) {
		super(container, name);
		if (null == tableViewer) {
			throw new IllegalArgumentException("The TableViewer object is not allowed to be null !");
		}

		this.tableViewer = tableViewer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jwic.controls.SelectableControl#click()
	 */
	public void click() {
		super.click();
		setShowDownload(true);
		requireRedraw();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jwic.base.IResourceControl#attachResource(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public void attachResource(HttpServletRequest req, HttpServletResponse res) throws IOException {
		try {
			String filename = "export.xls";
			HSSFWorkbook wb = createWorkBook();
			
			res.setContentType(MimeTypeUtil.getMimeTypeForFileName(filename));
			res.setHeader("Content-Disposition", "attachment; filename=" + filename);
			wb.write(res.getOutputStream());
			res.getOutputStream().close();
		} catch (NoClassDefFoundError e) {
			log.error("Error generating workbook:", e);
		}
	}

	private HSSFWorkbook createWorkBook() {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("Sheet");
		HSSFRow row = sheet.createRow(0);

		// Style for title cells
		HSSFFont font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setColor(HSSFColor.BLUE.index);

		HSSFCellStyle styleTitle = wb.createCellStyle();
		styleTitle.setFont(font);

		// Style for data date cells
		font = wb.createFont();
		HSSFCellStyle styleDate = wb.createCellStyle();
		styleDate.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));

		short col = 0;
		TableModel model = tableViewer.getModel();
		Iterator<TableColumn> it = model.getColumnIterator();

		// create title in the sheet
		while (it.hasNext()) {
			TableColumn column = it.next();
			if (!isColumnVisible(column)) {
				continue;
			}
			sheet.setColumnWidth(col, (short) (column.getWidth() * 40));
			HSSFCell cell = row.createCell(col++);
			cell.setCellValue(column.getTitle());
			cell.setCellStyle(styleTitle);
		}

		// add the datas from the table viewer
		IContentProvider<?> contentProvider = model.getContentProvider();
		Iterator<?> iter = contentProvider.getContentIterator(new Range());

		try {
			renderRows(iter, 0, model, sheet, styleDate);
		} catch (Throwable t) {
			log.error("Error rendering rows", t);
		}
		
		return wb;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void renderRows(Iterator<?> iter, int level, TableModel model, HSSFSheet sheet, HSSFCellStyle styleDate) {
		while (iter.hasNext()) {
			short col = 0;
			Object inputObj = iter.next();
			HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);

			IContentProvider contentProvider = model.getContentProvider();
			for (Iterator<TableColumn> it = model.getColumnIterator(); it.hasNext();) {;
				TableColumn column = it.next();
				if (!isColumnVisible(column)) {
					// skip column, it's not visible!
					continue;
				}
				// call the label provider's getCellLabel method to get the
				// CellLabel object
				CellLabel label = null;
				String rowKey = contentProvider.getUniqueKey(inputObj);
				boolean expanded = model.isExpanded(rowKey);
				HSSFCell cell = row.createCell(col++);
				
				try {
					label = tableViewer.getTableLabelProvider().getCellLabel(inputObj, column, new RowContext(expanded, level));
					Object obj = label.object;
					
					// set cell text and style
					if (obj != null) {
						// identify special style for Date and Number
						if (obj instanceof Number) {
							cell.setCellValue(((Number)obj).doubleValue());
							continue;
						} else if (obj instanceof Date) {
							cell.setCellValue((Date)obj);
							cell.setCellStyle(styleDate);
							continue;
						} else if (obj instanceof Boolean) {
							cell.setCellValue((Boolean)obj ? "Y" : "N");
							continue;
						}
					}
					
					String columnText = label.text;
					if (columnText != null){
						columnText = StringEscapeUtils.unescapeHtml(label.text);
					}
					cell.setCellValue(columnText);
				} catch (Throwable t) {
					cell.setCellValue(t.getMessage());
					log.error("Error rendering column " + column.getTitle(), t);
				}
			}
			// render children
			if (contentProvider.hasChildren(inputObj)) {
				Iterator children = contentProvider.getChildren(inputObj);
				renderRows(children, level + 1, model, sheet, styleDate);
			}
		}
	}

	/**
	 * @param column
	 * @return
	 */
	protected boolean isColumnVisible(TableColumn column) {
		return column.isVisible();
	}

	/**
	 * Returns the URL that calls the attachResource method.
	 * 
	 * @return
	 */
	public String getDownloadURL() {
		return getSessionContext().getCallBackURL() + "&"
			+ URL_RESOURCE_PARAM + "=1&"
			+ URL_CONTROLID_PARAM + "=" + getControlID();
	}

	/**
	 * @return Returns the showDownload.
	 */
	public boolean isShowDownload() {
		return showDownload;
	}

	/**
	 * @param showDownload
	 *            The showDownload to set.
	 */
	public void setShowDownload(boolean showDownload) {
		this.showDownload = showDownload;
	}

	/**
	 * @return if the button is plain or action
	 */
	public boolean isPlain() {
		return plain;
	}

	/**
	 * set this attribute to <code>true</code> when this button is supposed to
	 * be a regular button instead of an action type.
	 * 
	 * @param isPlain
	 */
	public void setPlain(boolean isPlain) {
		if (this.plain != isPlain) {
			requireRedraw();
		}

		this.plain = isPlain;
	}
}
