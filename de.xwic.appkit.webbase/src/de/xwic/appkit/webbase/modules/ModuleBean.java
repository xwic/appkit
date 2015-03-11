package de.xwic.appkit.webbase.modules;

import de.xwic.appkit.webbase.toolkit.app.Module;
import de.xwic.appkit.webbase.toolkit.app.SubModule;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;

import java.lang.String;import java.util.ArrayList;
import java.util.List;

/**
 * Created by boogie on 3/6/15.
 */
public final class ModuleBean implements JSONString{
	private final String path;
	private final String title;
	private final List<ModuleBean> children;
	private final String accessUrl;

	ModuleBean(String path, String title, List<ModuleBean> children, String accessUrl) {
		this.path = path;
		this.title = title;
		this.children = children;
		this.accessUrl = accessUrl;
	}

	public JSONObject serialize() throws JSONException {
		final JSONObject jsonObject = new JSONObject();
		JSONArray children = new JSONArray();
		if(this.children != null) {
			for (ModuleBean mb : this.children) {
				children.put(mb.serialize());
			}
		}

		jsonObject.put("children", children);
		jsonObject.put("title", this.title);
		jsonObject.put("path", this.path);
		jsonObject.put("url", this.accessUrl);

		return jsonObject;
	}

	public static ModuleBean deserialize(JSONObject serialized) throws JSONException {
		final String title = serialized.getString("title");
		final String path = serialized.getString("path");
		final String url = serialized.getString("url");
		final JSONArray childrenArray = serialized.getJSONArray("children");

		final List<ModuleBean> children = new ArrayList<ModuleBean>();
		if(childrenArray != null) {
			for (int i = 0; i < childrenArray.length(); i++) {
				JSONObject moduleBeanObject = childrenArray.getJSONObject(i);
				children.add(deserialize(moduleBeanObject));
			}
		}

		return new ModuleBean(path, title, children, url);
	}

	public String getPath() {
		return path;
	}

	public String getTitle() {
		return title;
	}

	public List<ModuleBean> getChildren() {
		return children;
	}

	public String getAccessUrl() {
		return accessUrl;
	}
	@Override
	public String toJSONString(){
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

	static ModuleBean fromModule(Module m, String baseUrl){
		final String key = m.getKey();
		final String title = m.getTitle();

		final List<SubModule> subs = m.getSubModules();
		final List<ModuleBean> subsAsBeans = new ArrayList<ModuleBean>();


		for (SubModule sub : subs) {
			subsAsBeans.add(subModuleToModuleBean(key, sub, baseUrl));
		}
		return new ModuleBean(key, title, subsAsBeans, baseUrl + key);
	}

	private static ModuleBean subModuleToModuleBean(String parentKey, SubModule sm, String baseUrl){
		final List<SubModule> subModules = sm.getSubModules();
		final List<ModuleBean> subsAsBeans = new ArrayList<ModuleBean>();
		final String key = parentKey + ";" + sm.getKey();

		for (SubModule sub : subModules) {
			subsAsBeans.add(subModuleToModuleBean(key, sub, baseUrl));
		}
		return new ModuleBean(key, sm.getTitle(), subsAsBeans, baseUrl + key);
	}

}
