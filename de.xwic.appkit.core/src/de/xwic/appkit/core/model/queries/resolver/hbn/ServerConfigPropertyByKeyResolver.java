/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.model.queries.resolver.hbn.ServerConfigPropertyByNameResolver
 * Created on 24.08.2005
 *
 */
package de.xwic.appkit.core.model.queries.resolver.hbn;

import org.hibernate.Query;
import org.hibernate.Session;

import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.dao.EntityQueryAdapter;
import de.xwic.appkit.core.dao.impl.hbn.HibernateUtil;
import de.xwic.appkit.core.model.entities.impl.ServerConfigProperty;
import de.xwic.appkit.core.model.queries.ServerConfigPropertyByKeyQuery;

/**
 * Query Resolver for the ServerConfigProperty Entity. <p>
 * 
 * @author Ronny Pfretzschner
 */
public class ServerConfigPropertyByKeyResolver extends EntityQueryAdapter {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.IEntityQueryResolver#resolve(de.xwic.appkit.core.dao.EntityQuery)
	 */
	public Object resolve(Class<? extends Object> entityClass, EntityQuery entityQuery, boolean justCount) {
		ServerConfigPropertyByKeyQuery query = (ServerConfigPropertyByKeyQuery)entityQuery;
		Session session = HibernateUtil.currentSession();
		StringBuffer sb = new StringBuffer();
		if (justCount) {
			sb.append("select count(*) ");
		}
		sb.append("from ")
		  .append(ServerConfigProperty.class.getName());
		if (query.getKey() != null) {
		  sb.append(" AS obj where obj.key like ?");
		}

		Query q = session.createQuery(sb.toString());
		if (query.getKey() != null) {
			q.setString(0, query.getKey());
		}
		
		return q;
	}
}
