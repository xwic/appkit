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
package de.xwic.appkit.core.cluster;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Test implementation of a service that returns a number in sequence unique across the cluster.
 * @author lippisch
 */
public class TestClusterService extends AbstractClusterService implements IClusterService, ITestClusterService {

	private final static Log log = LogFactory.getLog(TestClusterService.class);
	
	private long nextNumber = 0;
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.cluster.AbstractClusterService#onRegistration(java.lang.String, de.xwic.appkit.cluster.ICluster, de.xwic.appkit.cluster.IClusterServiceHandler)
	 */
	@Override
	public void onRegistration(String name, ICluster cluster, IClusterServiceHandler csHandler) {
		super.onRegistration(name, cluster, csHandler);
		
		cluster.addEventListener(new ClusterEventListener() {
			
			@Override
			public void receivedEvent(ClusterEvent event) {
				
				if ("numberSync".equals(event.getName())) {
					Long nn = (Long)event.getData();
					
					// Make sure to not accept synchronization from other nodes if we are the master node. It may otherwise
					// happen that when a new node enters the cluster, that it acts as a master before the synchronization is
					// completed. In such a case, events might be received from a pseudo-master instance that we need to
					// ignore to not get out of sync
					if (!isMaster()) {
						//log.debug("Sync received - next number: " + nn);
						nextNumber = nn;
					}
				}
			}
		}, getClass().getName());
		
	}
	
	
	/**
	 * Returns the next number in the sequence.
	 * @return
	 */
	public long takeNextNumber() {

		long result = -1;
		if (isMaster()) {
		
			result = nextNumber;
			nextNumber++;
			
			try {
				// send asynchronous event to other nodes to update the internal nextNumber value.
				// This is required to make sure all nodes "know" what the next number would be in case
				// the current master is going down and another node needs to take over and continue
				// By sending the event asynchronous, we accept the risk that the number returned is used
				// before other nodes have been updated. If asyncronous mode is turned off, it is ensured
				// that all nodes are updated before the nubmer is used, but it also slows down the process
				// as the user has to wait before the next number can be used.
				
				cluster.sendEvent(new ClusterEvent(getClass().getName(), "numberSync", new Long(nextNumber)), true);
			} catch (EventTimeOutException e) {
				log.warn("Error distributing numberSync event", e);
			}
			
		} else {
			
			//obtain next number from master
			IRemoteService rsMaster = csHandler.getMasterService();
			if (rsMaster != null) {
				Long num;
				try {
					num = (Long)rsMaster.invokeMethod("takeNextNumber", null);
				} catch (CommunicationException e) {
					
					throw new IllegalStateException("Retrieving number from remote master failed.", e);
					
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
		
		boolean alreadyMaster = isMaster();
		
		super.obtainMasterRole(remoteMasterData);
		
		// only "copy" the data from the old master if we obtain the master role within the cluster.
		// if we are already master, we should ignore the remote data as our is the most actual
		// information
		
		if (remoteMasterData != null && !alreadyMaster) {
			nextNumber = (Long)remoteMasterData;
		}
		
	}
	
}
