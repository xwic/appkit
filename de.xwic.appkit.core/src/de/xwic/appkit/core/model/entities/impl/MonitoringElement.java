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
package de.xwic.appkit.core.model.entities.impl;

import de.xwic.appkit.core.dao.Entity;
import de.xwic.appkit.core.model.entities.IMonitoringElement;

/**
 * Specifies entities and properties where the changes are monitored by the system.
 * 
 * @author Florian Lippisch
 */
public class MonitoringElement extends Entity implements IMonitoringElement {

	private String entityClassName = null;
	private String propertyName = null;
	private boolean monitored = false;
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.IMonitoringElement#getEntityClassName()
	 */
	public String getEntityClassName() {
		return entityClassName;
	}
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.IMonitoringElement#setEntityClassName(java.lang.String)
	 */
	public void setEntityClassName(String entityClassName) {
		this.entityClassName = entityClassName;
	}
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.IMonitoringElement#isMonitored()
	 */
	public boolean isMonitored() {
		return monitored;
	}
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.IMonitoringElement#setMonitored(boolean)
	 */
	public void setMonitored(boolean monitored) {
		this.monitored = monitored;
	}
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.IMonitoringElement#getPropertyName()
	 */
	public String getPropertyName() {
		return propertyName;
	}
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.IMonitoringElement#setPropertyName(java.lang.String)
	 */
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	
}
