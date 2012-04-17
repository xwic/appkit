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
