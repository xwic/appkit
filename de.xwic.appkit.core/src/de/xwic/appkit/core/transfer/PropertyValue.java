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
package de.xwic.appkit.core.transfer;

import de.xwic.appkit.core.dao.IEntity;

/**
 * Encapsulates the value of a property. 
 * 
 * @author Florian Lippisch
 */
public class PropertyValue {

	/** user has read/write access */
	public final static int READ_WRITE = 2;
	/** user has read access */
	public final static int READ = 1;
	/** use has no access to this property. The property does not contain data */
	public final static int NONE = 0;
	
	private Object value = null;
	private int access = READ_WRITE;
	private Class<?> type = null;
	
	/** indicates if the value has been modified */
	private boolean modified = false;
	
	/** indicates if the value has been loaded **/
	private boolean loaded = true;
	private boolean entityType = false;
	private int entityId = 0;
	
	/**
	 * Used by Axis to know which properties should be transfered with attributes.
	 * @return
	 */
	public static String[] getAttributeElements() {
		return new String[] { "access", "loaded", "entityType", "entityId", "modified" };
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		int lastDot = type.getName().lastIndexOf('.');
		if (lastDot != -1) {
			sb.append(type.getName().substring(lastDot + 1));
		} else {
			sb.append(type.getName());
		}

		switch (access) {
		case NONE: 
			sb.append("[--]"); 
			break;
		case READ: 
			sb.append("[R-]"); 
			break;
		case READ_WRITE: 
			sb.append("[RW]"); 
			break;
		}
		if (entityType) {
			sb.append(" #").append(entityId);
		}		
		if (loaded) {
			sb.append(" '").append(value).append("' ");
		} else {
			sb.append(" **Not Loaded**");
		}

		return sb.toString();
	}

	/**
	 * @return Returns the access.
	 */
	public int getAccess() {
		return access;
	}

	/**
	 * @param access The access to set.
	 */
	public void setAccess(int access) {
		this.access = access;
	}

	/**
	 * @return Returns the entityId.
	 */
	public int getEntityId() {
		return entityId;
	}

	/**
	 * @param entityId The entityId to set.
	 */
	public void setEntityId(int entityId) {
		this.entityId = entityId;
	}

	/**
	 * @return Returns the entityType.
	 */
	public boolean isEntityType() {
		return entityType;
	}

	/**
	 * @param entityType The entityType to set.
	 */
	public void setEntityType(boolean entityType) {
		this.entityType = entityType;
	}

	/**
	 * @return Returns the loaded.
	 */
	public boolean isLoaded() {
		return loaded;
	}

	/**
	 * @param loaded The loaded to set.
	 */
	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

	/**
	 * @return Returns the type.
	 */
	public Class<?> getType() {
		return type;
	}

	/**
	 * @param type The type to set.
	 */
	public void setType(Class<?> type) {
		this.type = type;
	}

	/**
	 * @return Returns the value.
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * @param value The value to set.
	 */
	public void setValue(Object value) {		
		this.value = value;
		
		if (isEntityType()) {
			if (value == null) {
				entityId = 0;
			} else {
				if (IEntity.class.isAssignableFrom(value.getClass())) {
					entityId = ((IEntity) value).getId();
				} else if (value instanceof EntityTransferObject) {
					entityId = ((EntityTransferObject) value).getEntityId();
				} else {
					throw new IllegalArgumentException("Not an Entity!");
				}
			}
		}
	}

	/**
	 * @return the modified
	 */
	public boolean isModified() {
		return modified;
	}

	/**
	 * @param modified the modified to set
	 */
	public void setModified(boolean modified) {
		this.modified = modified;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + access;
		result = PRIME * result + entityId;
		result = PRIME * result + (entityType ? 1231 : 1237);
		result = PRIME * result + (loaded ? 1231 : 1237);
		result = PRIME * result + (modified ? 1231 : 1237);
		result = PRIME * result + ((type == null) ? 0 : type.hashCode());
		result = PRIME * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final PropertyValue other = (PropertyValue) obj;
		if (access != other.access)
			return false;
		if (entityId != other.entityId)
			return false;
		if (entityType != other.entityType)
			return false;
		if (loaded != other.loaded)
			return false;
		if (modified != other.modified)
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
}
