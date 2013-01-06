/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.model.daos.impl.MitarbeiterDAO
 * Created on 18.07.2005
 *
 */
package de.xwic.appkit.core.model.daos.impl;

import java.util.List;

import de.xwic.appkit.core.dao.AbstractDAO;
import de.xwic.appkit.core.dao.DAOCallback;
import de.xwic.appkit.core.dao.DAOProviderAPI;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.DataAccessException;
import de.xwic.appkit.core.dao.Entity;
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
public class MitarbeiterDAO extends AbstractDAO implements
        IMitarbeiterDAO {

    /* (non-Javadoc)
     * @see de.xwic.appkit.core.dao.DAO#createEntity()
     */
    public IEntity createEntity() throws DataAccessException {
        return new Mitarbeiter();
    }

    /* (non-Javadoc)
     * @see de.xwic.appkit.core.dao.DAO#getEntityClass()
     */
    public Class<? extends IEntity> getEntityClass() {
        return IMitarbeiter.class;
    }

 	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.AbstractDAO#getEntityImplClass()
	 */
	public Class<? extends Entity> getEntityImplClass() {
		return Mitarbeiter.class;
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.AbstractDAO#getHistoryImplClass()
	 */
	public Class<? extends IHistory> getHistoryImplClass() {
		return MitarbeiterHistory.class;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see de.xwic.appkit.core.model.daos.IMitarbeiterDAO#getAllUNBetreuer()
	 */
	public List<?> getAllUNBetreuer() {
    	return (List<?>)provider.execute(new DAOCallback() {
    		public Object run(DAOProviderAPI api) {
    			EntityList list = api.getEntities(IMitarbeiter.class, null, new AllUNBetreuerQuery());
	    		return list;
    		}
        });
	}
    
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.AbstractDAO#validateEntity(de.xwic.appkit.core.dao.IEntity)
	 */
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
	public IMitarbeiter getByCurrentUser() {
		if (DAOSystem.getSecurityManager().getCurrentUser() == null) {
			return null;
		}
		String logonName = DAOSystem.getSecurityManager().getCurrentUser().getLogonName();
		CMFastSearchQuery query = new CMFastSearchQuery();
		query.setLogonName(logonName);
		IMitarbeiterDAO cmDAO = (IMitarbeiterDAO) DAOSystem.getDAO(IMitarbeiterDAO.class);
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
}
