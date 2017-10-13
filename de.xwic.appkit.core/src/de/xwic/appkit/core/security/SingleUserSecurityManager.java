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
package de.xwic.appkit.core.security;

import java.util.Set;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import de.xwic.appkit.core.ApplicationData;
import de.xwic.appkit.core.dao.ISecurityManager;

/**
 * Provides security information for a single user.
 * @author Florian Lippisch
 */
public class SingleUserSecurityManager implements ISecurityManager {

	private UserCredential credential = null;
	
	/**
	 * Constructor.
	 * @param user
	 */
	public SingleUserSecurityManager(UserCredential credential) {
		this.credential = credential;
	}

	/**
	 * Constructor.
	 * @param user
	 */
	public SingleUserSecurityManager(IUser user, Set<ScopeActionKey> allRights) {
		credential = new UserCredential(user, allRights);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.ISecurityManager#getCurrentUser()
	 */
	public IUser getCurrentUser() {
		return credential.getUser();
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.ISecurityManager#hasRight(java.lang.String, java.lang.String)
	 */
	public boolean hasRight(String scope, String action) {
		
		return credential.hasRight(scope, action);
		
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.ISecurityManager#getAccess(java.lang.String, java.lang.String)
	 */
	public int getAccess(String scope, String subscope) {
		return credential.getAccess(scope, subscope);
	}

	/**
	 * @return the credential
	 */
	public UserCredential getUserCredential() {
		return credential;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.ISecurityManager#logon(java.lang.String, java.lang.String)
	 */
	public boolean logon(String username, String password) {
		throw new UnsupportedOperationException("A logon is not supported by this security manager.");
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.ISecurityManager#detectUser()
	 */
	@Override
	public IUser detectUser() {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.ISecurityManager#logout()
	 */
	public void logout() {
		throw new UnsupportedOperationException("Logout is not supported by this security manager.");
	}

	public void dropCredentialFromCache(IUser user) {
		throw new NotImplementedException();
		
	}

	public void dropUserFromCache(IUser user) {
		throw new NotImplementedException();
		
	}
	
	public IUser findUser(String logonName) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.ISecurityManager#rememberActiveUser()
	 */
	@Override
	public void rememberActiveUser() {
		// TODO Auto-generated method stub
		
	}
}
