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
}
