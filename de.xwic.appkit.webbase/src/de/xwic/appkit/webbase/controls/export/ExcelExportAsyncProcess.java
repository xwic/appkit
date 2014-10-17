package de.xwic.appkit.webbase.controls.export;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import de.jwic.async.AbstractAsyncProcess;
import de.jwic.controls.tableviewer.CellLabel;
import de.jwic.controls.tableviewer.ITableLabelProvider;
import de.jwic.controls.tableviewer.RowContext;
import de.jwic.controls.tableviewer.TableColumn;
import de.jwic.controls.tableviewer.TableModel;
import de.jwic.data.IContentProvider;
import de.jwic.data.Range;
import de.xwic.appkit.webbase.table.Column;

public class ExcelExportAsyncProcess extends AbstractAsyncProcess{

	private final Log log = LogFactory.getLog(ExcelExportAsyncProcess.class);
	protected final TableModel model;
	protected final ITableLabelProvider labelProvider;
	
	private Thread currentThread;
	private final Iterator<?> contentIterator;
	private final int totalSize;
	
	
	/**
	 * @param model
	 * @param labelProvider
	 */
	public ExcelExportAsyncProcess(TableModel model, ITableLabelProvider labelProvider) {
		super();
		this.model = model;
		this.labelProvider = labelProvider;
		IContentProvider<?> contentProvider = model.getContentProvider();
		this.contentIterator = contentProvider.getContentIterator(new Range());
		
		this.totalSize = contentProvider.getTotalSize();
	}

	@Override
	protected final Object runProcess() {
		
		currentThread = Thread.currentThread();
		ByteArrayOutputStream out = null;
		Workbook wb;
		monitor.setInfoText("Starting...");
		monitor.setMaximum(1);
		monitor.setValue(0);
		try {
			out = new ByteArrayOutputStream();
			wb = createWorkBook();
			if (wb != null) {
				wb.write(out);
				monitor.setInfoText("Finished!");
				return out.toByteArray();
			} else {
				monitor.setInfoText("Cancelled");
				return null;
			}			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public final boolean canCancel() {
		return true;
	}
	
	
	protected Workbook createWorkBook() {
		Workbook wb = new SXSSFWorkbook(100);
		Sheet sheet = wb.createSheet("Sheet");
		Row row = sheet.createRow(0);

		// Style for title cells
		Font font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setColor(HSSFColor.BLUE.index);

		CellStyle styleTitle = wb.createCellStyle();
		styleTitle.setFont(font);

		// Style for data date cells
		font = wb.createFont();
		CellStyle styleDate = wb.createCellStyle();
		styleDate.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));

		short col = 0;
		
		Iterator<TableColumn> it = model.getColumnIterator();

		// create title in the sheet
		while (it.hasNext() && !cancelled) {
			TableColumn column = it.next();
			if (!isColumnVisible(column)) {
				continue;
			}
			sheet.setColumnWidth(col, (short) (column.getWidth() * 40));
			Cell cell = row.createCell(col++);
			cell.setCellValue(column.getTitle());
			cell.setCellStyle(styleTitle);
		}

		// add the datas from the table viewer
		
		monitor.setInfoText("Generating Content");
		monitor.setMaximum(totalSize);
		
		try {
			renderRows(contentIterator, 0, model, sheet, styleDate);
		} catch (Throwable t) {
			log.error("Error rendering rows", t);
		}
		
		if(cancelled){
			return null;
		}
		
		return wb;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void renderRows(Iterator<?> iter, int level, TableModel model, Sheet sheet, CellStyle styleDate) {
		int rowNumber = 0;
		while (iter.hasNext() && !cancelled) {
			monitor.setValue(rowNumber);
			short col = 0;
			Object inputObj = iter.next();
			rowNumber = sheet.getLastRowNum() + 1;
			Row row = sheet.createRow(rowNumber);
			
			IContentProvider contentProvider = model.getContentProvider();
			for (Iterator<TableColumn> it = model.getColumnIterator(); it.hasNext() && !cancelled;) {
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
				Cell cell = row.createCell(col++);
				
				try {
					label = this.labelProvider.getCellLabel(inputObj, column, new RowContext(expanded, level));
					Object obj = label.object;
					
					if(column.getUserObject() != null && column.getUserObject() instanceof Column){
						Column entityCol = (Column) column.getUserObject();
						if(entityCol.getColumnLabelProvider() != null){
							if(entityCol.getColumnLabelProvider().renderExcelCell(cell, label)){
								continue;
							}
						}
					}
					
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
			if (contentProvider.hasChildren(inputObj) && !cancelled) {
				Iterator children = contentProvider.getChildren(inputObj);
				renderRows(children, level + 1, model, sheet, styleDate);
			}
		}
	}
	
	/**
	 * @param column
	 * @return
	 */
	protected static boolean isColumnVisible(TableColumn column) {
		return column.isVisible();
	}
	
	@Override
	public final boolean cancel() {
		cancelled = true;
		currentThread.interrupt();
		return true;
	}

	
	public final boolean isCancelled() {
		return this.cancelled;
	}

}
