/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.security.IAction
 * Created on 02.08.2005
 *
 */
package de.xwic.appkit.core.security;

import de.xwic.appkit.core.dao.IEntity;

/**
 * Represents an action that can be done on a scope.
 * 
 * @author Florian Lippisch
 */
public interface IAction extends IEntity {

	/**
	 * Returns the name.
	 * @return
	 */
	public String getName();
	
	/**
	 * Set the name.
	 * @param name
	 */
	public void setName(String name);
	
}
