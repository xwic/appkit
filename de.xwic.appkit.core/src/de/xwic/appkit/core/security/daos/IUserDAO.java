/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.security.daos.IUserDAO
 * Created on 02.08.2005
 *
 */
package de.xwic.appkit.core.security.daos;

import java.util.Set;

import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.security.IUser;
import de.xwic.appkit.core.security.ScopeActionKey;

/**
 * @author Florian Lippisch
 */
public interface IUserDAO extends DAO<IUser> {

	/**
	 * Build a list of all rights for the specified user.
	 * @param user
	 * @return
	 */
	public Set<ScopeActionKey> buildAllRights(IUser user);
	
}
