/**
 * 
 */
package de.xwic.appkit.cluster.comm.cnode;

import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.xwic.appkit.cluster.Cluster;
import de.xwic.appkit.cluster.NodeAddress;
import de.xwic.appkit.cluster.NodeUnavailableException;
import de.xwic.appkit.cluster.comm.ICommProtocol;
import de.xwic.appkit.cluster.comm.Message;
import de.xwic.appkit.cluster.comm.OutboundChannel;
import de.xwic.appkit.cluster.comm.Response;
import de.xwic.appkit.cluster.impl.ClusterNode;

/**
 * @author lippisch
 *
 */
public class ClusterNodeClientProtocol implements ICommProtocol {

	public final static String CMD_CONNECT = "connect";
	public final static String CMD_CALLBACK = "callback";
	
	private final Log log = LogFactory.getLog(getClass());
	
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
			
		}
		
		return res;
	}

	/**
	 * Initiated by a remote node that wants to connect to this instance.
	 * @param inMessage
	 * @return
	 */
	private Response onConnect(Socket socket, Message inMessage) {
		
		int remotePort = Integer.parseInt(inMessage.getArgument());
		
		// open the channel back
		OutboundChannel oc = new OutboundChannel();
		try {
			NodeAddress nodeAddress = new NodeAddress(socket.getInetAddress().getHostAddress(), remotePort);
			oc.openConnection(nodeAddress, true);

			ClusterNode cNode = new ClusterNode(nodeAddress, oc);
			Cluster.instance().registerNode(cNode);

		} catch (NodeUnavailableException e) {
			log.error("Connection call-back failed.", e);
			return new Response(false, "Connection Failed " + e);
		}
		
		return null;
	}


}
