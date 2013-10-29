/**
 *
 */
package de.xwic.appkit.webbase.toolkit.components;

import de.jwic.base.IControlContainer;
import de.jwic.controls.wizard.Wizard;
import de.xwic.appkit.webbase.toolkit.app.Site;

/**
 * performNext() and performBack() now public
 *
 * @author Alexandru Bledea
 * @since Sep 24, 2013
 */
public class SiteWizardContainerNextPrevious extends SiteWizardContainer {

	/**
	 * @param wizard
	 * @param container
	 * @param site
	 * @param autoClose
	 */
	public SiteWizardContainerNextPrevious(Wizard wizard, IControlContainer container, Site site, boolean autoClose) {
		super(wizard, container, site, autoClose);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.components.WizardContainerFix#performNext()
	 */
	@Override
	public void performNext() {
		super.performNext();
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.components.WizardContainerFix#performBack()
	 */
	@Override
	public void performBack() {
		super.performBack();
	}
}
