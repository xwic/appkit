/**
 * 
 */
package de.xwic.appkit.webbase.toolkit.app;

import java.util.List;

/**
 * Main module for the menu structure.
 * 
 * @author Ronny Pfretzschner
 *
 */
public abstract class Module {

    private String title = "";
    private String key = "";

    protected List<SubModule> subModules;

    /**
     * Creates the module and its submodules.
     * 
     * @param site
     */
    public Module(Site site) {
        key = getModuleKey();
        subModules = createSubModules(site);
    }

   
    /**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * Create the SubModules. This method is called only once during
     * construction.
     * 
	 * @param site
	 * @return
	 */
    protected abstract List<SubModule> createSubModules(Site site);

    /**	
     * @return the unique module key.
     */
    protected abstract String getModuleKey();

    /**
     * Returns a list of the subModules.
     * 
     * @return
     */
    public List<SubModule> getSubModules() {
        return subModules;
    }

    /**
     * Returns the registered submodule with the given key.
     * 
     * @param key
     * @return
     */
    public SubModule getSubModule(String key) {
    	String[] keys = key.split(";");
    	
    	// find the first module
    	SubModule sm = null;
        for (SubModule subModule : subModules) {
            if (subModule.getKey().equals(keys[0])) {
            	sm = subModule;
            }
        }
        
        // drill deeper
    	for (int i = 1; i < keys.length && sm != null; i++) {
    		sm = sm.getSubModule(keys[i]);
    	}
        if (sm == null) {
        	throw new RuntimeException("The submodule with key: " + key + " was not registered to this module !");
        }
        return sm;
    }
    
}
