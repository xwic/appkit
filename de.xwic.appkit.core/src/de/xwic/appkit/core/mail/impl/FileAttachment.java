package de.xwic.appkit.core.mail.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.xwic.appkit.core.mail.IAttachment;

public class FileAttachment implements IAttachment {

	protected final Log log = LogFactory.getLog(getClass().getName());

	private final File file;

	public FileAttachment(File file) {
		this.file = file;
	}

	public String getContentType() {
		return "";
	}

	public byte[] getData() {
		byte[] data = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			data = new byte[(int) file.length()];
			fis.read(data);
			fis.close();
		} catch (FileNotFoundException e) {
			log.error("File with path \"" + file.getAbsolutePath() + "\" not valid: " + e.getMessage(), e);
		} catch (IOException e) {
			log.error("File with path \"" + file.getAbsolutePath() + "\" not valid: " + e.getMessage(), e);
		}

		return data;
	}

	public String getFileName() {
		return file.getName();
	}
}
