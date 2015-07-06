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
/**
 * 
 */
package de.xwic.appkit.core.security.impl;

import java.util.Date;

import de.xwic.appkit.core.dao.Entity;
import de.xwic.appkit.core.security.IUserSession;

/**
 * Used to store the username for cookie based, long term authentication.
 * The object is looked up by the key, which is stored in the cookie. 
 * @author Lippisch
 */
public class UserSession extends Entity implements IUserSession {

	private String key = null;
	private Date lastAccess = null;
	private String username = null;
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.impl.IUserSession#getKey()
	 */
	public String getKey() {
		return key;
	}
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.impl.IUserSession#setKey(java.lang.String)
	 */
	public void setKey(String key) {
		this.key = key;
	}
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.impl.IUserSession#getLastAccess()
	 */
	public Date getLastAccess() {
		return lastAccess;
	}
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.impl.IUserSession#setLastAccess(java.util.Date)
	 */
	public void setLastAccess(Date lastAccess) {
		this.lastAccess = lastAccess;
	}
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.impl.IUserSession#getUsername()
	 */
	public String getUsername() {
		return username;
	}
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.impl.IUserSession#setUsername(java.lang.String)
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
}
