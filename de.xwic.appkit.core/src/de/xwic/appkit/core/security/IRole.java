/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.security.IRole
 * Created on 02.08.2005
 *
 */
package de.xwic.appkit.core.security;

import de.xwic.appkit.core.dao.IEntity;

/**
 * A role defines a set of rights that can be assigned to one or more users.
 * @author Florian Lippisch
 */
public interface IRole extends IEntity {

	/**
	 * Returns the name of the role
	 * @return
	 */
	public String getName();
	
	/**
	 * Set the name of the role.
	 * @param name
	 */
	public void setName(String name);
	
}
