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
/*
 * de.pol.entitydescriptor.Property
 * Created on 04.05.2005
 *
 */
package de.xwic.appkit.core.config.model;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

import de.xwic.appkit.core.ApplicationData;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.dao.ISecurityManager;
import de.xwic.appkit.core.model.entities.IPicklistEntry;

/**
 * @author Florian Lippisch
 */
public class Property implements Comparable<Property> {

	/** denies the deletion of the referenced object */
	public final static int DENY = 0;
	/** delete the object that holds the reference (cascading deletion) */
	public final static int DELETE = 1;
	/** set the reference to NULL and update the object */
	public final static int CLEAR_REFERENCE = 2;
	/** 
	 * Allows the deletion of the referenced object without changing the reference to NULL.
	 * This option should be used with caution.
	 */
	public final static int IGNORE = 3;
	
	/** date is used as datetime */
	public final static int DATETYPE_DATETIME = 0;
	/** date is used as date only (default) */
	public final static int DATETYPE_DATE = 1;
	/** date is used as time only */
	public final static int DATETYPE_TIME = 2;
	
    private String name = null;
    private EntityDescriptor entityDescriptor = null;
    private PropertyDescriptor descriptor = null;
    private String picklistId = null;
    private int maxLength = 0; // 0 == unlimited.
    private boolean required;
    private boolean lazy = true;
    private boolean hidden = false;
    private boolean fileType = false;
    private boolean embeddedEntity = false;
    private int onRefDeletion = DENY;
    
    private boolean collection = false;
    private boolean isEntity = false;
    private boolean isPicklistEntry = false;
    private String collectionEntityType = null;
    
    private String entityType = null;
    private int dateType = DATETYPE_DATE;
    private List<DefaultPicklistEntry> defaultPicklistEntries = null;
    
    /**
     * Default constructor.
     *
     */
    public Property() {
    	
    }
    
    /**
	 * @param descriptor2
	 */
	public Property(PropertyDescriptor descriptor) {
		if (descriptor == null) {
			throw new IllegalArgumentException("PropertyDescriptor is null in Property!");
		}
		this.descriptor = descriptor;
		this.name = descriptor.getName();
		this.entityType = descriptor.getPropertyType().getName();
		this.isEntity = IEntity.class.isAssignableFrom(descriptor.getPropertyType());
		this.isPicklistEntry = IPicklistEntry.class.isAssignableFrom(descriptor.getPropertyType());
		this.collection = java.util.Collection.class.isAssignableFrom(descriptor.getPropertyType());
		
		if (name.equals("createdAt") || name.equals("lastModifiedAt")) {
			this.dateType = Property.DATETYPE_DATETIME;
		}
		
	}

