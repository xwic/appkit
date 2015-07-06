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

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.xwic.appkit.core.cluster.ClusterServiceStatus;
import de.xwic.appkit.core.cluster.CommunicationException;
import de.xwic.appkit.core.cluster.IClusterService;
import de.xwic.appkit.core.cluster.INode;
import de.xwic.appkit.core.cluster.INode.NodeStatus;
import de.xwic.appkit.core.cluster.IRemoteService;
import de.xwic.appkit.core.cluster.Message;
import de.xwic.appkit.core.cluster.RemoteInvokationException;
import de.xwic.appkit.core.cluster.Response;

/**
 * Handles all cluster service related activities.
 * 
 * @author lippisch
 */
public class ClusterServiceManager {

	private final static Log log = LogFactory.getLog(ClusterServiceManager.class);
	
	private Map<String, ClusterServiceHandler> services = new HashMap<String, ClusterServiceHandler>();
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
			
			ClusterServiceHandler csHandler = new ClusterServiceHandler(service);
			
			service.onRegistration(name, cluster, csHandler);
			services.put(name, csHandler);
			
			// now that the service is registered, we need to check if there are other nodes
			// running the same service. If a node is found, the one with the higher priority
			// will host the master service. This might lead to handing over (surrendering)
			// the master on the remote node
			
			List<IRemoteService> rsList = csHandler.getRemoteServices();
			
