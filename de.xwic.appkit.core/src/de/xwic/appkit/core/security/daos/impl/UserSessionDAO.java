/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.security.daos.impl.UserDAO
 * Created on 02.08.2005
 *
 */
package de.xwic.appkit.core.security.daos.impl;

import java.util.Date;

import de.xwic.appkit.core.dao.AbstractDAO;
import de.xwic.appkit.core.dao.DataAccessException;
import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.dao.Limit;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.core.security.IUser;
import de.xwic.appkit.core.security.IUserSession;
import de.xwic.appkit.core.security.daos.IUserSessionDAO;
import de.xwic.appkit.core.security.impl.UserSession;
import de.xwic.appkit.core.util.DigestTool;

/**
 * @author Florian Lippisch
 */
public class UserSessionDAO extends AbstractDAO<IUserSession, UserSession> implements IUserSessionDAO {
	/**
	 *
	 */
	public UserSessionDAO() {
		super(IUserSession.class, UserSession.class);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.DAO#createEntity()
	 */
	@Override
	public IEntity createEntity() throws DataAccessException {
		return new UserSession();
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.daos.IUserSessionDAO#getUserSession(java.lang.String)
	 */
	@Override
	public IUserSession getUserSession(String key) {

		PropertyQuery query = new PropertyQuery();
		query.addEquals("key", key);
		EntityList list = getEntities(new Limit(0, 1), query);
		if (list.size() > 0) {
			return (IUserSession) list.get(0);
		}
		return null;

	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.daos.IUserSessionDAO#createUserSession(de.xwic.appkit.core.security.IUser)
	 */
	@Override
	public IUserSession createUserSession(IUser user) {

		IUserSession us = (IUserSession)createEntity();
		us.setLastAccess(new Date());
		us.setUsername(user.getLogonName());

		String rawInfo = System.currentTimeMillis() + user.getLogonName().hashCode() + "_" + user.getLogonName() + "_xwic";
		us.setKey(DigestTool.encodeString(rawInfo));

		update(us);
		return us;
	}
}
