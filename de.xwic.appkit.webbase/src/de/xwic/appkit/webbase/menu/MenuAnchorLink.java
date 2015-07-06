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

package de.xwic.appkit.webbase.menu;

import de.jwic.base.IControlContainer;
import de.jwic.controls.AnchorLink;
import de.jwic.events.SelectionEvent;
import de.jwic.events.SelectionListener;
import de.xwic.appkit.webbase.toolkit.app.ExtendedApplication;
import de.xwic.appkit.webbase.toolkit.app.Site;

/**
 * @author bogdan
 *
 */
public class MenuAnchorLink extends AnchorLink {

	public MenuAnchorLink(IControlContainer container, String module, String subModule) {
		this(container, null, module, subModule);
	}

	public MenuAnchorLink(IControlContainer container, String name, final String module, final String subModule) {
		super(container, name);
		final Site site = ExtendedApplication.getSite(getSessionContext());
		this.setTemplateName(AnchorLink.class.getName());
		this.addSelectionListener(new SelectionListener() {

			@Override
			public void objectSelected(SelectionEvent event) {
				site.actionSelectMenu(module + ";" + subModule);
			}
		});
	}

}
