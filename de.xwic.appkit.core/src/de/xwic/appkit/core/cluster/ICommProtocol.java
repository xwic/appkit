/**
 * 
 */
package de.xwic.appkit.core.cluster;

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
	
	/**
	 * Invoked by the ClientHandler if the connection is lost to the remote client.
	 */
	public void onConnectionLost();
}
