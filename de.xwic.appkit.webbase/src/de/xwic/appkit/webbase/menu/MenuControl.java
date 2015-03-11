/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 *
 * com.netapp.commons.menu.MenuControl
 */

package de.xwic.appkit.webbase.menu;

import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.base.IncludeJsOption;
import de.jwic.base.JavaScriptSupport;
import de.xwic.appkit.webbase.modules.IMenuItemsProvider;
import de.xwic.appkit.webbase.toolkit.app.ExtendedApplication;
import de.xwic.appkit.webbase.toolkit.app.INavigationEventListener;
import de.xwic.appkit.webbase.toolkit.app.Module;
import de.xwic.appkit.webbase.toolkit.app.SubModule;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author eugen
 */
@JavaScriptSupport
public final class MenuControl extends ControlContainer implements INavigationEventListener {

	private final String rootItemTitle;
	private final List<Module> modules;
	private final INavigator navigator;
	private final IMenuItemsProvider menuAccessRetriever;

	MenuControl(IControlContainer container, String name, List<Module> modules, IMenuItemsProvider menuAccessRetriever,
			String rootItemTitle, INavigator navigator) {
		super(container, name);
		this.modules = new ArrayList<Module>(modules);
		this.rootItemTitle = rootItemTitle;
		this.navigator = navigator;
		this.menuAccessRetriever = menuAccessRetriever;
	}

	@Override
	public void actionPerformed(String actionName, String param) {
		if ("MainMenuClick".equals(actionName)) {
			navigator.activateModule(param);
		}
	}

	@IncludeJsOption
	public String getActiveModuleKey() {
		return navigator.getActiveModuleKey();
	}

	@IncludeJsOption
	public List<de.xwic.appkit.webbase.modules.MenuItem> getAdditionalItems() {
		return menuAccessRetriever.fetchModuleBeans();
	}

	@Override
	public void navigationChanged() {
		this.requireRedraw();
	}

	@IncludeJsOption
	public JSONObject getRootItem() {
		MenuItem rootItem = new MenuItem(null, this.rootItemTitle, "");
		for (Module m : this.modules) {
			rootItem.addMenuItem(new MenuItem(rootItem, m));
		}

		try {
			return rootItem.serialize();
		} catch (JSONException e) {
			log.error(e.getMessage(), e);
			return new JSONObject();
		}
	}

	private static final class MenuItem {

		private final MenuItem parent;
		private final List<MenuItem> subItems;
		private final String title;
		private final String key;

		public MenuItem(MenuItem parent, String title, String key) {
			this.subItems = new ArrayList<MenuItem>();
			this.title = title;
			this.parent = parent;
			this.key = key;
		}

		public MenuItem(MenuItem parent, Module module) {
			this(parent, module.getTitle(), module.getKey());
			for (SubModule sm : module.getSubModules()) {
				this.addMenuItem(new MenuItem(this, sm));
			}
		}

		private MenuItem(MenuItem parent, SubModule subModule) {
			this(parent, subModule.getTitle(), subModule.getKey());
			for (SubModule sm : subModule.getSubModules()) {
				this.addMenuItem(new MenuItem(this, sm));
			}
		}

		public String getTitle() {
			return title;
		}

		public String getId() {
			return this.key;
		}

		public String getKey() {
			String parentKey = "";
			if (parent != null) {
				parentKey = parent.getKey();
				if (!parentKey.trim().isEmpty()) {
					parentKey += ";";
				}
			}
			return parentKey + this.key;
		}

		public void addMenuItem(MenuItem menuItem) {
			this.subItems.add(menuItem);
		}

		public JSONObject serialize() throws JSONException {
			JSONObject object = new JSONObject();
			object.put("key", this.getKey());
			object.put("title", this.getTitle());
			object.put("id", this.getId());
			JSONArray children = new JSONArray();
			for (MenuItem m : this.subItems) {
				children.put(m.serialize());
			}
			object.put("children", children);
			return object;
		}
	}

	public static final class Builder{

		private final IControlContainer controlContainer;
		private final List<Module> modules;
		private final INavigator navigator;
		private String name;
		private String title;
		private IMenuItemsProvider accessRetriever;

		public Builder(IControlContainer controlContainer, INavigator navigator, List<Module> modules) {
			this.navigator = navigator;
			this.modules = modules;
			this.controlContainer = controlContainer;
			this.title = ExtendedApplication.getSite(controlContainer.getSessionContext()).getTitle();
			this.name = null;
			this.accessRetriever = NullItemsProvider.INSTANCE;
		}

		public Builder name(String name){
			this.name = name;
			return this;
		}

		public Builder title(String title){
			this.title = title == null ? this.title : title;
			return this;
		}


		public Builder additionalItemsProvider(IMenuItemsProvider accessRetriever){
			this.accessRetriever = accessRetriever == null ? NullItemsProvider.INSTANCE : accessRetriever;
			return this;
		}


		public MenuControl build(){
			return new MenuControl(this.controlContainer, this.name, this.modules, this.accessRetriever, this.title, this.navigator);
		}
	}

}
