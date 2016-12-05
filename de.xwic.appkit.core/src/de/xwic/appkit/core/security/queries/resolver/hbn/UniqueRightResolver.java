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
package de.xwic.appkit.core.security.queries.resolver.hbn;

import org.hibernate.Query;
import org.hibernate.Session;

import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.dao.IEntityQueryResolver;
import de.xwic.appkit.core.dao.impl.hbn.HibernateUtil;
import de.xwic.appkit.core.security.IRight;
import de.xwic.appkit.core.security.impl.Right;
import de.xwic.appkit.core.security.queries.UniqueRightQuery;

/**
 * Query Resolver for Right uniquetest. <p>
 *  
 * @author Florian Lippisch
 */
public class UniqueRightResolver implements IEntityQueryResolver {

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
		q.setLong(0, right.getId());
		q.setLong(1, right.getRole().getId());
		if (right.getScope() == null) {
			q.setSerializable(2, null);
		} else {
			q.setLong(2, right.getScope().getId());
		}
		q.setLong(3, right.getAction().getId());
		
		
		return q;
		
	}

}
