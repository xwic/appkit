/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.model.daos.impl.ReportFolderDAO
 * Created on Dec 7, 2007 by Aron Cotrau
 *
 */
package de.xwic.appkit.core.model.daos.impl;

import de.xwic.appkit.core.dao.AbstractDAO;
import de.xwic.appkit.core.dao.DataAccessException;
import de.xwic.appkit.core.dao.Entity;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.daos.IReportFolderDAO;
import de.xwic.appkit.core.model.entities.IReportFolder;
import de.xwic.appkit.core.model.entities.impl.ReportFolder;


/**
 * DAO implementation for the ReportFolder entity 
 *
 * @author Aron Cotrau
 */
public class ReportFolderDAO extends AbstractDAO implements IReportFolderDAO {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.AbstractDAO#getEntityImplClass()
	 */
	public Class<? extends Entity> getEntityImplClass() {
		return ReportFolder.class;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.DAO#createEntity()
	 */
	public IEntity createEntity() throws DataAccessException {
		return new ReportFolder();
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.DAO#getEntityClass()
	 */
	public Class<? extends IEntity> getEntityClass() {
		return IReportFolder.class;
	}

}
