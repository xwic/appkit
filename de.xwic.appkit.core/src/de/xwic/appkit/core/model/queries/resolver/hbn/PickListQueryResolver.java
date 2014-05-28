/*
 * Created on 25.07.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.xwic.appkit.core.model.queries.resolver.hbn;

import org.hibernate.Query;
import org.hibernate.Session;

import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.dao.EntityQueryAdapter;
import de.xwic.appkit.core.dao.impl.hbn.HibernateUtil;
import de.xwic.appkit.core.model.entities.impl.Pickliste;
import de.xwic.appkit.core.model.queries.PicklistQuery;

/**
 * @author Ronny
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PickListQueryResolver extends EntityQueryAdapter {

	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.IEntityQueryResolver#resolve(de.xwic.appkit.core.dao.EntityQuery)
	 */
	public Object resolve(Class<? extends Object> entityClass, EntityQuery entityQuery, boolean justCount) {
		PicklistQuery query = (PicklistQuery)entityQuery;
		Session session = HibernateUtil.currentSession();
		StringBuffer sb = new StringBuffer();
		if (justCount) {
			sb.append("select count(*) ");
		}
		sb.append("from ")
		  .append(Pickliste.class.getName());

		sb.append(" AS obj where obj.deleted = 0 ");
		
		if (query.getKey() != null) {
		  sb.append(" AND obj.key like ?");
		}

		Query q = session.createQuery(sb.toString());
		if (query.getKey() != null) {
			q.setString(0, query.getKey());
		}
		
		return q;
	}
}
