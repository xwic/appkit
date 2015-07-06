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
package de.xwic.appkit.webbase.toolkit.components;

import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.controls.ErrorWarning;
import de.jwic.controls.wizard.AbstractWizard;
import de.jwic.controls.wizard.WizardPage;
import de.xwic.appkit.webbase.toolkit.app.Site;

/**
 * @author Ronny Pfretzschner
 *
 */
public class SiteWizardContainerParam<WP extends WizardPage, W extends AbstractWizard<WP>> extends WizardContainer<WP, W> {

	protected final Site site;
	private boolean autoClose;

	/**
	 * @param wizard
	 * @param container
	 * @param site
	 */
	public SiteWizardContainerParam(W wizard, IControlContainer container, Site site) {
		this(wizard, container, site, false);
	}

	/**
	 * @param wizard
	 * @param container
	 * @param site
	 * @param autoClose
	 */
	public SiteWizardContainerParam(W wizard, IControlContainer container, Site site, boolean autoClose) {
		super(container, wizard);
		this.site = site;
		this.autoClose = autoClose;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.components.WizardContainerFix#onActivatePage(de.jwic.controls.ErrorWarning)
	 */
	@Override
	protected void onActivatePage(ErrorWarning errorWarning) {
		if (autoClose) {
			errorWarning.close();
		}
	}

	/**
	 * @return
	 */
	public ControlContainer openAsInnerPage() {
		ControlContainer page = new ControlContainer(site.getContentContainer());
		createControls(page);

		site.pushPage(page);
		setContainer(page);
		return page;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.components.WizardContainerFix#close()
	 */
	@Override
	public void close() {
		site.popPage(getContainer());
		super.close();
	}
}
