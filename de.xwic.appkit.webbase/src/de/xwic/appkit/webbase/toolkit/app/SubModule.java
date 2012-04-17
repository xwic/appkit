/**
 * 
 */
package de.xwic.appkit.webbase.toolkit.app;

import java.util.ArrayList;
import java.util.List;

import de.jwic.base.IControl;
import de.jwic.base.IControlContainer;

/**
 * Submodule for Modules.
 * 
 * @author Ronny Pfretzschner
 *
 */
public abstract class SubModule {

    private String title;

    protected List<SubModule> subModules = new ArrayList<SubModule>();
    
    /**
     * Creates the submodule.
     * 
     * @param site
     */
    public SubModule(Site site) {

    }

    /**
     * Creates the controls, usually an extension of InnerPage. The
     * root control must be returned.
     * 
     * @param container
     * @return
     */
    public abstract IControl createControls(IControlContainer container);

    /**
     * 
     * @return the unique key of this submodule
     */
    public abstract String getKey();

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
	 * @param key the key to set
	 * @deprecated The key can not be changed
	 */
	public void setKey(String key) {
		// deprecated. 'key' was never read
	}

	/**
	 * @return
	 */
	public List<SubModule> getSubModules() {
		return subModules;
	}

	/**
	 * @return
	 */
	public boolean hasSubModules() {
		return subModules.size() > 0;
	}

	/**
	 * @param key
	 * @return
	 */
	public SubModule getSubModule(String key) {
		for (SubModule subModule : subModules) {
			if (subModule.getKey().equals(key)) {
				return subModule;
			}
		}

		return null;
	}
    
}
