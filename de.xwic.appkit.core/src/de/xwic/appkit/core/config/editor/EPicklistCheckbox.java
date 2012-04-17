/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.config.editor.EPicklistCheckbox
 * Created on Nov 23, 2006 by Andra Iacovici
 *
 */
package de.xwic.appkit.core.config.editor;

/**
 * Defines a control used for displaying the picklist entries in checkbox format. 
 * 
 * @author Andra Iacovici
 * @editortag plCheck
 */
public class EPicklistCheckbox extends ENoBackgroundField {
	private int cols = 1;

	/**
	 * @return Returns the cols.
	 */
	public int getCols() {
		return cols;
	}

	/**
	 * Sets the number of columns
	 * @param cols
	 *            The cols to set.
	 */
	public void setCols(int cols) {
		this.cols = cols;
	}
}
