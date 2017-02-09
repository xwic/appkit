/*******************************************************************************
 * Copyright 2015 xWic group (http://www.xwic.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *  
 *******************************************************************************/
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
	private long entityID = -1L;
	
	private String dateiName = "";
	private long dateiGroesse = 0;
	private long fileID = -1L;
	
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
	public long getFileID() {
		return fileID;
	}

	/*
	 *  (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.IAnhang#setFileID(int)
	 */
	public void setFileID(long fileID) {
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
	public long getEntityID() {
		return entityID;
	}

	/**
	 * @param entityID the entityID to set
	 */
	public void setEntityID(long entityID) {
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
