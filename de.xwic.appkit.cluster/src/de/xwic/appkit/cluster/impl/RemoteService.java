/**
 * 
 */
package de.xwic.appkit.cluster.impl;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.xwic.appkit.cluster.ClusterServiceStatus;
import de.xwic.appkit.cluster.CommunicationException;
import de.xwic.appkit.cluster.INode;
import de.xwic.appkit.cluster.IRemoteService;
import de.xwic.appkit.cluster.Message;
import de.xwic.appkit.cluster.Response;

/**
 * @author lippisch
 */
public class RemoteService implements IRemoteService {

	private final static Log log = LogFactory.getLog(RemoteService.class);
	
	private INode remoteNode;
	private ClusterServiceStatus serviceStatus;
	private String serviceName;
	
	/**
	 * @param remoteNode
	 * @param serviceStatus
	 */
	public RemoteService(INode remoteNode, String serviceName, ClusterServiceStatus serviceStatus) {
		super();
		this.remoteNode = remoteNode;
		this.serviceName = serviceName;
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
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.cluster.IRemoteService#getNode()
	 */
	@Override
	public INode getNode() {
		return remoteNode;
	}
	/* (non-Javadoc)
	 * @see de.xwic.appkit.cluster.IRemoteService#isMaster()
	 */
	@Override
	public boolean isMaster() {
		return serviceStatus == ClusterServiceStatus.ACTIVE_MASTER;
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.cluster.IRemoteService#invokeMethod(java.lang.String, java.io.Serializable[])
	 */
	@Override
	public Serializable invokeMethod(String method, Serializable[] arguments) throws CommunicationException {
	
		log.debug("Invoke method '" + method + "' on node '" + remoteNode + "'");
		Response res = remoteNode.sendMessage(new Message(ClusterNodeClientProtocol.CMD_INVOKE_SERVICE, serviceName + ":" + method, arguments));
		if (res.isSuccess()) {
			return res.getData();
		} else {
			throw new CommunicationException("Invokation of service failed: " + res.getReason());
		}
	}
	
	
	
}
