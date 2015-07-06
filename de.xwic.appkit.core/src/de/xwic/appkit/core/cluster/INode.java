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

import java.util.Date;


/**
 * A node within the cluster.
 * @author lippisch
 *
 */
public interface INode {
	
	enum NodeStatus {
		/** A connection has not yet been established, only partial data is available */
		NEW,			
		/** The node is connected to this instance */ 
		CONNECTED,
		/** The connection to this node was lost, the node is currently disconnected. The
		 * cluster will try to re-establish the connection. */
		DISCONNECTED,
		/** The connection was disconnected and will not be re-connected */
		DISABLED
	}
	
	
	/**
	 * @return the remoteHost
	 */
	public NodeAddress getAddress();

	/**
	 * Send a message to the node.
	 * @param message
	 * @return
	 * @throws CommunicationException
	 */
	public Response sendMessage(Message message) throws CommunicationException;

	/**
	 * Returns the node status.
	 * @return
	 */
	public NodeStatus getStatus();
	
	/**
	 * Returns the unique name of the node or <code>null</code> if the node is yet unknown (NEW).
	 * @return
	 */
	public String getName();
	
	/**
	 * @return the disconnectedSince
	 */
	public Date getDisconnectedSince();

	/**
	 * Compare if this node is the address as the other node.
	 * @param n
	 * @return
	 */
	public boolean sameNode(INode n);
	
	/**
	 * Returns the master priority of this node.
	 * @return
	 */
	public int getMasterPriority();

}
