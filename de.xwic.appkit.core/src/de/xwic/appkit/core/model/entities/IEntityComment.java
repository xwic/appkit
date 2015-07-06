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
package de.xwic.appkit.core.model.entities;

import de.xwic.appkit.core.dao.IEntity;

/**
 * 
 *
 * @author Aron Cotrau
 */
public interface IEntityComment extends IEntity {

	/**
	 * @return the comment
	 */
	public String getComment();

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment);

	/**
	 * @return the entityId
	 */
	public int getEntityId();

	/**
	 * @param entityId the entityId to set
	 */
	public void setEntityId(int entityId);

	/**
	 * @return the entityType
	 */
	public String getEntityType();

	/**
	 * @param entityType the entityType to set
	 */
	public void setEntityType(String entityType);
	
	
	
	/**
	 * @return the attachment
	 */
	public IAnhang getAttachment();

	/**
	 * @param attachment the attachment to set
	 */
	public void setAttachment(IAnhang attachment);

	/**
	 * @return the readonlyComment
	 */
	public boolean isReadonlyComment();

	/**
	 * @param readonlyComment the readonlyComment to set
	 */
	public void setReadonlyComment(boolean readonlyComment);


	/**
	 * @return the subject
	 */
	public String getSubject();
	
	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject);
	
	/**
	 * @return the category
	 */
	public String getCategory();
	
	/**
	 * @param category the category to set
	 */
	public void setCategory(String category);	
}