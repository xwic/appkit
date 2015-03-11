package de.xwic.appkit.webbase.menu;

import de.xwic.appkit.webbase.modules.IMenuItemsProvider;
import de.xwic.appkit.webbase.modules.ModuleBean;

import java.util.Collections;
import java.util.List;

/**
 * Created by boogie on 3/10/15.
 */
class NullItemsProvider implements IMenuItemsProvider {

	public static final NullItemsProvider INSTANCE = new NullItemsProvider();

	private NullItemsProvider(){}

	@Override
	public List<ModuleBean> fetchModuleBeans() {
		return Collections.emptyList();
	}
}
