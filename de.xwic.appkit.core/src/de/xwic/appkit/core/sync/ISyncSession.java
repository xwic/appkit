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
	public void setSynchronized(String syncItemId, String internalEntityType, long internalEntityId);

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
	public int getSyncState(String entityType, long entityId);
}
