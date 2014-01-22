package de.xwic.appkit.webbase.utils;

import javax.activation.MimetypesFileTypeMap;

/**
 * Utility class for determinig the mime type for a filename. It just looks at the file's extension
 * 
 * @author Andrei Pat
 * 
 */
public class MimeTypeUtil {
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
		return mimeTypes.getContentType(filename);
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
