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
package de.xwic.appkit.core.security.impl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import de.xwic.appkit.core.dao.Entity;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.security.IAction;
import de.xwic.appkit.core.security.IActionSet;
import de.xwic.appkit.core.security.IScope;

/**
 * @author Florian Lippisch
 */
public class Scope extends Entity implements IScope {

	private String name = null;
	private String description = null;
	private Boolean securedScope;
	private Set<IEntity> actions = null;
	private Set<IEntity> actionSets = null;
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.IScope#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.IScope#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.IScope#getDescription()
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.IScope#setDescription(java.lang.String)
	 */
	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.IScope#getActions()
	 */
	@Override
	public Set<IEntity> getActions() {
		if (actions == null) {
			actions = new HashSet<IEntity>();
		}
		return actions;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.IScope#setActions(java.util.Set)
	 */
	@Override
	public void setActions(Set<IEntity> actions) {
		this.actions = actions;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.IScope#getActionSets()
	 */
	@Override
	public Set<IEntity> getActionSets() {
		if (actionSets == null) {
			actionSets = new HashSet<IEntity>();
		}
		return actionSets;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.IScope#setActionSets(java.util.Set)
	 */
	@Override
	public void setActionSets(Set<IEntity> actionSets) {
		this.actionSets = actionSets;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.IScope#getAllActions()
	 */
	@Override
	public Set<IEntity> getAllActions() {
		HashSet<IEntity> allActions = new HashSet<IEntity>();
		allActions.addAll(getActions());
		for (Iterator<?> it = getActionSets().iterator(); it.hasNext(); ) {
			IActionSet as = (IActionSet)it.next();
			allActions.addAll(as.getActions());
		}
		return allActions;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.IScope#isActionAllowed(de.xwic.appkit.core.security.IAction)
	 */
	@Override
	public boolean isActionAllowed(IAction action) {
		return getAllActions().contains(action);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.IScope#getRestrictGrantToPeers()
	 */
	@Override
	public Boolean getSecuredScope() {
		return securedScope;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.IScope#setRestrictGrantToPeers(java.lang.Boolean)
	 */
	@Override
	public void setSecuredScope(Boolean securedScope) {
		this.securedScope = securedScope;
	}
}
