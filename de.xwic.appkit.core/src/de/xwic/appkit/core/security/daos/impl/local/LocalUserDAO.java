/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.model.daos.impl.local.LocalUnternehmenDAO
 * Created on 03.08.2005
 *
 */
package de.xwic.appkit.core.security.daos.impl.local;

import de.xwic.appkit.core.dao.DataAccessException;
import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.security.IUser;
import de.xwic.appkit.core.security.daos.impl.UserDAO;
import de.xwic.appkit.core.security.queries.UniqueUserQuery;

/**
 * Server DAO for the User. <p>
 * 
 * @author Florian Lippisch
 */
public class LocalUserDAO extends UserDAO {
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.AbstractDAO#update(de.xwic.appkit.core.dao.IEntity)
	 */
	public void update(IEntity entity) throws DataAccessException {
		
		IUser newUser = (IUser) entity;
		EntityList erg = getEntities(null, new UniqueUserQuery(newUser));
		
		if (erg.size() == 0) {
			super.update(entity);
		} else {
			throw new DataAccessException("User mit dem LogonNamen: \"" + newUser.getName() + "\" bereits vorhanden!");
		}
	}

}
