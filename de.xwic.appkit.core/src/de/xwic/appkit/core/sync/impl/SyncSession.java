/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.connector.impl.sync.SyncSession
 * Created on 30.11.2007 by Mark Frewin
 *
 */
package de.xwic.appkit.core.sync.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.DataAccessException;
import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.dao.UseCase;
import de.xwic.appkit.core.model.daos.ISyncStateDAO;
import de.xwic.appkit.core.model.entities.ISyncState;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.core.sync.ISyncProfile;
import de.xwic.appkit.core.sync.ISyncSession;
import de.xwic.appkit.core.sync.ISynchronizable;
import de.xwic.appkit.core.ucexec.GetServerDateUseCase;

/**
 * @author Mark Frewin
 */
public class SyncSession implements ISyncSession {

	private ISyncProfile syncProfile;
	private Map<String, Integer> syncMap;
	private Map<String, Integer> entitySyncMap;
	
	/**
	 * Constructor
	 * 
	 * @param syncProfile
	 */
	public SyncSession(ISyncProfile syncProfile) {
		this.syncProfile = syncProfile;
		refreshSyncCache();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.connector.api.sync.ISyncSession#refreshSyncCache()
	 */
	public void refreshSyncCache() {
		syncMap = new HashMap<String, Integer>();
		entitySyncMap = new HashMap<String, Integer>();

		ISyncStateDAO ssDao = (ISyncStateDAO) DAOSystem.getDAO(ISyncStateDAO.class);

		PropertyQuery query = new PropertyQuery();
		query.addEquals("applicationId", syncProfile.getApplicationId());
		query.addEquals("deviceId", syncProfile.getDeviceId());
		query.addEquals("userId", "" + syncProfile.getUser().getId());

		EntityList results = ssDao.getEntities(null, query);

		for (Iterator<Object> it = results.iterator(); it.hasNext();) {
			ISyncState syncState = (ISyncState) it.next();
			
			int state = syncState.getState();
			
			syncMap.put(syncState.getExtItemId(), new Integer(state));
			entitySyncMap.put(syncState.getEntityType() + " " + syncState.getEntityId(), new Integer(state));
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.connector.api.sync.ISyncSession#getSyncState(de.xwic.appkit.core.connector.api.sync.ISynchronizable)
	 */
	public int getSyncState(ISynchronizable syncItem) {

		if (syncItem == null) {
			return ISyncSession.SYNC_STATE_UNKNOWN;
		}

		Integer cacheState = syncMap.get(syncItem.getExternalId());

		if (cacheState == null) {
			return ISyncSession.SYNC_STATE_UNKNOWN;
		} else {
			return cacheState.intValue();
		}

	}

	/**
	 * @param entityType
	 * @param entityId
	 * @return
	 */
	public int getSyncState(String entityType, int entityId) {

		String key = entityType + " " + entityId;
		Integer cacheState = entitySyncMap.get(key);

		if (cacheState == null) {
			return ISyncSession.SYNC_STATE_UNKNOWN;
		} else {
			return cacheState.intValue();
		}
	}
	
	/**
	 * @param entity
	 * @return the sync state for the given entity
	 */
	public int getSyncState(IEntity entity) {
		
		if (entity == null) {
			return ISyncSession.SYNC_STATE_UNKNOWN;
		}

		String key = entity.type().getName() + " " + entity.getId();
		Integer cacheState = entitySyncMap.get(key);

		if (cacheState == null) {
			return ISyncSession.SYNC_STATE_UNKNOWN;
		} else {
			return cacheState.intValue();
		}
	}
	
	public void disconnectItems(String syncItemId) {

		ISyncState syncState = findSyncStateEntity(syncItemId);
		ISyncStateDAO ssDAO = (ISyncStateDAO) DAOSystem.getDAO(ISyncStateDAO.class);

		if (syncState == null) {
			// -> not found -> create
			syncState = (ISyncState) ssDAO.createEntity();
			syncState.setApplicationId(syncProfile.getApplicationId());
			syncState.setDeviceId(syncProfile.getDeviceId());
			syncState.setUserId(Integer.toString(syncProfile.getUser().getId()));
			syncState.setExtItemId(syncItemId);
		
		}
		
		syncState.setState(ISyncSession.SYNC_STATE_NOT_SYNCD);
		ssDAO.update(syncState);
		
		int state = syncState.getState();
		syncMap.put(syncState.getExtItemId(), new Integer(state));
		entitySyncMap.put(syncState.getEntityType() + " " + syncState.getEntityId(), new Integer(state));		
		
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.sync.ISyncSession#disconnectItems(de.xwic.appkit.core.sync.ISynchronizable,
	 *      de.xwic.appkit.core.dao.IEntity)
	 */
	public void disconnectItems(ISynchronizable syncItem) {
		disconnectItems(syncItem.getExternalId());
	}

	public void setSynchronized(String syncItemId, String internalEntityType, int internalEntityId) {

		int state = ISyncSession.SYNC_STATE_SYNCD;
		ISyncStateDAO ssDAO = (ISyncStateDAO) DAOSystem.getDAO(ISyncStateDAO.class);

		// find entity
		ISyncState syncState = findSyncStateEntity(syncItemId);

		if (syncState == null) {
			// -> not found -> create
			syncState = (ISyncState) ssDAO.createEntity();
			syncState.setApplicationId(syncProfile.getApplicationId());
			syncState.setDeviceId(syncProfile.getDeviceId());
			syncState.setUserId(Integer.toString(syncProfile.getUser().getId()));
			syncState.setExtItemId(syncItemId);
		}

		// update common attributes
		syncState.setEntityType(internalEntityType);
		syncState.setEntityId(internalEntityId);
		syncState.setState(state);
		
		// get the serverside date
		UseCase serverDateUseCase = new GetServerDateUseCase();
		Object result = serverDateUseCase.execute();
		Date date = null;
		if (result instanceof Calendar) {
			date = ((Calendar)result).getTime(); 
		} else if (result instanceof Date) {
			date = (Date)result;
		} else {
			throw new DataAccessException("Error! Can not recieve the server date: " + result);
		}
		syncState.setLastSyncTime(date);
		
		if (null != syncProfile.getUser()) {
			syncState.setUserId(String.valueOf(syncProfile.getUser().getId()));
		}

		// -> store it
		ssDAO.update(syncState);
		
		syncMap.put(syncItemId, new Integer(state));
		entitySyncMap.put(syncState.getEntityType() + " " + syncState.getEntityId(), new Integer(state));
	}

	public void setSynchronized(String syncItemId, IEntity internalEntity) {
		
		if (syncItemId == null || internalEntity == null) {
			return;
		}

		setSynchronized(syncItemId, internalEntity.type().getName(), internalEntity.getId());
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.sync.ISyncSession#setSynchronized(de.xwic.appkit.core.sync.ISynchronizable,
	 *      de.xwic.appkit.core.dao.IEntity)
	 */
	public void setSynchronized(ISynchronizable syncItem, IEntity internalEntity) {

		if (syncItem == null || internalEntity == null) {
			return;
		}

		setSynchronized(syncItem.getExternalId(), internalEntity);
	}

	/**
	 * 
	 * @param syncItemId
	 * @return
	 */
	private ISyncState findSyncStateEntity(String syncItemId) {
		PropertyQuery query = new PropertyQuery();
		query.addEquals("applicationId", syncProfile.getApplicationId());
		query.addEquals("deviceId", syncProfile.getDeviceId());
		query.addEquals("userId", "" + syncProfile.getUser().getId());
		query.addEquals("extItemId", syncItemId);

		ISyncStateDAO ssDAO = (ISyncStateDAO) DAOSystem.getDAO(ISyncStateDAO.class);
		EntityList results = ssDAO.getEntities(null, query);

		if (results.getTotalSize() > 0) {
			// -> found
			return (ISyncState) results.get(0);
		}

		return null;
	}

}
