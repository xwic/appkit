/*******************************************************************************
 * Copyright 2015 xWic group (http://www.xwic.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *  
 *******************************************************************************/
/**
 * 
 */
package de.xwic.appkit.core.cluster.impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.xwic.appkit.core.cluster.CommunicationException;
import de.xwic.appkit.core.cluster.INode;
import de.xwic.appkit.core.cluster.INode.NodeStatus;
import de.xwic.appkit.core.cluster.Message;
import de.xwic.appkit.core.cluster.NodeAddress;
import de.xwic.appkit.core.cluster.NodeUnavailableException;
import de.xwic.appkit.core.cluster.Response;

/**
 * Holds the connection to a remote Node. 
 * @author lippisch
 */
public class OutboundChannel {

	//private final static int SOCKET_TIMEOUT = 10000; // 10 seconds
	
	private final Log log = LogFactory.getLog(getClass()); 
	
	private Socket socket = null;
	private NodeAddress nodeAddress;

	private ObjectOutputStream out;

	private ObjectInputStream in;

	private Cluster cluster;

	private INode node;

	/**
	 * Constructor.
	 * @param nodeAddress
	 */
	public OutboundChannel(Cluster cluster) {
		this.cluster = cluster;
		
	}
	
	/**
	 * Initiate the connection to the given node.
	 * @param nodeAddress
	 */
	public void openConnection(INode node, boolean callBack) throws NodeUnavailableException {
		openConnection(node, callBack, 0);
	}
	
	/**
	 * Initiate the connection to the given node.
	 * @param nodeAddress
	 */
	public void openConnection(INode node, boolean callBack, int remoteNodeId) throws NodeUnavailableException {
		
		this.node = node;
		this.nodeAddress = node.getAddress();
		int internalId = ((ClusterNode)node).getInternalNumber();
		
		try {
			openConnection();
		} catch (IOException e) {
			log.debug("Can not connect to node (" + e + ")");
			throw new NodeUnavailableException("Can not connect to node " + nodeAddress, e);
		}
		
		try {			
			
			// send identification
			Response res = sendMessage(new Message(Message.CMD_IDENTIFY_CLIENT, "ClusterNode"));
			if (!res.isSuccess()) {
				log.error("Client Identification failed " + res.getReason());
				closeConnection();
				throw new NodeUnavailableException("Connection initialization failed...");
			}
			
			
			int myPort = cluster.getConfig().getPortNumber();
			int masterPriority = cluster.getConfig().getMasterPriority();
			Message msg;
			if (callBack) {
				msg = new Message(ClusterNodeClientProtocol.CMD_CALLBACK, cluster.getConfig().getNodeName(), new Integer[] {myPort, remoteNodeId, masterPriority});
			} else {
				msg = new Message(ClusterNodeClientProtocol.CMD_CONNECT, cluster.getConfig().getNodeName(), new Integer[] {myPort, internalId, masterPriority});
			}
			res = sendMessage(msg);
			if (!res.isSuccess()) {
				log.error("The callback/connect process failed. " + res.getReason());
				throw new NodeUnavailableException("Connection callback failed...");
			}
			
			// connection successfully established
			
			
			// Ask remote node for the list of nodes
			res = sendMessage(new Message(ClusterNodeClientProtocol.CMD_GET_NODES));
			if (res.isSuccess()) {
				NodeInfo[] naList = (NodeInfo[])res.getData();
				for (NodeInfo na : naList) {
					// register the nodes. If the node is already known, it will not be registered by the cluster
					if (na != null && !na.getName().equals(cluster.getConfig().getNodeName())) {
						cluster.registerNode(na.getNodeAddress());
					}
				}
			}
			
			if (node.getStatus() == NodeStatus.DISCONNECTED || node.getStatus() == NodeStatus.NEW) {
				((ClusterNode)node)._connected(this);
				
				cluster.getClusterServiceManager().handleNewNode(node);
			}
			
		} catch (CommunicationException ce) {
			log.error("Communication error during connection initialization", ce);
			// abort the connection if initialization fails
			closeConnection();
			throw new NodeUnavailableException("Can not open connection", ce);
		}
			
	}
	
	/**
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * 
	 */
	private void openConnection() throws UnknownHostException, IOException {

		
		socket = new Socket(nodeAddress.getHostname(), nodeAddress.getPort());
		
//		socket.setSoTimeout(SOCKET_TIMEOUT);
		
		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());
	
	}
	
	/**
	 * Send a message and read the response.
	 * @param message
	 * @return
	 * @throws CommunicationException 
	 */
	public Response sendMessage(Message message) throws CommunicationException {
		
		try {
			
			Response response = null;
			// need to synchronize any communication accross the socket
			synchronized (out) {
				out.writeObject(message);
				
				response = (Response)in.readObject();
				if (response.getResponseTo() != 0 && response.getResponseTo() != message.getMessageId()) {
					// the response is not the one we expected
					throw new CommunicationException("Communication out of sync. Expected response to " + message.getMessageId() + " but received response #" + response.getResponseTo());
				}
			}
			return response;
		} catch (StreamCorruptedException sce) {
			// so the stream is corrupt.. disconnect the node and try to reconnect
			closeConnection();
			cluster.nodeDisconnected(node); // notify the cluster that the node was disconnected. The cluster will attempt to reconnect
			throw new CommunicationException("The connection to another node was corrupted and closed.");
			
		} catch (SocketException se) {
			closeConnection();
			cluster.nodeDisconnected(node); // notify the cluster that the node was disconnected. The cluster will attempt to reconnect
			throw new CommunicationException("The connection to another node got lost.");
			
		} catch (Exception e) {
			// if the de-serialization of the object failed, the stream might be corrupted
			// need to test if the 'De'serialization can resume properly or if the channel needs to be re-opened.
			log.error("Error receiving node response", e);
			throw new CommunicationException(e);
		}
	}
	
	/**
	 * Close all connection related objects.
	 */
	public void closeConnection() {
		
		if (out != null) { 
			try {
				out.close(); 
			} catch (Exception e) {
				log.error("Error closing Out", e);
			}
		};
		if (in != null) { 
			try {
				in.close(); 
			} catch (Exception e) {
				log.error("Error closing In", e);
			}
		};
		if (socket != null) { 
			try {
				socket.close(); 
			} catch (Exception e) {
				log.error("Error closing Socket", e);
			}
		};
	}
	
}
