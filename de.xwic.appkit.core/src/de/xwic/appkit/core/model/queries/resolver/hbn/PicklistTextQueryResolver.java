/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.model.queries.resolver.hbn.PicklistTextQueryResolver
 * Created on 11.07.2005
 *
 */
package de.xwic.appkit.core.model.queries.resolver.hbn;

import org.hibernate.Query;
import org.hibernate.Session;

import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.dao.IEntityQueryResolver;
import de.xwic.appkit.core.dao.impl.hbn.HibernateUtil;
import de.xwic.appkit.core.model.entities.impl.PicklistText;
import de.xwic.appkit.core.model.queries.PicklistTextQuery;

/**
 * @author Florian Lippisch
 */
public class PicklistTextQueryResolver implements IEntityQueryResolver {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.IEntityQueryResolver#resolve(de.xwic.appkit.core.dao.EntityQuery)
	 */
	public Object resolve(Class<? extends Object> entityClass, EntityQuery entityQuery, boolean justCount) {
		
		PicklistTextQuery query = (PicklistTextQuery)entityQuery;
		Session session = HibernateUtil.currentSession();
		StringBuffer sb = new StringBuffer();
		if (justCount) {
			sb.append("select count(*) ");
		}
		sb.append("from ")
		  .append(PicklistText.class.getName());
		
		sb.append(" AS obj where obj.deleted = 0 ");
		
		if (query.getPicklistEntryID() > 0) {
		  sb.append(" AND obj.picklistEntry.id = ?");
			if (query.getLanguageId() != null) {
				sb.append(" AND (obj.languageID = ? OR obj.languageID = ?)");
			}
		} else {
			if (query.getLanguageId() != null) {
				sb.append(" AND (obj.languageID = ? OR obj.languageID = ?)");
				if (query.getBezeichnung() != null) {
					sb.append (" AND obj.bezeichnung = ?");
				}
				if (query.getPicklisteID() > 0) {
					sb.append(" AND obj.picklistEntry.pickliste.id = ?");
				}
			}
		}

		Query q = session.createQuery(sb.toString());
		if (query.getPicklistEntryID() > 0) {
			q.setInteger(0, query.getPicklistEntryID());
			if (query.getLanguageId() != null) {
				q.setString(1, query.getLanguageId().toUpperCase());
				q.setString(2, query.getLanguageId().toLowerCase());
			}
		} else {
			if (query.getLanguageId() != null) {
				int idx = 0;
				q.setString(idx++, query.getLanguageId().toUpperCase());
				q.setString(idx++, query.getLanguageId().toLowerCase());
				if (query.getBezeichnung() != null) {
					q.setString(idx++, query.getBezeichnung());
				}
				if (query.getPicklisteID() > 0) {
					q.setInteger(idx++, query.getPicklisteID());
				}
			}
		}
		
		return q;
	}

}
