/*
 * Copyright (c) 2009 Network Appliance, Inc.
 * All rights reserved.
 */

package de.xwic.appkit.core.model.daos;

import java.util.List;

import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.model.entities.IMitarbeiter;
import de.xwic.appkit.core.model.entities.IUserViewConfiguration;

/**
 * @author Adrian Ionescu
 */
public interface IUserViewConfigurationDAO extends DAO {

	/**
	 * @param owner 
	 * @param entityClassName
	 * @param viewId
	 * @return
	 */
	public IUserViewConfiguration getMainConfigurationForView(IMitarbeiter owner, String entityClassName, String viewId);
	
	/**
	 * @param owner
	 * @param entityClassName
	 * @param viewId
	 * @return
	 */
	public List<IUserViewConfiguration> getUserConfigurationsForView(IMitarbeiter owner, String entityClassName, String viewId);

	/**
	 * @param currentUser
	 * @param entityClassName
	 * @param viewId
	 * @return
	 */
	public List<IUserViewConfiguration> getPublicUserConfigurationsForView(IMitarbeiter currentUser, String entityClassName, String viewId);
	
	/**
	 * @param owner
	 * @param entityClassName
	 * @param viewId
	 * @param name
	 * @param currentId
	 * @return
	 */
	public boolean configNameExists(IMitarbeiter owner, String entityClassName, String viewId, String name, int currentId);
}
