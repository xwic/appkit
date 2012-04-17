/*
 * de.xwic.appkit.core.dao.IEntity
 * Created on 04.04.2005
 *
 */
package de.xwic.appkit.core.dao;

import java.util.Date;

/**
 * An entity is stored in a database and managed by a corrosponding DAO
 * implementation.
 * @author Florian Lippisch
 */
public interface IEntity {
    
    /**
     * Returns the unique ID that identifies the object of the specified type.
     * @return
     */
    public int getId();
    
    /**
     * Set the unique ID of the objecft. 
     * @param id
     */
    public void setId(int id);
    
    /**
     * @return Returns the changed.
     */
    public boolean isChanged();

    /**
     * @return Returns true if the entity is marked as deleted.
     */
    public boolean isDeleted();
    
    /**
     * Sets the deleted flag
     * 
     * @param deleted
     */
    public void setDeleted(boolean deleted);
    
    /**
     * Set the changed flag of the entity.
     * @param changed
     */
    public void setChanged(boolean changed);

    /**
     * @return Returns the createdAt.
     */
    public Date getCreatedAt();
    
    /**
     * @return Returns the createdFrom.
     */
    public String getCreatedFrom();

    /**
     * @return Returns the lastmodifiedAt.
     */
    public Date getLastModifiedAt();
    
    /**
     * @return Returns the lastModifiedFrom.
     */
    public String getLastModifiedFrom();
    
    
    
    /**
     * 
     * @param createdAt
     */
    public void setCreatedAt(Date createdAt);
    
    /**
     * 
     * @param createdFrom
     */
    public void setCreatedFrom(String createdFrom);

    /**
     * 
     * @param modifiedAt
     */
    public void setLastModifiedAt(Date modifiedAt);
    
    /**
     * 
     * @param modfiedFrom
     */
    public void setLastModifiedFrom(String modfiedFrom);
    
    
    
    /**
     * @return Returns the version.
     */
    public long getVersion();
    
    /**
     * Returns the interface that specifies the type.
     * @return
     */
    public Class<? extends IEntity> type();

	/**
	 * Sets the DownloadVersion. <p>
	 * 
	 * System use only!
	 * 
	 * @param downloadVersion
	 */
	public void setDownloadVersion(long downloadVersion);
	
	/**
	 * System use only!
	 * 
	 * @return The DownloadVersion
	 */
	public long getDownloadVersion();

	/**
	 * Returns the ID this entity has on the server side. This field is used
	 * by the replication mechanism to store the server side ID for this entity.
	 * This field returns 0 on the server. If the entity is stored in a local client 
	 * database, this field returns either the ID of the entity on the server side
	 * or 0 if the entity has been created by the local client.
	 * 
	 * @return
	 */
	public int getServerEntityId();
	
	/**
	 * Set the ID of the entity on the server side. This field is used
	 * by the replication mechanism to store the server side ID for this entity.
	 * @param serverEntityID
	 */
	public void setServerEntityId(int serverEntityId);
	
}
