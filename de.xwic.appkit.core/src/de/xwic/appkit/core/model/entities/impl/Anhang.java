/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.model.entities.impl.Anhang
 * Created on 04.08.2005
 *
 */
package de.xwic.appkit.core.model.entities.impl;

import de.xwic.appkit.core.dao.Entity;
import de.xwic.appkit.core.model.entities.IAnhang;
import de.xwic.appkit.core.model.entities.IPicklistEntry;

/**
 * Implementation for the Anhang entity. <p>
 * 
 * @author Ronny Pfretzschner
 *
 */
public class Anhang extends Entity implements IAnhang {

	private String entityType = null;
	private int entityID = -1;
	
	private String dateiName = "";
	private long dateiGroesse = 0;
	private int fileID = -1;
	
	private boolean indirectReference = false;

	private IPicklistEntry kategorie;
	
	
	/**
     * default constructor. <p>
	 * Just for Hibernate and Webservice. <p>
	 * 
	 * Use this Constructor only at emergency because
	 * then you get just an empty object, <br>without check
	 * of the "must to have" properties.
	 */
	public Anhang() {
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.IAnhang#getDateiGroesse()
	 */
	public long getDateiGroesse() {
		return dateiGroesse;
	}
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.IAnhang#setDateiGroesse(long)
	 */
	public void setDateiGroesse(long dateiGroesse) {
		this.dateiGroesse = dateiGroesse;
	}
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.IAnhang#getDateiName()
	 */
	public String getDateiName() {
		return dateiName;
	}
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.IAnhang#setDateiName(java.lang.String)
	 */
	public void setDateiName(String dateiName) {
		this.dateiName = dateiName;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.IAnhang#getFileID()
	 */
	public int getFileID() {
		return fileID;
	}

	/*
	 *  (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.IAnhang#setFileID(int)
	 */
	public void setFileID(int fileID) {
		this.fileID = fileID;
	}

	/*
	 *  (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return ("Anhang '" + getDateiName() + "'");
	}

	/**
	 * @return the entityID
	 */
	public int getEntityID() {
		return entityID;
	}

	/**
	 * @param entityID the entityID to set
	 */
	public void setEntityID(int entityID) {
		this.entityID = entityID;
	}

	/**
	 * @return the entityType
	 */
	public String getEntityType() {
		return entityType;
	}

	/**
	 * @param entityType the entityType to set
	 */
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public IPicklistEntry getKategorie() {
		return kategorie;
	}

	public void setKategorie(IPicklistEntry kategorie) {
		this.kategorie = kategorie;
	}

	
	/**
	 * @return the indirectReference
	 */
	public boolean isIndirectReference() {
		return indirectReference;
	}

	
	/**
	 * @param indirectReference the indirectReference to set
	 */
	public void setIndirectReference(boolean indirectReference) {
		this.indirectReference = indirectReference;
	}
	
	
}
