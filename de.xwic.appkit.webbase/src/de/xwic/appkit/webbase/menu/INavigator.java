package de.xwic.appkit.webbase.menu;

/**
 * Created by boogie on 3/6/15.
 */
public interface INavigator {
	String getActiveModuleKey();
	void activateModule(String key);
}
