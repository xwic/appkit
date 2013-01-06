/**
 * 
 */
package de.xwic.appkit.cluster;

import java.util.ArrayList;
import java.util.List;

/**
 * Initial configuration of the cluster.
 * @author lippisch
 *
 */
public class ClusterConfiguration {

	private int portNumber = 11150;
	private String clusterName = "Unnamed";
	private String nodeName = "Unnamed";
	
	private List<NodeAddress> knownNodes = new ArrayList<NodeAddress>();

	/**
	 * Add a known cluster node address. This is the list of initially known nodes. During runtime,
	 * the cluster might connect to additional nodes or disconnect if nodes become unavailable.
	 * @param address
	 */
	public void addKnownNode(NodeAddress nodeAddress) {
		knownNodes.add(nodeAddress);
	}
	
	/**
	 * @return the portNumber
	 */
	public int getPortNumber() {
		return portNumber;
	}

	/**
	 * @param portNumber the portNumber to set
	 */
	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}

	/**
	 * @return the clusterName
	 */
	public String getClusterName() {
		return clusterName;
	}

	/**
	 * @param clusterName the clusterName to set
	 */
	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}

	/**
	 * @return the knownNodes
	 */
	public List<NodeAddress> getKnownNodes() {
		return knownNodes;
	}

	/**
	 * @return the nodeName
	 */
	public String getNodeName() {
		return nodeName;
	}

	/**
	 * @param nodeName the nodeName to set
	 */
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	
}
