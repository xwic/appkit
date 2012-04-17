/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.client.uitools.editors.mapper.WidgetProperty
 * Created on 10.11.2006 by Florian Lippisch
 *
 */
package de.xwic.appkit.webbase.editors.mappers;

import de.jwic.base.IControl;
import de.xwic.appkit.core.config.model.Property;

/**
 * @author Florian Lippisch
 *
 */
public class ControlProperty {

	private IControl control = null;
	private Property[] property = null;
	private boolean infoMode = false; 
	
	/**
	 * @param widget2
	 * @param property2
	 */
	public ControlProperty(IControl control, Property[] property) {
		this(control, property, false);
	}
	
	/**
	 * @param control
	 * @param property
	 * @param infoMode
	 */
	public ControlProperty(IControl control, Property[] property, boolean infoMode) {
		this.control = control;
		this.property = property;		
	}
	
	/**
	 * @return the control
	 */
	public IControl getWidget() {
		return control;
	}
	/**
	 * @param control the control to set
	 */
	public void setWidget(IControl control) {
		this.control = control;
	}
	/**
	 * @return the property
	 */
	public Property[] getProperty() {
		return property;
	}
	/**
	 * @param property the property to set
	 */
	public void setProperty(Property[] property) {
		this.property = property;
	}

	/**
	 * @return the infoMode
	 */
	public boolean isInfoMode() {
		return infoMode;
	}

	/**
	 * @param infoMode the infoMode to set
	 */
	public void setInfoMode(boolean infoMode) {
		this.infoMode = infoMode;
	}
}
