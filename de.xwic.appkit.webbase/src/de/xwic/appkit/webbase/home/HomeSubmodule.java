/*******************************************************************************
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
 *******************************************************************************/
package de.xwic.appkit.webbase.home;

import de.jwic.base.IControl;
import de.jwic.base.IControlContainer;
import de.xwic.appkit.webbase.toolkit.app.Site;
import de.xwic.appkit.webbase.toolkit.app.SubModule;


public class HomeSubmodule extends SubModule {

	/**
	 * @param site
	 */
	public HomeSubmodule(Site site) {
		super(site);
		setTitle("Get Started");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.toolkit.app.SubModule#createControls(de.jwic.base.IControlContainer)
	 */
	public IControl createControls(IControlContainer container) {
		return new HomePage(container, "home", site.getApplicationId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.toolkit.app.SubModule#getKey()
	 */
	public String getKey() {
		return "home";
	}

}
