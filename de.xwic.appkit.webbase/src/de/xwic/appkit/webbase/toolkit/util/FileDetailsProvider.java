/**
 * 
 */
package de.xwic.appkit.webbase.toolkit.util;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import de.jwic.base.ImageRef;

/**
 * Returns the appropriate icon for a given filetype.
 * @author lippisch
 */
public class FileDetailsProvider {

	private final static long SIZE_KB = 1024;
	private final static long SIZE_MB = SIZE_KB * 1024;
	private final static long SIZE_GB = SIZE_MB * 1024;
	private final static long SIZE_TB = SIZE_GB * 1024;
	
	private static Map<String, ImageRef> TYPE_TO_IMAGE_MAP = new HashMap<>();
	static {
		TYPE_TO_IMAGE_MAP.put("zip", ImageLibrary.FILEICON_M_ZIP);
		TYPE_TO_IMAGE_MAP.put("pdf", ImageLibrary.FILEICON_M_PDF);
		TYPE_TO_IMAGE_MAP.put("xls", ImageLibrary.FILEICON_M_XLS);
		TYPE_TO_IMAGE_MAP.put("xlsx", ImageLibrary.FILEICON_M_XLS);
		TYPE_TO_IMAGE_MAP.put("xlsm", ImageLibrary.FILEICON_M_XLS);
		TYPE_TO_IMAGE_MAP.put("doc", ImageLibrary.FILEICON_M_DOC);
		TYPE_TO_IMAGE_MAP.put("docx", ImageLibrary.FILEICON_M_DOC);
		TYPE_TO_IMAGE_MAP.put("zip", ImageLibrary.FILEICON_M_ZIP);
		TYPE_TO_IMAGE_MAP.put("gz", ImageLibrary.FILEICON_M_ZIP);
		TYPE_TO_IMAGE_MAP.put("tar", ImageLibrary.FILEICON_M_ZIP);
		TYPE_TO_IMAGE_MAP.put("ppt", ImageLibrary.FILEICON_M_PPT);
		TYPE_TO_IMAGE_MAP.put("pptx", ImageLibrary.FILEICON_M_PPT);
		TYPE_TO_IMAGE_MAP.put("jpg", ImageLibrary.FILEICON_M_IMG);
		TYPE_TO_IMAGE_MAP.put("jpeg", ImageLibrary.FILEICON_M_IMG);
		TYPE_TO_IMAGE_MAP.put("gif", ImageLibrary.FILEICON_M_IMG);
		TYPE_TO_IMAGE_MAP.put("png", ImageLibrary.FILEICON_M_IMG);
		TYPE_TO_IMAGE_MAP.put("bmp", ImageLibrary.FILEICON_M_IMG);
	}
	
	/**
	 * Returns the file icon for the given filename. If no special icon is found
	 * for the file extension, the general icon is returned.
	 * 
	 * @param filename
	 * @return
	 */
	public static ImageRef getFileIcon(String filename) {
		
		if (filename != null) {
			int lidx = filename.lastIndexOf('.');
			if (lidx != -1) {
				String ext = filename.substring(lidx + 1);
				ImageRef ref = TYPE_TO_IMAGE_MAP.get(ext.toLowerCase());
				if (ref != null) {
					return ref;
				}
			}
		}
		
		return ImageLibrary.FILEICON_M_GENERAL;
	}
	
	/**
	 * Converts the size of a file to a easier to read file-size, like
	 * 2048 to 2 KB.
	 * @param bytes
	 * @param nf
	 * @return
	 */
	public static String getFileSizeString(long bytes, NumberFormat nf) {
		
		if (bytes < SIZE_KB) {
			return nf.format(bytes) + " B";
		} else if (bytes < SIZE_MB) {
			return nf.format(((double)bytes) / (double)SIZE_KB) + " KB";
		} else if (bytes < SIZE_GB) {
			return nf.format(((double)bytes) / (double)SIZE_MB) + " MB";
		} else if (bytes < SIZE_TB) {
			return nf.format(((double)bytes) / (double)SIZE_GB) + " GB";
		} 
		return nf.format(((double)bytes) / (double)SIZE_TB) + " TB";
	}
	
}
