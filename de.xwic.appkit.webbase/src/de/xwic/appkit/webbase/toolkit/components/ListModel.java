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
/**
 * 
 */
package de.xwic.appkit.webbase.toolkit.components;

import de.jwic.util.SerObservable;
import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.dao.IEntity;

/**
 * Model for List views.
 * 
 * @author Ronny Pfretzschner
 * 
 */
public abstract class ListModel extends SerObservable {

	private EntityQuery query;
	private int selectedEntryId;

	/**
	 * Creates a model.
	 * 
	 * @param query,
	 *            which acts as a filter for the list
	 */
	public ListModel(EntityQuery query) {
		this.query = query;
	}

	/**
	 * 
	 * @return the query for the list
	 */
	public EntityQuery getQuery() {
		return query;
	}

	/**
	 * 
	 * @param query
	 *            for the list
	 */
	public void setQuery(EntityQuery query) {
		this.query = query;
		setChanged();
		notifyObservers();
	}

	/**
	 * Performs a simple delete of the given entity.
	 * 
	 * @param entityId
	 */
	public void delete(int entityId) {
		try {
			IEntity entity = getDAO().getEntity(entityId);
			getDAO().delete(entity);
		} catch (Exception e) {
			throw new DeleteFailedException(e);
		}
	}

	/**
	 * 
	 * @return the DAO of the entity type behind the list.
	 */
	public abstract DAO getDAO();

	/**
	 * Check, if new button can be pressed.
	 * 
	 * @return boolean
	 */
	public abstract boolean canNew();

	/**
	 * Check, if edit button can be pressed.
	 * 
	 * @return boolean
	 */
	public abstract boolean canEdit();

	/**
	 * Check, if delete button can be pressed.
	 * 
	 * @return boolean
	 */
	public abstract boolean canDelete();

	/**
	 * @return the selectedEntryId
	 */
	public int getSelectedEntryId() {
		return selectedEntryId;
	}

	/**
	 * @param selectedEntryId
	 *            the selectedEntryId to set
	 */
	public void setSelectedEntryId(int selectedEntryId) {
		this.selectedEntryId = selectedEntryId;
	}
}
