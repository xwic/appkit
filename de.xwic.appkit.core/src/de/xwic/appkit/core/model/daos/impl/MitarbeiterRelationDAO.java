/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.model.daos.impl.MitarbeiterRelationDAO
 * Created on Aug 6, 2008 by Aron Cotrau
 *
 */
package de.xwic.appkit.core.model.daos.impl;

import de.xwic.appkit.core.dao.AbstractDAO;
import de.xwic.appkit.core.dao.DataAccessException;
import de.xwic.appkit.core.dao.Entity;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.daos.IMitarbeiterRelationDAO;
import de.xwic.appkit.core.model.entities.IMitarbeiterRelation;
import de.xwic.appkit.core.model.entities.impl.MitarbeiterRelation;


/**
 * 
 *
 * @author Aron Cotrau
 */
public class MitarbeiterRelationDAO extends AbstractDAO implements IMitarbeiterRelationDAO {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.AbstractDAO#getEntityImplClass()
	 */
	@Override
	public Class<? extends Entity> getEntityImplClass() {
		return MitarbeiterRelation.class;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.DAO#createEntity()
	 */
	public IEntity createEntity() throws DataAccessException {
		MitarbeiterRelation mitRelation = new MitarbeiterRelation();
		
		return mitRelation;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.DAO#getEntityClass()
	 */
	public Class<? extends IEntity> getEntityClass() {
		return IMitarbeiterRelation.class;
	}

}
