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

	private String userLogonName;
	private int userId;
	
	/**
	 * Used for deserializing!
	 */
	public UniqueUserQuery() {
		
	}
	
	/**
	 * 
	 */
	public UniqueUserQuery(IUser user) {
		this.userLogonName = user.getLogonName();
		this.userId = user.getId();
	}
	
	/**
	 * @param userLogonName
	 * @param userId
	 */
	public UniqueUserQuery(String userLogonName, int userId) {
		this.userLogonName = userLogonName;
		this.userId = userId;
	}

	/**
	 * @return the logonName
	 */
	public String getUserLogonName() {
		return userLogonName;
	}
	
	/**
	 * @param userLogonName the userLogonName to set
	 */
	public void setUserLogonName(String userLogonName) {
		this.userLogonName = userLogonName;
	}

	/**
	 * @return the id
	 */
	public int getUserId() {
		return userId;
	}
	
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}
}
