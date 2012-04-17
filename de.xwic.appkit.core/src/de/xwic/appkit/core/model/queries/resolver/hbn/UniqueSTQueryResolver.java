/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.model.queries.resolver.hbn.UniqueSTQueryResolver
 * Created on 05.08.2005
 *
 */
package de.xwic.appkit.core.model.queries.resolver.hbn;

import org.hibernate.Query;
import org.hibernate.Session;

import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.dao.IEntityQueryResolver;
import de.xwic.appkit.core.dao.impl.hbn.HibernateUtil;
import de.xwic.appkit.core.model.entities.ISalesTeam;
import de.xwic.appkit.core.model.entities.impl.SalesTeam;
import de.xwic.appkit.core.model.queries.UniqueSTQuery;

/**
 * Query Resolver for SalesTeam uniquetest. <p>
 * 
 * @author Ronny Pfretzschner
 *
 */
public class UniqueSTQueryResolver implements IEntityQueryResolver {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.IEntityQueryResolver#resolve(de.xwic.appkit.core.dao.EntityQuery)
	 */
	public Object resolve(Class<? extends Object> entityClass, EntityQuery entityquery, boolean justCount) {
		UniqueSTQuery query = (UniqueSTQuery)entityquery;
		ISalesTeam st = query.getSt();
		
		Session session = HibernateUtil.currentSession();
		StringBuffer sb = new StringBuffer();
		if (justCount) {
			sb.append("select count(*) ");
		}
		sb.append("from ")
		  .append(SalesTeam.class.getName())
		  .append(" AS obj where ")
		  .append("obj.deleted = 0 AND ")
		  .append("obj.id != ? AND ")
		  .append("obj.bezeichnung like ?");

		Query q = session.createQuery(sb.toString());
		q.setInteger(0, st.getId());
		q.setString(1, st.getBezeichnung());
		
		return q;
	}
}
