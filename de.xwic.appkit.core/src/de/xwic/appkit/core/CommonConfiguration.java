/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.CommonConfiguration
 * Created on 28.11.2006 by rpf
 *
 */
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
