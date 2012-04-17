/*
 * Copyright (c) 2009 Network Appliance, Inc.
 * All rights reserved.
 */

package de.xwic.appkit.webbase.table;

import de.xwic.appkit.core.dao.IEntity;

/**
 * Wraps the data to be displayed in one row.
 * @author lippisch
 */
public class RowData {

	private int entityId = 0;
	
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
		return Integer.toString(entityId);
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
	public int getEntityId() {
		return entityId;
	}

}
