/*******************************************************************************
 * Copyright 2015 xWic group (http://www.xwic.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *  
 *******************************************************************************/
package de.xwic.appkit.core.security.queries;

import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.security.IUser;

/**
 * @author Florian Lippisch
 */
public class UniqueUserQuery extends EntityQuery {

	private String userLogonName;
	private long userId;
	
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
	public UniqueUserQuery(String userLogonName, long userId) {
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
	public long getUserId() {
		return userId;
	}
	
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}
}
