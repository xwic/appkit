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

import de.xwic.appkit.core.cluster.impl.Cluster;


/**
 * Provides access to the Cluster instance.
 * @author lippisch
 */
public class ClusterManager {

	private static ICluster instance = null;

	/**
	 * 
	 */
	private ClusterManager() {
	}

	
	/**
	 * Returns the single instance of the cluster. The method Cluster.init() must be invoked before 
	 * an instance can be accessed. 
	 * @return
	 */
	public static ICluster getCluster() {
		if (instance == null) {
			throw new IllegalStateException("The Cluster has not yet been initialized.");
		}
		return instance;
	}

	
	/**
	 * Initialize the cluster instance.
	 * @param config
	 */
	public static void init(ClusterConfiguration config) {
		
		if (instance != null) {
			throw new IllegalStateException("Cluster already initialized.");
		}
		Cluster c = new Cluster(config);
		c.initInternal();
		instance = c;
	}
}
