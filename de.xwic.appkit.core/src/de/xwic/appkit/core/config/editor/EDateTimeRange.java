/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.config.editor.EDateTimeRange
 * Created on Oct 31, 2007 by Aron Cotrau
 *
 */
package de.xwic.appkit.core.config.editor;

import de.xwic.appkit.core.config.model.Property;


/**
 * 
 *
 * @author Aron Cotrau
 */
public class EDateTimeRange extends EField {

	private Property[] endProperty = null;
	private Property[] allDay = null;

	
	/**
	 * @return the endProperty
	 */
	public Property[] getEndProperty() {
		return endProperty;
	}

	
	/**
	 * @param endProperty the endProperty to set
	 */
	public void setEndProperty(Property[] endProperty) {
		this.endProperty = endProperty;
	}
	
	/**
	 * @return the allDay
	 */
	public Property[] getAllDay() {
		return allDay;
	}
	
	/**
	 * @param allDay the allDay to set
	 */
	public void setAllDay(Property[] allDay) {
		this.allDay = allDay;
	}
	
}
