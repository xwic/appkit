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

package de.xwic.appkit.webbase.table;

import de.xwic.appkit.core.dao.IEntity;

/**
 * Wraps the data to be displayed in one row.
 * @author lippisch
 */
public class RowData {

	private long entityId = 0;
	
	private Object[] array = null;
	private IEntity entity = null;
	private Object object = null;

	private final EntityTableModel model;
	
	
	/**
	 * @param o 
	 * @param o
	 */
	public RowData(EntityTableModel model, Object data) {
		
		this.model = model;
		object = data;
		if (data.getClass().isArray()) {
			array = (Object[])data;
			entityId = (Integer)array[0];
		} else if (data instanceof IEntity) {
			entity = (IEntity)data;
			entityId = entity.getId();
		}
		
	}
	
	/**
	 * Returns true if the data is available as array.
	 * @return
	 */
	public boolean isArray() {
		return array != null;
	}
	
	/**
	 * Returns the data of the given property.
	 * @param propertyId
	 * @return
	 */
	public Object getData(String propertyId) {
		if (array != null) {
			Integer idx = model.getPropertyDataIndex(propertyId);
			if (idx != null) {
				return array[idx + 1];
			}
		}
		return null;
	}

	/**
	 * @return
	 */
	public String getUniqueKey() {
		return Long.toString(entityId);
	}

	/**
	 * @return the entity
	 */
	public IEntity getEntity() {
		return entity;
	}

	/**
	 * @return the object
	 */
	public Object getObject() {
		return object;
	}

	/**
	 * @return the entityId
	 */
	public long getEntityId() {
		return entityId;
	}

}
