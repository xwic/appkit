/**
 *
 */
package de.xwic.appkit.core.remote.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.security.IUser;
import de.xwic.appkit.core.security.ScopeActionKey;
import de.xwic.appkit.core.security.daos.IUserDAO;
import de.xwic.appkit.core.transport.xml.TransportException;
import de.xwic.appkit.core.transport.xml.XmlBeanSerializer;

/**
 * @author Alexandru Bledea
 * @since Jan 13, 2014
 */
public class UserRightsHandler implements IRequestHandler {

	public final static String PARAM_USER_ID = "uid";
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.remote.server.IRequestHandler#getAction()
	 */
	@Override
	public String getAction() {
		return RemoteDataAccessServlet.ACTION_GET_USER_RIGHTS;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.remote.server.IRequestHandler#handle(de.xwic.appkit.core.remote.server.IParameterProvider, javax.servlet.http.HttpServletResponse, java.io.PrintWriter)
	 */
	@Override
	public void handle(final IParameterProvider pp, final HttpServletResponse resp) throws TransportException {
		
		String strUser = pp.getParameter(PARAM_USER_ID);
		
		if (strUser == null || strUser.trim().isEmpty()) {
			throw new IllegalArgumentException("The string is empty!");
		}
		
		int userId = Integer.valueOf(strUser);
		
		IUserDAO dao = DAOSystem.getDAO(IUserDAO.class);
		
		IUser user = dao.getEntity(userId);
		Set<ScopeActionKey> rights = dao.buildAllRights(user);
		
		String serialized = XmlBeanSerializer.serializeToXML("result", rights);
		PrintWriter pwOut;
		try {
			pwOut = resp.getWriter();
		} catch (IOException e) {
			throw new TransportException(e);
		}
		pwOut.write(serialized);
	}

}
