/**
 *
 */
package de.xwic.appkit.core.remote.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.poi.util.IOUtils;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;

import de.xwic.appkit.core.util.UStream;

/**
 * @author Alexandru Bledea
 * @since Jan 14, 2014
 */
public class URemoteAccessClient {

	/**
	 * @param param
	 * @param config
	 * @return
	 */
	public static Document postRequest(final Map<String, String> param, final RemoteSystemConfiguration config) {
		try {
			InputStream is = getStream(param, config);
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));

			SAXReader xmlReader = new SAXReader();
			Document doc = xmlReader.read(rd);

			rd.close();
			is.close();

			return doc;
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
