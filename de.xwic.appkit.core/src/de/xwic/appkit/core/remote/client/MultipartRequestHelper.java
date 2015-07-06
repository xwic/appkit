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

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;

import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.remote.server.RemoteDataAccessServlet;
import de.xwic.appkit.core.security.IUser;

/**
 * @author Alexandru Bledea
 * @since Jan 15, 2014
 */
public class MultipartRequestHelper implements IRequestHelper {

	private final String contentType;
	private final long contentLength;
	private final MultipartEntity entity;
	private final String targetUrl;

	/**
	 * @param entity
	 * @param config
	 * @throws UnsupportedEncodingException
	 */
	public MultipartRequestHelper(final MultipartEntity entity, final RemoteSystemConfiguration config) throws UnsupportedEncodingException {
		targetUrl = config.getRemoteBaseUrl() + config.getApiSuffix();
		entity.addPart(RemoteDataAccessServlet.PARAM_RSID, new StringBody(config.getRemoteSystemId()));
		
		IUser currentUser = DAOSystem.getSecurityManager().getCurrentUser();
		if (currentUser != null) {
			entity.addPart(RemoteDataAccessServlet.PARAM_USERNAME, new StringBody(currentUser.getLogonName()));
		}
		
		this.entity = entity;
		contentType = entity.getContentType().getValue();
		contentLength = entity.getContentLength();
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.remote.client.IRequestHelper#getContentType()
	 */
	@Override
	public String getContentType() {
		return contentType;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.remote.client.IRequestHelper#getContentLen()
	 */
	@Override
	public long getContentLen() {
		return contentLength;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.remote.client.IRequestHelper#writeToStream(java.io.DataOutputStream)
	 */
	@Override
	public void writeToStream(final DataOutputStream wr) throws IOException {
		entity.writeTo(wr);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.remote.client.IRequestHelper#getTargetUrl()
	 */
	@Override
	public String getTargetUrl() {
		return targetUrl;
	}

}
