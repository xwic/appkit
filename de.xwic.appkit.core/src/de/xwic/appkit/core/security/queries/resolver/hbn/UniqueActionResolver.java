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
import de.xwic.appkit.core.dao.IEntityQueryResolver;
import de.xwic.appkit.core.dao.impl.hbn.HibernateUtil;
import de.xwic.appkit.core.security.IAction;
import de.xwic.appkit.core.security.impl.Action;
import de.xwic.appkit.core.security.queries.UniqueActionQuery;

/**
 * Query Resolver for Action uniquetest. <p>
 *  
 * @author Florian Lippisch
 */
public class UniqueActionResolver implements IEntityQueryResolver {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.IEntityQueryResolver#resolve(de.xwic.appkit.core.dao.EntityQuery)
	 */
	public Object resolve(Class<? extends Object> entityClass, EntityQuery entityquery, boolean justCount) {
		
		UniqueActionQuery query = (UniqueActionQuery)entityquery;
		IAction action = query.getAction();
		
		Session session = HibernateUtil.currentSession();
		StringBuffer sb = new StringBuffer();
		if (justCount) {
			sb.append("select count(*) ");
		}
		sb.append("from ")
		  .append(Action.class.getName())
		  .append(" AS obj where ")
		  .append("obj.id != ? AND ")
		  .append("obj.name LIKE ?");

		Query q = session.createQuery(sb.toString());
		q.setInteger(0, action.getId());
		q.setString(1, action.getName());
		
		return q;
		
	}

}
