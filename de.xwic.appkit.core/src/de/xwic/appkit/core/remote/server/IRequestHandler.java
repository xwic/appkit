/**
 * 
 */
package de.xwic.appkit.core.remote.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
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
	 * @param pwOut
	 * @throws TransportException
	 * @throws IOException 
	 */
	public void handle(final IParameterProvider pp, final HttpServletResponse resp, final PrintWriter pwOut) throws TransportException;
	
}
