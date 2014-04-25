/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.security.daos.IUserDAO
 * Created on 02.08.2005
 *
 */
package de.xwic.appkit.core.security.daos;

import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.security.IUser;
import de.xwic.appkit.core.security.IUserSession;

/**
 * @author Florian Lippisch
 */
public interface IUserSessionDAO extends DAO<IUserSession> {

	/**
	 * Returns the user session for the specified key.
	 * @param key
	 * @return
	 */
	public IUserSession getUserSession(String key);

	/**
	 * Create a new user session.
	 * @param user
	 * @return
	 */
	public IUserSession createUserSession(IUser user);

}
