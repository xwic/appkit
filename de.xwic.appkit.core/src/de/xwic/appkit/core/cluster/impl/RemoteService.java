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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.xwic.appkit.core.cluster.ClusterServiceStatus;
import de.xwic.appkit.core.cluster.CommunicationException;
import de.xwic.appkit.core.cluster.INode;
import de.xwic.appkit.core.cluster.IRemoteService;
import de.xwic.appkit.core.cluster.Message;
import de.xwic.appkit.core.cluster.Response;

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
