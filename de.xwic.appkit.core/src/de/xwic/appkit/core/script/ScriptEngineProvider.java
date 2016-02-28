/*
 * Copyright 2015 xWic group (http://www.xwic.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
**/
package de.xwic.appkit.core.script;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import de.xwic.appkit.core.config.ConfigurationManager;

/**
 * Provides access to ScriptEngine instances that are used within AppKit to script
 * various customizable components such as Editors.
 * 
 * The ScriptEngineProvider keeps a single instance of a ScriptEngineManager that has
 * the global scope for all ScriptEngine instances created. The language is always
 * JavaScript, but uses Mozilla Rhino under Java 1.6 and Oracle Nashorn under Java 1.7
 * and above. 
 * 
 * @author Lippisch
 */
public class ScriptEngineProvider {

	private static ScriptEngineProvider instance = null;
	
	private ScriptEngineManager engineManager;
	
	/**
	 * Private constructor, to turn this into a singleton.
	 */
	private ScriptEngineProvider() {
		engineManager = new ScriptEngineManager();
		
		// we can now add global functions etc. here...
		
	}
	
	/**
	 * Returns the single instance of the ScriptEngineProvider.
	 * @return
	 */
	public static ScriptEngineProvider instance() {
		if (instance == null) {
			synchronized (ScriptEngineProvider.class) {
				if (instance == null) {
					instance = new ScriptEngineProvider();
				}
			}
		}
		return instance;
	}
	
	
	/**
	 * Create a new ScriptEngine instance.
	 * @param logInfo - A string that will be placed into log entries made from within the engine.
	 * @return
	 */
	public ScriptEngine createEngine(String logInfo) {
		ScriptEngine engine = engineManager.getEngineByName("JavaScript");
		Bindings bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
		bindings.put("log", new ScriptLogger(logInfo));
		bindings.put("setup", ConfigurationManager.getSetup());
		bindings.put("profile", ConfigurationManager.getUserProfile());
		return engine;
	}
	

}

