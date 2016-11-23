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
 * de.xwic.appkit.core.model.impl.CRMObject
 * Created on 04.04.2005
 *
 */
package de.xwic.appkit.core.dao;

import java.util.Date;

import de.xwic.appkit.core.model.util.EntityUtil;

import javax.persistence.*;


/**
 * An entity is stored in a database and managed by a corrosponding DAO
 * implementation.
 * @author Florian Lippisch
 */
@MappedSuperclass
public class Entity implements IEntity {
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private long id;
	@Column(name = "SRVENTITY_ID")
    private int serverEntityId;
	@Column(name = "CREATED_AT")
    private Date createdAt = new Date();
	@Column(name = "LASTMODIFIED_AT")
    private Date lastModifiedAt = null;
	@Column(name = "CREATED_FROM", length = 50)
    private String createdFrom = "";
	@Column(name = "LASTMODIFIED_FROM", length = 50)
    private String lastModifiedFrom = "";
	@Version
	@Column(name = "VERSION")
    private long version = 0;
	@Column(name = "CHANGED")
    private boolean changed = false;
	@Column(name = "DELETED", nullable = false)
    private boolean deleted = false;
	@Column(name = "DOWNLOAD_VERSION")
	private long downloadVersion = -1;
	
    /**
     * Constructs a new entity.
     */
    public Entity(){
        //TODO at the moment, no problems visible to set NOT the default dates with real date...
        //therefore, commented out at this moment. 20.07.2005
        
        Date date = new Date();
        createdAt = date;
        lastModifiedAt = date;
    }
    
    /**
     * Returns the unique id that identifies this object.
     * @return
     */
    public long getId() {
        return id;
    }
    /**
     * Set the unique id. This method shall only be used by the orm tool.
     * @param newId
     */
    public void setId(long newId) {
        this.id = newId;
    }

    
    
    /**
     * @return Returns the changed.
     */
    public boolean isChanged() {
        return changed;
    }
    /**
     * @param changed The changed to set.
     */
    public void setChanged(boolean changed) {
        this.changed = changed;
    }
    /**
     * @return Returns the createdAt.
     */
    public Date getCreatedAt() {
        return createdAt;
    }
    /**
     * @param createdAt The createdAt to set.
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    /**
     * @return Returns the createdFrom.
     */
    public String getCreatedFrom() {
        return createdFrom;
    }
    /**
     * @param createdFrom The createdFrom to set.
     */
    public void setCreatedFrom(String createdFrom) {
        this.createdFrom = createdFrom;
    }
    /**
     * @return Returns the lastmodifiedAt.
     */
    public Date getLastModifiedAt() {
        return lastModifiedAt;
    }
    /**
     * @param lastmodifiedAt The lastmodifiedAt to set.
     */
    public void setLastModifiedAt(Date lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }
    /**
     * @return Returns the lastModifiedFrom.
     */
    public String getLastModifiedFrom() {
        return lastModifiedFrom;
    }
    /**
     * @param lastModifiedFrom The lastModifiedFrom to set.
     */
    public void setLastModifiedFrom(String lastModifiedFrom) {
        this.lastModifiedFrom = lastModifiedFrom;
    }
    /**
     * @return Returns the version.
     */
    public long getVersion() {
        return version;
    }
    /**
     * @param version The version to set.
     */
    public void setVersion(long version) {
        this.version = version;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
    	
    	String clName = getClass().getName();
    	int idx = clName.lastIndexOf('.');
    	if (idx !=  -1) {
    		clName = clName.substring(idx + 1);
    	}
    	
    	return clName + " #" + id + " Version " + version;
    	
    }

	/**
	 * @return the deleted
	 */
	public boolean isDeleted() {
		return deleted;
	}

	/**
	 * @param deleted the deleted to set
	 */
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public long getDownloadVersion() {
		return downloadVersion;
	}
	
	public void setDownloadVersion(long downloadVersion) {
		this.downloadVersion = downloadVersion;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.IEntity#type()
	 */
	@Override
	public Class<? extends IEntity> type() {
		return EntityUtil.type(getClass());
	}

    /*
     * (non-Javadoc)
     * @see de.xwic.appkit.core.dao.IEntity#getServerEntityId()
     */
	public int getServerEntityId() {
		return serverEntityId;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.IEntity#setServerEntityId(int)
	 */
	public void setServerEntityId(int serverEntityId) {
		this.serverEntityId = serverEntityId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + (isChanged() ? 1231 : 1237);
		result = PRIME * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
		result = PRIME * result + ((getCreatedFrom() == null) ? 0 : getCreatedFrom().hashCode());
		result = PRIME * result + (isDeleted() ? 1231 : 1237);
		result = PRIME * result + (int) (getDownloadVersion() ^ (getDownloadVersion() >>> 32));
		result = PRIME * result + getServerEntityId();
		result = PRIME * result + (int) (getVersion() ^ (getVersion() >>> 32));
		result = PRIME * result + ((type().getName() == null) ? 0 : type().getName().hashCode());
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
//		if (getClass() != obj.getClass())
//			return false;
		if (!(obj instanceof Entity)){
			return false;
		}
		final Entity other = (Entity) obj;
		if (!type().getName().equals(other.type().getName())) {
			return false;
		}
		if (isChanged() != other.isChanged())
			return false;
		if (getCreatedAt() == null) {
			if (other.getCreatedAt() != null)
				return false;
		} else if (!getCreatedAt().equals(other.getCreatedAt()))
			return false;
		if (getCreatedFrom() == null) {
			if (other.getCreatedFrom() != null)
				return false;
		} else if (!getCreatedFrom().equals(other.getCreatedFrom()))
			return false;
		if (isDeleted() != other.isDeleted())
			return false;
		if (getDownloadVersion() != other.getDownloadVersion())
			return false;
		if (getServerEntityId() != other.getServerEntityId())
			return false;
		if (getVersion() != other.getVersion())
			return false;
		
		if (getId() != other.getId()) {
			return false;
		}
		
		return true;
	}
}
