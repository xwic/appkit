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
/**
 * 
 */
package de.xwic.appkit.webbase.toolkit.app;

import java.util.List;

/**
 * Main module for the menu structure.
 * 
 * @author Ronny Pfretzschner
 *
 */
public abstract class Module {

    private String title = "";
    private String key = "";

    protected List<SubModule> subModules;

    /**
     * Creates the module and its submodules.
     * 
     * @param site
     */
    public Module(Site site) {
        key = getModuleKey();
        subModules = createSubModules(site);
    }

   
    /**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * Create the SubModules. This method is called only once during
     * construction.
     * 
	 * @param site
	 * @return
	 */
    protected abstract List<SubModule> createSubModules(Site site);

    /**	
     * Returns the module key. Uses the simple name of the class by default, but
     * can be overridden for a custom key. The module key must be unique across
     * the application.
     *  
     * @return the unique module key.
     */
    protected String getModuleKey() {
    	return getClass().getSimpleName();
    }

    /**
     * Returns a list of the subModules.
     * 
     * @return
     */
    public List<SubModule> getSubModules() {
        return subModules;
    }

    /**
     * Returns the registered submodule with the given key.
     * 
     * @param key
     * @return
     */
    public SubModule getSubModule(String key) {
    	String[] keys = key.split(";");
    	
    	// find the first module
    	SubModule sm = null;
        for (SubModule subModule : subModules) {
            if (subModule.getKey().equals(keys[0])) {
            	sm = subModule;
            }
        }
        
        // drill deeper
    	for (int i = 1; i < keys.length && sm != null; i++) {
    		sm = sm.getSubModule(keys[i]);
    	}
        if (sm == null) {
        	throw new RuntimeException("The submodule with key: " + key + " was not registered to this module !");
        }
        return sm;
    }
    
}
