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
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;

import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.dao.Limit;
import de.xwic.appkit.core.remote.server.RemoteDataAccessServlet;
import de.xwic.appkit.core.transfer.EntityTransferObject;
import de.xwic.appkit.core.transport.xml.TransportException;
import de.xwic.appkit.core.transport.xml.XmlEntityTransport;

/**
 * Client implementation to access entities on an XWic server. The
 * access works via the HTTP protocol.
 * 
 * @author lippisch
 */
public class RemoteDataAccessClient {

	private String remoteBaseUrl;
	private String remoteSystemId;

	
	
	/**
	 * @param remoteBaseUrl
	 * @param remoteSystemId 
	 */
	public RemoteDataAccessClient(String remoteBaseUrl, String remoteSystemId) {
		super();
		this.remoteBaseUrl = remoteBaseUrl;
		this.remoteSystemId = remoteSystemId;
		
		if (!remoteBaseUrl.endsWith("/")) {
			remoteBaseUrl = remoteBaseUrl + "/";
		}
		
		
	}
	
	/**
	 * Fetch an ETO from a server.
	 * @param entityType
	 * @param id
	 * @return
	 * @throws RemoteDataAccessException
	 * @throws TransportException 
	 */
	@SuppressWarnings("rawtypes")
	public EntityTransferObject getETO(String entityType, int id) throws RemoteDataAccessException, TransportException {
		
		Map<String, String> param = new HashMap<String, String>();
		param.put(RemoteDataAccessServlet.PARAM_ACTION, RemoteDataAccessServlet.ACTION_GET_ENTITY);
		param.put(RemoteDataAccessServlet.PARAM_ENTITY_TYPE, entityType);
		param.put(RemoteDataAccessServlet.PARAM_ENTITY_ID, Integer.toString(id));
		
		Limit limit = new Limit();
		
		Document doc = postRequest(param);
		
		XmlEntityTransport xet = new XmlEntityTransport();
		
		EntityList list = xet.createETOList(doc, limit);

		if (!list.isEmpty()) {
			return (EntityTransferObject) list.get(0);
		}
		return null;
	}
	
	
	/**
	 * Post a request and return a document.
	 * @param param
	 * @return
	 */
	private Document postRequest(Map<String, String> param) {
		
		try {
			String targetUrl = remoteBaseUrl + "rda.api";
			param.put("rsid", remoteSystemId);
	
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
			connection.setRequestMethod("POST");
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

//			String line;
//			StringBuffer response = new StringBuffer();
//			while ((line = rd.readLine()) != null) {
//				response.append(line);
//				response.append('\r');
//			}
//			
//			System.out.println(response);

			SAXReader xmlReader = new SAXReader();
			Document doc = xmlReader.read(rd);
			
			rd.close();
			is.close();
			
			return doc;
		} catch (Exception e) {
			throw new RemoteDataAccessException(e);
		}
		
	}
	
}
