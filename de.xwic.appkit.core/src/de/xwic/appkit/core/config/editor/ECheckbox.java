/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.config.editor.ECheckbox
 * Created on Nov 28, 2006 by Administrator
 *
 */
package de.xwic.appkit.core.config.editor;

/**
 * This tag defines the checkbox field.
 * 
 * @editortag checkbox
 * @author Andra Iacovici
 */
public class ECheckbox extends ENoBackgroundField {

	private String title = null;

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
}
