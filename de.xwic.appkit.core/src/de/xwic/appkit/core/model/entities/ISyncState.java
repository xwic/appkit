/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.model.entities.ISyncState
 * Created on Nov 28, 2007 by Aron Cotrau
 *
 */
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
