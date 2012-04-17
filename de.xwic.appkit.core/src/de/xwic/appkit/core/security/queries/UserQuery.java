/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.security.queries.UserQuery
 * Created on 02.08.2005
 *
 */
package de.xwic.appkit.core.security.queries;

import de.xwic.appkit.core.dao.EntityQuery;

/**
 * @author Florian Lippisch
 */
public class UserQuery extends EntityQuery {

	private String logonName = null;

	/**
	 * default constructor.
	 */
	public UserQuery() {
		
	}
	
	/**
	 * Search for the specified logonName.
	 * @param logonName
	 */
	public UserQuery(String logonName) {
		this.logonName = logonName;
	}
	
	/**
	 * @return Returns the logonName.
	 */
	public String getLogonName() {
		return logonName;
	}

	/**
	 * @param logonName The logonName to set.
	 */
	public void setLogonName(String logonName) {
		this.logonName = logonName;
	}
	
	
	
}
