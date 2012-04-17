package de.xwic.appkit.webbase.core;

import de.xwic.appkit.webbase.prefstore.IContextPreferenceProvider;
import de.xwic.appkit.webbase.prefstore.IPreferenceProvider;

/**
 * Grants access to globally used platform features.
 * 
 * Must get initialized during server startup.
 * 
 * @author lippisch
 */
public class Platform {

	private static Platform instance = null;
	private IPreferenceProvider preferenceProvider;
	private IContextPreferenceProvider contextPreferenceProvider;
	
	/**
	 * Private constructor ensures singleton behavior.
	 */
	private Platform() {
		
	}
	
	/**
	 * Returns true if the Platform was initialized correctly.
	 * @return
	 */
	public static boolean isInitialized() {
		return instance != null;
	}
	
	/**
	 * Initialize the platform. May only be invoked once. 
	 * @param preferenceProvider
	 */
	public static void initialize(IPreferenceProvider preferenceProvider, IContextPreferenceProvider contextPreferenceProvider) {
		
		if (instance != null) {
			throw new IllegalStateException("The instance is already initialized.");
		}
		instance = new Platform();
		instance.preferenceProvider = preferenceProvider;
		instance.contextPreferenceProvider = contextPreferenceProvider;
		
	}
	
	/**
	 * Returns the platform instance.
	 * @return
	 */
	public static Platform getPlatform() {
		if (instance == null) {
			throw new IllegalStateException("Platform is not yet initialized.");
		}
		return instance;
	}

	/**
	 * @return Returns the preferenceProvider.
	 */
	public static IPreferenceProvider getPreferenceProvider() {
		return getPlatform().preferenceProvider;
	}

	/**
	 * @return Returns the preferenceProvider.
	 */
	public static IContextPreferenceProvider getContextPreferenceProvider() {
		return getPlatform().contextPreferenceProvider;
	}
	
	
}
