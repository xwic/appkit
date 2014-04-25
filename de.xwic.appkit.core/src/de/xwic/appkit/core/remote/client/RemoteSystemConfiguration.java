/**
 * 
 */
package de.xwic.appkit.core.remote.client;


/**
 * @author Adrian Ionescu
 */
public class RemoteSystemConfiguration {

	private String remoteBaseUrl;
	private String remoteSystemId;
	private String apiSuffix;
	
	/**
	 * @param remoteBaseUrl
	 * @param remoteSystemId
	 */
	public RemoteSystemConfiguration(String remoteBaseUrl, String remoteSystemId, String apiSuffix) {
		this.remoteBaseUrl = remoteBaseUrl;
		this.remoteSystemId = remoteSystemId;
		this.apiSuffix = apiSuffix;
	}

	/**
	 * @return the remoteBaseUrl
	 */
	public String getRemoteBaseUrl() {
		return remoteBaseUrl;
	}
	
	/**
	 * @param remoteBaseUrl the remoteBaseUrl to set
	 */
	public void setRemoteBaseUrl(String remoteBaseUrl) {
		this.remoteBaseUrl = remoteBaseUrl;
	}
	
	/**
	 * @return the remoteSystemId
	 */
	public String getRemoteSystemId() {
		return remoteSystemId;
	}
	
	/**
	 * @param remoteSystemId the remoteSystemId to set
	 */
	public void setRemoteSystemId(String remoteSystemId) {
		this.remoteSystemId = remoteSystemId;
	}

	/**
	 * @return the apiSuffix
	 */
	public String getApiSuffix() {
		return apiSuffix;
	}

	/**
	 * @param apiSuffix the apiSuffix to set
	 */
	public void setApiSuffix(String apiSuffix) {
		this.apiSuffix = apiSuffix;
	}
}
