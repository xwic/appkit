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
import de.xwic.appkit.core.security.IRight;
import de.xwic.appkit.core.security.impl.Right;
import de.xwic.appkit.core.security.queries.UniqueRightQuery;

/**
 * Query Resolver for Right uniquetest. <p>
 *  
 * @author Florian Lippisch
 */
public class UniqueRightResolver extends EntityQueryAdapter {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.IEntityQueryResolver#resolve(de.xwic.appkit.core.dao.EntityQuery)
	 */
	public Object resolve(Class<? extends Object> entityClass, EntityQuery entityquery, boolean justCount) {
		
		UniqueRightQuery query = (UniqueRightQuery)entityquery;
		IRight right = query.getRight();
		
		Session session = HibernateUtil.currentSession();
		StringBuffer sb = new StringBuffer();
		if (justCount) {
			sb.append("select count(*) ");
		}
		sb.append("from ")
		  .append(Right.class.getName())
		  .append(" AS obj where ")
		  .append("obj.id != ? AND ")
		  .append("obj.role = ? AND ")
		  .append("obj.scope = ? AND ")
		  .append("obj.action = ?");

		Query q = session.createQuery(sb.toString());
		q.setInteger(0, right.getId());
		q.setInteger(1, right.getRole().getId());
		if (right.getScope() == null) {
			q.setSerializable(2, null);
		} else {
			q.setInteger(2, right.getScope().getId());
		}
		q.setInteger(3, right.getAction().getId());
		
		
		return q;
		
	}

}
