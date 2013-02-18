/**
 * 
 */
package de.xwic.appkit.cluster.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.xwic.appkit.cluster.INode;
import de.xwic.appkit.cluster.NodeUnavailableException;

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
