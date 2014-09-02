/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.model.queries.resolver.hbn.PicklistEntryQueryResolver
 * Created on 11.07.2005
 *
 */
package de.xwic.appkit.core.model.queries.resolver.hbn;

import org.hibernate.Query;
import org.hibernate.Session;

import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.dao.IEntityQueryResolver;
import de.xwic.appkit.core.dao.impl.hbn.HibernateUtil;
import de.xwic.appkit.core.model.entities.impl.PicklistEntry;
import de.xwic.appkit.core.model.queries.PicklistEntryQuery;

/**
 * @author Ronny Pfretzschner
 */
public class PicklistEntryQueryResolver implements IEntityQueryResolver {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.IEntityQueryResolver#resolve(de.xwic.appkit.core.dao.EntityQuery)
	 */
	public Object resolve(Class<? extends Object> entityClass, EntityQuery entityQuery, boolean justCount) {
		
		PicklistEntryQuery query = (PicklistEntryQuery)entityQuery;
		Session session = HibernateUtil.currentSession();
		StringBuffer sb = new StringBuffer();
		if (justCount) {
			sb.append("select count(*) ");
		}
		sb.append("from ")
		  .append(PicklistEntry.class.getName());
		
		//query.get
		sb.append(" AS obj where obj.deleted = 0 ");
		
		if (query.getEntryID() > 0) {
			  sb.append(" AND obj.id = ?");
		}
		
		else if (query.getPickListID() > 0) {
		  sb.append(" AND obj.pickliste.id = ?");
		}

		Query q = session.createQuery(sb.toString());
		if (query.getPickListID() > 0) {
			q.setInteger(0, query.getPickListID());
		}
		else if (query.getEntryID() > 0) {
		    q.setInteger(0, query.getEntryID());
		}

		return q;
	}
}
