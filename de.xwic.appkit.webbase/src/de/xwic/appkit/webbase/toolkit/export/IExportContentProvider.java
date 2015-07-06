/*******************************************************************************
 * Copyright 2015 xWic group (http://www.xwic.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *  
 *******************************************************************************/
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
	@Deprecated
	public static final String CONTENT_TYPE_PDF = "application/pdf";
	@Deprecated
	public static final String CONTENT_TYPE_XLS = "application/vnd.ms-excel";
	@Deprecated
	public static final String CONTENT_TYPE_RTF = "application/rtf";
	@Deprecated
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
