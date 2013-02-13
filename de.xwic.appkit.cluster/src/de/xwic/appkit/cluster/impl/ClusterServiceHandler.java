/**
 * 
 */
package de.xwic.appkit.cluster.impl;

import java.util.ArrayList;
import java.util.List;

import de.xwic.appkit.cluster.IClusterService;
import de.xwic.appkit.cluster.IClusterServiceHandler;
import de.xwic.appkit.cluster.IRemoteService;

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
