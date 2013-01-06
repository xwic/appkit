/**
 * 
 */
package de.xwic.appkit.cluster.comm;

import java.net.Socket;

/**
 * A communication protocol to handle a specific client. Allows handling of different client types such 
 * as a Cluster Node, Job Server or Admin Console.
 * 
 * @author lippisch
 */
public interface ICommProtocol {

	/**
	 * Handle an incoming message and return the response.
	 * @param socket 
	 * @param inMessage
	 * @return
	 */
	public Response handleMessage(Socket socket, Message inMessage);
	
}
