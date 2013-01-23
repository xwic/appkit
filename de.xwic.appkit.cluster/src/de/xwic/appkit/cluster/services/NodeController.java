/**
 * 
 */
package de.xwic.appkit.cluster.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.xwic.appkit.cluster.INode;
import de.xwic.appkit.cluster.NodeUnavailableException;
import de.xwic.appkit.cluster.comm.OutboundChannel;
import de.xwic.appkit.cluster.impl.Cluster;

/**
 * Controls the vital functions of the current node. Performs heartbeat checks with known nodes,
 * to maintain the list of nodes. Also connects to other nodes.
 * @author lippisch
 */
public class NodeController implements Runnable {

	private final static long MIN_WAIT_CHECK_NEW_NODES = 5000; // 5 seconds

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
			
			checkNewNodes();
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {}
			
		}
		

	}

	/**
	 * 
	 */
	@SuppressWarnings("incomplete-switch")
	private void checkNewNodes() {
		
		if (System.currentTimeMillis() - lastCheckNewNodes > MIN_WAIT_CHECK_NEW_NODES) {
			
			INode nodes[]  = new INode[0];
			nodes = cluster.getNodes().toArray(nodes);
			
			for (INode node : nodes) {
				switch (node.getStatus()) {
				case NEW:
				case DISCONNECTED :
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
					
					break;
				}
			}
			
			lastCheckNewNodes = System.currentTimeMillis();
		}
		
	}

}
