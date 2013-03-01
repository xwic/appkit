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
