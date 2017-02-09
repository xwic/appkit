/*******************************************************************************
 * Copyright 2015 xWic group (http://www.xwic.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *  
 *******************************************************************************/
package de.xwic.appkit.core.model.queries.resolver.hbn;

import org.hibernate.Query;
import org.hibernate.Session;

import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.dao.impl.hbn.HibernateUtil;
import de.xwic.appkit.core.model.entities.impl.Mitarbeiter;
import de.xwic.appkit.core.model.queries.CMFastSearchQuery;

/**
 * Query Resolver for the Mitarbeiter entity. <p>
 * 
 * The short Filter is displayed at the common Mitarbeiter toblist selection. <br>
 * If values are null, they will be ignored within the search.
 * 
 * @author Ronny Pfretzschner
 *
 */
public class CMQueryShortFilterResolver extends QueryResolver {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.IEntityQueryResolver#resolve(de.xwic.appkit.core.dao.EntityQuery)
	 */
	public Object resolve(Class<? extends Object> entityClass, EntityQuery entityQuery, boolean justCount) {
    	CMFastSearchQuery query = (CMFastSearchQuery)entityQuery;
		Session session = HibernateUtil.currentSession();
		StringBuffer sbFrom = new StringBuffer();
		StringBuffer sb = new StringBuffer();
		if (justCount) {
			sbFrom.append("select count(*) ");
		} else {
			sbFrom.append("select obj ");
		}
		sbFrom.append("from ").append(Mitarbeiter.class.getName()).append(" AS obj ");
		
		boolean hasBefore = true;
		sb.append(" WHERE obj.deleted = 0 ");
    	
		String name = query.getNachname();
		String vorname = query.getVorname();
		long einheitID = query.getEinheitPeID();
		long salesTeamID = query.getSalesTeamID();
		String logonName = query.getLogonName();
		long dokID = query.getDokID();
		long svID = query.getSvID();
		
		// hide all entries with "ausgeschieden" flag - probably later based upon Query object.
		sb.append(" AND obj.ausgeschieden = '0'");
		
		if (name != null && name.length() > 0) {
			  sb.append(hasBefore ? " AND " : " where  ");
			  sb.append("obj.nachname like ?");
			  hasBefore = true;
		}
		if (vorname != null && vorname.length() > 0) {
			  sb.append(hasBefore ? " AND " : " where  ");
			  sb.append("obj.vorname like ?");
			  hasBefore = true;
		}
		if (einheitID > 0) {
			  sb.append(hasBefore ? " AND " : " where  ");
			  sb.append("obj.einheit.id = ?");
			  hasBefore = true;
		}
		if (salesTeamID > 0) {
			  sb.append(hasBefore ? " AND " : " where  ");
			  sb.append("obj.salesTeam.id = ?");
			  hasBefore = true;
		}
		if (logonName != null && logonName.length() > 0) {
			  sb.append(hasBefore ? " AND " : " where  ");
			  sb.append("obj.logonName like ?");
			  hasBefore = true;
		}
		if (svID > 0) {
			sb.append(hasBefore ? " AND " : " where ");
			//TODO: dirty fix for error handling. here we cannot see Schriftverkehr class
			//sb.append(" obj.id IN (select sv.mitarbeiter.id from ").append(Schriftverkehr.class.getName());
			sb.append(" obj.id IN (select sv.mitarbeiter.id from ").append("de.xwic.appkit.core.model.entities.impl.Schriftverkehr");
			sb.append(" sv where sv.id = ?)");
			hasBefore = true;
		} 
		
		if (!justCount) {
			sb.append(addSortingClause(query, "obj", hasBefore, sbFrom, Mitarbeiter.class));
		}
		
		Query q = session.createQuery(sbFrom + "\n" + sb.toString());
		int pos = 0;
		
		if (name != null && name.length() > 0) {
			String newName = name.replace('*', '%');
			q.setString(pos++, newName);
		}
		if (vorname != null && vorname.length() > 0) {
			String newName = vorname.replace('*', '%');
			q.setString(pos++, newName);
		}
		if (einheitID > 0) {
			q.setLong(pos++, einheitID);
		}
		if (salesTeamID > 0) {
			q.setLong(pos++, salesTeamID);
		}
		if (logonName != null && logonName.length() > 0) {
			String newName = logonName.replace('*', '%');
			q.setString(pos++, newName);
		}
		
		if (svID > 0) {
			q.setLong(pos++, svID);
		} else if (dokID > 0) {
			q.setLong(pos++, dokID);
		}
		
    	return q;
	}
}
