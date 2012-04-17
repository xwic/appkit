/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.connectors.sync.ISyncSession
 * Created on 30.11.2007 by Mark Frewin
 *
 */
package de.xwic.appkit.core.sync;

import de.xwic.appkit.core.dao.IEntity;

/**
 * @author Mark Frewin
 */
public interface ISyncSession {

	/**
	 * item is ignored from synchronize process
	 */
	public final static int SYNC_STATE_NOT_SYNCD = 0;
	/**
	 * the item has been synchronizedd
	 */
	public final static int SYNC_STATE_SYNCD = 1;
	/**
	 * there is conflict during the automatic synchronization
	 * 
	 */
	public final static int SYNC_STATE_CONFLICT = 2;
	/**
	 * the new item does not yet have a sync setting
	 * 
	 */
	public final static int SYNC_STATE_UNKNOWN = 3;

	/**
	 * 
	 * 
	 */
	public void refreshSyncCache();

	/**
	 * 
	 * @param syncItem
	 * @return
	 */
	public int getSyncState(ISynchronizable syncItem);

	/**
	 * 
	 * @param state
	 */
	public void setSynchronized(ISynchronizable syncItem, IEntity internalEntity);
	
	/**
	 * @param syncItemId
	 * @param internalEntity
	 */
	public void setSynchronized(String syncItemId, IEntity internalEntity);
	
	/**
	 * @param syncItemId
	 * @param internalEntityType
	 * @param internalEntityId
	 */
	public void setSynchronized(String syncItemId, String internalEntityType, int internalEntityId);

	/**
	 * Disconnects an external item from an internal item. The item is no longer
	 * synchronized and set to the state NOT_SYNCHRONIZED.
	 * 
	 */
	public void disconnectItems(ISynchronizable syncItem);
	
	/**
	 * Disconnects an external item from an internal item. The item is no longer
	 * synchronized and set to the state NOT_SYNCHRONIZED.
	 * 
	 */
	public void disconnectItems(String syncItemId);

	/**
	 * @param entity
	 * @return the sync state for the given entity
	 */
	public int getSyncState(IEntity entity);
	
	/**
	 * @param entityType
	 * @param entityId
	 * @return the sync state for the given parameters. refers to ISISS entities
	 */
	public int getSyncState(String entityType, int entityId);
}
