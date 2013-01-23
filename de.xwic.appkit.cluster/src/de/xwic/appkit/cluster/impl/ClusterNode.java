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

	private static int _INT_NUM = 0;
	
	private NodeAddress nodeAddress;
	private OutboundChannel channel;

	private NodeStatus status = NodeStatus.NEW; 
	private String name = null;
	private int internalNumber;
	
	
	/**
	 * @param newNode
	 * @param oc
	 */
	public ClusterNode(NodeAddress nodeAddress) {
		this.nodeAddress = nodeAddress;
		internalNumber = _INT_NUM++;
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

	/**
	 * @return the status
	 */
	public NodeStatus getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(NodeStatus status) {
		this.status = status;
	}

	/**
	 * A connection was established
	 * @param outboundChannel
	 */
	public void _connected(OutboundChannel outboundChannel) {
		this.channel = outboundChannel;
		status = NodeStatus.CONNECTED;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the internalNumber
	 */
	public int getInternalNumber() {
		return internalNumber;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (name == null) {
			sb.append("Unnamed");
		} else {
			sb.append(name);
		}
		sb.append(" [").append(nodeAddress).append("]")
		.append(" #").append(internalNumber);
		return sb.toString();
	}
	
}
