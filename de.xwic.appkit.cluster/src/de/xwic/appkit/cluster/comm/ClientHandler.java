/**
 * 
 */
package de.xwic.appkit.cluster.comm;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.xwic.appkit.cluster.comm.cnode.ClusterNodeClientProtocol;

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
	
	
	/**
	 * @param socket
	 */
	public ClientHandler(Socket socket) {
		super();
		this.socket = socket;
	}



	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		
		try {
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
							protocol = new ClusterNodeClientProtocol();
						} else if ("Console".equals(msgIn.getArgument())) {
							protocol = new ConsoleClientProtocol();
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
		
		// handle exiting properly (i.e. notify Cluster instance)

	}

}
