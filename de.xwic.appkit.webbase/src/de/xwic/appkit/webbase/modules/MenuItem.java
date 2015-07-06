/*******************************************************************************
 * Copyright 2015 xWic group (http://www.xwic.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *  
 *******************************************************************************/
package de.xwic.appkit.webbase.modules;

import de.xwic.appkit.webbase.toolkit.app.Module;
import de.xwic.appkit.webbase.toolkit.app.SubModule;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by boogie on 3/6/15.
 */
public final class MenuItem implements JSONString {

	private final String path;
	private final String title;
	private final List<MenuItem> children;
	private final String accessUrl;

	public MenuItem(String path, String title, List<MenuItem> children, String accessUrl) {
		this.path = path;
		this.title = title;
		if(children != null) {
			children = Collections.unmodifiableList(children);
		}
		this.children = children;
		this.accessUrl = accessUrl;
	}

	public final JSONObject serialize() throws JSONException {
		final JSONObject jsonObject = new JSONObject();
		JSONArray children = new JSONArray();
		if (this.children != null) {
			for (MenuItem mb : this.children) {
				children.put(mb.serialize());
			}
		}

		jsonObject.put("title", this.title);
		jsonObject.put("path", this.path);
		jsonObject.put("url", this.accessUrl);
		jsonObject.put("children", children);

		return jsonObject;
	}

	public static MenuItem deserialize(JSONObject serialized) throws JSONException {
		final String title = serialized.getString("title");
		final String path = serialized.getString("path");;
		final String url = serialized.getString("url");
		final JSONArray childrenArray = serialized.getJSONArray("children");

		final List<MenuItem> children = new ArrayList<MenuItem>();
		if (childrenArray != null) {
			for (int i = 0; i < childrenArray.length(); i++) {
				JSONObject moduleBeanObject = childrenArray.getJSONObject(i);
				children.add(deserialize(moduleBeanObject));
			}
		}
		if(children.isEmpty()){
			return new MenuItem(path, title, null, url);
		}
		return new MenuItem(path, title, children, url);
	}

	public final String getPath() {
		return path;
	}

	public final String getTitle() {
		return title;
	}

	public final List<MenuItem> getChildren() {
		return this.children;
	}

	public String getAccessUrl() {
		return accessUrl;
	}

	@Override
	public String toJSONString() {
		try {
			return this.serialize().toString();
		} catch (JSONException e) {
			return null;
		}
	}

	@Override
	public String toString() {
		return "ModuleBean{" +
				"accessUrl='" + accessUrl + '\'' +
				", path='" + path + '\'' +
				", title='" + title + '\'' +
				'}';
	}

	public static MenuItem fromModule(Module m, String baseUrl) {
		final String key = m.getKey();
		final String title = m.getTitle();

		final List<SubModule> subs = m.getSubModules();
		final List<MenuItem> subsAsBeans = new ArrayList<MenuItem>();

		for (SubModule sub : subs) {
			subsAsBeans.add(subModuleToModuleBean(key, sub, baseUrl));
		}
		return new MenuItem(key, title, subsAsBeans, baseUrl + key);
	}

	private static MenuItem subModuleToModuleBean(String parentKey, SubModule sm, String baseUrl) {
		final List<SubModule> subModules = sm.getSubModules();
		final List<MenuItem> subsAsBeans = new ArrayList<MenuItem>();
		final String key = parentKey + ";" + sm.getKey();

		for (SubModule sub : subModules) {
			subsAsBeans.add(subModuleToModuleBean(key, sub, baseUrl));
		}
		return new MenuItem(key, sm.getTitle(), subsAsBeans, baseUrl + key);
	}

}
