/**
 * 
 */
package de.xwic.appkit.core.model.entities;

import de.xwic.appkit.core.dao.IEntity;

/**
 * @author lippisch
 *
 */
public interface IQuickLaunch extends IEntity {

	/**
	 * @return the username
	 */
	public abstract String getUsername();

	/**
	 * The user this QuickLaunch is used for.
	 * @param username the username to set
	 */
	public abstract void setUsername(String username);

	/**
	 * @return the reference
	 */
	public abstract String getReference();

	/**
	 * The reference to the module. Follows a specific URI pattern, i.e.:
	 * menu:module/submodule
	 * entity:IBookingRequest/123456
	 * extension:time-entry
	 * @param reference the reference to set
	 */
	public abstract void setReference(String reference);

	/**
	 * @return the order
	 */
	public abstract int getOrder();

	/**
	 * @param order the order to set
	 */
	public abstract void setOrder(int order);
	
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