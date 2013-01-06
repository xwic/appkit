/**
 * 
 */
package de.xwic.appkit.cluster.comm;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.xwic.appkit.cluster.Cluster;
import de.xwic.appkit.cluster.CommunicationException;
import de.xwic.appkit.cluster.NodeAddress;
import de.xwic.appkit.cluster.NodeUnavailableException;
import de.xwic.appkit.cluster.comm.cnode.ClusterNodeClientProtocol;

/**
 * Holds the connection to a remote Node. 
 * @author lippisch
 */
public class OutboundChannel {

	private final Log log = LogFactory.getLog(getClass()); 
	
	private Socket socket = null;
	private NodeAddress nodeAddress;

	private ObjectOutputStream out;

	private ObjectInputStream in;

	/**
	 * Constructor.
	 * @param nodeAddress
	 */
	public OutboundChannel() {
		
	}
	
	/**
	 * Initiate the connection to the given node.
	 * @param nodeAddress
	 */
	public void openConnection(NodeAddress nodeAddress, boolean callBack) throws NodeUnavailableException {
		this.nodeAddress = nodeAddress;
		
		try {
			openConnection();
		} catch (IOException e) {
			log.error("Can not connect to node", e);
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
			
			
			int myPort = Cluster.instance().getConfig().getPortNumber();
			Message msg;
			if (callBack) {
				msg = new Message(ClusterNodeClientProtocol.CMD_CALLBACK, Integer.toString(myPort));
			} else {
				msg = new Message(ClusterNodeClientProtocol.CMD_CONNECT, Integer.toString(myPort));
			}
			res = sendMessage(msg);
			if (!res.isSuccess()) {
				log.error("The callback/connect process failed. " + res.getReason());
				throw new NodeUnavailableException("Connection callback failed...");
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
			
			out.writeObject(message);
			
			Response response = (Response)in.readObject();
			return response;
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
