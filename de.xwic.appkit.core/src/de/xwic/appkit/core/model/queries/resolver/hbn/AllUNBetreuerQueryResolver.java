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

import de.xwic.appkit.core.CommonConfiguration;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.dao.IEntityQueryResolver;
import de.xwic.appkit.core.dao.impl.hbn.HibernateUtil;
import de.xwic.appkit.core.model.daos.IServerConfigPropertyDAO;

/**
 * @author Ronny Pfretzschner
 *
 */
public class AllUNBetreuerQueryResolver implements IEntityQueryResolver {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.IEntityQueryResolver#resolve(de.xwic.appkit.core.dao.EntityQuery)
	 */
	public Object resolve(Class<? extends Object> entityClass, EntityQuery query, boolean justCount) {
		
		IServerConfigPropertyDAO propDao = (IServerConfigPropertyDAO)DAOSystem.getDAO(IServerConfigPropertyDAO.class);
		int roleID = propDao.getConfigInteger(CommonConfiguration.CONF_PROP_ROLLE_UN_CM_BETREUER);

		Session session = HibernateUtil.currentSession();
		StringBuffer sb = new StringBuffer();
		//TODO: dirty fix for error handling. here we cannot see UnternehmenMitarbeiter class
		//sb.append("select distinct um.mitarbeiter from ").append(UnternehmenMitarbeiter.class.getName());
		sb.append("select distinct um.mitarbeiter from de.xwic.appkit.core.model.entities.impl.UnternehmenMitarbeiter");
		sb.append(" AS um WHERE um.deleted = 0 AND um.rolle.id = ").append(roleID);
		
		Query q = session.createQuery(sb.toString());
    	return q;
	}
}
