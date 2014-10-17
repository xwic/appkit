/**
 *
 */
package de.xwic.appkit.webbase.toolkit.components;

import de.jwic.base.IControlContainer;
import de.jwic.controls.wizard.AbstractWizard;
import de.jwic.controls.wizard.WizardPage;
import de.xwic.appkit.webbase.toolkit.app.Site;

/**
 * @author Ronny Pfretzschner
 *
 */
public class SiteWizardContainer extends SiteWizardContainerParam<WizardPage, AbstractWizard<WizardPage>> {

	/**
	 * @param wizard
	 * @param container
	 * @param site
	 */
	public SiteWizardContainer(AbstractWizard<WizardPage> wizard, IControlContainer container, Site site) {
		this(wizard, container, site, false);
	}

	/**
	 * @param wizard
	 * @param container
	 * @param site
	 * @param autoClose
	 */
	public SiteWizardContainer(AbstractWizard<WizardPage> wizard, IControlContainer container, Site site, boolean autoClose) {
		super(wizard, container, site, autoClose);
	}

}
