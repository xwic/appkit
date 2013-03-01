/**
 * 
 */
package de.xwic.appkit.cluster.impl;

import java.io.Serializable;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.xwic.appkit.cluster.ClusterEvent;
import de.xwic.appkit.cluster.ClusterServiceStatus;
import de.xwic.appkit.cluster.IClusterService;
import de.xwic.appkit.cluster.ICommProtocol;
import de.xwic.appkit.cluster.INode;
import de.xwic.appkit.cluster.Message;
import de.xwic.appkit.cluster.NodeAddress;
import de.xwic.appkit.cluster.NodeUnavailableException;
import de.xwic.appkit.cluster.RemoteInvokationException;
import de.xwic.appkit.cluster.Response;

/**
 * @author lippisch
 *
 */
public class ClusterNodeClientProtocol implements ICommProtocol {

	public final static String CMD_CONNECT = "connect";
	public final static String CMD_CALLBACK = "callback";
	public final static String CMD_EVENT = "event";
	public final static String CMD_GET_NODES = "getNodes";

	public final static String CMD_GET_SERVICE_STATUS = "getServiceStatus";
	public final static String CMD_TAKE_MASTER_ROLE = "takeMasterRole";
	public final static String CMD_SURRENDER_SERVICE = "surrenderService";
	public final static String CMD_INVOKE_SERVICE = "invokeService";
	
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
		String command = inMessage.getCommand();
		if (command != null) {
			if (command.equals(CMD_CONNECT)) {
				// initiates a new node-to-node connection. This is initiated by the foreign node
				// and causes this node to open a connection back to the other Node. this allows 
				// a bi-directional communication
				
				res = onConnect(socket, inMessage);
				
			} else if (command.equals(CMD_CALLBACK)) {
				res = onCallback(socket, inMessage);
	
			} else if (command.equals(CMD_EVENT)) {
				res = onEvent(socket, inMessage);
	
			} else if (command.equals(CMD_GET_NODES)) {
				res = onGetNodes(socket, inMessage);
				
			} else if (command.equals(CMD_GET_SERVICE_STATUS)) {
				res = onGetServiceStatus(socket, inMessage);

			} else if (command.equals(CMD_TAKE_MASTER_ROLE)) {
				res = onTakeMasterRole(socket, inMessage);
				
			} else if (command.equals(CMD_SURRENDER_SERVICE)) {
				res = onSurrenderService(socket, inMessage);
			
			} else if (command.equals(CMD_INVOKE_SERVICE)) {
				res = onInvokeService(socket, inMessage);
				
			}
		}
		if (res != null) {
			res.setResponseTo(inMessage.getMessageId());
		}
		return res;
	}
	/**
	 * @param socket
	 * @param inMessage
	 * @return
	 */
	private Response onInvokeService(Socket socket, Message inMessage) {
		
		String[] args = inMessage.getArgument().split(":");
		String serviceName = args[0];
		String methodName = args[1];
		
		try {
			return cluster.getClusterServiceManager().invokeService(serviceName, methodName, (Serializable[]) inMessage.getContainer());
		} catch (RemoteInvokationException e) {
			return new Response(false, e.toString());
		}
		
	}

	/**
	 * @param socket
	 * @param inMessage
	 * @return
	 */
	private Response onSurrenderService(Socket socket, Message inMessage) {

		String serviceName = inMessage.getArgument();
		cluster.getClusterServiceManager().remoteSurrenderService(serviceName, remoteNode, inMessage.getContainer());
		
		return null;
	}

	/**
	 * @param socket
	 * @param inMessage
	 * @return
	 */
	private Response onTakeMasterRole(Socket socket, Message inMessage) {

		String serviceName = inMessage.getArgument();
		Serializable data = cluster.getClusterServiceManager().takeMasterRole(serviceName, remoteNode);
		
		return new Response(true, null, data);
	}

	/**
	 * Returns the status of a service installation.
	 * @param socket
	 * @param inMessage
	 * @return
	 */
	private Response onGetServiceStatus(Socket socket, Message inMessage) {

		String serviceName = inMessage.getArgument();
		ClusterServiceStatus status;
		
		try {
			IClusterService cs = cluster.getClusterService(serviceName);
			if (cs.isMaster()) {
				status = ClusterServiceStatus.ACTIVE_MASTER;
			} else {
				status = ClusterServiceStatus.ACTIVE_SLAVE;
			}
		} catch (IllegalArgumentException iae) {
			status = ClusterServiceStatus.NO_SUCH_SERVICE;
		}
		return new Response(true, status.toString());
		
	}

	/**
	 * @param socket
	 * @param inMessage
	 * @return
	 */
	private Response onGetNodes(Socket socket, Message inMessage) {
		
		INode[] nodes = cluster.getNodes();
		NodeInfo[] naList = new NodeInfo[nodes.length];
		
		for (int i = 0; i < naList.length; i++) {
			if (nodes[i].getName() != null) {
				naList[i] = new NodeInfo(nodes[i].getName(), nodes[i].getAddress());
			}
		}
		
		return new Response(true, null, naList);
	}

	/**
	 * @param socket
	 * @param inMessage
	 * @return
	 */
	private Response onEvent(Socket socket, Message inMessage) {

		ClusterEvent event = (ClusterEvent)inMessage.getContainer();
		if (event != null) {
			cluster._receivedEvent(event);
		}
		
		return null;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.cluster.comm.ICommProtocol#onConnectionLost()
	 */
	@Override
	public void onConnectionLost() {
		if (remoteNode != null) {
			remoteNode._disconnected();
			cluster.nodeDisconnected(remoteNode);
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
		int remoteMasterPriority = data[2];

		NodeAddress nodeAddress = new NodeAddress(socket.getInetAddress().getHostAddress(), remotePort);

		log.debug("Initial Connection Attempt from " + nodeAddress + ", identified as Node '" + remoteNodeName + "'");
		
		remoteNode  = (ClusterNode)cluster.getNodeByName(remoteNodeName);
		if (remoteNode  == null) { // this is a node we do not have in our list.
			remoteNode  = new ClusterNode(nodeAddress);
			remoteNode.setName(remoteNodeName);
			remoteNode.setMasterPriority(remoteMasterPriority);
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
		int remoteMasterPriority = data[2];
		
		log.info("Callback from remote node '" + remoteNodeName + "' (#" + internalNodeId + ")");
		
		remoteNode  = (ClusterNode)cluster.getNodeById(internalNodeId);
		if (remoteNode != null) {
			remoteNode.setName(remoteNodeName);
			remoteNode.setMasterPriority(remoteMasterPriority);
		} else {
			log.warn("Can not find the node with the internal number " + internalNodeId);
		}
		
		return null;
	}


}
