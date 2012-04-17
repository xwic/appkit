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