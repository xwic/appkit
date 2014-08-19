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
import de.xwic.appkit.core.security.IActionSet;
import de.xwic.appkit.core.security.impl.ActionSet;
import de.xwic.appkit.core.security.queries.UniqueActionSetQuery;

/**
 * Query Resolver for ActionSet uniquetest. <p>
 *  
 * @author Florian Lippisch
 */
public class UniqueActionSetResolver implements IEntityQueryResolver {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.IEntityQueryResolver#resolve(de.xwic.appkit.core.dao.EntityQuery)
	 */
	public Object resolve(Class<? extends Object> entityClass, EntityQuery entityquery, boolean justCount) {
		
		UniqueActionSetQuery query = (UniqueActionSetQuery)entityquery;
		IActionSet actionSet = query.getActionSet();
		
		Session session = HibernateUtil.currentSession();
		StringBuffer sb = new StringBuffer();
		if (justCount) {
			sb.append("select count(*) ");
		}
		sb.append("from ")
		  .append(ActionSet.class.getName())
		  .append(" AS obj where ")
		  .append("obj.id != ? AND ")
		  .append("obj.name LIKE ?");

		Query q = session.createQuery(sb.toString());
		q.setInteger(0, actionSet.getId());
		q.setString(1, actionSet.getName());
		
		return q;
		
	}

}
