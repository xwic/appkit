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
	
	enum NodeStatus {
		/** A connection has not yet been established, only partial data is available */
		NEW,			
		/** The node is connected to this instance */ 
		CONNECTED,
		/** The connection to this node was lost, the node is currently disconnected. The
		 * cluster will try to re-establish the connection. */
		DISCONNECTED,
		/** The connection was disconnected and will not be re-connected */
		DISABLED
	}
	
	
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

	/**
	 * Returns the node status.
	 * @return
	 */
	public NodeStatus getStatus();
	
	/**
	 * Returns the unique name of the node or <code>null</code> if the node is yet unknown (NEW).
	 * @return
	 */
	public String getName();
}
