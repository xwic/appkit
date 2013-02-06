/**
 * 
 */
package de.xwic.appkit.cluster;

import java.io.Serializable;

/**
 * Abstract implementation of the IClusterService interface.
 * @author lippisch
 */
public abstract class AbstractClusterService implements IClusterService {

	protected String myName = null;
	protected ICluster cluster;

	protected boolean master = false;
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.cluster.IClusterService#onRegistration(java.lang.String, de.xwic.appkit.cluster.ICluster)
	 */
	@Override
	public void onRegistration(String name, ICluster cluster) {
		this.myName = name;
		this.cluster = cluster;
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
}
