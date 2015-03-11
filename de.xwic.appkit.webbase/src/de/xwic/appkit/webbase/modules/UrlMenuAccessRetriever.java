package de.xwic.appkit.webbase.modules;

import de.xwic.appkit.core.util.StreamUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by boogie on 3/10/15.
 */
public final class UrlMenuAccessRetriever implements IMenuItemsProvider {

	private final String baseUrl;
	private final String user;
	private final String itemTitle;
	public UrlMenuAccessRetriever(String itemTitle, String baseUrl, String user) {
		this.baseUrl = baseUrl;
		this.user = user;
		this.itemTitle = itemTitle;
	}

	@Override
	public List<ModuleBean> fetchModuleBeans() {
		InputStream inputStream = null;
		BufferedInputStream inputStreamReader = null;
		BufferedReader bufferedReader = null;
		try {
			inputStream = new URL(baseUrl + "/" + user).openStream();
			inputStreamReader = new BufferedInputStream(inputStream);
			bufferedReader = new BufferedReader(new InputStreamReader(inputStreamReader));

			final StringBuilder sb = new StringBuilder();
			String inputLine;
			while ((inputLine = bufferedReader.readLine()) != null) {
				sb.append(inputLine);
			}
			final JSONObject rootModule = new JSONObject(sb.toString());
			return Collections.singletonList(ModuleBean.deserialize(rootModule));
		} catch (MalformedURLException e) {
			return null;
		} catch (IOException e) {
			return null;
		} catch (JSONException e) {
			return null;
		}finally {
			StreamUtil.close(inputStream, inputStreamReader, bufferedReader);
		}
	}

}
