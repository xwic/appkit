/**
 * 
 */
package de.xwic.appkit.core.model.entities;

import java.util.Date;

import de.xwic.appkit.core.dao.IEntity;

/**
 * @author lippisch
 */
public interface INews extends IEntity {

	/**
	 * @return the author
	 */
	public abstract String getAuthor();

	/**
	 * @param author the author to set
	 */
	public abstract void setAuthor(String author);

	/**
	 * @return the title
	 */
	public abstract String getTitle();

	/**
	 * @param title the title to set
	 */
	public abstract void setTitle(String title);

	/**
	 * @return the body
	 */
	public abstract String getBody();

	/**
	 * @param body the body to set
	 */
	public abstract void setBody(String body);

	/**
	 * @return the publishDate
	 */
	public abstract Date getPublishDate();

	/**
	 * @param publishDate the publishDate to set
	 */
	public abstract void setPublishDate(Date publishDate);

	/**
	 * @return the visibleUntil
	 */
	public abstract Date getVisibleUntil();

	/**
	 * @param visibleUntilDate the visibleUntil to set
	 */
	public abstract void setVisibleUntil(Date visibleUntil);

	/**
	 * @return the visible
	 */
	public abstract boolean isVisible();

	/**
	 * @param visible the visible to set
	 */
	public abstract void setVisible(boolean visible);
	
	/**
	 * Returns the appID used to identify the independent 
	 * application running in Pulse.  
	 * 
	 * @return
	 */
	String getAppId();

	/**
	 * Sets the application ID.
	 * 
	 * @param appId
	 */
	void setAppId(String appId);
	
}