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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.poi.util.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import de.xwic.appkit.core.util.UStream;

/**
 * @author Alexandru Bledea
 * @since Jan 14, 2014
 */
public final class URemoteAccessClient {

	private final static Log log = LogFactory.getLog(URemoteAccessClient.class);

	/**
	 *
	 */
	private URemoteAccessClient() {
	}

	/**
	 * @param param
	 * @param config
	 * @return
	 */
	public static Document postRequest(final Map<String, String> param, final RemoteSystemConfiguration config) {
		BufferedReader rd = null;

		byte[] bytes = null;

		try {
			bytes = getReponseByteArray(param, config);
			rd = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytes)));
			return new SAXReader().read(rd);
		} catch (RemoteDataAccessException rdae) {
			throw rdae;
		} catch (final DocumentException de) {
			if (null != bytes) {
				log.error(String.format("Failed to transfer \n---\n%s\n---\n", new String(bytes)), de);
			}
			throw new RemoteDataAccessException("Transfer error", de);
		} catch (Exception e) {
			throw new RemoteDataAccessException("Transfer error", e);
		} finally {
			IoUtil.close(rd);
		}
	}

	/**
	 * @param param
	 * @param config
	 * @return
	 */
	public static byte[] getReponseByteArray(final Map<String, String> param,
			final RemoteSystemConfiguration config) {
		try {
			SimpleRequestHelper requestHelper = new SimpleRequestHelper(param, config);
			return postRequest(requestHelper, config);
		} catch (RemoteDataAccessException rdae) {
			throw rdae;
		} catch (Exception e) {
			throw new RemoteDataAccessException(e);
		}
	}

	/**
	 * @param builder
	 * @param config
	 * @return
	 */
	public static int multipartRequestInt(final MultipartEntity builder,
			final RemoteSystemConfiguration config) {
		try {
			MultipartRequestHelper requestHelper = new MultipartRequestHelper(builder, config);
			byte[] byteArray = postRequest(requestHelper, config);
			return Integer.valueOf(new String(byteArray));
		} catch (RemoteDataAccessException rdae) {
			throw rdae;
		} catch (Exception e) {
			throw new RemoteDataAccessException(e);
		}
	}

	/**
	 * @param param
	 * @param config
	 * @return
	 */
	public static byte[] postRequest(final IRequestHelper helper, final RemoteSystemConfiguration config) {
		CloseableHttpResponse response = null;
		try {

			CloseableHttpClient client = PoolingHttpConnectionManager.getInstance().getClientInstance(config);
			HttpPost post = new HttpPost(helper.getTargetUrl());

			post.setEntity(helper.getHttpEntity());

			response = client.execute(post);

			int responseCode = response.getStatusLine().getStatusCode();
			if (responseCode != HttpURLConnection.HTTP_OK) {
				throw new RemoteDataAccessException(response.getStatusLine().getReasonPhrase());
			}
			return EntityUtils.toByteArray(response.getEntity());
		} catch (RemoteDataAccessException re) {
			throw re;
		} catch (Exception e) {
			throw new RemoteDataAccessException(e);
		}finally {
			if(response != null){
				try {
					response.close();
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				}
			}
		}
	}

}
