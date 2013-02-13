/**
 * 
 */
package de.xwic.appkit.cluster;

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
