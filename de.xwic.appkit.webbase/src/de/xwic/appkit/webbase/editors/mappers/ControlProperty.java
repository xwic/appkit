/*******************************************************************************
 * Copyright 2015 xWic group (http://www.xwic.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *  
 *******************************************************************************/
package de.xwic.appkit.webbase.editors.mappers;

import de.jwic.base.IControl;
import de.xwic.appkit.core.config.model.Property;

/**
 * @author Florian Lippisch
 *
 */
public class ControlProperty<T extends IControl> {

	private T control = null;
	private Property[] property = null;
	private boolean infoMode = false; 
	
	/**
	 * @param widget2
	 * @param property2
	 */
	public ControlProperty(T control, Property[] property) {
		this(control, property, false);
	}
	
	/**
	 * @param control
	 * @param property
	 * @param infoMode
	 */
	public ControlProperty(T control, Property[] property, boolean infoMode) {
		this.control = control;
		this.property = property;	
		this.infoMode = infoMode;
	}
	
	/**
	 * @return the control
	 */
	public T getWidget() {
		return control;
	}
	/**
	 * @param control the control to set
	 */
	public void setWidget(T control) {
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
