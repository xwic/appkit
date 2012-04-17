/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.model.daos.impl.SyncStateDAO
 * Created on Nov 28, 2007 by Aron Cotrau
 *
 */
package de.xwic.appkit.core.model.daos.impl;

import de.xwic.appkit.core.dao.AbstractDAO;
import de.xwic.appkit.core.dao.DataAccessException;
import de.xwic.appkit.core.dao.Entity;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.daos.ISyncStateDAO;
import de.xwic.appkit.core.model.entities.ISyncState;
import de.xwic.appkit.core.model.entities.impl.SyncState;


/**
 * DAO implementation for SyncState.
 *
 * @author Aron Cotrau
 */
public class SyncStateDAO extends AbstractDAO implements ISyncStateDAO {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.AbstractDAO#getEntityImplClass()
	 */
	public Class<? extends Entity> getEntityImplClass() {
		return SyncState.class;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.DAO#createEntity()
	 */
	public IEntity createEntity() throws DataAccessException {
		return new SyncState();
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.DAO#getEntityClass()
	 */
	public Class<? extends IEntity> getEntityClass() {
		return ISyncState.class;
	}

}
