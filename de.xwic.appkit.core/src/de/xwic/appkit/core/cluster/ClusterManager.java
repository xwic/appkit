/**
 * 
 */
package de.xwic.appkit.core.cluster;

import de.xwic.appkit.core.cluster.impl.Cluster;


/**
 * Provides access to the Cluster instance.
 * @author lippisch
 */
public class ClusterManager {

	private static ICluster instance = null;

	/**
	 * 
	 */
	private ClusterManager() {
	}

	
	/**
	 * Returns the single instance of the cluster. The method Cluster.init() must be invoked before 
	 * an instance can be accessed. 
	 * @return
	 */
	public static ICluster getCluster() {
		if (instance == null) {
			throw new IllegalStateException("The Cluster has not yet been initialized.");
		}
		return instance;
	}

	
	/**
	 * Initialize the cluster instance.
	 * @param config
	 */
	public static void init(ClusterConfiguration config) {
		
		if (instance != null) {
			throw new IllegalStateException("Cluster already initialized.");
		}
		Cluster c = new Cluster(config);
		c.initInternal();
		instance = c;
	}
}
