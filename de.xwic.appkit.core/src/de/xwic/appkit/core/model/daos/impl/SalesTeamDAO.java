/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.model.daos.impl.SalesTeamDAO
 * Created on 18.07.2005
 *
 */
package de.xwic.appkit.core.model.daos.impl;

import de.xwic.appkit.core.dao.AbstractDAO;
import de.xwic.appkit.core.dao.DataAccessException;
import de.xwic.appkit.core.dao.Entity;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.dao.ValidationResult;
import de.xwic.appkit.core.model.daos.ISalesTeamDAO;
import de.xwic.appkit.core.model.entities.ISalesTeam;
import de.xwic.appkit.core.model.entities.impl.SalesTeam;

/**
 * DAO for the SalesTeam business object. <p>
 * 
 * @author Ronny Pfretzschner
 */
public class SalesTeamDAO extends AbstractDAO implements ISalesTeamDAO {

    /* (non-Javadoc)
     * @see de.xwic.appkit.core.dao.DAO#createEntity()
     */
    public IEntity createEntity() throws DataAccessException {
        return new SalesTeam();
    }

    /* (non-Javadoc)
     * @see de.xwic.appkit.core.dao.DAO#getEntityClass()
     */
    public Class<? extends IEntity> getEntityClass() {
        return ISalesTeam.class;
    }

 	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.AbstractDAO#getEntityImplClass()
	 */
	public Class<? extends Entity> getEntityImplClass() {
		return SalesTeam.class;
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.AbstractDAO#validateEntity(de.xwic.appkit.core.dao.IEntity)
	 */
	public ValidationResult validateEntity(IEntity entity) {
		ValidationResult result = new ValidationResult();
		ISalesTeam newST = (ISalesTeam) entity;
		
		if (newST.getBezeichnung() == null || newST.getBezeichnung().length() < 1) {
			result.addError("st.bezeichnung", ValidationResult.ENTITY_ARGUMENTS_ERROR_RESOURCE_KEY);
		}
		return result;
	}
}
