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

import de.xwic.appkit.core.cluster.NodeAddress;

/**
 * @author lippisch
 *
 */
public class NodeInfo implements Serializable {

	private String name;
	private NodeAddress nodeAddress;
	/**
	 * @param name
	 * @param nodeAddress
	 */
	public NodeInfo(String name, NodeAddress nodeAddress) {
		super();
		this.name = name;
		this.nodeAddress = nodeAddress;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return the nodeAddress
	 */
	public NodeAddress getNodeAddress() {
		return nodeAddress;
	}
	
	

}
