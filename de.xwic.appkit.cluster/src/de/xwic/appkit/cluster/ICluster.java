/**
 * 
 */
package de.xwic.appkit.cluster;

import java.util.List;

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
	public abstract List<INode> getNodes();

}