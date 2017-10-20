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
import de.jwic.base.ImageRef;

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
  
	protected String description = null;
	protected String fullTitle = null;
	protected ImageRef iconLarge = null;
	protected boolean defaultQuickLaunch = false;
	protected boolean commonModule = true;

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
     * Must return a unique key of the submodule used for the navigation. Uses the
     * simple classname by default, but can be overriden to make it unique.
     * @return the unique key of this submodule
     */
    public String getKey() {
    	return getClass().getSimpleName();
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

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the moduleTitle
	 */
	public String getFullTitle() {
		return fullTitle;
	}

	/**
	 * @param moduleTitle the moduleTitle to set
	 */
	public void setFullTitle(String moduleTitle) {
		this.fullTitle = moduleTitle;
	}

	/**
	 * @return the largeIcon
	 */
	public ImageRef getIconLarge() {
		return iconLarge;
	}

	/**
	 * @param largeIcon the largeIcon to set
	 */
	public void setIconLarge(ImageRef largeIcon) {
		this.iconLarge = largeIcon;
	}

	/**
	 * @return the qlDefault
	 */
	public boolean isDefaultQuickLaunch() {
		return defaultQuickLaunch;
	}

	/**
	 * @param qlDefault the qlDefault to set
	 */
	public void setDefaultQuickLaunch(boolean qlDefault) {
		this.defaultQuickLaunch = qlDefault;
	}

	/**
	 * @return the commonModule
	 */
	public boolean isCommonModule() {
		return commonModule;
	}

	/**
	 * @param commonModule the commonModule to set
	 */
	public void setCommonModule(boolean commonModule) {
		this.commonModule = commonModule;
	}


}
