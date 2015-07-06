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
package de.xwic.appkit.core.model.entities;

import java.util.Date;

import de.xwic.appkit.core.dao.IEntity;

/**
 * interface for the SyncState object.
 * 
 * @author Aron Cotrau
 */
public interface ISyncState extends IEntity {

	/**
	 * @return the applicationId
	 */
	public String getApplicationId();

	/**
	 * @param applicationId
	 *            the applicationId to set
	 */
	public void setApplicationId(String applicationId);

	/**
	 * @return the deviceId
	 */
	public String getDeviceId();

	/**
	 * @param deviceId
	 *            the deviceId to set
	 */
	public void setDeviceId(String deviceId);

	/**
	 * @return the entityId
	 */
	public int getEntityId();

	/**
	 * @param entityId
	 *            the entityId to set
	 */
	public void setEntityId(int entityId);

	/**
	 * @return the entityType
	 */
	public String getEntityType();

	/**
	 * @param entityType
	 *            the entityType to set
	 */
	public void setEntityType(String entityType);

	/**
	 * @return the extItemId
	 */
	public String getExtItemId();

	/**
	 * @param extItemId
	 *            the extItemId to set
	 */
	public void setExtItemId(String extItemId);

	/**
	 * @return the lastSyncTime
	 */
	public Date getLastSyncTime();

	/**
	 * @param lastSyncTime
	 *            the lastSyncTime to set
	 */
	public void setLastSyncTime(Date lastSyncTime);

	/**
	 * @return the state
	 */
	public int getState();

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(int state);

	/**
	 * @return the userId
	 */
	public String getUserId();

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId);
}
