/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.security.daos.IRoleDAO
 * Created on 02.08.2005
 *
 */
package de.xwic.appkit.core.security.daos;

import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.security.IRole;

/**
 * @author Florian Lippisch
 */
public interface IRoleDAO extends DAO<IRole> {

	/**
	 * @param roleName
	 * @return
	 */
	public IRole getRoleByName(String roleName);
	
}
