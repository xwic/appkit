/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.config.editor.EEntityField
 * Created on Nov 22, 2006 by Administrator
 *
 */
package de.xwic.appkit.core.config.editor;

/**
 * 
 * A field that handles single attachments 
 * 
 * @author Ronny Pfretzschner
 * @editortag singleAttachment
 */
public class ESingleAttachmentField extends EField {

	private String fileName = null;
	private String fileTypes = null;
	
	
	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * the fileName
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the fileTypes
	 */
	public String getFileTypes() {
		return fileTypes;
	}

	/**
	 * the file types
	 * @param fileTypes the fileTypes to set
	 */
	public void setFileTypes(String fileTypes) {
		this.fileTypes = fileTypes;
	}


	

}
