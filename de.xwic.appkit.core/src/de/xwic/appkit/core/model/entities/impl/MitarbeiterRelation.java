/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.model.entities.impl.MitarbeiterRelation
 * Created on Aug 6, 2008 by Aron Cotrau
 *
 */
package de.xwic.appkit.core.model.entities.impl;

import java.util.Date;

import de.xwic.appkit.core.dao.Entity;
import de.xwic.appkit.core.model.entities.IMitarbeiter;
import de.xwic.appkit.core.model.entities.IMitarbeiterRelation;
import de.xwic.appkit.core.model.entities.IPicklistEntry;


/**
 * Defines the Mitarbeiter relation entity. 
 * @author Aron Cotrau
 */
public class MitarbeiterRelation extends Entity implements IMitarbeiterRelation {

	private IMitarbeiter mitarbeiter = null;
	private IMitarbeiter relatedMitarbeiter = null;
	
	private IPicklistEntry rolle = null;
	
	private Date startDate = null;
	private Date endDate = null;
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.IMitarbeiterRelation#getMitarbeiter()
	 */
	public IMitarbeiter getMitarbeiter() {
		return mitarbeiter;
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.IMitarbeiterRelation#setMitarbeiter(de.xwic.appkit.core.model.entities.IMitarbeiter)
	 */
	public void setMitarbeiter(IMitarbeiter mitarbeiter) {
		this.mitarbeiter = mitarbeiter;
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.IMitarbeiterRelation#getRelatedMitarbeiter()
	 */
	public IMitarbeiter getRelatedMitarbeiter() {
		return relatedMitarbeiter;
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.IMitarbeiterRelation#setRelatedMitarbeiter(de.xwic.appkit.core.model.entities.IMitarbeiter)
	 */
	public void setRelatedMitarbeiter(IMitarbeiter relatedMitarbeiter) {
		this.relatedMitarbeiter = relatedMitarbeiter;
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.IMitarbeiterRelation#getRolle()
	 */
	public IPicklistEntry getRolle() {
		return rolle;
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.IMitarbeiterRelation#setRolle(de.xwic.appkit.core.model.entities.IPicklistEntry)
	 */
	public void setRolle(IPicklistEntry rolle) {
		this.rolle = rolle;
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.IMitarbeiterRelation#getStartDate()
	 */
	public Date getStartDate() {
		return startDate;
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.IMitarbeiterRelation#setStartDate(java.util.Date)
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.IMitarbeiterRelation#getEndDate()
	 */
	public Date getEndDate() {
		return endDate;
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.IMitarbeiterRelation#setEndDate(java.util.Date)
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	
}
