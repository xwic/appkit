/**
 * 
 */
package de.xwic.appkit.webbase.toolkit.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import de.jwic.base.Control;
import de.jwic.base.ControlContainer;
import de.jwic.base.IControl;
import de.jwic.base.IControlContainer;
import de.jwic.base.Page;
import de.jwic.controls.ErrorWarning;
import de.jwic.controls.Label;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.security.IUser;
import de.xwic.appkit.webbase.core.Platform;
import de.xwic.appkit.webbase.prefstore.IPreferenceStore;

/**
 * This is the main page for the application.
 * 
 * @author Ronny Pfretzschner
 *
 */
public class Site extends Page {
	
    protected List<Module> modules = new ArrayList<Module>();

    protected StackedContainerWithEvent controlStack;
    protected ControlContainer dialogContainer;
    protected BreadCrumbControl breadCrumb;
    protected ErrorWarning globalErrorControl;
    protected LogoutControl btLogout;

    protected String activeModuleKey = "";
    protected String activeSubModuleKey = "";

    protected List<INavigationEventListener> navigationListeners = new ArrayList<INavigationEventListener>();
    
    /**
     * Creates the main page for the application.
     * 
     * @param container
     */
    public Site(IControlContainer container) {
        super(container);
        Label lbUsername = new Label(this, "lbUsername");
        IUser user = DAOSystem.getSecurityManager().getCurrentUser();

        if (user != null) {
        	String text = user.getName() + " ";

//        	if (user.getLanguage() != null && user.getLanguage().length() > 0) {
//        		text += user.getLanguage().toUpperCase();
//        	} 
            lbUsername.setText(text);
        	
        } else {
        	lbUsername.setText("no user found!");
        }


        
        Label lbRole = new Label(this, "lbRole");
        lbRole.setText("");

        controlStack = new StackedContainerWithEvent(this, "content");
        
        dialogContainer = new ControlContainer(this, "dialogs");

        breadCrumb = new BreadCrumbControl(this, "breadCrumb");
        breadCrumb.setControlStack(controlStack);

        btLogout = new LogoutControl(this, "btLogout");
        btLogout.setTitle("Logout");
        
        globalErrorControl = new ErrorWarning(this, "errorWarning");
        globalErrorControl.setAutoClose(true);
        globalErrorControl.setShowStackTrace(false);
        
    }

    /**
     * Register listener to be informed, when the user changed the controls.
     * 
     * @param listener
     */
    public void addNavigationEventListener(INavigationEventListener listener) {
    	if (!navigationListeners.contains(listener)) {
    		navigationListeners.add(listener);
    	}
    }
    
    /**
     * Unregister the given listener.
     * 
     * @param listener
     */
    public void removeNavigationEventListener(INavigationEventListener listener) {
    	if (navigationListeners.contains(listener)) {
    		navigationListeners.remove(listener);
    	}
    }
    
    /**
     * Inform all listeners.
     */
    @SuppressWarnings("unchecked")
	protected void navigationChanged() {
    	List<INavigationEventListener> clone = (List<INavigationEventListener>)((ArrayList<INavigationEventListener>) navigationListeners).clone();
    	
    	for (INavigationEventListener navigationEventListener : clone) {
			navigationEventListener.navigationChanged();
		}
    }
    
    

	/**
	 * @return the activeModuleKey
	 */
	public String getActiveModuleKey() {
		return activeModuleKey;
	}

	/**
	 * @param activeModuleKey the activeModuleKey to set
	 */
	public void setActiveModuleKey(String activeModuleKey) {
		this.activeModuleKey = activeModuleKey;
	}

	/**
	 * @return the activeSubModuleKey
	 */
	public String getActiveSubModuleKey() {
		return activeSubModuleKey;
	}

	/**
	 * @param activeSubModuleKey the activeSubModuleKey to set
	 */
	public void setActiveSubModuleKey(String activeSubModuleKey) {
		this.activeSubModuleKey = activeSubModuleKey;
	}

	/**
	 * Set a message for the exit button.
	 * 
	 * @param message
	 */
	public void setLogoutButtonConfirmMsg(String message) {
        btLogout.setInfoMessage(message);
    }

