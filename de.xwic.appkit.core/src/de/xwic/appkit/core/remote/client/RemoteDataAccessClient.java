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

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.dao.EntityKey;
import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.dao.Limit;
import de.xwic.appkit.core.dao.UseCase;
import de.xwic.appkit.core.remote.server.RemoteDataAccessServlet;
import de.xwic.appkit.core.remote.server.RemoteFunctionCallHandler;
import de.xwic.appkit.core.remote.server.UseCaseHandler;
import de.xwic.appkit.core.remote.server.UserRightsHandler;
import de.xwic.appkit.core.security.ScopeActionKey;
import de.xwic.appkit.core.transfer.EntityTransferObject;
import de.xwic.appkit.core.transport.xml.EntityQuerySerializer;
import de.xwic.appkit.core.transport.xml.EtoEntityNodeParser;
import de.xwic.appkit.core.transport.xml.EtoSerializer;
import de.xwic.appkit.core.transport.xml.ObjectArrayEntityNodeParser;
import de.xwic.appkit.core.transport.xml.RemoteFunctionCallSerializer;
import de.xwic.appkit.core.transport.xml.TransportException;
import de.xwic.appkit.core.transport.xml.UseCaseSerializer;
import de.xwic.appkit.core.transport.xml.XmlBeanSerializer;
import de.xwic.appkit.core.transport.xml.XmlEntityTransport;

/**
 * Client implementation to access entities on an XWic server. The
 * access works via the HTTP protocol.
 *
 * @author lippisch
 */
public class RemoteDataAccessClient implements IRemoteDataAccessClient {

	private final RemoteSystemConfiguration config;

