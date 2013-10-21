/*
 * (c) Copyright 2005, 2006 by pol GmbH
 *
 * de.xwic.appkit.core.model.daos.impl.ReportTemplateDAO
 * Created on Dec 7, 2007 by Aron Cotrau
 *
 */
package de.xwic.appkit.core.model.daos.impl;

import de.xwic.appkit.core.dao.AbstractDAO;
import de.xwic.appkit.core.dao.DataAccessException;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.daos.IReportTemplateDAO;
import de.xwic.appkit.core.model.entities.IReportTemplate;
import de.xwic.appkit.core.model.entities.impl.ReportTemplate;


/**
 * DAO interface for ReportTemplate
 *
 * @author Aron Cotrau
 */
public class ReportTemplateDAO extends AbstractDAO<IReportTemplate, ReportTemplate> implements IReportTemplateDAO {

	/**
	 *
	 */
	public ReportTemplateDAO() {
		super(IReportTemplate.class, ReportTemplate.class);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.DAO#createEntity()
	 */
	@Override
	public IEntity createEntity() throws DataAccessException {
		return new ReportTemplate();
	}

}
