package de.xwic.appkit.webbase.modules;

import de.xwic.appkit.webbase.toolkit.app.Module;
import de.xwic.appkit.webbase.toolkit.app.Site;

import java.lang.String;import java.util.List;

/**
 * Created by boogie on 3/6/15.
 */
public interface IModuleFactory {

	List<Module> createModules(String user, Site site);
}
