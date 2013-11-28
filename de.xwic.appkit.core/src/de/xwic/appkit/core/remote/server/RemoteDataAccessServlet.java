/**
 * 
 */
package de.xwic.appkit.core.remote.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.xwic.appkit.core.access.AccessHandler;
import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.transfer.EntityTransferObject;
import de.xwic.appkit.core.transport.xml.XmlEntityTransport;

/**
 * This servlet allows remote applications to access entities on the server. It is the counterpart
 * to the remote.client package.
 * 
 * @author lippisch
 */
public class RemoteDataAccessServlet extends HttpServlet {

	public final static String PARAM_ACTION = "a";
	public final static String PARAM_ENTITY_TYPE = "et";
	public final static String PARAM_ENTITY_ID = "eid";
	public final static String PARAM_VERSION = "ev";
	public final static String PARAM_QUERY = "pq";
	public final static String PARAM_LIMIT = "limit";
	
	public final static String ACTION_GET_ENTITY = "ge";
	public final static String ACTION_GET_ENTITIES = "gea";

	public final static Log log = LogFactory.getLog(RemoteDataAccessServlet.class);
	
	private AccessHandler accessHandler;

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
			
			if (action.equals(ACTION_GET_ENTITY)) {
				handleGetEntity(req, resp, pwOut);
				
			} else if (action.equals(ACTION_GET_ENTITIES)) {
				handleGetEntities(req, resp, pwOut);
				
			} else {
				throw new IllegalArgumentException("Unknown action");
			}
			
			pwOut.flush();
			pwOut.close();
			
		} catch (Exception e) {
			log.error(e);
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.toString());
		}
		
		
	}

	/**
	 * @param req
	 * @param resp
	 * @param pwOut
	 * @throws ConfigurationException 
	 * @throws IOException 
	 */
	private void handleGetEntities(HttpServletRequest req, HttpServletResponse resp, PrintWriter pwOut) throws ConfigurationException, IOException {
		String entityType = req.getParameter(PARAM_ENTITY_TYPE);
		
		assertValue(entityType, "Entity Type not specified");
		
		EntityDescriptor entityDescriptor = DAOSystem.getEntityDescriptor(entityType);
		EntityList list = accessHandler.getEntities(entityType, null, null);
	
		XmlEntityTransport et = new XmlEntityTransport();
		et.write(pwOut, list, entityDescriptor);
		
	}


	/**
	 * @param req
	 * @param resp
	 * @param pwOut
	 * @throws ConfigurationException 
	 * @throws IOException 
	 */
	private void handleGetEntity(HttpServletRequest req, HttpServletResponse resp, PrintWriter pwOut) throws ConfigurationException, IOException {

		String entityType = req.getParameter(PARAM_ENTITY_TYPE);
		String entityId = req.getParameter(PARAM_ENTITY_ID);
		
		assertValue(entityType, "Entity Type not specified");
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
}
