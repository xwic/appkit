/**
 * 
 */
package de.xwic.appkit.webbase.toolkit.app;

import de.jwic.base.ImageRef;

/**
 * Adds some descriptive properties to a SubModule that can be displayed in the navigation or
 * the Pulse Home page.
 * 
 * @author lippisch
 */
public abstract class LaunchCardSubModule extends SubModule {

	protected String description = null;
	protected String fullTitle = null;
	protected ImageRef iconLarge = null;
	protected boolean defaultQuickLaunch = false;
	
	/**
	 * @param site
	 */
	public LaunchCardSubModule(Site site) {
		super(site);
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the moduleTitle
	 */
	public String getFullTitle() {
		return fullTitle;
	}

	/**
	 * @param moduleTitle the moduleTitle to set
	 */
	public void setFullTitle(String moduleTitle) {
		this.fullTitle = moduleTitle;
	}

	/**
	 * @return the largeIcon
	 */
	public ImageRef getIconLarge() {
		return iconLarge;
	}

	/**
	 * @param largeIcon the largeIcon to set
	 */
	public void setIconLarge(ImageRef largeIcon) {
		this.iconLarge = largeIcon;
	}

	/**
	 * @return the qlDefault
	 */
	public boolean isDefaultQuickLaunch() {
		return defaultQuickLaunch;
	}

	/**
	 * @param qlDefault the qlDefault to set
	 */
	public void setDefaultQuickLaunch(boolean qlDefault) {
		this.defaultQuickLaunch = qlDefault;
	}

	
	
}