	/**
	 * Register a main module on this site.
	 * 
	 * @param module
	 */
    public void addModule(Module module) {
        modules.add(module);
    }

    /**
     * Removes a main module from this site.
     * 
     * @param module
     */
    public void removeModule(Module module) {
        modules.remove(module);
    }

    /**
     * Register a main module on the given position on this site.
     * 
     * @param module
     * @param pos
     */
    public void addModule(Module module, int pos) {
        if (modules.size() >= pos)
            modules.add(pos, module);
        else addModule(module);
    }

    /**
     * Get all registered modules.
     * 
     * @return
     */
    public List<Module> getModules() {
        return modules;
    }

    /**
     * An entry in the menu was pressed.
     * 
     * @param key
     */
    public void actionSelectMenu(String key) {
        // handle menue action
        int idx = key.indexOf(';');
        if (idx != -1) {
            String moduleKey = key.substring(0, idx);
            String subModuleKey = key.substring(idx + 1);

//            if (!moduleKey.equals(activeModuleKey) || !subModuleKey.equals(activeSubModuleKey)) {
                //MessageDialog dirtyEditorDialog = new MessageDialog(GetContentContainer());
                //dirtyEditorDialog.Message = "You are going to leave an Editor with unsaved changes. Continue?";
                //dirtyEditorDialog.DialogFinished += new DialogEventHandler(dirtyEditorDialog_DialogFinished);
                //dirtyEditorDialog.Title = "You have unsaved changes...";
                //dirtyEditorDialog.openAsPage();
                loadModule(moduleKey, subModuleKey);
//            }
        }
    }

    /**
     * Returns the context based preference store for site settings.
     * @return
     */
    public IPreferenceStore getPreferenceStore() {
    	String context = getClass().getName() + "_" + getSessionContext().getApplication().getClass().getName();
    	return Platform.getContextPreferenceProvider().getPreferenceStore(context);
    }
    
    /**
     * Load the module.
     * 
     * @param moduleKey
     * @param subModuleKey
     */
    private void loadModule(String moduleKey, String subModuleKey) {
        // destroy all controls on the active stack.
        // this feature could probably be removed later,
        // to increase useability.
        Iterator<Control> e = controlStack.getControls();
        
        List<IControl> temp = new ArrayList<IControl>();
        
        while (e.hasNext()) {
            temp.add((IControl)e.next());
        }

        for (IControl control : temp) {
        	control.destroy();
		}
        

        Module module = getModuleByKey(moduleKey);
        if (module == null) {
        	log.warn("Module with key '" + moduleKey + "' does not exist!");
        	return; // this module does not exist -> hard exit.
        }
        if ("".equals(subModuleKey)) {
            activeSubModuleKey = module.getSubModules().get(0).getKey();
        }
        activeModuleKey = moduleKey;

        SubModule subModule = module.getSubModule(subModuleKey);
        if (subModule == null) {
        	subModule = module.getSubModules().get(0);
        	activeSubModuleKey = subModule.getKey();
        } else {
        	activeSubModuleKey = subModuleKey;
        }
        
        
        IControl control = subModule.createControls(controlStack);
        controlStack.setCurrentControlName(control.getName());

        // store the last active module/submodule in the preference store
        if (Platform.isInitialized()) {
        	IPreferenceStore prefStore = getPreferenceStore();
        	prefStore.setValue("activeModuleKey", moduleKey);
        	prefStore.setValue("activeSubModuleKey", subModuleKey);
        	try {
				prefStore.flush();
			} catch (IOException e1) {
				log.error("Error flushing preference store settings", e1);
			}
        }
        
        navigationChanged();
    }
    
    /**
     * Trys to restore the module the user has viewed the last time. Returns false
     * if the restoration failed. This could happen if the user accesses the system
     * the first time or if a previous used module is no longer available.
     * @return
     */
    public boolean restoreNavigation() {

    	if (Platform.isInitialized()) {
			IPreferenceStore prefStore = getPreferenceStore();
			String moduleKey = prefStore.getString("activeModuleKey", null);
			String subModuleKey = prefStore.getString("activeSubModuleKey", null);
			
			if (moduleKey != null && subModuleKey != null) {
				try {
					Module module = getModuleByKey(moduleKey);
					SubModule sm = module.getSubModule(subModuleKey);
					if (sm == null) {
						return false; // do not restore if the module does no longer exist
					}
				} catch (RuntimeException re) {
					return false; // the module key does not exist -> do not restore old navigation
				}
				loadModule(moduleKey, subModuleKey);
				return true;
					
			}
		}
		return false;
    }

