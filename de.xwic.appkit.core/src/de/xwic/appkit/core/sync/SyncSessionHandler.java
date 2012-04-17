/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.sync.SyncSessionHandler
 * Created on Feb 22, 2008 by Aron Cotrau
 *
 */
package de.xwic.appkit.core.sync;

import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.security.IUser;


/**
 * Handler for the SyncSession object, to make sure we use only one for the whole application.
 * @author Aron Cotrau
 */
public class SyncSessionHandler {

	private static ISyncSession session = null;
	private static ISyncProfile profile = null;
	
	static {
		IUser user = DAOSystem.getSecurityManager().getCurrentUser();
		profile = SyncManager.getInstance().createProfile(ISyncProfile.PROFILE_OUTLOOK, "DEFAULT", user);
		session = SyncManager.getInstance().createSyncSession(profile);
	}
	
	
	private SyncSessionHandler() {
		// block instanciation
	}
	
	/**
	 * returns the session
	 * @return
	 */
	public static ISyncSession getSession() {
		return session;
	}
	
	/**
	 * returns the profile
	 * @return
	 */
	public static ISyncProfile getProfile() {
		return profile;
	}
}
