package de.xwic.appkit.webbase.modules;

import de.jwic.events.SessionAdapter;
import de.jwic.events.SessionEvent;
import de.xwic.appkit.core.util.CollectionUtil;
import de.xwic.appkit.core.util.Functions;
import de.xwic.appkit.webbase.toolkit.app.Module;
import de.xwic.appkit.webbase.toolkit.app.Site;
import de.xwic.appkit.webbase.toolkit.app.SubModule;

import java.lang.Exception;import java.lang.Override;import java.lang.String;import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by boogie on 3/5/15.
 */
public final class MenuUrlRedirecter extends SessionAdapter{
	public static final String REDIRECT_PARAM = "goto";

	private final Site site;

	public MenuUrlRedirecter(Site site) {
		this.site = site;
	}

	@Override
	public final void sessionStarted(SessionEvent event) {
		this.sessionReused(event);
	}

	@Override
	public final void sessionReused(SessionEvent event) {
		final String param = event.getParameter(REDIRECT_PARAM);
		//no param, no problem. just skip this stuff
		if(param == null || param.isEmpty()){
			return;
		}

		final String[] split = param.split(";");
		//should be a "module;submodule;..." thing
		//so after split string length should be grater then 1
		if(split.length <= 1){
			return;
		}
		final String moduleKey = split[0];
		final Set<String> subModulesKeys = getSubmodules(split);

		final Module module = site.getModuleByKey(moduleKey);
		//no module, no problem
		if(module == null){
			return;
		}
		try {
			final SubModule deepestSubmodule = module.getSubModule(CollectionUtil.join(subModulesKeys, Functions.<String>identity(), ";"));
			//if there is no sub-module in the url param, just return
			if (deepestSubmodule == null) {
				return;
			}
		}catch(Exception e){
			return;
		}

		//otherwise, all possible tests have passed, so we activate the correct module
		site.actionSelectMenu(param);
	}




	/**
	 * return the submodules form the given string (all stuff except the first)
	 * @param split
	 * @return
	 */
	private Set<String> getSubmodules(String[] split) {
		final Set<String> subModules = new LinkedHashSet<String>(Arrays.asList(split));
		subModules.remove(split[0]);
		return subModules;
	}
}
