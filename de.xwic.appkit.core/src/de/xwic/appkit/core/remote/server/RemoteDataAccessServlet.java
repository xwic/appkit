/**
 * 
 */
package de.xwic.appkit.core.remote.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;

import de.xwic.appkit.core.access.AccessHandler;
import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.dao.Limit;
import de.xwic.appkit.core.remote.util.UETO;
import de.xwic.appkit.core.transfer.EntityTransferObject;
import de.xwic.appkit.core.transport.xml.EntityQuerySerializer;
import de.xwic.appkit.core.transport.xml.TransportException;
import de.xwic.appkit.core.transport.xml.XmlEntityTransport;

/**
 * This servlet allows remote applications to access entities on the server. It is the counterpart
 * to the remote.client package.
 * 
 * @author lippisch
 */
public class RemoteDataAccessServlet extends HttpServlet {

	public final static String PARAM_RSID = "rsid";
	public final static String PARAM_ACTION = "a";
	public final static String PARAM_ENTITY_TYPE = "et";
	public final static String PARAM_ENTITY_ID = "eid";
	public final static String PARAM_ENTITY_PROPERTY = "prop";
	public final static String PARAM_VERSION = "ev";
	public final static String PARAM_QUERY = "pq";
	public final static String PARAM_LIMIT = "limit";
	public final static String PARAM_ETO = "eto";
	
	public final static String ACTION_GET_ENTITY = "ge";
	public final static String ACTION_GET_ENTITIES = "gea";
	public final static String ACTION_UPDATE_ENTITY = "ue";
	public final static String ACTION_GET_COLLECTION = "gc";
	public final static String ACTION_SOFT_DELETE = "sdel";
	public final static String ACTION_DELETE = "del";
	
	public final static String ELM_RESPONSE = "resp";
	public final static String PARAM_VALUE = "value";
	public final static String RESPONSE_OK = "1";
	public final static String RESPONSE_FAILED = "0";

	public final static Log log = LogFactory.getLog(RemoteDataAccessServlet.class);
	
	private AccessHandler accessHandler;

	/**
	 * 
	 */
	public RemoteDataAccessServlet() {
		
		accessHandler = AccessHandler.getInstance();
		
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		handleRequest(req, resp);
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		handleRequest(req, resp);
	}

	/**
	 * @param req
	 * @param resp
	 * @throws IOException 
	 */
	private void handleRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		// the API is taking its arguments from the URL and the parameters

