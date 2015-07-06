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

import de.xwic.appkit.core.util.StreamUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

/**
 * Fetches additional menu items from a given url and return a single menu item entry with all items as a a child of the single item root
 * Given URL should point to a ModuleProviderServer, or something that conforms to the same API as the ModuleProviderServlet
 *
 * Created by boogie on 3/10/15.
 */
public final class SinglePointUrlMenuItemsProvider implements IMenuItemsProvider {

	private final String baseUrl;
	private final String user;

	public SinglePointUrlMenuItemsProvider(String baseUrl, String user) {
		this.baseUrl = baseUrl;
		this.user = user;
	}

	@Override
	public List<MenuItem> fetchModuleBeans() {
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
			return Collections.singletonList(MenuItem.deserialize(rootModule));
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
