/**
 * 
 */
package de.xwic.appkit.cluster.impl;

import de.xwic.appkit.cluster.INode;
import de.xwic.appkit.cluster.impl.ClusterServiceManager.ClusterServiceStatus;

/**
 * @author lippisch
 */
public class RemoteServiceData {

	private INode remoteNode;
	private ClusterServiceStatus serviceStatus;
	
	/**
	 * @param remoteNode
	 * @param serviceStatus
	 */
	public RemoteServiceData(INode remoteNode, ClusterServiceStatus serviceStatus) {
		super();
		this.remoteNode = remoteNode;
		this.serviceStatus = serviceStatus;
	}
	/**
	 * @return the serviceStatus
	 */
	public ClusterServiceStatus getServiceStatus() {
		return serviceStatus;
	}
	/**
	 * @param serviceStatus the serviceStatus to set
	 */
	public void setServiceStatus(ClusterServiceStatus serviceStatus) {
		this.serviceStatus = serviceStatus;
	}
	/**
	 * @return the remoteNode
	 */
	public INode getRemoteNode() {
		return remoteNode;
	}
	
	
	
}
