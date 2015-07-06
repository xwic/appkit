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
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.entity.mime.MultipartEntity;
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
		InputStream is = null;
		BufferedReader rd = null;

		byte[] bytes = null;

		try {
			is = getStream(param, config);
			bytes = IoUtil.readBytes(is);
			IoUtil.close(is);
			is = null;

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
			IoUtil.close(is);
			IoUtil.close(rd);
		}
	}

	/**
	 * @param param
	 * @param config
	 * @return
	 */
	public static InputStream getStream(final Map<String, String> param, final RemoteSystemConfiguration config) {
		try {
			SimpleRequestHelper requestHelper = new SimpleRequestHelper(param, config);
			return postRequest(requestHelper);
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
	public static int multipartRequestInt(final MultipartEntity builder, final RemoteSystemConfiguration config) {
		InputStream in = null;
		try {
			MultipartRequestHelper requestHelper = new MultipartRequestHelper(builder, config);
			in = postRequest(requestHelper);
			byte[] byteArray = IOUtils.toByteArray(in);
			return Integer.valueOf(new String(byteArray));
		} catch (RemoteDataAccessException rdae) {
			throw rdae;
		} catch (Exception e) {
			throw new RemoteDataAccessException(e);
		} finally {
			UStream.close(in);
		}
	}

	/**
	 * @param param
	 * @param config
	 * @return
	 */
	public static InputStream postRequest(final IRequestHelper helper) {
		try {
			URL url = new URL(helper.getTargetUrl());
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST"); // always POST, we don't need to support GET
			connection.setRequestProperty("Content-Type", helper.getContentType());
			connection.setRequestProperty("Content-Length", String.valueOf(helper.getContentLen()));
			connection.setRequestProperty("Content-Language", "en-US");

			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Send request
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			helper.writeToStream(wr);
			wr.flush();
			wr.close();

			int responseCode = connection.getResponseCode();
			if (responseCode != HttpURLConnection.HTTP_OK) {
				throw new RemoteDataAccessException(connection.getResponseMessage());
			}
			return connection.getInputStream();
		} catch (RemoteDataAccessException re) {
			throw re;
		} catch (Exception e) {
			throw new RemoteDataAccessException(e);
		}
	}

}
