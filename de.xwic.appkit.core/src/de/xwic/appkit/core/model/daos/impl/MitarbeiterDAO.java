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
package de.xwic.appkit.core.model.daos.impl;

import java.util.List;

import de.xwic.appkit.core.dao.AbstractHistoryDAO;
import de.xwic.appkit.core.dao.DAOCallback;
import de.xwic.appkit.core.dao.DAOProviderAPI;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.dao.IHistory;
import de.xwic.appkit.core.dao.Limit;
import de.xwic.appkit.core.dao.ValidationResult;
import de.xwic.appkit.core.model.daos.IMitarbeiterDAO;
import de.xwic.appkit.core.model.entities.IMitarbeiter;
import de.xwic.appkit.core.model.entities.impl.Mitarbeiter;
import de.xwic.appkit.core.model.entities.impl.history.MitarbeiterHistory;
import de.xwic.appkit.core.model.queries.AllUNBetreuerQuery;
import de.xwic.appkit.core.model.queries.CMFastSearchQuery;
import de.xwic.appkit.core.model.queries.PropertyQuery;

/**
 * @author Ronny Pfretzschner
 */
public class MitarbeiterDAO extends AbstractHistoryDAO<IMitarbeiter, Mitarbeiter> implements IMitarbeiterDAO {

	/**
	 *
	 */
	public MitarbeiterDAO() {
		super(IMitarbeiter.class, Mitarbeiter.class);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.AbstractDAO#getHistoryImplClass()
	 */
	@Override
	public Class<? extends IHistory> getHistoryImplClass() {
		return MitarbeiterHistory.class;
	}

	/*
	 *  (non-Javadoc)
	 * @see de.xwic.appkit.core.model.daos.IMitarbeiterDAO#getAllUNBetreuer()
	 */
	@Override
	public List<?> getAllUNBetreuer() {
    	return (List<?>)provider.execute(new DAOCallback() {
    		@Override
			public Object run(DAOProviderAPI api) {
    			EntityList list = api.getEntities(IMitarbeiter.class, null, new AllUNBetreuerQuery());
	    		return list;
    		}
        });
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.AbstractDAO#validateEntity(de.xwic.appkit.core.dao.IEntity)
	 */
	@Override
	public ValidationResult validateEntity(IEntity entity) {
		ValidationResult result = super.validateEntity(entity);
		IMitarbeiter thisMit = (IMitarbeiter) entity;

		IMitarbeiter vorgesetzter = thisMit.getVorgesetzter();

		if (vorgesetzter != null && thisMit.getId() == vorgesetzter.getId()) {
			result.addError("vorgesetzter", "mitarbeiter.validate.vorgesetzter.error");
		}

		return result;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.daos.IMitarbeiterDAO#getByCurrentUser()
	 */
	@Override
	public IMitarbeiter getByCurrentUser() {
		if (DAOSystem.getSecurityManager().getCurrentUser() == null) {
			return null;
		}
		String logonName = DAOSystem.getSecurityManager().getCurrentUser().getLogonName();
		CMFastSearchQuery query = new CMFastSearchQuery();
		query.setLogonName(logonName);
		IMitarbeiterDAO cmDAO = DAOSystem.getDAO(IMitarbeiterDAO.class);
		EntityList list = cmDAO.getEntities(new Limit(0, 1), query);
		if (list.size() > 0) {
			return (IMitarbeiter) list.get(0);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.daos.IMitarbeiterDAO#getMittarbeiterByUsername(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public IMitarbeiter getMittarbeiterByUsername(String username) {
		if (username != null && username.trim().length() > 0) {
			PropertyQuery query = new PropertyQuery();
			query.addEquals("logonName", username);
			List<IMitarbeiter> list  = getEntities(null, query);

			if (list != null && list.size() > 0) {
				return list.get(0);
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.core.model.daos.IMitarbeiterDAO#getAllMyReports()
	 */
	@Override
	public List<IMitarbeiter> getAllMyReports() {
		return getAllReportsByUser(getByCurrentUser());
	}

	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.core.model.daos.IMitarbeiterDAO#getAllReportsByUser(de.xwic.appkit.core.model.entities.IMitarbeiter)
	 */
	@SuppressWarnings ("unchecked")
	@Override
	public List<IMitarbeiter> getAllReportsByUser(IMitarbeiter leader) {
		if (leader != null) {
			PropertyQuery query = new PropertyQuery();
			query.addEquals("vorgesetzter", leader);
			
			return getEntities(null, query);
		}
		return null;
	}
}
