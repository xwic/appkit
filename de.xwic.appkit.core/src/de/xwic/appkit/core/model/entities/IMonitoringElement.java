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
package de.xwic.appkit.core.model.entities;

import de.xwic.appkit.core.dao.IEntity;

/**
 * Specifies entities and properties where the changes are monitored by the system.
 * 
 * @author Florian Lippisch
 */
public interface IMonitoringElement extends IEntity {

	/**
	 * @return the entityClassName
	 */
	public abstract String getEntityClassName();

	/**
	 * @param entityClassName the entityClassName to set
	 */
	public abstract void setEntityClassName(String entityClassName);

	/**
	 * @return the monitored
	 */
	public abstract boolean isMonitored();

	/**
	 * @param monitored the monitored to set
	 */
	public abstract void setMonitored(boolean monitored);

	/**
	 * @return the propertyName
	 */
	public abstract String getPropertyName();

	/**
	 * @param propertyName the propertyName to set
	 */
	public abstract void setPropertyName(String propertyName);

}