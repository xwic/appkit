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
