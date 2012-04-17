/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.security.queries.resolver.hbn.UserQueryResolver
 * Created on 02.08.2005
 *
 */
package de.xwic.appkit.core.security.queries.resolver.hbn;

import org.hibernate.Query;
import org.hibernate.Session;

import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.dao.IEntityQueryResolver;
import de.xwic.appkit.core.dao.impl.hbn.HibernateUtil;
import de.xwic.appkit.core.security.impl.User;
import de.xwic.appkit.core.security.queries.UserQuery;

/**
 * @author Florian Lippisch
 */
public class UserQueryResolver implements IEntityQueryResolver {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.IEntityQueryResolver#resolve(de.xwic.appkit.core.dao.EntityQuery)
	 */
	public Object resolve(Class<? extends Object> entityClass, EntityQuery query, boolean justCount) {
		
		UserQuery userquery = (UserQuery)query;
		Session session = HibernateUtil.currentSession();
		StringBuffer sb = new StringBuffer();
		if (justCount) {
			sb.append("select count(*) ");
		}
		sb.append("from ")
		  .append(User.class.getName());
		if (userquery.getLogonName() != null) {
		  sb.append(" AS obj where obj.logonName = ?");
		}

		Query q = session.createQuery(sb.toString());
		if (userquery.getLogonName() != null) {
			q.setString(0, userquery.getLogonName());
		}
		
		return q;
		
	}

}
