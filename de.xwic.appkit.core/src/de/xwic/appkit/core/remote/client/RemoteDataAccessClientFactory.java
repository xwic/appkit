/**
 * 
 */
package de.xwic.appkit.core.remote.client;


/**
 * @author Adrian Ionescu
 */
public class RemoteDataAccessClientFactory {

	/**
	 * @param remoteBaseUrl
	 * @param remoteSystemId
	 * @return
	 */
	public static IRemoteDataAccessClient getClient(RemoteSystemConfiguration config) {
		// default implementation		
		return new RemoteDataAccessClient(config);
	}
	
}
