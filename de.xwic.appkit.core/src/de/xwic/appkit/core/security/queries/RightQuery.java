/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.security.queries.RightQuery
 * Created on 09.08.2005
 *
 */
package de.xwic.appkit.core.security.queries;

import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.security.IRole;

/**
 * Query for IRight objects.
 * @author Florian Lippisch
 */
public class RightQuery extends EntityQuery {

	private IRole role = null;
	
	/**
	 * 
	 */
	public RightQuery() {
		
	}
	
	/**
	 * Constructor.
	 * @param role
	 */
	public RightQuery(IRole role) {
		this.role = role;
	}

	/**
	 * @return Returns the role.
	 */
	public IRole getRole() {
		return role;
	}

	/**
	 * @param role The role to set.
	 */
	public void setRole(IRole role) {
		this.role = role;
	}
	
	
	
}
