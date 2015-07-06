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
package de.xwic.appkit.core.security;

import java.util.Set;

import de.xwic.appkit.core.dao.IEntity;

/**
 * Bundles actions that can be assigned to one or more scopes.
 * 
 * @author Florian Lippisch
 */
public interface IActionSet extends IEntity {

	/**
	 * Returns the name of the action set.
	 * @return
	 */
	public String getName();
	
	/**
	 * Set the name of the action set.
	 * @param id
	 */
	public void setName(String name);
	
	/**
	 * Returns a set of actions assigned to this ActionSet.
	 * @return
	 */
	public Set<IEntity> getActions();
	
	/**
	 * Sets the list of actions assigned to this ActionSet.
	 * @param actions
	 */
	public void setActions(Set<IEntity> actions);
	
}
