/**
 * 
 */
package de.xwic.appkit.core.security;

import java.util.Date;

import de.xwic.appkit.core.dao.IEntity;

/**
 * @author Lippisch
 *
 */
public interface IUserSession extends IEntity {

	/**
	 * @return the key
	 */
	public abstract String getKey();

	/**
	 * @param key the key to set
	 */
	public abstract void setKey(String key);

	/**
	 * @return the lastAccess
	 */
	public abstract Date getLastAccess();

	/**
	 * @param lastAccess the lastAccess to set
	 */
	public abstract void setLastAccess(Date lastAccess);

	/**
	 * @return the username
	 */
	public abstract String getUsername();

	/**
	 * @param username the username to set
	 */
	public abstract void setUsername(String username);

}