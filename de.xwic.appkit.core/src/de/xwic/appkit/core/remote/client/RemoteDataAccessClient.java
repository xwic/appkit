/**
 * 
 */
package de.xwic.appkit.core.remote.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;

import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.dao.Limit;
import de.xwic.appkit.core.remote.server.RemoteDataAccessServlet;
import de.xwic.appkit.core.transfer.EntityTransferObject;
import de.xwic.appkit.core.transport.xml.EntityQuerySerializer;
import de.xwic.appkit.core.transport.xml.TransportException;
import de.xwic.appkit.core.transport.xml.XmlEntityTransport;

/**
 * Client implementation to access entities on an XWic server. The
 * access works via the HTTP protocol.
 * 
 * @author lippisch
 */
public class RemoteDataAccessClient implements IRemoteDataAccessClient {

	private RemoteSystemConfiguration config;
	
	/**
	 * @param remoteBaseUrl
	 * @param remoteSystemId 
	 */
	public RemoteDataAccessClient(RemoteSystemConfiguration config) {
		this.config = config;
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
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.remote.client.IRemoteDataAccessClient#getETOs(java.lang.String, de.xwic.appkit.core.dao.Limit, de.xwic.appkit.core.dao.EntityQuery)
	 */
	@Override
	public EntityList<EntityTransferObject> getETOs(String entityType, Limit limit, EntityQuery query) throws RemoteDataAccessException, TransportException {
		Map<String, String> param = new HashMap<String, String>();
		param.put(RemoteDataAccessServlet.PARAM_ACTION, RemoteDataAccessServlet.ACTION_GET_ENTITIES);
		param.put(RemoteDataAccessServlet.PARAM_ENTITY_TYPE, entityType);
		param.put(RemoteDataAccessServlet.PARAM_LIMIT, EntityQuerySerializer.limitToString(limit));
		param.put(RemoteDataAccessServlet.PARAM_QUERY, EntityQuerySerializer.queryToString(query));
		
		Document doc = postRequest(param);
		
		XmlEntityTransport xet = new XmlEntityTransport();
		
		EntityList list = xet.createETOList(doc, limit);

		return list;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.remote.client.IRemoteDataAccessClient#updateETO(java.lang.String, de.xwic.appkit.core.transfer.EntityTransferObject)
	 */
	@Override
	public void updateETO(String entityType, EntityTransferObject eto) throws RemoteDataAccessException, TransportException, IOException, ConfigurationException {
		
		Map<String, String> param = new HashMap<String, String>();
		param.put(RemoteDataAccessServlet.PARAM_ACTION, RemoteDataAccessServlet.ACTION_UPDATE_ENTITY);
		param.put(RemoteDataAccessServlet.PARAM_ENTITY_TYPE, eto.getEntityClass().getName());
		
		EntityDescriptor descr = DAOSystem.getEntityDescriptor(entityType);
		StringWriter sw = new StringWriter();
		XmlEntityTransport xet = new XmlEntityTransport();
		xet.write(sw, eto, descr);
		
		param.put(RemoteDataAccessServlet.PARAM_ETO, sw.toString());
		
		postRequest(param);
	}	
	
	/**
	 * Post a request and return a document.
	 * @param param
	 * @return
	 */
	private Document postRequest(Map<String, String> param) {
		
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
}
