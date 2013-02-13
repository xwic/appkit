/**
 * 
 */
package de.xwic.appkit.cluster.impl;

import java.net.Socket;
import java.util.Date;

import de.xwic.appkit.cluster.ClusterEvent;
import de.xwic.appkit.cluster.IClusterService;
import de.xwic.appkit.cluster.ICommProtocol;
import de.xwic.appkit.cluster.INode;
import de.xwic.appkit.cluster.Message;
import de.xwic.appkit.cluster.Response;


/**
 * Handles a console client used by admins or test tools.
 * 
 * @author lippisch
 */
public class ConsoleClientProtocol implements ICommProtocol {

	public final static String CMD_TIME = "time";
	public final static String CMD_NODES = "nodes";
	public final static String CMD_EVENT = "event";
	public final static String CMD_SERVICES = "services";
	
	private Cluster cluster;
	
	
	
	/**
	 * @param cluster
	 */
	public ConsoleClientProtocol(Cluster cluster) {
		super();
		this.cluster = cluster;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.cluster.comm.ICommProtocol#handleMessage(java.net.Socket, de.xwic.appkit.cluster.comm.Message)
	 */
	@Override
	public Response handleMessage(Socket socket, Message inMessage) {
		
		if (inMessage.getCommand().equals(CMD_TIME)) {
			return new Response(true, new Date().toString());
			
		} else if(inMessage.getCommand().equals(CMD_SERVICES)){
			
			return onServices(socket, inMessage);
			
		} else if(inMessage.getCommand().equals(CMD_NODES)){
			
			StringBuilder sb = new StringBuilder();
			for (INode node : cluster.getNodes()) {
				sb.append(node.toString()).append(" <").append(node.getStatus()).append(">\n");
			}
			return new Response(true, sb.toString());
		
		} else if(inMessage.getCommand().equals(CMD_EVENT)){
			String arg = inMessage.getArgument();
			int idx = arg.indexOf(':');
			if (idx != -1) {
				String ns = arg.substring(0, idx);
				String en = arg.substring(idx + 1);
				try {
					cluster.sendEvent(new ClusterEvent(ns, en), true);
					return new Response(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return new Response(false);
		} else {
			return new Response(false, "Unknown Command");
		}
		
		
	}

	/**
	 * @param socket
	 * @param inMessage
	 * @return
	 */
	private Response onServices(Socket socket, Message inMessage) {

		StringBuilder sb = new StringBuilder();
		for (String name : cluster.getInstalledClusterServiceNames()) {
			IClusterService cs = cluster.getClusterService(name);
			sb.append(name).append(" <").append(cs.getClass().getSimpleName()).append("> | ").append(cs.isMaster() ? " Master " : " Slave ").append("\n");
		}
		return new Response(true, sb.toString());
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.cluster.comm.ICommProtocol#onConnectionLost()
	 */
	@Override
	public void onConnectionLost() {
		// nothing to do
	}
	
}
