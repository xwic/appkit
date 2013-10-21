/*
 * Copyright (c) 2009 Network Appliance, Inc.
 * All rights reserved.
 */

package de.xwic.appkit.core.model.daos.impl;

import de.xwic.appkit.core.dao.AbstractDAO;
import de.xwic.appkit.core.dao.DataAccessException;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.daos.IUserListProfileDAO;
import de.xwic.appkit.core.model.entities.IUserListProfile;
import de.xwic.appkit.core.model.entities.impl.UserListProfile;

/**
 *
 * @author Aron Cotrau
 */
public class UserListProfileDAO extends AbstractDAO<IUserListProfile, UserListProfile> implements IUserListProfileDAO {

	/**
	 *
	 */
	public UserListProfileDAO() {
		super(IUserListProfile.class, UserListProfile.class);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.DAO#createEntity()
	 */
	@Override
	public IEntity createEntity() throws DataAccessException {
		return new UserListProfile();
	}

}
