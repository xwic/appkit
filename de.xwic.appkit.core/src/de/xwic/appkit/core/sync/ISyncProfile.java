/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.connectors.sync.ISyncProfile
 * Created on 30.11.2007 by Mark Frewin
 *
 */
package de.xwic.appkit.core.sync;

import de.xwic.appkit.core.security.IUser;

/**
 * @author Mark Frewin
 */
public interface ISyncProfile {
	
	/** */
	public static final String PROFILE_OUTLOOK = "Outlook";

	/**
	 * 
	 * @return
	 */
	public String getKey();
	
	/**
	 * @return the applicationId
	 */
	public String getApplicationId();
	/**
	 * @return the clientId
	 */
	public String getDeviceId();

	/**
	 * @return the user
	 */
	public IUser getUser();
	
}
