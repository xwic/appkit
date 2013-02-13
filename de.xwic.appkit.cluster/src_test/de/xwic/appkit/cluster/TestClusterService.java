/**
 * 
 */
package de.xwic.appkit.cluster;

import java.io.Serializable;

/**
 * Test implementation of a service that returns a number in sequence unique across the cluster.
 * @author lippisch
 */
public class TestClusterService extends AbstractClusterService implements IClusterService, ITestClusterService {

	private long nextNumber = 0;
	
	
	/**
	 * Returns the next number in the sequence.
	 * @return
	 */
	public long takeNextNumber() {

		long result = -1;
		if (isMaster()) {
		
			result = nextNumber;
			nextNumber++;
			
		} else {
			
			//obtain next number from master
			IRemoteService rsMaster = csHandler.getMasterService();
			if (rsMaster != null) {
				Long num;
				try {
					num = (Long)rsMaster.invokeMethod("takeNextNumber", null);
				} catch (CommunicationException e) {
					
					throw new IllegalStateException("Retrieving number from remote master failed.");
					
				}
				result = num;
			}
			
		}
		
		return result;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.cluster.AbstractClusterService#surrenderMasterRole()
	 */
	@Override
	public Serializable surrenderMasterRole() {
		super.surrenderMasterRole();
		
		return new Long(nextNumber);
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.cluster.AbstractClusterService#obtainMasterRole(java.io.Serializable)
	 */
	@Override
	public void obtainMasterRole(Serializable remoteMasterData) {
		super.obtainMasterRole(remoteMasterData);
		
		if (remoteMasterData != null) {
			nextNumber = (Long)remoteMasterData;
		}
		
	}
	
}
