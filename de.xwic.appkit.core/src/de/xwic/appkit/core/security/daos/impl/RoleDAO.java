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
import de.xwic.appkit.core.dao.Entity;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.security.IRole;
import de.xwic.appkit.core.security.daos.IRoleDAO;
import de.xwic.appkit.core.security.impl.Role;

/**
 * @author Florian Lippisch
 */
public class RoleDAO extends AbstractDAO implements IRoleDAO {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.DAO#createEntity()
	 */
	public IEntity createEntity() throws DataAccessException {
		return new Role();
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.DAO#getEntityClass()
	 */
	public Class<? extends IEntity> getEntityClass() {
		return IRole.class;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.AbstractDAO#getEntityImplClass()
	 */
	public Class<? extends Entity> getEntityImplClass() {
		return Role.class;
	}

}
