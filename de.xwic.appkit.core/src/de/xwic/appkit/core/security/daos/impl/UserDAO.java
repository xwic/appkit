/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.security.daos.impl.UserDAO
 * Created on 02.08.2005
 *
 */
package de.xwic.appkit.core.security.daos.impl;

import de.xwic.appkit.core.dao.AbstractDAO;
import de.xwic.appkit.core.security.IUser;
import de.xwic.appkit.core.security.daos.IUserDAO;
import de.xwic.appkit.core.security.impl.User;

/**
 * @author Florian Lippisch
 */
public class UserDAO extends AbstractDAO<IUser, User> implements IUserDAO {

	/**
	 *
	 */
	public UserDAO() {
		super(IUser.class, User.class);
	}

}
