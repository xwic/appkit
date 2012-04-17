/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.config.editor.ENumberInputField
 * Created on Nov 27, 2006 by Administrator
 *
 */
package de.xwic.appkit.core.config.editor;

/**
 * A field used for parsing numbers
 * 
 * @author Andra Iacovici
 * @editortag number
 */
public class ENumberInputField extends EField {
	private String format = "double";

	/**
	 * @return Returns the number format.
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * The format of the number. default is "double"
	 * @param format
	 *            The number format to set.
	 */
	public void setFormat(String format) {
		this.format = format;
	}
}
