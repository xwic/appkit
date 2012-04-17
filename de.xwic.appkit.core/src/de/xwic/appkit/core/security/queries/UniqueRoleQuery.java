/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.security.queries.UniqueActionQuery
 * Created on 08.08.2005
 *
 */
package de.xwic.appkit.core.security.queries;

import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.security.IRole;

/**
 * @author Florian Lippisch
 */
public class UniqueRoleQuery extends EntityQuery {

	private IRole role = null;
	
	/**
	 * Default Constructor.
	 */
	public UniqueRoleQuery() {
		
	}

	/**
	 * 
	 */
	public UniqueRoleQuery(IRole role ) {
		this.role = role;
	}

	/**
	 * @return Returns the action.
	 */
	public IRole getRole() {
		return role;
	}

	/**
	 * @param action The action to set.
	 */
	public void setRole(IRole role) {
		this.role = role;
	}

}
