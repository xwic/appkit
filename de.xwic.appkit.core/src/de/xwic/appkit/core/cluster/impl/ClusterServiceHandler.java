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
package de.xwic.appkit.core.cluster.impl;

import java.util.ArrayList;
import java.util.List;

import de.xwic.appkit.core.cluster.IClusterService;
import de.xwic.appkit.core.cluster.IClusterServiceHandler;
import de.xwic.appkit.core.cluster.IRemoteService;

/**
 * @author lippisch
 *
 */
public class ClusterServiceHandler implements IClusterServiceHandler {

	private IClusterService service = null;
	private IRemoteService masterService = null;
	private List<IRemoteService> remoteServices = new ArrayList<IRemoteService>();
	
	/**
	 * @param service
	 */
	public ClusterServiceHandler(IClusterService service) {
		super();
		this.service = service;
	}

	/**
	 * @return the remoteServices
	 */
	public List<IRemoteService> getRemoteServices() {
		return remoteServices;
	}

	/**
	 * @return the service
	 */
	public IClusterService getClusterService() {
		return service;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.cluster.IClusterServiceHandler#getMasterService()
	 */
	@Override
	public IRemoteService getMasterService() {
		return masterService;
	}

	/**
	 * @param masterService the masterService to set
	 */
	public void setMasterService(IRemoteService masterService) {
		this.masterService = masterService;
	}
	
	
}
