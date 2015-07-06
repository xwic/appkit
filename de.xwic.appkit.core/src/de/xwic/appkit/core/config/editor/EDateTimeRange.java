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
