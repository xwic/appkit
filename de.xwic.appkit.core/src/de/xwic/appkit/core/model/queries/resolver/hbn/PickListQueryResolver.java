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
import de.xwic.appkit.core.dao.IEntityQueryResolver;
import de.xwic.appkit.core.dao.impl.hbn.HibernateUtil;
import de.xwic.appkit.core.model.entities.impl.Pickliste;
import de.xwic.appkit.core.model.queries.PicklistQuery;

/**
 * @author Ronny
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PickListQueryResolver implements IEntityQueryResolver {

	
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
