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
package de.xwic.appkit.core.access;

import java.util.HashSet;
import java.util.Set;

/**
 * Used to determine if a specified property is monitored.
 * @author Florian Lippisch
 */
public class ObjectMonitoringTypeProperties {

	private String entityType;
	private boolean typeMonitored = false;
	private Set<String> monitoredProperties = new HashSet<String>();
	
	/**
	 * Constructor.
	 * @param entityType
	 * @param monitoredProperties
	 */
	/*default */ ObjectMonitoringTypeProperties(String entityType) {
		this.entityType = entityType;
	}

	/**
	 * Update the monitoring flag of a property.
	 * @param propertyName
	 * @param monitored
	 */
	/*default*/ synchronized void update(String propertyName, boolean monitored) {
		if (monitored) {
			monitoredProperties.add(propertyName);
		} else {
			monitoredProperties.remove(propertyName);
		}
	}

	/**
	 * Udpate the monitoring flag for the whole type.
	 * @param monitored
	 */
	/*default*/ synchronized void update(boolean monitored) {
		this.typeMonitored = monitored;
	}

	/**
	 * Returns true if the specified property is monitored.
	 * @param propertyName
	 * @return
	 */
	public boolean isMonitored(String propertyName) {
		return typeMonitored && monitoredProperties.contains(propertyName);
	}

	/**
	 * @return the entityType
	 */
	public String getEntityType() {
		return entityType;
	}

	/**
	 * @return the typeMonitored
	 */
	public boolean isTypeMonitored() {
		return typeMonitored;
	}
	
}
