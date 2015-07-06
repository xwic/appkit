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
package de.xwic.appkit.core.model.entities.impl;

import de.xwic.appkit.core.dao.Entity;
import de.xwic.appkit.core.model.entities.IAnhang;
import de.xwic.appkit.core.model.entities.IEntityComment;


/**
 * 
 *
 * @author Aron Cotrau
 */
public class EntityComment extends Entity implements IEntityComment {

	private String entityType;
	private String comment;
	private int entityId;
	
	private IAnhang attachment;
	private boolean readonlyComment;

	private String subject;
	private String category;
	
	
	/**
	 * @return the attachment
	 */
	public IAnhang getAttachment() {
		return attachment;
	}

	/**
	 * @param attachment the attachment to set
	 */
	public void setAttachment(IAnhang attachment) {
		this.attachment = attachment;
	}

	/**
	 * @return the readonlyComment
	 */
	public boolean isReadonlyComment() {
		return readonlyComment;
	}

	/**
	 * @param readonlyComment the readonlyComment to set
	 */
	public void setReadonlyComment(boolean readonlyComment) {
		this.readonlyComment = readonlyComment;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.IEntityComment#getComment()
	 */
	public String getComment() {
		return comment;
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.IEntityComment#setComment(java.lang.String)
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.IEntityComment#getEntityId()
	 */
	public int getEntityId() {
		return entityId;
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.IEntityComment#setEntityId(int)
	 */
	public void setEntityId(int entityId) {
		this.entityId = entityId;
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.IEntityComment#getEntityType()
	 */
	public String getEntityType() {
		return entityType;
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.IEntityComment#setEntityType(java.lang.String)
	 */
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}
	
}
