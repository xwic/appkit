/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.model.entities.IReportFolder
 * Created on Dec 7, 2007 by Aron Cotrau
 *
 */
package de.xwic.appkit.core.model.entities;

import de.xwic.appkit.core.dao.IEntity;


/**
 * interface for the ReportFolder entity.
 *
 * @author Aron Cotrau
 */
public interface IReportFolder extends IEntity {

	/**
	 * @return the name
	 */
	public String getName();
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name);
	
	/**
	 * @return the parent
	 */
	public IReportFolder getParent();
	
	/**
	 * @param parent the parent to set
	 */
	public void setParent(IReportFolder parent);	
	
}
