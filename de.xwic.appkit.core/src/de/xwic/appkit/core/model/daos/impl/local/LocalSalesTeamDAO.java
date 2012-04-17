/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.model.daos.impl.local.LocalSalesTeamDAO
 * Created on 05.08.2005
 *
 */
package de.xwic.appkit.core.model.daos.impl.local;

import de.xwic.appkit.core.dao.DataAccessException;
import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.daos.impl.SalesTeamDAO;
import de.xwic.appkit.core.model.entities.ISalesTeam;
import de.xwic.appkit.core.model.queries.UniqueSTQuery;

/**
 * Server DAO for the SalesTeam entity. <p>
 *  
 * @author Ronny Pfretzschner
 */
public class LocalSalesTeamDAO extends SalesTeamDAO {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.AbstractDAO#update(de.xwic.appkit.core.dao.IEntity)
	 */
	public void update(IEntity entity) throws DataAccessException {
		ISalesTeam newST = (ISalesTeam) entity;
		EntityList erg = getEntities(null, new UniqueSTQuery(newST));
		
		if (erg.size() == 0) {
			super.update(entity);
		} else {
			StringBuffer errMessage = new StringBuffer();
			errMessage.append("SalesTeam mit Bezeichnung: ").append(newST.getBezeichnung());
			errMessage.append(" existiert bereits.");
			
			throw new DataAccessException(errMessage.toString());
		}	
	}
}
