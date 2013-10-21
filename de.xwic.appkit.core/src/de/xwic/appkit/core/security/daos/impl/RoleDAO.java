/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.security.daos.impl.RoleDAO
 * Created on 02.08.2005
 *
 */
package de.xwic.appkit.core.security.daos.impl;

import de.xwic.appkit.core.dao.AbstractDAO;
import de.xwic.appkit.core.dao.DataAccessException;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.security.IRole;
import de.xwic.appkit.core.security.daos.IRoleDAO;
import de.xwic.appkit.core.security.impl.Role;

/**
 * @author Florian Lippisch
 */
public class RoleDAO extends AbstractDAO<IRole, Role> implements IRoleDAO {

	/**
	 *
	 */
	public RoleDAO() {
		super(IRole.class, Role.class);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.DAO#createEntity()
	 */
	@Override
	public IEntity createEntity() throws DataAccessException {
		return new Role();
	}

}
