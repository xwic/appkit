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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.dao.Limit;
import de.xwic.appkit.core.remote.server.RemoteDataAccessServlet;
import de.xwic.appkit.core.remote.util.UETO;
import de.xwic.appkit.core.transfer.EntityTransferObject;
import de.xwic.appkit.core.transport.xml.EntityQuerySerializer;
import de.xwic.appkit.core.transport.xml.EtoEntityNodeParser;
import de.xwic.appkit.core.transport.xml.ObjectArrayEntityNodeParser;
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
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.remote.client.IRemoteDataAccessClient#getETO(java.lang.String, int)
	 */
	@SuppressWarnings("rawtypes")
	public EntityTransferObject getETO(String entityType, int id) throws RemoteDataAccessException, TransportException {
		
		Map<String, String> param = new HashMap<String, String>();
		param.put(RemoteDataAccessServlet.PARAM_ACTION, RemoteDataAccessServlet.ACTION_GET_ENTITY);
		param.put(RemoteDataAccessServlet.PARAM_ENTITY_TYPE, entityType);
		param.put(RemoteDataAccessServlet.PARAM_ENTITY_ID, Integer.toString(id));
		
		Document doc = postRequest(param);
		
		XmlEntityTransport xet = new XmlEntityTransport();
		
		EntityList list = xet.createList(doc, new Limit(), new EtoEntityNodeParser());

		if (!list.isEmpty()) {
			return (EntityTransferObject) list.get(0);
		}
		
		return null;
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.remote.client.IRemoteDataAccessClient#getETOs(java.lang.String, de.xwic.appkit.core.dao.Limit, de.xwic.appkit.core.dao.EntityQuery)
	 */
	@Override
	public EntityList getList(String entityType, Limit limit, EntityQuery query) throws RemoteDataAccessException, TransportException {
		Map<String, String> param = new HashMap<String, String>();
		param.put(RemoteDataAccessServlet.PARAM_ACTION, RemoteDataAccessServlet.ACTION_GET_ENTITIES);
		param.put(RemoteDataAccessServlet.PARAM_ENTITY_TYPE, entityType);
		param.put(RemoteDataAccessServlet.PARAM_LIMIT, EntityQuerySerializer.limitToString(limit));
		param.put(RemoteDataAccessServlet.PARAM_QUERY, EntityQuerySerializer.queryToString(query));
		
		Document doc = postRequest(param);
		
		EntityList list = null;
		XmlEntityTransport xet = new XmlEntityTransport();		
		if (query.getColumns() == null) {
			list = xet.createList(doc, limit, new EtoEntityNodeParser());
		} else {
			list = xet.createList(doc, limit, new ObjectArrayEntityNodeParser());
		}

		return list;
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.remote.client.IRemoteDataAccessClient#getETOCollection(java.lang.String, int, java.lang.String)
	 */
	@Override
	public List<?> getETOCollection(String entityType, int entityId, String propertyName) throws RemoteDataAccessException, TransportException, IOException, ConfigurationException {
		Map<String, String> param = new HashMap<String, String>();
		param.put(RemoteDataAccessServlet.PARAM_ACTION, RemoteDataAccessServlet.ACTION_GET_COLLECTION);
		param.put(RemoteDataAccessServlet.PARAM_ENTITY_TYPE, entityType);
		param.put(RemoteDataAccessServlet.PARAM_ENTITY_ID, Integer.toString(entityId));
		param.put(RemoteDataAccessServlet.PARAM_ENTITY_PROPERTY, propertyName);
		
		Document doc = postRequest(param);
		
		XmlEntityTransport xet = new XmlEntityTransport();		
		EntityList list = xet.createList(doc, new Limit(), new EtoEntityNodeParser());

		return list;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.remote.client.IRemoteDataAccessClient#updateETO(java.lang.String, de.xwic.appkit.core.transfer.EntityTransferObject)
	 */
	@Override
	public EntityTransferObject updateETO(String entityType, EntityTransferObject eto) throws RemoteDataAccessException, TransportException, IOException, ConfigurationException {
		
		Map<String, String> param = new HashMap<String, String>();
		param.put(RemoteDataAccessServlet.PARAM_ACTION, RemoteDataAccessServlet.ACTION_UPDATE_ENTITY);
		param.put(RemoteDataAccessServlet.PARAM_ENTITY_TYPE, entityType);
		
		String serialized = UETO.serialize(entityType, eto);
		
		param.put(RemoteDataAccessServlet.PARAM_ETO, serialized);
		
		// after an update, the updated entity is returned from the server
		Document doc = postRequest(param);
		
		String strEto = doc.asXML();

		try {
			return UETO.deserialize(strEto);
		} catch (DocumentException e) {
			throw new RemoteDataAccessException(e);
		}
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
