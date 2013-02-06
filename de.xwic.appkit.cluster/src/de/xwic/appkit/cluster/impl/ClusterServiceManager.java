/**
 * 
 */
package de.xwic.appkit.cluster.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.xwic.appkit.cluster.CommunicationException;
import de.xwic.appkit.cluster.IClusterService;
import de.xwic.appkit.cluster.INode;
import de.xwic.appkit.cluster.INode.NodeStatus;
import de.xwic.appkit.cluster.Message;
import de.xwic.appkit.cluster.Response;

/**
 * Handles all cluster service related activities.
 * 
 * @author lippisch
 */
public class ClusterServiceManager {

	public enum ClusterServiceStatus {
		NO_SUCH_SERVICE,
		ACTIVE_MASTER,
		ACTIVE_SLAVE
	}
	
	private final static Log log = LogFactory.getLog(ClusterServiceManager.class);
	
	private Map<String, ClusterServiceWrapper> services = new HashMap<String, ClusterServiceWrapper>();
	private Cluster cluster;

	/**
	 * @param cluster
	 */
	public ClusterServiceManager(Cluster cluster) {
		this.cluster = cluster;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.cluster.ICluster#registerClusterService(java.lang.String, de.xwic.appkit.cluster.IClusterService)
	 */
	public void registerClusterService(String name, IClusterService service) {
		
		synchronized (services) {
			if (services.containsKey(name)) {
				throw new IllegalArgumentException("A IClusterService with the name '" + name + "' is already registered.");
			}
			service.onRegistration(name, cluster);
			
			ClusterServiceWrapper csWrapper = new ClusterServiceWrapper(service);
			
			services.put(name, csWrapper);
			
			// now that the service is registered, we need to check if there are other nodes
			// running the same service. If a node is found, the one with the higher priority
			// will host the master service. This might lead to handing over (surrendering)
			// the master on the remote node
			
			List<RemoteServiceData> rsList = csWrapper.getRemoteServices();
			
			INode[] nodes = cluster.getNodes();
			for (INode node : nodes) {
				if (node.getStatus() == NodeStatus.CONNECTED) {
					Message msg = new Message(ClusterNodeClientProtocol.CMD_GET_SERVICE_STATUS, name);
					try {
						Response resp = node.sendMessage(msg);
						if (resp.isSuccess()) {
							ClusterServiceStatus status = ClusterServiceStatus.valueOf(resp.getReason());
							rsList.add(new RemoteServiceData(node, status));
						} else {
							log.warn("Can not retrieve remote service status : " + resp.getReason());
						}
					} catch (CommunicationException ce) {
						log.warn("Error retrieving service status from node " + node, ce);
					}
				}
			}
			
			// now that we know about all the other nodes, we can search for the master...
			boolean foundOneMaster = false;
			for (RemoteServiceData rsd : rsList) {
				if (rsd.getServiceStatus() == ClusterServiceStatus.ACTIVE_MASTER) {
					foundOneMaster = true;
					log.debug("Found a remote service that is currently master with MasterPriority (" + rsd.getRemoteNode().getMasterPriority() + ")");
					if (rsd.getRemoteNode().getMasterPriority() < cluster.getConfig().getMasterPriority()) {
						// this node has a higher master priority. This means that we have to take over the master role
						// from the remote node.
						
						obtainMasterRole(name, service, rsd.getRemoteNode());
						
					} else {
						csWrapper.setMasterNode(rsd.getRemoteNode());
					}
				}
			}
			
			if (!foundOneMaster) {
				// there is no master service on the cluster. This means that the current service will take this role
				service.obtainMasterRole(null); 
			}
			
		}
		
	}
	
	/**
	 * @param name
	 * @param service
	 * @param remoteNode
	 */
	private void obtainMasterRole(String name, IClusterService service, INode remoteNode) {

		Serializable remoteServiceData = null;
		Message msg = new Message(ClusterNodeClientProtocol.CMD_TAKE_MASTER_ROLE, name);
		try {
			Response resp = remoteNode.sendMessage(msg);
			if (resp.isSuccess()) {
				remoteServiceData = resp.getData();
			}
		} catch (CommunicationException e) {
			// taking the master role failed. Did the remote node go down in the meantime?
			// This node will take the new role regardless of this...
			log.warn("Error during 'Take Master Role' for service '" + name + "'.", e);
		}

		service.obtainMasterRole(remoteServiceData);
		
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.cluster.ICluster#getClusterService(java.lang.String)
	 */
	public IClusterService getClusterService(String name) {
		
		synchronized (services) {
			if (!services.containsKey(name)) {
				throw new IllegalArgumentException("A IClusterService with the name '" + name + "' is not registered.");
			}
			return services.get(name).getClusterService();
		}
		
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.cluster.ICluster#getInstalledClusterServiceNames()
	 */
	public Collection<String> getInstalledClusterServiceNames() {
		return services.keySet();
	}

	/**
	 * Prepare handover of the master role to a remote service.
	 * @param serviceName
	 * @param remoteNode
	 * @return
	 */
	public Serializable takeMasterRole(String serviceName, ClusterNode remoteNode) {

		Serializable data = null;
		
		synchronized (services) {

			ClusterServiceWrapper csWrapper = services.get(serviceName);
			if (csWrapper != null) {
				
				IClusterService service = csWrapper.getClusterService();
				if (service.isMaster()) {
					data = service.surrenderMasterRole();
					
					boolean found = false;
					List<RemoteServiceData> remoteServices = csWrapper.getRemoteServices();
					for (RemoteServiceData rsd : remoteServices) {
						if (rsd.getRemoteNode().sameNode(remoteNode)) {
							rsd.setServiceStatus(ClusterServiceStatus.ACTIVE_MASTER);
							found = true;
							break;
						}
					}
					if (!found) {
						// this is a new node, add it to the list
						remoteServices.add(new RemoteServiceData(remoteNode, ClusterServiceStatus.ACTIVE_MASTER));
					}
					csWrapper.setMasterNode(remoteNode);
					
				}
				
			}
			
		}
		return data;
	}

	/**
	 * A new node was connected with this node. We need to sync the services with each other.
	 * @param node
	 */
	public void handleNewNode(INode node) {

		synchronized (services) {
			for (String name : getInstalledClusterServiceNames()) {
	
				ClusterServiceWrapper csWrapper = services.get(name);
				
				if (node.getStatus() == NodeStatus.CONNECTED) {
					Message msg = new Message(ClusterNodeClientProtocol.CMD_GET_SERVICE_STATUS, name);
					try {
						Response resp = node.sendMessage(msg);
						if (resp.isSuccess()) {
							ClusterServiceStatus status = ClusterServiceStatus.valueOf(resp.getReason());
							
							if (status == ClusterServiceStatus.ACTIVE_MASTER) {
								// the remote service is currently a master
								
								IClusterService clusterService = csWrapper.getClusterService();
								if (clusterService.isMaster()) { // only try if we are already a master. If we are a slave, another node exists with a higher prio 
									if (node.getMasterPriority() < cluster.getConfig().getMasterPriority()) {
										// this node has a higher master priority. This means that we have to take over the master role
										// from the remote node.
										
										obtainMasterRole(name, clusterService, node);
									} else {
										// the other node has a higher priority -> let's surrender!
										
										Serializable data = clusterService.surrenderMasterRole();
										Message msgSur = new Message(ClusterNodeClientProtocol.CMD_SURRENDER_SERVICE, name, data);
										node.sendMessage(msgSur);
										csWrapper.setMasterNode(node);
										
									}
								}

								
							} else if (status == ClusterServiceStatus.ACTIVE_SLAVE) {
								// the remote service is currently a slave. This means that it knows a node
								// with a higher priority. We do not have to do anything in that case.
							}
							
						} else {
							log.warn("Can not retrieve remote service status : " + resp.getReason());
						}
					} catch (CommunicationException ce) {
						log.warn("Error retrieving service status from node " + node, ce);
					}
				}
			}
		}
		
		
	}

	/**
	 * A remote service who was a master has surrendered in favor of this nodes service.
	 * @param serviceName
	 * @param remoteNode
	 * @param container
	 */
	public void remoteSurrenderService(String serviceName, ClusterNode remoteNode, Serializable container) {

		synchronized (services) {
			
			ClusterServiceWrapper csWrapper = services.get(serviceName);
			if (csWrapper != null) {
				
				IClusterService service = csWrapper.getClusterService();
				if (service.isMaster()) {
					
					service.obtainMasterRole(container);
					
				} else {
					
					log.warn("A remote node is trying to surrender, but this node is not a master.");
					
				}
				
			}
			
		}
		
	}

	
}
