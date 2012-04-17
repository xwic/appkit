/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.model.daos.impl.MonitoringElementDAO
 * Created on 04.01.2008 by Florian Lippisch
 *
 */
package de.xwic.appkit.core.model.daos.impl;

import de.xwic.appkit.core.dao.AbstractDAO;
import de.xwic.appkit.core.dao.DataAccessException;
import de.xwic.appkit.core.dao.Entity;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.daos.IMonitoringElementDAO;
import de.xwic.appkit.core.model.entities.IMonitoringElement;
import de.xwic.appkit.core.model.entities.impl.MonitoringElement;

/**
 * @author Florian Lippisch
 */
public class MonitoringElementDAO extends AbstractDAO implements IMonitoringElementDAO {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.AbstractDAO#getEntityImplClass()
	 */
	public Class<? extends Entity> getEntityImplClass() {
		return MonitoringElement.class;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.DAO#createEntity()
	 */
	public IEntity createEntity() throws DataAccessException {
		return new MonitoringElement();
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.DAO#getEntityClass()
	 */
	public Class<? extends IEntity> getEntityClass() {
		return IMonitoringElement.class;
	}

}
