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
 * de.xwic.appkit.webbase.toolkit.app.SplashControl
 */
package de.xwic.appkit.webbase.toolkit.app;

import de.jwic.base.IControlContainer;
import de.jwic.base.Page;
import de.xwic.appkit.core.config.Bundle;
import de.xwic.appkit.core.config.ConfigurationManager;
import de.xwic.appkit.core.config.Setup;
import de.xwic.appkit.webbase.toolkit.util.BundleAccessor;

/**
 * Splash control for loading action.
 * 
 * @author Ronny Pfretzschner
 *
 */
public class SplashControl extends Page {
    
	/**
	 * Creates the splash screen.
	 * 
	 * @param con tainer
	 */
	public SplashControl(IControlContainer container) {
	    super(container);
	    Bundle bundle = BundleAccessor.getDomainBundle(this, ExtendedApplication.CORE_DOMAIN_ID);
	    setTitle(bundle.getString("splash.loading.title"));
	    setTemplateName(SplashControl.class.getName());
	}

	/**
	 * Load the Control.
	 */
	public void actionLoad() {
	    performLoad();
	}
	
	/**
	 * Returns the setup to allow the display of additional information, i.e. version etc.
	 * @param key
	 * @return
	 */
	public Setup getSetup() {
		return ConfigurationManager.getSetup();
	}
	
	/**
	 * Load the control.
	 */
	protected void performLoad() {
	    // to be overriden.
	}
}
