/**
 * 
 */
package de.xwic.appkit.cluster;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import de.xwic.appkit.cluster.comm.InboundConnectionHandler;
import de.xwic.appkit.cluster.services.NodeController;

/**
 * Central accessor to obtain information about the cluster, it's nodes and ways to interact with it.
 * The Cluster object is a singleton, which means a single VM can only be member of one cluster at a time. This
 * allows more convenient access to the cluster.
 * 
 * This 'Cluster' implementation creates a grid of Nodes that are all connected to each other to broadcast messages.
 * There is no central master or hub, that delegates messages. Thus, all nodes are equal.
 * 
 * @author lippisch
 */
public class Cluster {

	private static Cluster instance = null;
	
	private ClusterConfiguration config;
	private InboundConnectionHandler inbConHandler = null;
	
	private Queue<NodeAddress> newNodeQueue = new ConcurrentLinkedQueue<NodeAddress>();
	
	private List<INode> nodes = new ArrayList<INode>(); 
	
	/**
	 * Private Constructor.
	 */
	private Cluster(ClusterConfiguration config) {
		this.config = config;
		
	}
	
	/**
	 * Returns the single instance of the cluster. The method Cluster.init() must be invoked before 
	 * an instance can be accessed. 
	 * @return
	 */
	public static Cluster instance() {
		if (instance == null) {
			throw new IllegalStateException("The Cluster has not yet been initialized.");
		}
		return instance;
	}
	
	/**
	 * Initialize the cluster instance.
	 * @param config
	 */
	public static void init(ClusterConfiguration config) {
		
		if (instance != null) {
			throw new IllegalStateException("Cluster already initialized.");
		}
		instance = new Cluster(config);
		instance.initInternal();
	}

	/**
	 * Internal initialization
	 */
	private void initInternal() {

		inbConHandler = new InboundConnectionHandler(config.getPortNumber());
		Thread tConHandler = new Thread(inbConHandler, "InboundConnectionHandler");
		tConHandler.setDaemon(true);
		tConHandler.start();
		
		for (NodeAddress na : config.getKnownNodes()) {
			queueNode(na);
		}
		
		// initiate the NodeController
		Thread tNC = new Thread(new NodeController(), "NodeController");
		tNC.setDaemon(true);
		tNC.start();
		
	}

	/**
	 * Add an address to be connected to. The node controller will attempt to connect
	 * to the specified node.
	 * @param na
	 */
	public synchronized void queueNode(NodeAddress na) {
		if (!newNodeQueue.contains(na)) {
			newNodeQueue.add(na);
		}
	}

	/**
	 * Returns the next queued new node address to be connected to.
	 * @return
	 */
	public NodeAddress nextNewNode() {
		return newNodeQueue.poll();
	}

	/**
	 * @return the config
	 */
	public ClusterConfiguration getConfig() {
		return config;
	}
	
	/**
	 * INTERNAL. Register an active node.
	 * @param node
	 */
	public void registerNode(INode node) {
		synchronized (nodes) {
			nodes.add(node);
		}
	}

	/**
	 * @return the nodes
	 */
	public List<INode> getNodes() {
		return nodes;
	}
	
	
}
