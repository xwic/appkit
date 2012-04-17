/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.config.editor.EDateRange
 * Created on Mar 2, 2007 by Aron Cotrau
 *
 */
package de.xwic.appkit.core.config.editor;

import java.util.Date;

/**
 * Defines a control that can be used to filter for dates which are between 2 already
 * set dates.
 * 
 * @editortag dateRange
 * @author Aron Cotrau
 */
public class EDateRange extends EField {
	private String toProperty = "";

	/**
	 * @return the toProperty
	 */
	public String getToProperty() {
		return toProperty;
	}

	/**
	 * This is the highest {@link Date} type property
	 * 
	 * @param toProperty the toProperty to set
	 */
	public void setToProperty(String toProperty) {
		this.toProperty = toProperty;
	}
	
}