	/**
     * @return Returns the keylistid.
     */
    public String getPicklistId() {
        return picklistId;
    }
    /**
     * @param keylistid The keylistid to set.
     */
    public void setPicklistId(String picklistId) {
        this.picklistId = picklistId;
    }
    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }
    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
	/**
	 * @return Returns the maxLength.
	 */
	public int getMaxLength() {
		return maxLength;
	}
	/**
	 * @param maxLength The maxLength to set.
	 */
	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	/**
	 * @return Returns the descriptor.
	 */
	public PropertyDescriptor getDescriptor() {
		if (descriptor == null) {
			throw new IllegalStateException("Property was initialized for Generator! A PropertyDescriptor is not available!");
		}
		return descriptor;
	}

	/**
	 * @param descriptor The descriptor to set.
	 */
	public void setDescriptor(PropertyDescriptor descriptor) {
		this.descriptor = descriptor;
	}
	
	/**
	 * @return Returns required
	 */
	public boolean getRequired() {
		return required;
	}
	
	/**
	 * @param required The required to set
	 */
	public void setRequired(boolean required) {
		this.required = required;
	}

	/**
	 * @return the entityDescriptor
	 */
	public EntityDescriptor getEntityDescriptor() {
		return entityDescriptor;
	}

	/**
	 * @param entityDescriptor the entityDescriptor to set
	 */
	public void setEntityDescriptor(EntityDescriptor entityDescriptor) {
		this.entityDescriptor = entityDescriptor;
	}
	
	/**
	 * Returns the access type for this property based upon the current user.
	 * @return
	 */
	public int getAccess() {
		return DAOSystem.getSecurityManager().getAccess(entityDescriptor.getClassname(), name);
	}
	
	/**
	 * Returns true if the current use has readWrite access to this property.
	 * @return
	 */
	public boolean hasReadWriteAccess() {
		return getAccess() == ISecurityManager.READ_WRITE;
	}
	
	/**
	 * Returns true if the current user has read or readWrite access to this property.
	 * @return
	 */
	public boolean hasReadAccess() {
		if (DAOSystem.getSecurityManager().hasRight(entityDescriptor.getClassname(), ApplicationData.SECURITY_ACTION_READ)) {
			return  getAccess() != ISecurityManager.NONE;
		}
		return false;
	}

	/**
	 * @return the lazy
	 */
	public boolean isLazy() {
		return lazy;
	}

	/**
	 * @param lazy the lazy to set
	 */
	public void setLazy(boolean lazy) {
		this.lazy = lazy;
	}

	/**
	 * @return the onRefDeletion
	 */
	public int getOnRefDeletion() {
		return onRefDeletion;
	}

	/**
	 * @param onRefDeletion the onRefDeletion to set
	 */
	public void setOnRefDeletion(int onRefDeletion) {
		this.onRefDeletion = onRefDeletion;
	}

	/**
	 * @return the hidden
	 */
	public boolean isHidden() {
		return hidden;
	}

	/**
	 * @param hidden the hidden to set
	 */
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	/**
	 * @return the fileType
	 */
	public boolean isFileType() {
		return fileType;
	}

	/**
	 * @param fileType the fileType to set
	 */
	public void setFileType(boolean fileType) {
		this.fileType = fileType;
	}

	/**
	 * @return the collection
	 */
	public boolean isCollection() {
		return collection;
	}

	/**
	 * @param collection the collection to set
	 */
	public void setCollection(boolean collection) {
		this.collection = collection;
	}

	/**
	 * @return the collectionEntityType
	 */
	public String getCollectionEntityType() {
		return collectionEntityType;
	}

	/**
	 * @param collectionEntityType the collectionEntityType to set
	 */
	public void setCollectionEntityType(String collectionEntityType) {
		this.collectionEntityType = collectionEntityType;
	}

	/**
	 * @return the entityType
	 */
	public String getEntityType() {
		return entityType;
	}

	/**
	 * @param entityType the entityType to set
	 */
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Property arg0) {
		Property other = (Property) arg0;
		return name.compareTo(other.getName());
	}

	/**
	 * @return the dateType
	 */
	public int getDateType() {
		return dateType;
	}

	/**
	 * @param dateType the dateType to set
	 */
	public void setDateType(int dateType) {
		this.dateType = dateType;
	}

	/**
	 * Add a new default picklist entry
	 * @param dpe
	 */
	public void addDefaultPicklistEntry(DefaultPicklistEntry dpe) {
		if (defaultPicklistEntries == null) {
			defaultPicklistEntries = new ArrayList<DefaultPicklistEntry>();
		}
		defaultPicklistEntries.add(dpe);
	}
	
	/**
	 * @return the defaultPicklistEntries
	 */
	public List<DefaultPicklistEntry> getDefaultPicklistEntries() {
		return defaultPicklistEntries;
	}

	/**
	 * @return the embeddedEntity
	 */
	public boolean isEmbeddedEntity() {
		return embeddedEntity;
	}

	/**
	 * @param embeddedEntity the embeddedEntity to set
	 */
	public void setEmbeddedEntity(boolean embeddedEntity) {
		this.embeddedEntity = embeddedEntity;
	}

	/**
	 * Returns true if this property is a IEntity object. 
	 * @return the isEntity
	 */
	public boolean isEntity() {
		return isEntity;
	}

	/**
	 * @return the isPicklistEntry
	 */
	public boolean isPicklistEntry() {
		return isPicklistEntry;
	}

	/**
	 * @param isPicklistEntry the isPicklistEntry to set
	 */
	public void setPicklistEntry(boolean isPicklistEntry) {
		this.isPicklistEntry = isPicklistEntry;
	}

}
