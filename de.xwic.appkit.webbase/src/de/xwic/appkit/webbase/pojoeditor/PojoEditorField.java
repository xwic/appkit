/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 * 
 * de.xwic.appkit.webbase.pojoeditor.EditorField 
 */

package de.xwic.appkit.webbase.pojoeditor;

import de.jwic.base.Control;

/**
 * @author Andrei Pat
 *
 */
public class PojoEditorField {

	public String label;
	public Control control;
	public String propertyName;

	/**
	 * 
	 */
	public PojoEditorField() {

	}

	/**
	 * @param label
	 * @param control
	 */
	public PojoEditorField(String label, Control control, String propertyName) {
		super();
		this.label = label;
		this.control = control;
		this.propertyName = propertyName;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label
	 *            the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return the control
	 */
	public Control getControl() {
		return control;
	}

	/**
	 * @param control
	 *            the control to set
	 */
	public void setControl(Control control) {
		this.control = control;
	}

	
	/**
	 * @return the propertyName
	 */
	public String getPropertyName() {
		return propertyName;
	}

	
	/**
	 * @param propertyName the propertyName to set
	 */
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
}