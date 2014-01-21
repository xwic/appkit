/**
 *
 */
package de.xwic.appkit.core.remote.server;

import javax.servlet.http.HttpServletResponse;

import de.xwic.appkit.core.transport.xml.TransportException;


/**
 * @author Adrian Ionescu
 */
public interface IRequestHandler {

	/**
	 * @return
	 */
	public String getAction();

	/**
	 * @param pp
	 * @param resp
	 * @throws TransportException
	 */
	void handle(IParameterProvider pp, HttpServletResponse resp) throws TransportException;

}
