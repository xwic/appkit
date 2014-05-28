/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.model.queries.resolver.hbn.UniqueUNQueryResolver
 * Created on 03.08.2005
 *
 */
package de.xwic.appkit.core.security.queries.resolver.hbn;

import org.hibernate.Query;
import org.hibernate.Session;

import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.dao.EntityQueryAdapter;
import de.xwic.appkit.core.dao.impl.hbn.HibernateUtil;
import de.xwic.appkit.core.security.IRole;
import de.xwic.appkit.core.security.impl.Role;
import de.xwic.appkit.core.security.queries.UniqueRoleQuery;

/**
 * Query Resolver for Role uniquetest. <p>
 *  
 * @author Florian Lippisch
 */
public class UniqueRoleResolver extends EntityQueryAdapter {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.IEntityQueryResolver#resolve(de.xwic.appkit.core.dao.EntityQuery)
	 */
	public Object resolve(Class<? extends Object> entityClass, EntityQuery entityquery, boolean justCount) {
		
		UniqueRoleQuery query = (UniqueRoleQuery)entityquery;
		IRole role = query.getRole();
		
		Session session = HibernateUtil.currentSession();
		StringBuffer sb = new StringBuffer();
		if (justCount) {
			sb.append("select count(*) ");
		}
		sb.append("from ")
		  .append(Role.class.getName())
		  .append(" AS obj where ")
		  .append("obj.id != ? AND ")
		  .append("obj.name LIKE ?");

		Query q = session.createQuery(sb.toString());
		q.setInteger(0, role.getId());
		q.setString(1, role.getName());
		
		return q;
		
	}

}
