/**
 * 
 */
package de.xwic.appkit.core.cluster;

import java.util.List;


/**
 * @author lippisch
 *
 */
public interface IClusterServiceHandler {

	/**
	 * @return the masterNode
	 */
	public abstract IRemoteService getMasterService();

	/**
	 * Returns the list of RemoteService implementations.
	 * @return
	 */
	public abstract List<IRemoteService> getRemoteServices();
	
}