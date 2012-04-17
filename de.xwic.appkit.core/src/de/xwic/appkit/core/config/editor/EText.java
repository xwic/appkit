/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.config.editor.EText
 * Created on 10.11.2006 by Florian Lippisch
 *
 */
package de.xwic.appkit.core.config.editor;

/**
 * Creates a text control to input plain text. The control can be created as 
 * multiline field.
 * 
 * @editortag text
 * @author Florian Lippisch
 */
public class EText extends ERegExField {

	private boolean multiline = false;
	
	/**
	 * @return the multiline
	 */
	public boolean isMultiline() {
		return multiline;
	}

	/**
	 * @param multiline the multiline to set
	 * @default false
	 */
	public void setMultiline(boolean multiline) {
		this.multiline = multiline;
	}
}
