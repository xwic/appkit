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
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Listens to the specified port for connections from other nodes. If a node connects, it spins of
 * a separate thread to handle the communication with this client.
 * 
 * @author lippisch
 *
 */
public class InboundConnectionHandler implements Runnable {

	private Log log = LogFactory.getLog(getClass());
	private int port;
	private Cluster cluster;
	
	
	/**
	 * @param port
	 */
	public InboundConnectionHandler(Cluster cluster, int port) {
		super();
		this.cluster = cluster;
		this.port = port;
	}



	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

		ServerSocket srvSocket;
		try {
			srvSocket = new ServerSocket(port);
			
		} catch (IOException e) {
			log.error("Can not open server socket", e);
			return; // exit
		}
		
		int conHandlerCount = 0;
		int errCount = 0;
		long lastErr = 0;
		while (true) {
			try {
				Socket socket = srvSocket.accept();
				
				log.debug("Accepted connection from " + socket.getInetAddress().getHostAddress());
				
				ClientHandler ch = new ClientHandler(cluster, socket);
				
				Thread t = new Thread(ch, "ConnectionHandler-" + socket.getInetAddress().getHostAddress() + "-" + conHandlerCount++);
				t.setDaemon(true); // launch as a Deamon
				t.start();
				
			} catch (IOException e) {
				log.error("Error accepting incoming connection...", e);
				if ((System.currentTimeMillis() - lastErr) < 3000) { // the last error was just 3 seconds ago
					errCount++;
					if (errCount > 100) { 
						log.error("More than 100 errors occured within the last 3 seconds. Giving up.");
						break; // break the loop
					}
				} else {
					errCount = 0;
				}
				lastErr = System.currentTimeMillis();
			}
		}
		
	}
	
}
