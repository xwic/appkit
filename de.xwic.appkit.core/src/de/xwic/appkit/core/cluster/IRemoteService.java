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
 * Represents a remote cluster service. Used to invoke methods on the remote service.
 * 
 * @author lippisch
 */
public interface IRemoteService {

	/**
	 * Returns the status of the remote service.
	 * @return
	 */
	public ClusterServiceStatus getServiceStatus();
	
	/**
	 * Returns the node this remote service is pointing to.
	 * @return
	 */
	public INode getNode();
	
	/**
	 * Returns true if the remote service is the master.
	 * @return
	 */
	public boolean isMaster();
	
	/**
	 * Invoke a method on the remote service.
	 * @param method
	 * @param arguments
	 * @return
	 * @throws CommunicationException 
	 */
	public Serializable invokeMethod(String method, Serializable[] arguments) throws CommunicationException;
	
}