			INode[] nodes = cluster.getNodes();
			for (INode node : nodes) {
				if (node.getStatus() == NodeStatus.CONNECTED) {
					Message msg = new Message(ClusterNodeClientProtocol.CMD_GET_SERVICE_STATUS, name);
					try {
						Response resp = node.sendMessage(msg);
						if (resp.isSuccess()) {
							ClusterServiceStatus status = ClusterServiceStatus.valueOf(resp.getReason());
							rsList.add(new RemoteService(node, name, status));
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
			for (IRemoteService rsd : rsList) {
				if (rsd.getServiceStatus() == ClusterServiceStatus.ACTIVE_MASTER) {
					foundOneMaster = true;
					log.debug("Found a remote service that is currently master with MasterPriority (" + rsd.getNode().getMasterPriority() + ")");
					if (rsd.getNode().getMasterPriority() < cluster.getConfig().getMasterPriority()) {
						// this node has a higher master priority. This means that we have to take over the master role
						// from the remote node.
						
						obtainMasterRole(name, service, rsd.getNode());
						
					} else {
						csHandler.setMasterService(rsd);
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

			ClusterServiceHandler csWrapper = services.get(serviceName);
			if (csWrapper != null) {
				
				IRemoteService masterService = null;
				IClusterService service = csWrapper.getClusterService();
				if (service.isMaster()) {
					data = service.surrenderMasterRole();
					
					boolean found = false;
					List<IRemoteService> remoteServices = csWrapper.getRemoteServices();
					for (IRemoteService rservice : remoteServices) {
						RemoteService rsd = (RemoteService)rservice;
						if (rsd.getNode().sameNode(remoteNode)) {
							rsd.setServiceStatus(ClusterServiceStatus.ACTIVE_MASTER);
							masterService = rsd;
							found = true;
							break;
						}
					}
					if (!found) {
						// this is a new node, add it to the list
						RemoteService rs = new RemoteService(remoteNode, serviceName, ClusterServiceStatus.ACTIVE_MASTER);
						remoteServices.add(rs);
						masterService = rs;
					}
					
					csWrapper.setMasterService(masterService);
					
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
	
				ClusterServiceHandler csWrapper = services.get(name);
				
				if (node.getStatus() == NodeStatus.CONNECTED) {
					Message msg = new Message(ClusterNodeClientProtocol.CMD_GET_SERVICE_STATUS, name);
					try {
						Response resp = node.sendMessage(msg);
						if (resp.isSuccess()) {
							ClusterServiceStatus status = ClusterServiceStatus.valueOf(resp.getReason());
							
							IRemoteService rs = new RemoteService(node, name, status);
							csWrapper.getRemoteServices().add(rs);
							
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
										csWrapper.setMasterService(rs);
										
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
			
			ClusterServiceHandler csWrapper = services.get(serviceName);
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

	/**
	 * @param serviceName
	 * @param methodName
	 * @param container
	 * @return
	 * @throws RemoteInvokationException 
	 */
	public Response invokeService(String serviceName, String methodName, Serializable[] arguments) throws RemoteInvokationException {

		ClusterServiceHandler csWrapper = services.get(serviceName);
		IClusterService clusterService = csWrapper.getClusterService();

		// find the best matching method for the arguments given
		Method bestMatch = null;
		int matchIdx = 0;
		for (Method m : clusterService.getClass().getMethods()) {
			
			if (m.getName().equals(methodName)) { // found a method with the same name
				// check which arguments match best
				Class<?>[] paramTypes = m.getParameterTypes();
				
				if (paramTypes.length == 0 && (arguments == null || arguments.length == 0)) {
					bestMatch = m;
					matchIdx = 9999;
					break; // there will be no better match
				} else if (arguments != null && paramTypes.length == arguments.length) {
					// same length
					int mNum = 0;
					for (int i = 0; i < arguments.length; i++) {
						if (arguments[i] != null) {
							if (paramTypes[i].isAssignableFrom(arguments[i].getClass())) {
								mNum++;
							} else {
								mNum = -1;
								break;
							}
						} 
					}
					if (mNum != -1 && mNum > matchIdx) {
						matchIdx = mNum;
						bestMatch = m;
					}
					
				}
				
			}
			
		}
		
		if (bestMatch == null) {
			throw new IllegalArgumentException("No such method: " + methodName);
		}
		
		try {
			Object result = bestMatch.invoke(clusterService, (Object[])arguments);
			
			log.debug("Invoke of local '" + methodName + "' returned " + result);
			
			return new Response(true, null, (Serializable)result);
		} catch (Exception e) {
			throw new RemoteInvokationException("Error invoking method.", e);
		}
		
	}

	/**
	 * The given node was disconnected. Checks if the remote node is hosting the master for any
	 * service and if so, surrenders the master. 
	 * @param remoteNode
	 */
	void handleDisconnectedNode(INode remoteNode) {
		
		int myMasterPrio = cluster.getConfig().getMasterPriority();
		synchronized (services) {
			for (String name : getInstalledClusterServiceNames()) {
	
				ClusterServiceHandler csWrapper = services.get(name);
				IRemoteService rsMaster = csWrapper.getMasterService();
				if (rsMaster != null) {
					if (rsMaster.getNode().sameNode(remoteNode)) {
						// the node that went down was hosting the master service.
						// We need to check if we are the logical next master and if so, become master. If a different, active node
						// is the next master, change to the new master
						
						IRemoteService nextMaster = null;
						for (IRemoteService rs : csWrapper.getRemoteServices()) {
							if (rs.getServiceStatus() != ClusterServiceStatus.NO_SUCH_SERVICE &&
								rs.getNode().getStatus() == NodeStatus.CONNECTED) {
								// the node is active
								
								if (rs.getNode().getMasterPriority() > myMasterPrio && (nextMaster == null || rs.getNode().getMasterPriority() > nextMaster.getNode().getMasterPriority())) {
									nextMaster = rs;
								}
							}
						}
						
						if (nextMaster == null) {
							// either no other node exists or we have the highest master priority of all remaining active tasks
							csWrapper.getClusterService().obtainMasterRole(null);
							csWrapper.setMasterService(null);
						} else {
							// simply point to the newly identified master node. We assume that the other node
							// also experienced the connection loss to the old master, so the new master node will
							// obtain the master role by itself.
							((RemoteService)nextMaster).setServiceStatus(ClusterServiceStatus.ACTIVE_MASTER);
							csWrapper.setMasterService(nextMaster);
						}
						
					}
				}

			}
		}
		
	}

	
}
