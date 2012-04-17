/*
 * Copyright 2005 pol GmbH
 *
 * de.pol.crm.fileservice.hbn.HbnFile
 * Created on 08.09.2005
 *
 */
package de.xwic.appkit.core.file.impl.hbn;

import java.sql.Blob;

/**
 * POJO used to store and restore a file in the database.
 * @author Florian Lippisch
 */
public class HbnFile {
	
	private int id = 0;
	private String filename = null;
	private String contentType = null;
	private long filesize = 0;
	private Blob data = null;
	
	
	/**
	 * @return Returns the data.
	 */
	public Blob getData() {
		return data;
	}
	/**
	 * @param data The data to set.
	 */
	public void setData(Blob data) {
		this.data = data;
	}
	/**
	 * @return Returns the filename.
	 */
	public String getFilename() {
		return filename;
	}
	/**
	 * @param filename The filename to set.
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}
	/**
	 * @return Returns the filesize.
	 */
	public long getFilesize() {
		return filesize;
	}
	/**
	 * @param filesize The filesize to set.
	 */
	public void setFilesize(long filesize) {
		this.filesize = filesize;
	}
	/**
	 * @return Returns the id.
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * Returns the content type.
	 * @return
	 */
	public String getContentType() {
		return contentType;
	}
	/**
	 * Sets the content type.
	 * @param contentType
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

}
