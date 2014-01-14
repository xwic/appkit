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

import de.jwic.upload.Upload;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.IFileHandler;
import de.xwic.appkit.core.transport.xml.TransportException;

/**
 * @author Alexandru Bledea
 * @since Jan 13, 2014
 */
public class RemoteFileAccessHandler {

	private final Log log = LogFactory.getLog(getClass());

	public static final String PARAM_FH_ACTION = "fha";

	public static final String PARAM_FH_ID = "fh_i";

	public static final String PARAM_FH_ACTION_DELETE = "fh_d";
	public static final String PARAM_FH_ACTION_LOAD = "fh_l";

	public static final String PARAM_FH_ACTION_UPLOAD = "fh_u";
	public static final String PARAM_FH_UPLOAD_FILENAME = "fh_n";
	public static final String PARAM_FH_UPLOAD_SIZE = "fh_s";
	public static final String PARAM_FH_STREAM = "fh_is";

	private final IFileHandler localHandler;

	/**
	 *
	 */
	RemoteFileAccessHandler() {
		localHandler = DAOSystem.getFileHandler();
	}

	/**
	 * @param req
	 * @param resp
	 * @param pwOut
	 * @param multipart
	 * @param multipart
	 * @throws TransportException
	 */
	void handle(final HttpServletRequest req, final HttpServletResponse resp, final PrintWriter pwOut, final Upload upload, final boolean multipart)
			throws TransportException {
		String action = get(req, PARAM_FH_ACTION);
		if (action.equals(PARAM_FH_ACTION_DELETE)) {
			delete(req, resp, pwOut);
		} else if (action.equals(PARAM_FH_ACTION_UPLOAD)) {
//			upload(req, resp, pwOut);
		} else {
			throw new UnsupportedOperationException("No such implementation " + action);
		}
	}

	/**
	 * @param upload
	 * @param resp
	 * @param pwOut
	 * @throws TransportException
	 */
	void upload(final Upload upload, final HttpServletResponse resp, final PrintWriter pwOut) throws TransportException {
//		String filename = get(req, PARAM_FH_UPLOAD_FILENAME);
//		long size = getLong(req, PARAM_FH_UPLOAD_SIZE);

	}

	/**
	 * @param req
	 * @param resp
	 * @param pwOut
	 * @throws TransportException
	 */
	private void delete(final HttpServletRequest req, final HttpServletResponse resp, final PrintWriter pwOut) throws TransportException {
		int id = getInt(req, PARAM_FH_ID);
		log.info("Received request to delete " + id);
		localHandler.deleteFile(id);
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

	/**
	 * @param req
	 * @param what
	 * @return
	 * @throws TransportException
	 */
	private int getInt(final HttpServletRequest req, final String what) throws TransportException {
		return (int) getLong(req, what);
	}

	/**
	 * @param req
	 * @param what
	 * @return
	 * @throws TransportException
	 */
	private long getLong(final HttpServletRequest req, final String what) throws TransportException {
		String string = get(req, what);
		try {
			return Long.parseLong(string);
		} catch (NumberFormatException nfe) {
			log.error(String.format("Invalid request!", nfe));
			throw new IllegalArgumentException("Invalid request!", nfe);
		}
	}

}
