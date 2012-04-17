/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.dao.EntityKey
 * Created on 12.01.2007 by Florian Lippisch
 *
 */
package de.xwic.appkit.core.dao;

/**
 * @author Florian Lippisch
 */
public class EntityKey {

	private String type = null;
	private int id = 0;
	
	/**
	 * @param type
	 * @param id
	 */
	public EntityKey(String type, int id) {
		super();
		this.type = type;
		this.id = id;
	}

	/**
	 * Construct a new key from an entity.
	 * @param entity
	 */
	public EntityKey(IEntity entity) {
		type = entity.type().getName();
		id = entity.getId();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + id;
		result = PRIME * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final EntityKey other = (EntityKey) obj;
		if (id != other.id)
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	
	
}
