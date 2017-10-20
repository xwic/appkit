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

import java.util.ArrayList;
import java.util.List;

import de.jwic.base.IControl;
import de.jwic.base.IControlContainer;

/**
 * Submodule for Modules.
 * 
 * @author Ronny Pfretzschner
 *
 */
public abstract class SubModule {

    private String title;
    protected List<SubModule> subModules = new ArrayList<SubModule>();
	protected Site site;
    
    /**
     * Creates the submodule.
     * 
     * @param site
     */
    public SubModule(Site site) {
		this.site = site;

    }

    /**
     * Creates the controls, usually an extension of InnerPage. The
     * root control must be returned.
     * 
     * @param container
     * @return
     */
    public abstract IControl createControls(IControlContainer container);

    /**
     * 
     * @return the unique key of this submodule
     */
    public abstract String getKey();

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
	 * @param key the key to set
	 * @deprecated The key can not be changed
	 */
	public void setKey(String key) {
		// deprecated. 'key' was never read
	}

	/**
	 * @return
	 */
	public List<SubModule> getSubModules() {
		return subModules;
	}

	/**
	 * @return
	 */
	public boolean hasSubModules() {
		return subModules.size() > 0;
	}

	/**
	 * @param key
	 * @return
	 */
	public SubModule getSubModule(String key) {
		for (SubModule subModule : subModules) {
			if (subModule.getKey().equals(key)) {
				return subModule;
			}
		}

		return null;
	}
    
}
