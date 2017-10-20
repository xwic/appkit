/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 * 
 * de.xwic.appkit.core.AppkitModelConfig 
 */
package de.xwic.appkit.core;

import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.model.daos.IEntityChangeLogDAO;
import de.xwic.appkit.core.model.daos.INewsDAO;
import de.xwic.appkit.core.model.daos.IQuickLaunchDAO;

/**
 * @author dotto
 *
 */
public class AppkitModelConfig {

	/**
	 * Returns the INewsDAO. 
	 * @return
	 */
	public static INewsDAO getNewsDAO() {
		return (INewsDAO)DAOSystem.getDAO(INewsDAO.class);
	}
	
	/**
	 * Returns the IQuickLaunchDAO.
	 * @return
	 */
	public static IQuickLaunchDAO getQuickLaunchDAO() {
		return (IQuickLaunchDAO)DAOSystem.getDAO(IQuickLaunchDAO.class);
	}
	
	
	/**
	 * @return
	 */
	public static IEntityChangeLogDAO getEntityChangeLogDAO() {
		return DAOSystem.getDAO(IEntityChangeLogDAO.class);
	}

	
}