    /**
     * Returns the module by given key.
     * 
     * @param key
     * @return
     */
    public Module getModuleByKey(String key) {
        for (Module mod : modules) {
            if (mod.getKey().equals(key)) {
                return mod;
            }
        }

        throw new RuntimeException("The module with key: " + activeModuleKey + " was not registered !");
    }

    /**
     * Returns the content stack container. Full-Size content controls should use this container.
     * Dialog windows should use the DialogContainer.
     * 
     * @return
     */
    public IControlContainer getContentContainer() {
        return controlStack;
    }
    
    /**
     * Returns a container for dialog windows, which have an absolute position, most
     * usually floating above the other content.
     * @return
     */
    public IControlContainer getDialogContainer() {
    	return dialogContainer;
    }

    /**
     * Pushes the given control on the stack.
     * 
     * @param control
     */
    public void pushPage(IControl control) {
        if (control.getContainer() == controlStack) {
            /*pageStack.Add(control.Name);*/
            controlStack.setCurrentControlName(control.getName());
            
            //TODO throw event!
            navigationChanged();
        } else {
            throw new RuntimeException("Wrong container used! Use site.getContentContainer() as container!");
        }
    }

    /**
     * Removes the given control from the stack.
     * 
     * @param control
     */
    public void popPage(IControl control) {
        controlStack.removeControl(control.getName());
        navigationChanged();
    }


    /**
     * The URL for the help button.
     * 
     * @return
     */
    public String getHelpUrl() {
    	return ExtendedApplication.getInstance(this).getHelpURL();
    }

    /**
     * Show the ErrorWarning with an Error.
     * 
     * @param message
     */
    public void showError(String message) {
        globalErrorControl.showError(message);
        globalErrorControl.setRequireRedraw(true); // control does not trigger this 
    }

    /**
     * Show the ErrorWarning as warning with yellow background or specified with given css.
     * 
     * @param message
     * @param cssClass
     */
    public void showWarning(String message, String cssClass) {
        globalErrorControl.showWarning(message, cssClass);
        globalErrorControl.setRequireRedraw(true); // control does not trigger this 
    }

    /**
     * Show the ErrorWarning as warning with yellow background.
     * 
     * @param message
     */
    public void showWarning(String message) {
        globalErrorControl.showWarning(message);
        globalErrorControl.setRequireRedraw(true); // control does not trigger this 
    }


    /**
     * Show the ErrorWarning with an exception.
     * 
     * @param e
     */
    public void showError(Exception e) {
        globalErrorControl.showError(e);
        globalErrorControl.setRequireRedraw(true); // control does not trigger this 
    }
    
    public String getCurrentYear() {
    	Calendar cal = Calendar.getInstance();
    	return Integer.toString(cal.get(Calendar.YEAR));
    }
    
    /* (non-Javadoc)
     * @see de.jwic.base.Page#setClientTop(int)
     */
    @Override
    public void setClientTop(int clientTop) {
    	// copy the value to the inner page (if available)
    	super.setClientTop(clientTop);
    	Control ctrl = controlStack.getControl(controlStack.getCurrentControlName());
    	if (ctrl instanceof InnerPage) {
    		((InnerPage)ctrl).setScrollTop(clientTop);
    	}
    }

    /* (non-Javadoc)
     * @see de.jwic.base.Page#setClientTop(int)
     */
    @Override
    public void setClientLeft(int clientLeft) {
    	// copy the value to the inner page (if available)
    	super.setClientLeft(clientLeft);
    	Control ctrl = controlStack.getControl(controlStack.getCurrentControlName());
    	if (ctrl instanceof InnerPage) {
    		((InnerPage)ctrl).setScrollLeft(clientLeft);
    	}
    }

}
