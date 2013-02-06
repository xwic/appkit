/**
 * 
 */
package de.xwic.appkit.cluster.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.xwic.appkit.cluster.ClusterConfiguration;
import de.xwic.appkit.cluster.ClusterEvent;
import de.xwic.appkit.cluster.ClusterEventListener;
import de.xwic.appkit.cluster.EventTimeOutException;
import de.xwic.appkit.cluster.ICluster;
import de.xwic.appkit.cluster.IClusterService;
import de.xwic.appkit.cluster.INode;
import de.xwic.appkit.cluster.INode.NodeStatus;
import de.xwic.appkit.cluster.Message;
import de.xwic.appkit.cluster.NodeAddress;
import de.xwic.appkit.cluster.Response;
import de.xwic.appkit.cluster.TransportResult;

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

	private final static Log log = LogFactory.getLog(Cluster.class);
	
	private final static long MAX_EVENT_WAIT_TIME = 1000 * 60 * 1; // event time out is 1 minute.
	
	private ClusterConfiguration config;
	private InboundConnectionHandler inbConHandler = null;
	
	private INode[] nodes = new INode[0]; 
	
	private Queue<EventWrapper> eventQueue = new ConcurrentLinkedQueue<EventWrapper>();
	private Thread tEventQueue;

	private ClusterServiceManager clServiceManager;

	/**
	 * Internal wrapper for event listeners to filter by namespace.
	 * @author lippisch
	 */
	private class EventListenerWrapper {
		ClusterEventListener listener;
		String namespace = null;
		public EventListenerWrapper(ClusterEventListener listener, String namespace) {
			super();
			this.listener = listener;
			this.namespace = namespace;
		}
		
	}
	
	private List<EventListenerWrapper> listeners = Collections.synchronizedList(new ArrayList<EventListenerWrapper>());
	
	/**
	 * Private Constructor.
	 */
	public Cluster(ClusterConfiguration config) {
		this.config = config;
		this.clServiceManager = new ClusterServiceManager(this);
		
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

		tEventQueue = new Thread(new EventQueueController(this), "EventQueueController");
		tEventQueue.setDaemon(true);
		tEventQueue.start();

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
			// check if node already exists
			for (INode n : nodes) {
				if (node.sameNode(n)) {
					return;
				}
			}
			int l = nodes.length;
			INode[] newNodes = new INode[l + 1];
			System.arraycopy(nodes, 0, newNodes, 0, l);
			newNodes[l] = node;
			nodes = newNodes;
		}
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.cluster.ICluster#getNodes()
	 */
	@Override
	public INode[] getNodes() {
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
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.cluster.ICluster#addEventListener(de.xwic.appkit.cluster.ClusterEventListener)
	 */
	@Override
	public void addEventListener(ClusterEventListener listener) {
		addEventListener(listener, null);
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.cluster.ICluster#addEventListener(de.xwic.appkit.cluster.ClusterEventListener, java.lang.String)
	 */
	@Override
	public void addEventListener(ClusterEventListener listener, String namespace) {

		listeners.add(new EventListenerWrapper(listener, namespace));
		
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.cluster.ICluster#sendEvent(de.xwic.appkit.cluster.ClusterEvent, boolean)
	 */
	@Override
	public void sendEvent(ClusterEvent event, boolean asynchronous) throws EventTimeOutException {
		
		EventWrapper ew = new EventWrapper(event, Thread.currentThread(), asynchronous);
		eventQueue.add(ew);
		tEventQueue.interrupt();	// wake up the queue
		
		if (!asynchronous) { // wait until the event is completed
			
			long start = System.currentTimeMillis();
			while (!ew.isCompleted()) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					
				}
				if (System.currentTimeMillis() - start > MAX_EVENT_WAIT_TIME) {
					throw new EventTimeOutException();
				}
			}
			
		}
		
	}
	
	/**
	 * INTERNAL - handle an incoming event.
	 * @param event
	 */
	public void _receivedEvent(ClusterEvent event) {
		
		EventListenerWrapper[] lst = new EventListenerWrapper[listeners.size()];
		lst = listeners.toArray(lst);
		
		for (EventListenerWrapper elw : lst) {
			if (elw.namespace == null || elw.namespace.equals(event.getNamespace())) {
				try {
					elw.listener.receivedEvent(event);
				} catch (Throwable t) {
					log.error("Event Listener '" + elw.listener.getClass().getName() + "' did throw exception for handling event '" + event + "'", t);
				}
			}
		}
		
	}
	
	/**
	 * Returns the next event waiting to be processed. Removes it fromt he queue.
	 * @return
	 */
	public EventWrapper nextEvent() {
		return eventQueue.poll();
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.cluster.ICluster#sendMessage(de.xwic.appkit.cluster.Message)
	 */
	@Override
	public TransportResult[] sendMessage(Message message) {

		INode[] nList = getNodes();
		TransportResult[] results = new TransportResult[nList.length];
		for (int i = 0; i < nList.length; i++) {
			INode node = nList[i];
			Response response = null;
			Throwable exception = null;
			if (node.getStatus() == NodeStatus.CONNECTED) {
				try {
					response = node.sendMessage(message);
				} catch (Throwable e) {
					exception = e;
				}
			}
			results[i] = new TransportResult(response, node, exception);
		}
		
		return results;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.cluster.ICluster#registerClusterService(java.lang.String, de.xwic.appkit.cluster.IClusterService)
	 */
	@Override
	public void registerClusterService(String name, IClusterService service) {
		clServiceManager.registerClusterService(name, service);		
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.cluster.ICluster#getClusterService(java.lang.String)
	 */
	@Override
	public IClusterService getClusterService(String name) {
		return clServiceManager.getClusterService(name);
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.cluster.ICluster#getInstalledClusterServiceNames()
	 */
	@Override
	public Collection<String> getInstalledClusterServiceNames() {
		return clServiceManager.getInstalledClusterServiceNames();
	}


	/**
	 * @return the clServiceManager
	 */
	public ClusterServiceManager getClusterServiceManager() {
		return clServiceManager;
	}
	
}
