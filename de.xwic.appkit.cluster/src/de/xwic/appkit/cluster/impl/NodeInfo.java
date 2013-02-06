/**
 * 
 */
package de.xwic.appkit.cluster.impl;

import java.io.Serializable;

import de.xwic.appkit.cluster.NodeAddress;

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
