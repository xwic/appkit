/**
 * 
 */
package de.xwic.appkit.cluster.impl;

import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.xwic.appkit.cluster.INode.NodeStatus;
import de.xwic.appkit.cluster.NodeAddress;
import de.xwic.appkit.cluster.NodeUnavailableException;
import de.xwic.appkit.cluster.comm.ICommProtocol;
import de.xwic.appkit.cluster.comm.Message;
import de.xwic.appkit.cluster.comm.OutboundChannel;
import de.xwic.appkit.cluster.comm.Response;

/**
 * @author lippisch
 *
 */
public class ClusterNodeClientProtocol implements ICommProtocol {

	public final static String CMD_CONNECT = "connect";
	public final static String CMD_CALLBACK = "callback";
	
	private final Log log = LogFactory.getLog(getClass());
	
	private Cluster cluster;
	private ClusterNode remoteNode = null;
	
	
	/**
	 * @param cluster
	 */
	public ClusterNodeClientProtocol(Cluster cluster) {
		super();
		this.cluster = cluster;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.cluster.comm.ICommProtocol#handleMessage(de.xwic.appkit.cluster.comm.Message)
	 */
	@Override
	public Response handleMessage(Socket socket, Message inMessage) {

		Response res = null;
		if (inMessage.getCommand().equals(CMD_CONNECT)) {
			// initiates a new node-to-node connection. This is initiated by the foreign node
			// and causes this node to open a connection back to the other Node. this allows 
			// a bi-directional communication
			
			res = onConnect(socket, inMessage);
			
		} else if (inMessage.getCommand().equals(CMD_CALLBACK)) {
			res = onCallback(socket, inMessage);
		}
		
		return res;
	}
	/* (non-Javadoc)
	 * @see de.xwic.appkit.cluster.comm.ICommProtocol#onConnectionLost()
	 */
	@Override
	public void onConnectionLost() {
		if (remoteNode != null) {
			remoteNode.setStatus(NodeStatus.DISCONNECTED);
		}
	}
	
	/**
	 * Initiated by a remote node that wants to connect to this instance.
	 * @param inMessage
	 * @return
	 */
	private Response onConnect(Socket socket, Message inMessage) {
		
		String remoteNodeName = inMessage.getArgument();
		Integer[] data = (Integer[])inMessage.getContainer();
		int remotePort = data[0];
		int internalNodeId = data[1];

		NodeAddress nodeAddress = new NodeAddress(socket.getInetAddress().getHostAddress(), remotePort);

		log.debug("Initial Connection Attempt from " + nodeAddress + ", identified as Node '" + remoteNodeName + "'");
		
		remoteNode  = (ClusterNode)cluster.getNodeByName(remoteNodeName);
		if (remoteNode  == null) { // this is a node we do not have in our list.
			remoteNode  = new ClusterNode(nodeAddress);
			remoteNode .setName(remoteNodeName);
			cluster.registerNode(remoteNode );
		}
		
		// open the channel back
		OutboundChannel oc = new OutboundChannel(cluster);
		try {
			log.debug("Attempt to open reversal connection to remote node '" + remoteNode + "'");
			oc.openConnection(remoteNode , true, internalNodeId);

		} catch (NodeUnavailableException e) {
			log.error("Connection call-back failed.", e);
			return new Response(false, "Connection Failed " + e);
		}
		
		return null;
	}



	/**
	 * We made an initial connection attempt to the remote node, which is now
	 * calling back to establish the reversal communication channel.
	 * @param socket
	 * @param inMessage
	 * @return
	 */
	private Response onCallback(Socket socket, Message inMessage) {
		
		String remoteNodeName = inMessage.getArgument();
		Integer[] data = (Integer[])inMessage.getContainer();
		int internalNodeId = data[1];
		
		log.info("Callback from remote node '" + remoteNodeName + "' (#" + internalNodeId + ")");
		
		remoteNode  = (ClusterNode)cluster.getNodeById(internalNodeId);
		if (remoteNode != null) {
			remoteNode.setName(remoteNodeName);
		} else {
			log.warn("Can not find the node with the internal number " + internalNodeId);
		}
		
		return null;
	}


}
