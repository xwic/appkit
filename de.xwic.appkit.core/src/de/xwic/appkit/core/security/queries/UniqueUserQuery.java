/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.security.queries.UniqueActionQuery
 * Created on 08.08.2005
 *
 */
package de.xwic.appkit.core.security.queries;

import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.security.IUser;

/**
 * @author Florian Lippisch
 */
public class UniqueUserQuery extends EntityQuery {

	private IUser user = null;
	
	/**
	 * Default Constructor.
	 */
	public UniqueUserQuery() {
		
	}

	/**
	 * 
	 */
	public UniqueUserQuery(IUser user ) {
		this.user = user;
	}

	/**
	 * @return Returns the action.
	 */
	public IUser getUser() {
		return user;
	}

	/**
	 * @param action The action to set.
	 */
	public void setUser(IUser user) {
		this.user = user;
	}

}
