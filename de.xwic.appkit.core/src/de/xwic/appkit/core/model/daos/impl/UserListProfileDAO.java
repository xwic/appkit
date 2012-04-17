/*
 * Copyright (c) 2009 Network Appliance, Inc.
 * All rights reserved.
 */

package de.xwic.appkit.core.model.daos.impl;

import de.xwic.appkit.core.dao.AbstractDAO;
import de.xwic.appkit.core.dao.DataAccessException;
import de.xwic.appkit.core.dao.Entity;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.daos.IUserListProfileDAO;
import de.xwic.appkit.core.model.entities.IUserListProfile;
import de.xwic.appkit.core.model.entities.impl.UserListProfile;

/**
 * 
 * @author Aron Cotrau
 */
public class UserListProfileDAO extends AbstractDAO implements IUserListProfileDAO {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.AbstractDAO#getEntityImplClass()
	 */
	@Override
	public Class<? extends Entity> getEntityImplClass() {
		return UserListProfile.class;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.DAO#createEntity()
	 */
	@Override
	public IEntity createEntity() throws DataAccessException {
		return new UserListProfile();
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.DAO#getEntityClass()
	 */
	@Override
	public Class<? extends IEntity> getEntityClass() {
		return IUserListProfile.class;
	}

}