	/**
	 * @param remoteBaseUrl
	 * @param remoteSystemId
	 */
	public RemoteDataAccessClient(final RemoteSystemConfiguration config) {
		this.config = config;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.remote.client.IRemoteDataAccessClient#getETO(java.lang.String, int)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public EntityTransferObject getETO(final String entityType, final int id) throws RemoteDataAccessException, TransportException {

		Map<String, String> param = new HashMap<String, String>();
		param.put(RemoteDataAccessServlet.PARAM_ACTION, RemoteDataAccessServlet.ACTION_GET_ENTITY);
		param.put(RemoteDataAccessServlet.PARAM_ENTITY_TYPE, entityType);
		param.put(RemoteDataAccessServlet.PARAM_ENTITY_ID, Integer.toString(id));

		Document doc = postRequest(param);

		XmlEntityTransport xet = new XmlEntityTransport(ETOSessionCache.getInstance().getSessionCache());

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
	public EntityList getList(final String entityType, final Limit limit, final EntityQuery query) throws RemoteDataAccessException, TransportException {
		Map<String, String> param = new HashMap<String, String>();
		param.put(RemoteDataAccessServlet.PARAM_ACTION, RemoteDataAccessServlet.ACTION_GET_ENTITIES);
		param.put(RemoteDataAccessServlet.PARAM_ENTITY_TYPE, entityType);
		param.put(RemoteDataAccessServlet.PARAM_LIMIT, EntityQuerySerializer.limitToString(limit));
		param.put(RemoteDataAccessServlet.PARAM_QUERY, EntityQuerySerializer.queryToString(query));

		Document doc = postRequest(param);

		EntityList list = null;
		XmlEntityTransport xet = new XmlEntityTransport(ETOSessionCache.getInstance().getSessionCache());
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
	public List<?> getETOCollection(final String entityType, final int entityId, final String propertyName) throws RemoteDataAccessException, TransportException, IOException, ConfigurationException {
		Map<String, String> param = new HashMap<String, String>();
		param.put(RemoteDataAccessServlet.PARAM_ACTION, RemoteDataAccessServlet.ACTION_GET_COLLECTION);
		param.put(RemoteDataAccessServlet.PARAM_ENTITY_TYPE, entityType);
		param.put(RemoteDataAccessServlet.PARAM_ENTITY_ID, Integer.toString(entityId));
		param.put(RemoteDataAccessServlet.PARAM_ENTITY_PROPERTY, propertyName);

		Document doc = postRequest(param);

		XmlEntityTransport xet = new XmlEntityTransport(ETOSessionCache.getInstance().getSessionCache());
		EntityList list = xet.createList(doc, new Limit(), new EtoEntityNodeParser());

		return list;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.remote.client.IRemoteDataAccessClient#updateETO(java.lang.String, de.xwic.appkit.core.transfer.EntityTransferObject)
	 */
	@Override
	public EntityTransferObject updateETO(final String entityType, final EntityTransferObject eto) throws RemoteDataAccessException, TransportException, IOException, ConfigurationException {

		Map<String, String> param = new HashMap<String, String>();
		param.put(RemoteDataAccessServlet.PARAM_ACTION, RemoteDataAccessServlet.ACTION_UPDATE_ENTITY);
		param.put(RemoteDataAccessServlet.PARAM_ENTITY_TYPE, entityType);

		String serialized = EtoSerializer.serialize(entityType, eto);

		param.put(RemoteDataAccessServlet.PARAM_ETO, serialized);

		// after an update, the updated entity is returned from the server
		Document doc = postRequest(param);

		String strEto = doc.asXML();

		try {
			return EtoSerializer.deserialize(strEto);
		} catch (DocumentException e) {
			throw new RemoteDataAccessException(e);
		}
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.remote.client.IRemoteDataAccessClient#deleteETO(java.lang.String, de.xwic.appkit.core.transfer.EntityTransferObject, boolean)
	 */
	@Override
	public void delete(final String entityType, final int id, final long version, final boolean softDelete) {
		Map<String, String> param = new HashMap<String, String>();

		String action = softDelete ? RemoteDataAccessServlet.ACTION_SOFT_DELETE : RemoteDataAccessServlet.ACTION_DELETE;
		param.put(RemoteDataAccessServlet.PARAM_ACTION, action);

		param.put(RemoteDataAccessServlet.PARAM_ENTITY_TYPE, entityType);
		param.put(RemoteDataAccessServlet.PARAM_ENTITY_ID, String.valueOf(id));
		param.put(RemoteDataAccessServlet.PARAM_VERSION, String.valueOf(version));

		postRequest(param);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.remote.client.IRemoteDataAccessClient#executeUseCase(de.xwic.appkit.core.dao.UseCase)
	 */
	@Override
	public Object executeUseCase(UseCase uc) throws TransportException {
		Map<String, String> param = new HashMap<String, String>();

		param.put(RemoteDataAccessServlet.PARAM_ACTION, RemoteDataAccessServlet.ACTION_EXECUTE_USE_CASE);
		param.put(UseCaseHandler.PARAM_USE_CASE, UseCaseSerializer.serialize(uc));

		Document doc = postRequest(param);
		Element root = doc.getRootElement();
		
		XmlBeanSerializer xml = new XmlBeanSerializer(ETOSessionCache.getInstance().getSessionCache());
		
		Object x = xml.readValue(null, root, null);
		
		return x;
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.remote.client.IRemoteDataAccessClient#getUserRights(int)
	 */
	@Override
	public Set<ScopeActionKey> getUserRights(int userId) throws TransportException {
		Map<String, String> param = new HashMap<String, String>();

		param.put(RemoteDataAccessServlet.PARAM_ACTION, RemoteDataAccessServlet.ACTION_GET_USER_RIGHTS);
		param.put(UserRightsHandler.PARAM_USER_ID, String.valueOf(userId));

		Document doc = postRequest(param);
		Element root = doc.getRootElement();
		
		XmlBeanSerializer xml = new XmlBeanSerializer(ETOSessionCache.getInstance().getSessionCache());
		Set<ScopeActionKey> result = (Set<ScopeActionKey>) xml.deserializeBean(root);
		
		return result;
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.remote.client.IRemoteDataAccessClient#executeRemoteFunctionCall(de.xwic.appkit.core.remote.client.IRemoteFunctionCallConditions)
	 */
	@Override
	public Object executeRemoteFunctionCall(IRemoteFunctionCallConditions conditions)
			throws TransportException {
		Map<String, String> param = new HashMap<String, String>();

		param.put(RemoteDataAccessServlet.PARAM_ACTION, RemoteDataAccessServlet.ACTION_FUNCTION_CALL_HANDLE);
		param.put(RemoteFunctionCallHandler.PARAM_FUNCTION_CALL, RemoteFunctionCallSerializer.serialize(conditions));

		Document doc = postRequest(param);
		Element root = doc.getRootElement();
		
		XmlBeanSerializer xml = new XmlBeanSerializer(ETOSessionCache.getInstance().getSessionCache());
		
		Object x = xml.readValue(null, root, null);
		
		return x;
	}
	
	/**
	 * Post a request and return a document.
	 * @param param
	 * @return
	 */
	private Document postRequest(final Map<String, String> param) {
		return URemoteAccessClient.postRequest(param, config);
	}

}
