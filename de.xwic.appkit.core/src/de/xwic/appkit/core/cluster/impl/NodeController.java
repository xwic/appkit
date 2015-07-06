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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.xwic.appkit.core.cluster.INode;
import de.xwic.appkit.core.cluster.NodeUnavailableException;

/**
 * Controls the vital functions of the current node. Performs heartbeat checks with known nodes,
 * to maintain the list of nodes. Also connects to other nodes.
 * @author lippisch
 */
public class NodeController implements Runnable {

	private final static long MIN_WAIT_CHECK_NEW_NODES = 2000; // 5 seconds
	private final static long MAX_RETRY_AGE = 1000 * 60; // 1 minute

	private final Log log = LogFactory.getLog(getClass());
	
	private long lastCheckNewNodes = 0;

	private Cluster cluster;

	/**
	 * @param cluster
	 */
	public NodeController(Cluster cluster) {
		super();
		this.cluster = cluster;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		
		while (true) {
			
			checkNodes();
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {}
			
		}
		

	}

	/**
	 * 
	 */
	@SuppressWarnings("incomplete-switch")
	private void checkNodes() {
		
			
		INode[] nodes  = cluster.getNodes();
		
		for (INode node : nodes) {
			switch (node.getStatus()) {
			case NEW:
			case DISCONNECTED :

				if (System.currentTimeMillis() - lastCheckNewNodes > MIN_WAIT_CHECK_NEW_NODES) {
					long age = (node.getDisconnectedSince() != null ? System.currentTimeMillis() - node.getDisconnectedSince().getTime() : 0);
					
					if (age > MAX_RETRY_AGE) {
						log.debug("Disabling node after too many retries... " + node);
						((ClusterNode)node)._disable();
					} else {
					
						log.debug("Attempting to connect to node " + node);
						
						try {
							OutboundChannel oc = new OutboundChannel(cluster);
							oc.openConnection(node, false);
							
							log.info("Connection to " + node + " established.");
							
							
						} catch (NodeUnavailableException nue) {
							log.debug("The node '" + node + "' is unavailable at the moment.");
						} catch (Exception e) {
							log.error("Attempt to connect to node '" + node + "' failed", e);
						}
						
						
					
					}
					lastCheckNewNodes = System.currentTimeMillis();
				}
				break;
			}
		}
		
		
	}

}