		String action = req.getParameter(PARAM_ACTION);
		if (action == null || action.isEmpty()) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Missing Parameters");
			return;
		}
		
		// all responses will now basically be an XML document, so we can do some preparations
		resp.setContentType("text/xml");
		PrintWriter pwOut = resp.getWriter();

		try {
			
			String entityType = req.getParameter(PARAM_ENTITY_TYPE);
			assertValue(entityType, "Entity Type not specified");
			
			if (action.equals(ACTION_GET_ENTITY)) {
				handleGetEntity(entityType, req, resp, pwOut);
				
			} else if (action.equals(ACTION_GET_ENTITIES)) {
				handleGetEntities(entityType, req, resp, pwOut);
				
			} else if (action.equals(ACTION_UPDATE_ENTITY)) {
				handleUpdateEntity(entityType, req, resp, pwOut);
					
			} else if (action.equals(ACTION_GET_COLLECTION)) {
				handleGetCollection(entityType, req, resp, pwOut);
				
			} else if (action.equals(ACTION_DELETE) || action.equals(ACTION_SOFT_DELETE)) {
				handleDelete(entityType, req, resp, pwOut, action.equals(ACTION_SOFT_DELETE));

			} else {
				throw new IllegalArgumentException("Unknown action");
			}
			
			pwOut.flush();
			pwOut.close();
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.toString());
		}
		
		
	}

	/**
	 * @param entityType
	 * @param req
	 * @param resp
	 * @param pwOut
	 * @param equals
	 * @throws ConfigurationException 
	 */
	private void handleDelete(String entityType, HttpServletRequest req, HttpServletResponse resp, PrintWriter pwOut,
			boolean softDelete) throws ConfigurationException {
		
		String entityId = req.getParameter(PARAM_ENTITY_ID);
		String version = req.getParameter(PARAM_VERSION);
		
		assertValue(entityId, "Entity Id not specified");
		assertValue(version, "Version not specified");
		
		if (softDelete) {
			accessHandler.softDelete(entityType, Integer.parseInt(entityId), Long.parseLong(version));
		} else {
			accessHandler.delete(entityType, Integer.parseInt(entityId), Long.parseLong(version));
		}
		
		printResponse(pwOut, RESPONSE_OK);
	}

	/**
	 * @param entityType
	 * @param req
	 * @param resp
	 * @param pwOut
	 * @throws ConfigurationException 
	 * @throws IOException 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void handleGetCollection(String entityType, HttpServletRequest req, HttpServletResponse resp, PrintWriter pwOut) throws ConfigurationException, IOException {
		
		String entityId = req.getParameter(PARAM_ENTITY_ID);
		String propName = req.getParameter(PARAM_ENTITY_PROPERTY);
		
		assertValue(entityId, "Entity Id not specified");
		assertValue(propName, "Entity PropertyName not specified");

		int eId = Integer.parseInt(entityId);
		
		
		Object collection = accessHandler.getETOCollection(entityType, eId, propName);
	
		List list = new ArrayList();
		if (collection instanceof Collection<?>) {
			for (Object o : (Collection<?>)collection) {
				list.add(o);
			}
		}

		EntityDescriptor entityDescriptor = DAOSystem.getEntityDescriptor(entityType);
		XmlEntityTransport et = new XmlEntityTransport();
		et.write(pwOut, list, entityDescriptor);
	}

	/**
	 * @param entityType 
	 * @param req
	 * @param resp
	 * @param pwOut
	 * @throws DocumentException 
	 * @throws TransportException 
	 * @throws ConfigurationException 
	 * @throws IOException 
	 */
	private void handleUpdateEntity(String entityType, HttpServletRequest req, HttpServletResponse resp, PrintWriter pwOut) throws DocumentException, TransportException, ConfigurationException, IOException {
		
		String strEto = req.getParameter(PARAM_ETO);
		
		EntityTransferObject eto = UETO.deserialize(strEto);
		
		EntityTransferObject result = accessHandler.updateETO(eto);
		
		if (result == null) {
			throw new IllegalStateException("Result ETO is null");
		}
		
		String strResult = UETO.serialize(entityType, result);
		
		pwOut.write(strResult);
		pwOut.flush();
	}

	/**
	 * @param entityType 
	 * @param req
	 * @param resp
	 * @param pwOut
	 * @throws ConfigurationException 
	 * @throws IOException 
	 */
	private void handleGetEntities(String entityType, HttpServletRequest req, HttpServletResponse resp, PrintWriter pwOut) throws ConfigurationException, IOException, TransportException {
		Limit limit = null;
		String strLimit = req.getParameter(PARAM_LIMIT);
		if (strLimit != null && !strLimit.isEmpty()) {
			limit = EntityQuerySerializer.stringToLimit(strLimit);
		}
		
		EntityQuery query = null;
		String strQuery = req.getParameter(PARAM_QUERY);
		if (strQuery != null && !strQuery.isEmpty()) {
			query = EntityQuerySerializer.stringToQuery(strQuery);
		}
		
		EntityDescriptor entityDescriptor = DAOSystem.getEntityDescriptor(entityType);
		EntityList list = accessHandler.getEntities(entityType, limit, query);
	
		XmlEntityTransport et = new XmlEntityTransport();		
		et.write(pwOut, list, entityDescriptor, query.getColumns());		
	}


	/**
	 * @param entityType 
	 * @param req
	 * @param resp
	 * @param pwOut
	 * @throws ConfigurationException 
	 * @throws IOException 
	 */
	private void handleGetEntity(String entityType, HttpServletRequest req, HttpServletResponse resp, PrintWriter pwOut) throws ConfigurationException, IOException {

		String entityId = req.getParameter(PARAM_ENTITY_ID);
		
		assertValue(entityId, "Entity Id not specified");
		
		EntityDescriptor entityDescriptor = DAOSystem.getEntityDescriptor(entityType);
		EntityTransferObject eto = accessHandler.getETO(entityType, Integer.parseInt(entityId));

		XmlEntityTransport et = new XmlEntityTransport();
		et.write(pwOut, eto, entityDescriptor);
	}

	/**
	 * @param entityType
	 * @param message
	 */
	private void assertValue(String value, String message) {
		if (value == null || value.isEmpty()) {
			throw new IllegalArgumentException(message);
		}
	}
	
	/**
	 * @param pwOut 
	 */
	private void printResponse(PrintWriter pwOut, String response) {
		Document docResponse = DocumentFactory.getInstance().createDocument();
		Element rootResponse = docResponse.addElement(ELM_RESPONSE);
		rootResponse.addAttribute(PARAM_VALUE, response);
		pwOut.write(docResponse.asXML());
	}
}
