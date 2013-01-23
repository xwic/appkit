/**
 * 
 */
package de.xwic.appkit.cluster.impl;

import java.util.ArrayList;
import java.util.List;

import de.xwic.appkit.cluster.ClusterConfiguration;
import de.xwic.appkit.cluster.ICluster;
import de.xwic.appkit.cluster.INode;
import de.xwic.appkit.cluster.NodeAddress;
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
public class Cluster implements ICluster {

	
	private ClusterConfiguration config;
	private InboundConnectionHandler inbConHandler = null;
	
	private List<INode> nodes = new ArrayList<INode>(); 
	
	/**
	 * Private Constructor.
	 */
	public Cluster(ClusterConfiguration config) {
		this.config = config;
		
	}


	/**
	 * Internal initialization
	 */
	public void initInternal() {

		inbConHandler = new InboundConnectionHandler(this, config.getPortNumber());
		Thread tConHandler = new Thread(inbConHandler, "InboundConnectionHandler");
		tConHandler.setDaemon(true);
		tConHandler.start();
		
		for (NodeAddress na : config.getKnownNodes()) {
			registerNode(na);
		}
		
		// initiate the NodeController
		Thread tNC = new Thread(new NodeController(this), "NodeController");
		tNC.setDaemon(true);
		tNC.start();
		
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.cluster.ICluster#getConfig()
	 */
	@Override
	public ClusterConfiguration getConfig() {
		return config;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.cluster.ICluster#registerNode(de.xwic.appkit.cluster.NodeAddress)
	 */
	@Override
	public void registerNode(NodeAddress na) {
		registerNode(new ClusterNode(na));
	}
	
	/**
	 * Add a known node to the cluster
	 * @param node
	 */
	public void registerNode(INode node) {
		synchronized (nodes) {
			nodes.add(node);
		}
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.cluster.ICluster#getNodes()
	 */
	@Override
	public List<INode> getNodes() {
		return nodes;
	}


	/**
	 * @param remoteNodeName
	 */
	public INode getNodeByName(String remoteNodeName) {
		
		for (INode node : nodes) {
			if (node.getName() != null && remoteNodeName.equals(node.getName())) {
				return node;
			}
		}
		return null;
		
	}


	/**
	 * @param internalNodeId
	 * @return
	 */
	public INode getNodeById(int internalNodeId) {
		for (INode node : nodes) {
			if (((ClusterNode)node).getInternalNumber() == internalNodeId) {
				return node;
			}
		}
		return null;
	}
	
	
}
