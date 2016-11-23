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
package de.xwic.appkit.core.model.entities.impl;

import java.util.Date;

import de.xwic.appkit.core.dao.Entity;
import de.xwic.appkit.core.model.entities.ISyncState;
import de.xwic.appkit.core.sync.ISyncSession;

/**
 * @author Aron Cotrau
 */
public class SyncState extends Entity implements ISyncState {

	private String userId;
	private String applicationId;
	private String deviceId;
	private String extItemId;
	private int state = ISyncSession.SYNC_STATE_UNKNOWN;
	private String entityType;
	private long entityId;
	private Date lastSyncTime;

	/**
	 * default constructor.
	 * <p>
	 * Just for Hibernate and Webservice.
	 * <p>
	 * 
	 * Use this Constructor only at emergency because then you get just an empty
	 * object, <br>
	 * without check of the "must to have" properties.
	 */
	public SyncState() {
	}

	/**
	 * @return the applicationId
	 */
	public String getApplicationId() {
		return applicationId;
	}

	/**
	 * @param applicationId
	 *            the applicationId to set
	 */
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	/**
	 * @return the deviceId
	 */
	public String getDeviceId() {
		return deviceId;
	}

	/**
	 * @param deviceId
	 *            the deviceId to set
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	/**
	 * @return the entityId
	 */
	public long getEntityId() {
		return entityId;
	}

	/**
	 * @param entityId
	 *            the entityId to set
	 */
	public void setEntityId(long entityId) {
		this.entityId = entityId;
	}

	/**
	 * @return the entityType
	 */
	public String getEntityType() {
		return entityType;
	}

	/**
	 * @param entityType
	 *            the entityType to set
	 */
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	/**
	 * @return the extItemId
	 */
	public String getExtItemId() {
		return extItemId;
	}

	/**
	 * @param extItemId
	 *            the extItemId to set
	 */
	public void setExtItemId(String extItemId) {
		this.extItemId = extItemId;
	}

	/**
	 * @return the lastSyncTime
	 */
	public Date getLastSyncTime() {
		return lastSyncTime;
	}

	/**
	 * @param lastSyncTime
	 *            the lastSyncTime to set
	 */
	public void setLastSyncTime(Date lastSyncTime) {
		this.lastSyncTime = lastSyncTime;
	}

	/**
	 * @return the state
	 */
	public int getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(int state) {
		this.state = state;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

}
