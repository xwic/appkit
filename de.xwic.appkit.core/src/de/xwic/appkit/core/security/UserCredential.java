/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.security.UserCredential
 * Created on 09.08.2005
 *
 */
package de.xwic.appkit.core.security;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.ISecurityManager;
import de.xwic.appkit.core.security.daos.IUserDAO;

/**
 * Contains the user and his rights. Used as a container to transfer the
 * user and his rights to the client. 
 * @author Florian Lippisch
 */
public class UserCredential {

	private IUser user = null;
	private Set<ScopeActionKey> rights = null;
	
	// since there are no setter/getter for the cache,
	// it will not be transfered via the WebService 
	private Map<String, Integer> accessCache = new HashMap<String, Integer>(); 
	
	/**
	 * Constructor.
	 */
	public UserCredential() {
		
	}

	/**
	 * @param user2
	 */
	public UserCredential(IUser user) {
		this.user = user;
		
		IUserDAO userDAO = (IUserDAO)DAOSystem.getDAO(IUserDAO.class);
		this.rights = userDAO.buildAllRights(user);
	}

	/**
	 * 
	 * @param user
	 * @param rights
	 */
	public UserCredential(IUser user, Set<ScopeActionKey> rights) {
		this.user = user;
		this.rights = rights;
	}

	/**
	 * @return Returns the rights.
	 */
	public Set<ScopeActionKey> getRights() {
		return rights;
	}

	/**
	 * @param rights The rights to set.
	 */
	public void setRights(Set<ScopeActionKey> rights) {
		this.rights = rights;
	}

	/**
	 * @return Returns the user.
	 */
	public IUser getUser() {
		return user;
	}

	/**
	 * @param user The user to set.
	 */
	public void setUser(IUser user) {
		this.user = user;
	}

	/**
	 * Returns true if the user has the right.
	 * @param scope
	 * @param action
	 * @return
	 */
	public boolean hasRight(String scope, String action) {
		return rights.contains(new ScopeActionKey(scope, action));
	}

	/**
	 * @param scope
	 * @param subscope
	 * @return
	 */
	public int getAccess(String scope, String subscope) {
		
		String key = scope + "." + subscope;

		Integer access = accessCache.get(key);
		if (access == null) { 
			
			// get concrete rights
			int right = ISecurityManager.READ_WRITE; 	// Default right is ReadWrite 
			
			if (rights.contains(new ScopeActionKey(key, ISecurityManager.ACTION_WRITE))) {
				right = ISecurityManager.READ_WRITE;
			} else if (rights.contains(new ScopeActionKey(key, ISecurityManager.ACTION_READ))) {
				right = ISecurityManager.READ;
			} else if (rights.contains(new ScopeActionKey(key, ISecurityManager.ACTION_NONE))) {
				right = ISecurityManager.NONE;
			} else {
				
				// inherit default rights (if there are any)
				String globalKey = scope + ".*";
				if (rights.contains(new ScopeActionKey(globalKey, ISecurityManager.ACTION_WRITE))) {
					right = ISecurityManager.READ_WRITE;
				} else if (rights.contains(new ScopeActionKey(globalKey, ISecurityManager.ACTION_READ))) {
					right = ISecurityManager.READ;
				} else if (rights.contains(new ScopeActionKey(globalKey, ISecurityManager.ACTION_NONE))) {
					right = ISecurityManager.NONE;
				}			
				
			}
			access = new Integer(right);
			accessCache.put(key, access);
		}
		
		return access.intValue();
	}
	
}
