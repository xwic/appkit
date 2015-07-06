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
package de.xwic.appkit.core.cluster;

import java.io.Serializable;


/**
 * Cluster Services use the cluster messaging infrastructure to behave as one single service across
 * the entire cluster. There is always one master instance on one single node. As nodes get added
 * to the cluster, they sync up on services and if required, give up their master role if another
 * one with a higher priority exists.
 * 
 * 
 * @author lippisch
 */
public interface IClusterService {

	/**
	 * Invoked during the registration of the service on a cluster. If this method throws
	 * an exception, the service will not be registered.
	 * @param cluster
	 * @param csHandler 
	 */
	public void onRegistration(String name, ICluster cluster, IClusterServiceHandler csHandler);
	
	/**
	 * Returns true if this service instance is the master instance in the cluster.
	 * @return
	 */
	public boolean isMaster();
	
	/**
	 * The active node has been connected to another node that has the privilege to
	 * become or remain as master. The current instance must give up the Master flag
	 * and return any data that might be relevant for the new master. 
	 */
	public Serializable surrenderMasterRole();
	
	/**
	 * Invoked when the service is assigned the master role. Other service instances on other nodes
	 * surrender their role.
	 */
	public void obtainMasterRole(Serializable remoteMasterData);
	
}
