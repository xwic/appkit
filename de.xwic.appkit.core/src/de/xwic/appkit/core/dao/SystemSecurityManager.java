package de.xwic.appkit.core.dao;

import de.xwic.appkit.core.dao.ISecurityManager;
import de.xwic.appkit.core.security.IUser;
import de.xwic.appkit.core.security.impl.User;

/**
 * SecurityManager implementation used during system initialization.
 * @author Florian Lippisch
 */
public class SystemSecurityManager implements ISecurityManager {

	public final static String USER = "SYSTEM";
	
	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.ISecurityManager#getCurrentUser()
	 */
	@Override
	public IUser getCurrentUser() {
		User user = new User();
		user.setName(USER);
		user.setLogonName(USER);
		return user;
	}

	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.ISecurityManager#hasRight(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean hasRight(String scope, String action) {
		return true;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.ISecurityManager#getAccess(java.lang.String, java.lang.String)
	 */
	@Override
	public int getAccess(String scope, String subscope) {
		return ISecurityManager.READ_WRITE;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.ISecurityManager#logon(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean logon(String username, String password) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.ISecurityManager#logout()
	 */
	@Override
	public void logout() {
		throw new UnsupportedOperationException();		
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.ISecurityManager#dropCredentialFromCache(de.xwic.appkit.core.security.IUser)
	 */
	@Override
	public void dropCredentialFromCache(IUser user) {
		throw new UnsupportedOperationException();		
		
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.ISecurityManager#dropUserFromCache(de.xwic.appkit.core.security.IUser)
	 */
	@Override
	public void dropUserFromCache(IUser user) {
		throw new UnsupportedOperationException();		
		
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.ISecurityManager#findUser(java.lang.String)
	 */
	@Override
	public IUser findUser(String logonName) {
		throw new UnsupportedOperationException();		
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.ISecurityManager#detectUser()
	 */
	@Override
	public IUser detectUser() {
		return null;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.ISecurityManager#rememberActiveUser()
	 */
	@Override
	public void rememberActiveUser() {
		
	}
}
