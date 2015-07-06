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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.jwic.base.Application;
import de.jwic.base.Control;
import de.jwic.base.IControlContainer;
import de.jwic.base.Page;
import de.jwic.base.SessionContext;
import de.xwic.appkit.core.config.ConfigurationManager;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.security.IUser;
import de.xwic.appkit.webbase.toolkit.login.LoginControl;
import de.xwic.appkit.webbase.toolkit.login.LoginModel;
import de.xwic.appkit.webbase.toolkit.login.LoginModel.ILoginListener;

/**
 * Main application for a jwic application. The specific project
 * application should extend this class.
 * 
 * @author Ronny Pfretzschner
 *
 */
public abstract class ExtendedApplication extends Application implements INavigationEventListener {
	
	/**
	 * Domain ID for the core component.
	 */
	public final static String CORE_DOMAIN_ID = "core";
	
    private Site site = null;
    private String selectedSite;
    private Map<String, Object> stateDict;
    
    private String startedByUser = null;

    /**
     * Creates the Application.
     */
    public ExtendedApplication() {
        stateDict = new HashMap<String, Object>();
    }

    /*
     * (non-Javadoc)
     * @see de.pol.netapp.spc.toolkit.app.INavigationEventListener#navigationChanged()
     */
    public void navigationChanged() {
    	selectedSite = site.getActiveModuleKey() + ";" + site.getActiveSubModuleKey();
    }
    
    /**
	 * @return the site
	 */
	public Site getSite() {
		return site;
	}

	/**
	 * @param site the site to set
	 */
	public void setSite(Site site) {
		this.site = site;
	}

	/**
	 * @return the selectedSite
	 */
	public String getSelectedSite() {
		return selectedSite;
	}

	/**
	 * @param selectedSite the selectedSite to set
	 */
	public void setSelectedSite(String selectedSite) {
		this.selectedSite = selectedSite;
	}

	/**
	 * @return the stateDict
	 */
	public Map<String, Object> getStateDict() {
		return stateDict;
	}

	/**
	 * @param stateDict the stateDict to set
	 */
	public void setStateDict(Map<String, Object> stateDict) {
		this.stateDict = stateDict;
	}

	/**
	 * Static access to the application.
	 * 
	 * @param control
	 * @return
	 */
    public static ExtendedApplication getInstance(Control control) {
        return getInstance(control.getSessionContext());
    }

    /**
	 * Static access to the application.
	 *
	 * @param control
	 * @return
	 */
    public static ExtendedApplication getInstance(final SessionContext sessionContext) {
		return (ExtendedApplication)sessionContext.getApplication();
    }

    /**
	 * Static access to the site.
	 *
	 * @param control
	 * @return
	 */
    public static Site getSite(final SessionContext sessionContext) {
		return getInstance(sessionContext).getSite();
    }

    /*
     * (non-Javadoc)
     * @see de.jwic.base.Application#createRootControl(de.jwic.base.IControlContainer)
     */
    public Control createRootControl(IControlContainer container) {
        getSessionContext().setExitURL(ConfigurationManager.getSetup().getProperty("ExitURL"));
        SplashLoader splash = new SplashLoader(container, this);
        splash.setTemplateName(getSplashTemplateName());
        return splash;

    }

    protected String getSplashTemplateName() {
    	return getClass().getName() + "Splash";
    }
    protected String getSiteTemplateName() {
    	return getClass().getName() + "Site";
    }
    
    public abstract String getPageTitle();
    
    /**
     * Loads the application.
     * 
     * @param container
     */
    protected void performLoad(final SessionContext container) {
    	Page loginPage = new Page(container);
    	loginPage.setTemplateName(ExtendedApplication.class.getName() + "LoginPage");
    	loginPage.setTitle(getPageTitle());
    	
    	LoginModel model = new LoginModel();
    	new LoginControl(loginPage, "loginControl", model);
    	container.pushTopControl(loginPage);
    	
    	model.addListener(new ILoginListener() {

			public void loginSuccessful() {

				loadSite(container);
			}
    		
    	});

    }
    
    /**
     * Create Site page, pushes on top and load Application
     * @param container
     */
    protected void loadSite(SessionContext container) {
        site = new Site(container);
        site.setTemplateName(getSiteTemplateName());
        container.pushTopControl(site);
        site.addNavigationEventListener(ExtendedApplication.this);

        // now load the specific locale according to the current user, if the language setting is set
        IUser currentUser = DAOSystem.getSecurityManager().getCurrentUser();
        startedByUser = currentUser != null ? currentUser.getLogonName() : null;
        
        Locale locale = null; 
        if (null != currentUser && null != currentUser.getLanguage()) {
        	locale = LocaleFactory.getLocaleForLanguage(currentUser.getLanguage());
        }
        
        if (locale == null) {
        	// default language = en
        	locale = LocaleFactory.getLocaleForLanguage("en");
        }
        
        site.getSessionContext().setLocale(locale);
        
        // now load the application
        loadApp(site);
    }

    /**
     * @return the help url from the application.
     */
    public abstract String getHelpURL();
    
    /**
     * Loads the application. Project specific content.
     * @param site
     */
    protected abstract void loadApp(Site site);

    /**
     * Small helper for the splash screen.
     * 
     * @author Ronny Pfretzschner
     *
     */
    private class SplashLoader extends SplashControl {

        private ExtendedApplication encInstance;
        
        /**
         * Creates the loader class.
         * 
         * @param container
         * @param encInstance
         */
        public SplashLoader(IControlContainer container, ExtendedApplication encInstance) {
            super(container);
            this.encInstance = encInstance;
        }
        
        /*
         * (non-Javadoc)
         * @see de.pol.netapp.spc.toolkit.app.SplashControl#performLoad()
         */
        protected void performLoad() {
            encInstance.performLoad(getSessionContext());
            destroy();
        }

    }

	/**
	 * @return the startedByUser
	 */
	public String getStartedByUser() {
		return startedByUser;
	}
}
