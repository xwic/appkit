/**
 * 
 */
package de.xwic.appkit.webbase.toolkit.components;

import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.ecolib.wizard.Wizard;
import de.xwic.appkit.webbase.toolkit.app.Site;

/**
 * @author Ronny Pfretzschner
 *
 */
public class SiteWizardContainer extends WizardContainerFix {

	private Site site = null;
	
	public SiteWizardContainer(Wizard wizard, IControlContainer container, Site site) {
		super(container, wizard);
		this.site = site;
		
	}

	
	public ControlContainer openAsInnerPage() {
        ControlContainer page = new ControlContainer(site.getContentContainer());
        createControls(page);
        
        site.pushPage(page);
        setContainer(page);
        return page;
	}
	
	@Override
	public void close() {
        site.popPage(getContainer());
       super.close();
	}
}
