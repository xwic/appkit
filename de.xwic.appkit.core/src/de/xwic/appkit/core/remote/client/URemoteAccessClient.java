/**
 *
 */
package de.xwic.appkit.core.remote.client;

import static de.xwic.appkit.core.remote.server.RemoteDataAccessServlet.PARAM_RSID;
import static org.apache.http.entity.ContentType.TEXT_PLAIN;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.poi.util.IOUtils;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;

import de.xwic.appkit.core.remote.server.RemoteDataAccessServlet;

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
			String targetUrl = config.getRemoteBaseUrl() + config.getApiSuffix();
			param.put(RemoteDataAccessServlet.PARAM_RSID, config.getRemoteSystemId());

			StringBuilder sbParam = new StringBuilder();
			for (Entry<String, String> entry : param.entrySet()) {
				if (sbParam.length() != 0) {
					sbParam.append("&");
				}
				sbParam.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), "UTF-8"));
			}
			String urlParameters = sbParam.toString();

			URL url = new URL(targetUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST"); // always POST, we don't need to support GET
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");

			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Send request
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			// Get Response
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));

			SAXReader xmlReader = new SAXReader();
			Document doc = xmlReader.read(rd);

			rd.close();
			is.close();

			return doc;
		} catch (Exception e) {
			throw new RemoteDataAccessException(e);
		}
	}

	/**
	 * @param param
	 * @param config
	 * @return
	 */
	public static int postRequest(final MultipartEntityBuilder builder, final long len, final RemoteSystemConfiguration config) {
		try {
			String targetUrl = config.getRemoteBaseUrl() + config.getApiSuffix();
			builder.addPart(PARAM_RSID, new StringBody(config.getRemoteSystemId(), TEXT_PLAIN));
			HttpEntity build = builder.build();
			URL url = new URL(targetUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", build.getContentType().getValue());
			connection.setRequestProperty("Content-Length", String.valueOf(build.getContentLength()));
			connection.setRequestProperty("Content-Language", "en-US");

			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			// Send request
//			OutputStream out = connection.getOutputStream();
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());

			build.writeTo(wr);
			wr.flush();
			wr.close();

			int responseCode = connection.getResponseCode();
			String responseMessage = connection.getResponseMessage();
			System.out.println(responseCode + " . " + responseMessage);

			InputStream inputStream = connection.getInputStream();
			byte[] byteArray = IOUtils.toByteArray(inputStream);
			System.out.println(new String(byteArray));
//			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
//
//			wr.writeBytes(urlParameters);
//			wr.flush();
//			wr.close();
			return 0;
		} catch (Exception e) {
			throw new RemoteDataAccessException(e);
		}
	}

}
