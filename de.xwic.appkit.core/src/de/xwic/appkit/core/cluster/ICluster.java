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

import java.util.Collection;



/**
 * @author lippisch
 *
 */
public interface ICluster {

	/**
	 * Add an address to be connected to. The node controller will attempt to connect
	 * to the specified node.
	 * @param na
	 */
	public abstract void registerNode(NodeAddress na);

	/**
	 * @return the config
	 */
	public abstract ClusterConfiguration getConfig();

	/**
	 * @return the nodes
	 */
	public abstract INode[] getNodes();

	/**
	 * Send an event to all nodes.
	 * @param event
	 * @param asynchronous 	Set to true to return immediately or false to wait until the event was processed by ALL nodes.
	 * @throws EventTimeOutException 
	 */
	public abstract void sendEvent(ClusterEvent event, boolean asynchronous) throws EventTimeOutException;
	
	/**
	 * Send a message to all connected nodes. Returns an array with the result that equals the number of known nodes.
	 * @param message
	 * @return
	 */
	public abstract TransportResult[] sendMessage(Message message);
	
	/**
	 * Register an event listener for any event, regardless of the namespace.
	 * @param listener
	 */
	public abstract void addEventListener(ClusterEventListener listener);
	
	/**
	 * Register an event listener for a specific namespace only.
	 * @param listener
	 * @param namespace
	 */
	public abstract void addEventListener(ClusterEventListener listener, String namespace);
	
	/**
	 * Register a service under the specified name.
	 * @param name
	 * @param service
	 */
	public abstract void registerClusterService(String name, IClusterService service);
	
	
	/**
	 * Returns the cluster service with the specified name. Throws an IllegalArgumentException
	 * if no service with this name is installed.
	 * 
	 * @param name
	 * @return
	 */
	public abstract IClusterService getClusterService(String name);
	
	/**
	 * Returns the list of installed cluster services.
	 * @return
	 */
	public abstract Collection<String> getInstalledClusterServiceNames();
	
}