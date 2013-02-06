/**
 * 
 */
package de.xwic.appkit.cluster.impl;

import java.util.ArrayList;
import java.util.List;

import de.xwic.appkit.cluster.IClusterService;
import de.xwic.appkit.cluster.INode;

/**
 * @author lippisch
 *
 */
public class ClusterServiceWrapper {

	private IClusterService service = null;
	private INode masterNode = null;
	private List<RemoteServiceData> remoteServices = new ArrayList<RemoteServiceData>();
	
	/**
	 * @param service
	 */
	public ClusterServiceWrapper(IClusterService service) {
		super();
		this.service = service;
	}

	/**
	 * @return the remoteServices
	 */
	public List<RemoteServiceData> getRemoteServices() {
		return remoteServices;
	}

	/**
	 * @return the service
	 */
	public IClusterService getClusterService() {
		return service;
	}

	/**
	 * @return the masterNode
	 */
	public INode getMasterNode() {
		return masterNode;
	}

	/**
	 * @param masterNode the masterNode to set
	 */
	public void setMasterNode(INode masterNode) {
		this.masterNode = masterNode;
	}
	
	
	
}
