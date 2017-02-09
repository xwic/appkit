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
/**
 *
 */
package de.xwic.appkit.core.remote.client;

import static de.xwic.appkit.core.remote.server.RemoteDataAccessServlet.PARAM_RSID;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.remote.server.RemoteDataAccessServlet;
import de.xwic.appkit.core.security.IUser;

/**
 * @author Alexandru Bledea
 * @since Jan 15, 2014
 */
class SimpleRequestHelper implements IRequestHelper {
	
	private final String targetUrl;
	private final UrlEncodedFormEntity entity;

	/**
	 * @param param
	 * @param config
	 * @throws RemoteDataAccessException
	 */
	public SimpleRequestHelper(final Map<String, String> param, final RemoteSystemConfiguration config) throws RemoteDataAccessException {
		try {
			targetUrl = config.getRemoteBaseUrl() + config.getApiSuffix();
			param.put(PARAM_RSID, config.getRemoteSystemId());
			
			IUser currentUser = DAOSystem.getSecurityManager().getCurrentUser();
			if (currentUser != null) {
				param.put(RemoteDataAccessServlet.PARAM_USERNAME, currentUser.getLogonName());
			}

			List<NameValuePair> urlParametersList = new ArrayList<NameValuePair>();
			for (Entry<String, String> entry : param.entrySet()) {
				urlParametersList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			entity = new UrlEncodedFormEntity(urlParametersList, "UTF-8");
			entity.setContentType("application/x-www-form-urlencoded; charset=UTF-8");

		} catch (Exception e) {
			throw new RemoteDataAccessException(e);
		}
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.remote.client.IRequestHelper#getTargetUrl()
	 */
	@Override
	public String getTargetUrl() {
		return targetUrl;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.remote.client.IRequestHelper#getHttpEntity()
	 */
	@Override
	public HttpEntity getHttpEntity() {
		return entity;
	}


}
