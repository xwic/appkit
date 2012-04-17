/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.security.IRight
 * Created on 02.08.2005
 *
 */
package de.xwic.appkit.core.security;

import de.xwic.appkit.core.dao.IEntity;

/**
 * Defines an action on a scope assigned to a role. The scope may be
 * <code>null</code>. 
 * @author Florian Lippisch
 */
public interface IRight extends IEntity {

	/**
	 * Returns the role this right is assigned to.
	 * @return
	 */
	public IRole getRole();
	
	/**
	 * Set the role this right is assigned to.
	 * @param role
	 */
	public void setRole(IRole role);
	
	/**
	 * Returns the scope this right applies to.
	 * @return
	 */
	public IScope getScope();
	
	/**
	 * Sets the scope this right applies to.
	 * @param scope
	 */
	public void setScope(IScope scope);
	
	/**
	 * Returns the action on the scope.
	 * @return
	 */
	
	public IAction getAction();
	/**
	 * Sets the action on the scope.
	 * @param action
	 */
	public void setAction(IAction action);
	
}
