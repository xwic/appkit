package de.xwic.appkit.webbase.menu;

import de.xwic.appkit.webbase.toolkit.app.Site;

/**
 * Created by boogie on 3/6/15.
 */
public final class SiteNavigator implements INavigator{

	private final Site site;

	public SiteNavigator(Site site) {
		this.site = site;
	}

	@Override
	public String getActiveModuleKey() {
		return site.getActiveModuleKey() + ";" + site.getActiveSubModuleKey();
	}

	@Override
	public void activateModule(String key) {
		site.actionSelectMenu(key);
	}
}
