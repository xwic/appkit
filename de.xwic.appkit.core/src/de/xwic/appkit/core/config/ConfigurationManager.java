/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.config.ConfigurationManager
 * Created on 09.11.2006 by Florian Lippisch
 *
 */
package de.xwic.appkit.core.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the platform configuration.
 * @author Florian Lippisch
 */
public class ConfigurationManager {

	private Setup setup = null;
	private Profile userProfile = null;
	private List<ConfigurationListener> listeners = new ArrayList<ConfigurationListener>();
	
	private static ConfigurationManager instance = null;
	
	/**
	 * Constructor.
	 */
	private ConfigurationManager() {
		
	}
	
	/**
	 * Add an configuration listener.
	 * @param listener
	 */
	public static void addConfigurationListener(ConfigurationListener listener) {
		if (instance == null) {
			instance = new ConfigurationManager();
		}
		instance.listeners.add(listener);
	}
	
	/**
	 * Remove an configurationListener.
	 * @param listener
	 */
	public static void removeConfigurationListener(ConfigurationListener listener) {
		if (instance != null) {
			instance.listeners.remove(listener);
		}
	}
	
	/**
	 * Returns the current setup or null if no setup has been loaded.
	 */
	public static Setup getSetup() {
		if (instance == null) {
			return null;
		}
		return instance.setup;
	}

	/**
	 * Returns the current user profile or null if no setup has been loaded.
	 */
	public static Profile getUserProfile() {
		if (instance == null) {
			return null;
		}
		return instance.userProfile;
	}
	
	/**
	 * Set a new setup.
	 * @param newSetup
	 */
	public static void setSetup(Setup newSetup) {
		if (instance == null) {
			instance = new ConfigurationManager();
		}
		instance.setup = newSetup;
		instance.userProfile = newSetup.getDefaultProfile();
		
		// notify listeners
		ConfigurationEvent event = new ConfigurationEvent(newSetup);
		Object[] lst = instance.listeners.toArray();
		for (int i = 0; i < lst.length; i++) {
			ConfigurationListener listener = (ConfigurationListener)lst[i];
			listener.configurationRefreshed(event);
		}
	}
	
	/**
	 * Change the user profile.
	 * @param profile
	 */
	public static void setUserProfile(Profile profile) {
		if (instance != null) {
			instance.userProfile = profile;
		} else {
			throw new IllegalStateException("The setup must be set before setting the user profile!");
		}
	}
	
}
