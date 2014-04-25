/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.security.daos.impl.RoleDAO
 * Created on 02.08.2005
 *
 */
package de.xwic.appkit.core.security.daos.impl;

import de.xwic.appkit.core.dao.AbstractDAO;
import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.dao.Limit;
import de.xwic.appkit.core.model.queries.PropertyQuery;
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
	 * @see de.xwic.appkit.core.security.daos.IRoleDAO#getRoleByName(java.lang.String)
	 */
	@Override
	public IRole getRoleByName(String roleName) {
		
		PropertyQuery pq = new PropertyQuery();
		pq.addEquals("name", roleName);
		
		EntityList<IRole> list = getEntities(new Limit(0,1), pq);
		
		if (!list.isEmpty()) {
			return list.get(0);
		}
		
		return null;
	}

}
