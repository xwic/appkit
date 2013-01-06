/**
 * 
 */
package de.xwic.appkit.cluster.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.xwic.appkit.cluster.Cluster;
import de.xwic.appkit.cluster.NodeAddress;
import de.xwic.appkit.cluster.comm.OutboundChannel;
import de.xwic.appkit.cluster.impl.ClusterNode;

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

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		
		cluster = Cluster.instance();
		
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
	private void checkNewNodes() {
		
		if (System.currentTimeMillis() - lastCheckNewNodes > MIN_WAIT_CHECK_NEW_NODES) {
			
			NodeAddress newNode;
			while ((newNode = cluster.nextNewNode()) != null) {
				log.info("Attempting to connect to node " + newNode);
				
				try {
					OutboundChannel oc = new OutboundChannel();
					oc.openConnection(newNode, false);
					
					ClusterNode cNode = new ClusterNode(newNode, oc);
					cluster.registerNode(cNode);
					
				} catch (Exception e) {
					log.error("Attempt to connect to node '" + newNode + "' failed", e);
				}
			}
			
		}
		lastCheckNewNodes = System.currentTimeMillis();
		
	}

}
