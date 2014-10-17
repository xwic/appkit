/**
 * 
 */
package de.xwic.appkit.webbase.toolkit.components;

import de.jwic.base.IControlContainer;
import de.jwic.controls.wizard.AbstractWizard;
import de.jwic.controls.wizard.WizardPage;

/**
 * Fix, because the ecolib container did not work...
 * 
 * @author Ronny Pfretzschner
 *
 */
public class WizardContainerFix extends WizardContainer<WizardPage, AbstractWizard<WizardPage>> {

	/**
	 * @param parent
	 * @param wizard
	 */
	public WizardContainerFix(IControlContainer parent, AbstractWizard<WizardPage> wizard) {
		super(parent, wizard);
	}

}
