/**
 * 
 */
package de.xwic.appkit.core.model.entities.impl;

import de.xwic.appkit.core.dao.Entity;
import de.xwic.appkit.core.model.entities.IQuickLaunch;

/**
 * References a module that can be opened from the home page of Pulse.
 * @author lippisch
 */
public class QuickLaunch extends Entity implements IQuickLaunch {

	private String username = null;
	private String reference = null;
	private int order = 0;
	private String appId;
	
	/* (non-Javadoc)
	 * @see com.netapp.pulse.start.model.entities.impl.IQuickLaunch#getUsername()
	 */
	@Override
	public String getUsername() {
		return username;
	}
	/* (non-Javadoc)
	 * @see com.netapp.pulse.start.model.entities.impl.IQuickLaunch#setUsername(java.lang.String)
	 */
	@Override
	public void setUsername(String username) {
		this.username = username;
	}
	/* (non-Javadoc)
	 * @see com.netapp.pulse.start.model.entities.impl.IQuickLaunch#getReference()
	 */
	@Override
	public String getReference() {
		return reference;
	}
	/* (non-Javadoc)
	 * @see com.netapp.pulse.start.model.entities.impl.IQuickLaunch#setReference(java.lang.String)
	 */
	@Override
	public void setReference(String reference) {
		this.reference = reference;
	}
	/* (non-Javadoc)
	 * @see com.netapp.pulse.start.model.entities.impl.IQuickLaunch#getOrder()
	 */
	@Override
	public int getOrder() {
		return order;
	}
	/* (non-Javadoc)
	 * @see com.netapp.pulse.start.model.entities.impl.IQuickLaunch#setOrder(int)
	 */
	@Override
	public void setOrder(int order) {
		this.order = order;
	}
	
	/* (non-Javadoc)
	 * @see com.netapp.pulse.start.model.entities.INews#getAppId()
	 */
	@Override
	public String getAppId() {
		return appId;
	}
	
	/* (non-Javadoc)
	 * @see com.netapp.pulse.start.model.entities.INews#setAppId(java.lang.String)
	 */
	@Override
	public void setAppId(String appId) {
		this.appId = appId;
	}
	
}
