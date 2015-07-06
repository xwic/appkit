/*******************************************************************************
 * Copyright 2015 xWic group (http://www.xwic.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *  
 *******************************************************************************/
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
