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

/**
 * Listener for AccessHandler event.
 * @author Florian Lippisch
 */
public interface AccessListener {

	/**
	 * Notification that an entity has been deleted.
	 * @param event
	 */
	public void entityDeleted(AccessEvent event);
	
	/**
	 * Notification that an entity has been updated.
	 * @param event
	 */
	public void entityUpdated(AccessEvent event);
	
	/**
	 * Notification that an entity has been deleted using softDelete.
	 * @param event
	 */
	public void entitySoftDeleted(AccessEvent event);
	
}
