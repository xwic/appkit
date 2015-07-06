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
package de.xwic.appkit.core;

import de.xwic.appkit.core.dao.DAOFactory;
import de.xwic.appkit.core.dao.DAOProvider;

/**
 * From this configuration you can create a DAOFactory for the system. <p>
 * 
 * @author Ronny Pfretzschner
 *
 */
public class CommonConfiguration {

	/** config key rolle betreuer UN_CM */
	public final static String CONF_PROP_ROLLE_UN_CM_BETREUER = "ROLLE.UN.CM.BETREUER";
	
	/**
	 * Creates a new DAOFactory for the System. <p>
	 * 
	 * @param provider the DAOProvider
	 * @param useLocalDao true, if localDao should be used
	 * @return a new DAOFactory
	 */
	public static DAOFactory createCommonDaoFactory(DAOProvider provider, boolean useLocalDao) {
		return new DefaultDAOFactory(provider, useLocalDao);
	}
	
	/**
	 * Creates a new DAOFactory for the System. <p>
	 * 
	 * Uses LocalDaos per default.
	 * 
	 * @param provider the DAOProvider
	 * @return a new DAOFactory
	 */
	public static DAOFactory createCommonDaoFactory(DAOProvider provider) {
		return createCommonDaoFactory(provider, true);
	}
	
}
