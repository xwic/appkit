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
 * Defines the object where actions can aply to. A scope can define the entity
 * types of your application (i.e. Customer, Product).
 * 
 * Actions and ActionSets can be assigned to a scope to define which actions can
 * be applied to the scope.
 * 
 * @author Florian Lippisch
 */
public interface IScope extends IEntity {
	
	/**
	 * Returns true if the specified action can be assigned to this scope.
	 * @param action
	 * @return
	 */
	public boolean isActionAllowed(IAction action);
	
	/**
	 * Get the name.
	 * @return
	 */
	public String getName();
	/**
	 * Set the name.
	 * @param name
	 */
	public void setName(String name);
	
	/**
	 * Returns the description.
	 * @return
	 */
	public String getDescription();
	
	/**
	 * Sets the description of the scope.
	 * @param description
	 */
	public void setDescription(String description);
	
	/**
	 * Returns the directly assigned actions.
	 * @return Set of IAction
	 */
	public Set<IEntity> getActions();
	
	/**
	 * Sets the set of actions assigned to the scope.
	 * @param actions
	 */
	public void setActions(Set<IEntity> actions);
	
	/**
	 * Returns the ActionSets assigned to this scope.
	 * @return
	 */
	public Set<IEntity> getActionSets();
	
	/**
	 * Sets the ActionSets assigned to this scope.
	 * @param actionSets
	 */
	public void setActionSets(Set<IEntity> actionSets);
	
	/**
	 * Returns a set of all actions that are assigned to the scope. This includes
	 * the actions of all ActionSets and directly assigned actions.
	 * @return
	 */
	public Set<IEntity> getAllActions();
	
	
}
