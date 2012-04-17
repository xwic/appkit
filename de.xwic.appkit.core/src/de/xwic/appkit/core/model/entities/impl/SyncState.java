/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.model.entities.impl.SyncState
 * Created on Nov 28, 2007 by Aron Cotrau
 *
 */
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
	private int entityId;
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
	public int getEntityId() {
		return entityId;
	}

	/**
	 * @param entityId
	 *            the entityId to set
	 */
	public void setEntityId(int entityId) {
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
