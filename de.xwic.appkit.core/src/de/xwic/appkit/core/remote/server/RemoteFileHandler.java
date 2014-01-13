/**
 *
 */
package de.xwic.appkit.core.remote.server;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.xwic.appkit.core.dao.IFileHandler;
import de.xwic.appkit.core.transport.xml.TransportException;

/**
 * @author Alexandru Bledea
 * @since Jan 13, 2014
 */
public class RemoteFileHandler {

	private final Log log = LogFactory.getLog(getClass());

	private static final String PARAM_ACTION = "fha";

	private static final String PARAM_ID = "id";
	private static final String PARAM_DELETE = "del";

	private IFileHandler handler;

	/**
	 * @param req
	 * @param resp
	 * @param pwOut
	 * @throws TransportException
	 */
	void handle(final HttpServletRequest req, final HttpServletResponse resp, final PrintWriter pwOut) throws TransportException {
		String action = get(req, PARAM_ACTION);
		if (action.equals(PARAM_DELETE)) {
			String id = get(req, PARAM_ID);
			pwOut.write(id);
			pwOut.flush();
			pwOut.close();
		}
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
