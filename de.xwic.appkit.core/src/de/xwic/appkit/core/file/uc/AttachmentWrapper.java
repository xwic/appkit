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
package de.xwic.appkit.core.file.uc;

import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.entities.IAnhang;

/**
 * @author Ronny Pfretzschner
 *
 */
public class AttachmentWrapper implements IAttachmentWrapper {

	
    private IAnhang anhang;
    //private IEntity entity;
    private String key;

    private String tmpFileName = null;
    private String contentType = "";
	
    private String fileName = null;
    private long contentLength = 0;
    
    private String entityType = null;
    private int entityId = 0;
    
    private IEntity entity;
    
    private int fileId = 0;
    
    private boolean deleted = false;
    
    public AttachmentWrapper(String key, String filename, String tmpFileName, String contentType, long contentLength) {
        this.key = key;
        this.fileName = filename;
        this.contentLength = contentLength;
        this.tmpFileName = tmpFileName;
        this.contentType = contentType;
    }
    
    public AttachmentWrapper(String key, IAnhang anhang) {
    	this.key = key;
        this.fileName = anhang.getDateiName();
        this.contentLength = anhang.getDateiGroesse();
        this.entityId = anhang.getEntityID();
        this.entityType = anhang.getEntityType();
        this.fileId = anhang.getFileID();
        this.deleted = anhang.isDeleted();
        this.anhang = anhang;
    }
    
    
    /* (non-Javadoc)
	 * @see de.jwic.entitytools.file.uc.IAttachmentWrapper#isDeleted()
	 */
    public boolean isDeleted() {
    	return deleted;
    }
    
    public void setDeleted(boolean deleted) {
    	this.deleted = deleted;
    }

    
    /* (non-Javadoc)
	 * @see de.jwic.entitytools.file.uc.IAttachmentWrapper#getFileId()
	 */
    public int getFileId() {
    	return fileId;
    }
    
    public void setFileId(int fileId) {
    	this.fileId = fileId;
    }
    /* (non-Javadoc)
	 * @see de.jwic.entitytools.file.uc.IAttachmentWrapper#getFileName()
	 */
    public String getFileName() {
    	return fileName;
    }
    
    /* (non-Javadoc)
	 * @see de.jwic.entitytools.file.uc.IAttachmentWrapper#getContentLength()
	 */
    public long getContentLength() {
    	return contentLength;
    }
    
    
	/* (non-Javadoc)
	 * @see de.jwic.entitytools.file.uc.IAttachmentWrapper#getEntity()
	 */
	public int getEntityId() {
		return entity != null ? entity.getId() : entityId;
	}
	
	public String getEntityType() {
		return entity != null ? entity.type().getName() : entityType;
	}
	/* (non-Javadoc)
	 * @see de.jwic.entitytools.file.uc.IAttachmentWrapper#setEntity(de.jwic.entitytools.base.IEntity)
	 */
	public void setEntity(IEntity entity) {
		this.entity = entity;
		this.entityId = entity.getId();
		this.entityType = entity.type().getName();
	}
	/* (non-Javadoc)
	 * @see de.jwic.entitytools.file.uc.IAttachmentWrapper#getKey()
	 */
	public String getKey() {
		return key;
	}
	/* (non-Javadoc)
	 * @see de.jwic.entitytools.file.uc.IAttachmentWrapper#setKey(java.lang.String)
	 */
	public void setKey(String key) {
		this.key = key;
	}
	/* (non-Javadoc)
	 * @see de.jwic.entitytools.file.uc.IAttachmentWrapper#getTmpFileName()
	 */
	public String getTmpFileName() {
		return tmpFileName;
	}
	/* (non-Javadoc)
	 * @see de.jwic.entitytools.file.uc.IAttachmentWrapper#setTmpFileName(java.lang.String)
	 */
	public void setTmpFileName(String tmpFileName) {
		this.tmpFileName = tmpFileName;
	}
	/* (non-Javadoc)
	 * @see de.jwic.entitytools.file.uc.IAttachmentWrapper#getContentType()
	 */
	public String getContentType() {
		return contentType;
	}
	/* (non-Javadoc)
	 * @see de.jwic.entitytools.file.uc.IAttachmentWrapper#setContentType(java.lang.String)
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public void setContentLength(long length) {
		this.contentLength = length;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the anhang
	 */
	public IAnhang getAnhang() {
		return anhang;
	}

	/**
	 * @param anhang the anhang to set
	 */
	public void setAnhang(IAnhang anhang) {
		this.anhang = anhang;
	}
}
