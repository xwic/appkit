/**
 * 
 */
package de.xwic.appkit.cluster.impl;

import de.xwic.appkit.cluster.CommunicationException;
import de.xwic.appkit.cluster.INode;
import de.xwic.appkit.cluster.NodeAddress;
import de.xwic.appkit.cluster.comm.Message;
import de.xwic.appkit.cluster.comm.OutboundChannel;
import de.xwic.appkit.cluster.comm.Response;

/**
 * Represents a node within the cluster.
 * 
 * @author lippisch
 */
public class ClusterNode implements INode {

	private NodeAddress nodeAddress;
	private OutboundChannel channel;

	/**
	 * @param newNode
	 * @param oc
	 */
	public ClusterNode(NodeAddress nodeAddress, OutboundChannel channel) {
		this.nodeAddress = nodeAddress;
		this.channel = channel;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.cluster.INode#getAddress()
	 */
	@Override
	public NodeAddress getAddress() {
		return nodeAddress;
	}

	/**
	 * Send a message to the node.
	 * @param message
	 * @return
	 * @throws CommunicationException
	 */
	public Response sendMessage(Message message) throws CommunicationException {
		return channel.sendMessage(message);
	}

}
