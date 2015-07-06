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
