/**
 * 
 */
package de.xwic.appkit.cluster.impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.xwic.appkit.cluster.ICommProtocol;
import de.xwic.appkit.cluster.Message;
import de.xwic.appkit.cluster.Response;

/**
 * Handles an incoming connection.
 * 
 * @author lippisch
 *
 */
public class ClientHandler implements Runnable {

	private final Log log = LogFactory.getLog(getClass());
	
	private Socket socket;
	private ICommProtocol protocol = null;
	
	
	private Cluster cluster;
	
	/**
	 * @param socket
	 */
	public ClientHandler(Cluster cluster, Socket socket) {
		super();
		this.cluster = cluster;
		this.socket = socket;
	}



	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		
		try {
			
			//socket.setSoTimeout(10000); // set the timeout to 10 seconds.
			
			ObjectInputStream oIn = new ObjectInputStream(socket.getInputStream());
			ObjectOutputStream oOut = new ObjectOutputStream(socket.getOutputStream());
			
			
			while (true) {
				Message msgIn;
				try {
					msgIn = (Message)oIn.readObject();
				} catch (IOException e) {
					// this occurs when the connection is closed
					log.info("Client " + socket.getInetAddress() + " disconnected.");
					break;
				} catch (ClassNotFoundException e) {
					log.error("Can not handle incoming message - stream might be corrupted..", e);
					break;
				}
				
				Response response = null;
				
				System.out.println("Received: " + msgIn.getCommand() + " ('" + msgIn.getArgument() + "')");
				
				// as long as the client is not identified, no protocol is selected. 
				if (protocol == null) { 
					if (Message.CMD_IDENTIFY_CLIENT.equals(msgIn.getCommand())) {
						// argument must contain client type, which causes the selection of the right protocol
						// hard-coded here right now, but might get externalized into some configuration file
						// later on...
						if ("ClusterNode".equals(msgIn.getArgument())) {
							protocol = new ClusterNodeClientProtocol(cluster);
						} else if ("Console".equals(msgIn.getArgument())) {
							protocol = new ConsoleClientProtocol(cluster);
						} else {
							log.debug("Invalid protocol selected.");
							response = new Response(false, "Invalid protocol selected.");
						}
						
						if (protocol != null) {
							log.info("Selected protocol " + protocol.getClass().getName());
						}
						
					} else {
						log.debug("Invalid command received - awaiting client identification..");
					}
				} else {
					response = protocol.handleMessage(socket, msgIn);
				}
				
				if (response == null) {
					response = new Response(true); // send success response
				}
				
				oOut.writeObject(response);
				
			}
			
		} catch (IOException e) {
			log.error("Communication error with client", e);
			// will cause an exit.. 
		}
		
		if (protocol != null) {
			protocol.onConnectionLost();
		}
		// close the socket.
		try {
			socket.close();
		} catch (IOException e) {
			log.warn("Error closing socket.", e);
		}
		
		// handle exiting properly (i.e. notify Cluster instance)

	}

}
