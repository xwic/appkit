/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.connectors.sync.SyncManager
 * Created on 30.11.2007 by Mark Frewin
 *
 */
package de.xwic.appkit.core.sync;

import java.util.HashMap;
import java.util.Map;

import de.xwic.appkit.core.security.IUser;
import de.xwic.appkit.core.sync.impl.SyncProfile;
import de.xwic.appkit.core.sync.impl.SyncSession;

/**
 * @author Mark Frewin
 */
public class SyncManager {
	
	private static SyncManager instance = null;
	private Map<String, SyncProfile> syncProfiles;
	
	
	/**
	 * Private constructor to block instatiation from outside.
	 *
	 */
	private SyncManager() {
		//just to block instantiation
		syncProfiles = new HashMap<String, SyncProfile>();
	}
	
	/**
	 * 
	 * @return
	 */
	public static synchronized SyncManager getInstance() {
		if  (instance == null)
			instance = new SyncManager();
		
		return instance;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
	
	/**
	 * 
	 * @param applicationId
	 * @param clientId
	 * @param currentUser
	 * @return
	 */
	public ISyncProfile createProfile(String applicationId, String clientId, IUser user) {
		
		String key = SyncProfile.generateKey(applicationId, clientId, user);
						
		if (syncProfiles.containsKey(key)){
			return syncProfiles.get(key);
		} else {
			SyncProfile syncProfile = new SyncProfile(applicationId, clientId, user);
			syncProfiles.put(syncProfile.getKey(), syncProfile);
			return syncProfile;
		}
				
	}
	
	/**
	 * 
	 * @param profile
	 * @return
	 */
	public ISyncSession createSyncSession(ISyncProfile profile) {
		return new SyncSession(profile);
	}
	
}
