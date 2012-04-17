/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.security.IActionSet
 * Created on 02.08.2005
 *
 */
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
