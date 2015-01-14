/**
 * 
 */
package de.xwic.appkit.core.cluster;

import java.io.Serializable;


/**
 * Abstract implementation of the IClusterService interface.
 * @author lippisch
 */
public abstract class AbstractClusterService implements IClusterService {

	protected String myName = null;
	protected ICluster cluster;

	protected boolean master = false;
	protected IClusterServiceHandler csHandler;
	
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.cluster.IClusterService#onRegistration(java.lang.String, de.xwic.appkit.cluster.ICluster, de.xwic.appkit.cluster.impl.ClusterServiceHandler)
	 */
	@Override
	public void onRegistration(String name, ICluster cluster, IClusterServiceHandler csHandler) {
		this.myName = name;
		this.cluster = cluster;
		this.csHandler = csHandler;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.cluster.IClusterService#isMaster()
	 */
	@Override
	public boolean isMaster() {
		return master;
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.cluster.IClusterService#surrenderMasterRole()
	 */
	@Override
	public Serializable surrenderMasterRole() {
		master = false;
		return null;
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.cluster.IClusterService#obtainMasterRole(java.io.Serializable)
	 */
	@Override
	public void obtainMasterRole(Serializable remoteMasterData) {
		
		master = true;
		
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.cluster.IClusterService#isSurrenderServiceByPrio()
	 */
	@Override
	public boolean isSurrenderServiceByPrio() {
		return true;
	}
}
