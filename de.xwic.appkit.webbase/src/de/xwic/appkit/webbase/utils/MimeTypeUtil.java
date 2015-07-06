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
package de.xwic.appkit.webbase.utils;

import javax.activation.MimetypesFileTypeMap;

/**
 * Utility class for determinig the mime type for a filename. It just looks at the file's extension
 * 
 * @author Andrei Pat
 * 
 */
public class MimeTypeUtil {
	private static final String GENERIC_MIME_TYPE = "application/octet-stream";
	private static final MimetypesFileTypeMap mimeTypes = new MimetypesFileTypeMap();
	static {
		init();
	}

	/**
	 * @param filename
	 *            name of the file (with extension)
	 * @return the mime type
	 */
	public static String getMimeTypeForFileName(String filename) {
		String mimeType = mimeTypes.getContentType(filename);
		if (mimeType != null) {
			return mimeType;
		} else {
			return GENERIC_MIME_TYPE;
		}
	}

	/**
	 * Init the mime type list
	 */
	public static void init() {
		mimeTypes.addMimeTypes("application/vnd.ms-excel xls xlt");
		mimeTypes.addMimeTypes("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet xlsx");
		mimeTypes.addMimeTypes("application/msword doc dot");
		mimeTypes.addMimeTypes("application/vnd.openxmlformats-officedocument.wordprocessingml.document docx");
		mimeTypes.addMimeTypes("application/pdf pdf");
		mimeTypes.addMimeTypes("application/rtf rtf");
		mimeTypes.addMimeTypes("text/csv csv");
		mimeTypes.addMimeTypes("application/vnd.ms-powerpoint ppt pps pot");
		mimeTypes.addMimeTypes("application/vnd.ms-outlook msg");
		mimeTypes.addMimeTypes("image/png png");
		mimeTypes.addMimeTypes("image/jpeg jpg jpe jpeg");
		mimeTypes.addMimeTypes("image/gif gif");
		mimeTypes.addMimeTypes("application/zip zip");
		mimeTypes.addMimeTypes("text/plain txt");
	}
}
