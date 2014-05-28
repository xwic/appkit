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
import de.xwic.appkit.core.security.impl.Right;
import de.xwic.appkit.core.security.queries.RightQuery;

/**
 * Query Resolver for Right query. <p>
 *  
 * @author Florian Lippisch
 */
public class RightQueryResolver extends EntityQueryAdapter {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.IEntityQueryResolver#resolve(de.xwic.appkit.core.dao.EntityQuery)
	 */
	public Object resolve(Class<? extends Object> entityClass, EntityQuery entityquery, boolean justCount) {
		
		RightQuery query = (RightQuery)entityquery;
		IRole role = query.getRole();
		
		Session session = HibernateUtil.currentSession();
		StringBuffer sb = new StringBuffer();
		if (justCount) {
			sb.append("select count(*) ");
		}
		sb.append("from ")
		  .append(Right.class.getName())
		  .append(" AS obj where ")
		  .append("obj.role = ?");

		Query q = session.createQuery(sb.toString());
		q.setInteger(0, role.getId());
		
		return q;
		
	}

}
