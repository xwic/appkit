/**
 * 
 */
package de.xwic.appkit.cluster;

import de.xwic.appkit.cluster.comm.Message;
import de.xwic.appkit.cluster.comm.Response;

/**
 * A node within the cluster.
 * @author lippisch
 *
 */
public interface INode {
	
	
	/**
	 * @return the remoteHost
	 */
	public NodeAddress getAddress();

	/**
	 * Send a message to the node.
	 * @param message
	 * @return
	 * @throws CommunicationException
	 */
	public Response sendMessage(Message message) throws CommunicationException;

}
