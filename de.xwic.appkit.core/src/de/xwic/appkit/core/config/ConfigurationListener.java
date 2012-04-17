/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.config.ConfigurationListener
 * Created on 09.11.2006 by Florian Lippisch
 *
 */
package de.xwic.appkit.core.config;

/**
 * Listen to configuration changes. 
 * @author Florian Lippisch
 */
public interface ConfigurationListener {

	/**
	 * Invoked when a new setup is registered.
	 * @param event
	 */
	public void configurationRefreshed(ConfigurationEvent event);
	
}
