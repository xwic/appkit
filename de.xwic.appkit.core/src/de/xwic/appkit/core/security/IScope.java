/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.security.IScope
 * Created on 02.08.2005
 *
 */
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
