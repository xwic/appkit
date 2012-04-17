/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.config.ConfigurationEvent
 * Created on 09.11.2006 by Florian Lippisch
 *
 */
package de.xwic.appkit.core.config;

/**
 * @author Florian Lippisch
 *
 */
public class ConfigurationEvent {

	private Setup setup;

	/**
	 * Constructor.
	 * @param setup
	 */
	public ConfigurationEvent(Setup setup) {
		this.setup = setup;
	}
	
	/**
	 * @return the setup
	 */
	public Setup getSetup() {
		return setup;
	}

	/**
	 * @param setup the setup to set
	 */
	void setSetup(Setup setup) {
		this.setup = setup;
	}
	
}
