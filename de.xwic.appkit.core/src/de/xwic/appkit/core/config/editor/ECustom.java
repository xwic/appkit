/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.config.editor.ECustom
 * Created on 28.11.2006 by Florian Lippisch
 *
 */
package de.xwic.appkit.core.config.editor;

/**
 * Custom tag for controls which have the builders defined through the custom
 * extension point.
 * 
 * @author Florian Lippisch
 * @editortag custom
 */
public class ECustom extends EField {

	private String extension = null;

	/**
	 * @return the extension
	 */
	public String getExtension() {
		return extension;
	}

	/**
	 * @param extension the extension to set
	 */
	public void setExtension(String extension) {
		this.extension = extension;
	}
	
}
