/*
 * Copyright (c) 2009 Network Appliance, Inc.
 * All rights reserved.
 */

package de.xwic.appkit.core.model.daos.impl;

import java.util.List;

import de.xwic.appkit.core.dao.AbstractDAO;
import de.xwic.appkit.core.dao.DataAccessException;
import de.xwic.appkit.core.dao.Entity;
import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.daos.IUserViewConfigurationDAO;
import de.xwic.appkit.core.model.entities.IMitarbeiter;
import de.xwic.appkit.core.model.entities.IUserViewConfiguration;
import de.xwic.appkit.core.model.entities.impl.UserViewConfiguration;
import de.xwic.appkit.core.model.queries.PropertyQuery;

/**
 * @author Adrian Ionescu
 */
public class UserViewConfigurationDAO extends AbstractDAO implements IUserViewConfigurationDAO {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.AbstractDAO#getEntityImplClass()
	 */
	@Override
	public Class<? extends Entity> getEntityImplClass() {
		return UserViewConfiguration.class;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.DAO#createEntity()
	 */
	@Override
	public IEntity createEntity() throws DataAccessException {
		return new UserViewConfiguration();
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.DAO#getEntityClass()
	 */
	@Override
	public Class<? extends IEntity> getEntityClass() {
		return IUserViewConfiguration.class;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.daos.IUserViewConfigurationDAO#getMainConfigurationForView(de.xwic.appkit.core.model.entities.IMitarbeiter, java.lang.String, java.lang.String)
	 */
	@Override
	public IUserViewConfiguration getMainConfigurationForView(IMitarbeiter owner, String entityClassName, String viewId) {
		PropertyQuery pq = new PropertyQuery();
		
		pq.addEquals("owner", owner);
		pq.addEquals("className", entityClassName);
		pq.addEquals("viewId", viewId);
		pq.addEquals("mainConfiguration", true);
		
		EntityList list = getEntities(null, pq);
		
		return list.size() > 0 ? (IUserViewConfiguration) list.get(0) : null;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.daos.IUserViewConfigurationDAO#getUserConfigurationsForView(de.xwic.appkit.core.model.entities.IMitarbeiter, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<IUserViewConfiguration> getUserConfigurationsForView(IMitarbeiter owner, String entityClassName, String viewId) {
		PropertyQuery pq = new PropertyQuery();
		
		pq.addEquals("owner", owner);
		pq.addEquals("className", entityClassName);
		pq.addEquals("viewId", viewId);
		
		pq.setSortField("name");
		pq.setSortDirection(PropertyQuery.SORT_DIRECTION_UP);
		
		return getEntities(null, pq);
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.daos.IUserViewConfigurationDAO#getPublicUserConfigurationsForView(de.xwic.appkit.core.model.entities.IMitarbeiter, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<IUserViewConfiguration> getPublicUserConfigurationsForView(IMitarbeiter currentUser, String entityClassName, String viewId) {
		PropertyQuery pq = new PropertyQuery();
		
		pq.addNotEquals("owner", currentUser);
		pq.addEquals("className", entityClassName);
		pq.addEquals("viewId", viewId);
		pq.addEquals("public", true);
		pq.addEquals("mainConfiguration", false); // the main config should never be public, but just in case
		
		pq.setSortField("name");
		pq.setSortDirection(PropertyQuery.SORT_DIRECTION_UP);
		
		return getEntities(null, pq);
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.daos.IUserViewConfigurationDAO#configNameExists(de.xwic.appkit.core.model.entities.IMitarbeiter, java.lang.String, java.lang.String, java.lang.String, int)
	 */
	@Override
	public boolean configNameExists(IMitarbeiter owner, String entityClassName, String viewId, String name, int currentId) {
		PropertyQuery pq = new PropertyQuery();
		
		pq.addEquals("owner", owner);
		pq.addEquals("className", entityClassName);
		pq.addEquals("viewId", viewId);
		pq.addEquals("name", name);
		pq.addNotEquals("id", currentId);
		pq.addNotEquals("mainConfiguration", true);

		return !getEntities(null, pq).isEmpty();
	}
}
