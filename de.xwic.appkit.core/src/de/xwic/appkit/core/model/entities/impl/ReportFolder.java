/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.model.entities.impl.ReportFolder
 * Created on Dec 7, 2007 by Aron Cotrau
 *
 */
package de.xwic.appkit.core.model.entities.impl;

import de.xwic.appkit.core.dao.Entity;
import de.xwic.appkit.core.model.entities.IReportFolder;


/**
 * implementation for the ReportFolder entity.
 *
 * @author Aron Cotrau
 */
public class ReportFolder extends Entity implements IReportFolder {

	private IReportFolder parent;
	private String name;
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the parent
	 */
	public IReportFolder getParent() {
		return parent;
	}
	
	/**
	 * @param parent the parent to set
	 */
	public void setParent(IReportFolder parent) {
		this.parent = parent;
	}
	
}
