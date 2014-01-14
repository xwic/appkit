/**
 *
 */
package de.xwic.appkit.core.remote.server;

import static de.xwic.appkit.core.remote.server.RemoteDataAccessServlet.RESPONSE_OK;
import static de.xwic.appkit.core.remote.server.RemoteDataAccessServlet.printResponse;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.IFileHandler;
import de.xwic.appkit.core.transport.xml.TransportException;

/**
 * @author Alexandru Bledea
 * @since Jan 13, 2014
 */
public class RemoteFileAccessHandler implements IRequestHandler {

	private final Log log = LogFactory.getLog(getClass());

	public static final String PARAM_FH_ACTION = "fha";

	public static final String PARAM_FH_ID = "id";
	public static final String PARAM_FH_DELETE = "del";

	private IFileHandler handler;

	/**
	 *
	 */
	RemoteFileAccessHandler() {
		handler = DAOSystem.getFileHandler();
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.remote.server.IRequestHandler#getAction()
	 */
	@Override
	public String getAction() {
		return RemoteDataAccessServlet.ACTION_FILE_HANDLE;
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.remote.server.IRequestHandler#handle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.io.PrintWriter)
	 */
	@Override
	public void handle(final HttpServletRequest req, final HttpServletResponse resp, final PrintWriter pwOut) throws TransportException {
		String action = get(req, PARAM_FH_ACTION);
		if (action.equals(PARAM_FH_DELETE)) {
			delete(req, resp, pwOut);
		}
	}

	/**
	 * @param req
	 * @param resp
	 * @param pwOut
	 * @throws TransportException
	 */
	private void delete(final HttpServletRequest req, final HttpServletResponse resp, final PrintWriter pwOut) throws TransportException {
		String idString = get(req, PARAM_FH_ID);
		int id;
		try {
			id = Integer.parseInt(idString);
		} catch (NumberFormatException nfe) {
			log.error(String.format("Failed to parse '%s'", idString, nfe));
			throw new IllegalArgumentException("Invalid delete request!", nfe);
		}
		log.info("Received request to delete " + id);
		handler.deleteFile(id);
		printResponse(pwOut, RESPONSE_OK);
	}

	/**
	 * @param req
	 * @param what
	 * @return
	 * @throws TransportException
	 */
	private String get(final HttpServletRequest req, final String what) throws TransportException{
		String parameter = req.getParameter(what);
		if (parameter == null){
			throw new TransportException("Illegal arguments");
		}
		return parameter;
	}
}
