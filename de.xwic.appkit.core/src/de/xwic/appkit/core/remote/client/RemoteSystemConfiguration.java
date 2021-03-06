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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((apiSuffix == null) ? 0 : apiSuffix.hashCode());
		result = prime * result + ((remoteBaseUrl == null) ? 0 : remoteBaseUrl.hashCode());
		result = prime * result + ((remoteSystemId == null) ? 0 : remoteSystemId.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RemoteSystemConfiguration other = (RemoteSystemConfiguration) obj;
		if (apiSuffix == null) {
			if (other.apiSuffix != null)
				return false;
		} else if (!apiSuffix.equals(other.apiSuffix))
			return false;
		if (remoteBaseUrl == null) {
			if (other.remoteBaseUrl != null)
				return false;
		} else if (!remoteBaseUrl.equals(other.remoteBaseUrl))
			return false;
		if (remoteSystemId == null) {
			if (other.remoteSystemId != null)
				return false;
		} else if (!remoteSystemId.equals(other.remoteSystemId))
			return false;
		return true;
	}

}
