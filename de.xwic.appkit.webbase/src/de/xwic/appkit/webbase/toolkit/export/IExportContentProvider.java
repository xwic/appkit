/**
 * 
 */
package de.xwic.appkit.webbase.toolkit.export;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Oleksiy Samokhvalov
 *
 */
public interface IExportContentProvider {
	
	public static final String CONTENT_TYPE_PDF = "application/pdf";

	public static final String CONTENT_TYPE_XLS = "application/x-msdownload";
	
	public static final String CONTENT_TYPE_RTF = "application/rtf";
	
	public static final String CONTENT_TYPE_DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
	
	
	/**
	 * 
	 * @return the file name of the exported content.
	 */
	String getFileName();
	
	
	/**
	 * @return the content type of the exported content.
	 */
	String getContentType();
	
	/**
	 * Sets the content type of this report. 
	 * 
	 * @param reportContentType
	 */
	void setReportContentType(String reportContentType);

	
	/**
	 * Writes the content to the given output stream.
	 * 
	 * @param os
	 * @throws IOException
	 */
	void writeContent(OutputStream os) throws IOException;
}
