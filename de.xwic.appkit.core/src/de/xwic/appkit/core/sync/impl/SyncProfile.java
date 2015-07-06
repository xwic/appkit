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
package de.xwic.appkit.core.sync.impl;

import de.xwic.appkit.core.security.IUser;
import de.xwic.appkit.core.sync.ISyncProfile;

/**
 * @author Mark Frewin
 */
public class SyncProfile implements ISyncProfile {
		
	private String applicationId;
	private String clientId;
	private IUser user;
	
	/**
	 * 
	 * @param applicationId
	 * @param clientId
	 * @param user
	 */
	public SyncProfile (String applicationId, String clientId, IUser user) {
		this.applicationId = applicationId;
		this.clientId = clientId;
		this.user = user;
		
	}
	
	/**
	 * Generates a unique key
	 * 
	 * @param applicationId
	 * @param clientId
	 * @param user
	 * @return
	 */
	public static String generateKey (String application_Id, String client_Id, IUser a_user) {
		String userLogon = a_user != null ? a_user.getLogonName() : "";
		return "[app]" + application_Id + "[client]" + client_Id + "[user]" + userLogon;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.core.connector.api.sync.ISyncProfile#getKey()
	 */
	public String getKey() {
		return SyncProfile.generateKey(applicationId, clientId, user);
	}

	/**
	 * @return the applicationId
	 */
	public String getApplicationId() {
		return applicationId;
	}

	/**
	 * @return the clientId
	 */
	public String getDeviceId() {
		return clientId;
	}

	/**
	 * @return the user
	 */
	public IUser getUser() {
		return user;
	}
	
}
