/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.model.entities.impl.IAnhang
 * Created on 04.08.2005
 *
 */
package de.xwic.appkit.core.model.entities;

import de.xwic.appkit.core.dao.IEntity;

/**
 * Interface for the Anhang entity. <p>
 *  
 * @author Ronny Pfretzschner
 */
public interface IAnhang extends IEntity {

	/**
	 * Picklist for category of an attachment.
	 */
	public final static String PL_AH_CATEGORIE = "ah.kategorie";
	
	/**
	 * @return the entityID
	 */
	public int getEntityID();

	/**
	 * @param entityID the entityID to set
	 */
	public void setEntityID(int entityID);

	/**
	 * @return the entityType
	 */
	public String getEntityType();

	/**
	 * @param entityType the entityType to set
	 */
	public void setEntityType(String entityType);

	/**
	 * @return Returns the dateiGroesse.
	 */
	public long getDateiGroesse();

	/**
	 * @param dateiGroesse The dateiGroesse to set.
	 */
	public void setDateiGroesse(long dateiGroesse);

	/**
	 * @return Returns the dateiName.
	 */
	public String getDateiName();

	/**
	 * @param dateiName The dateiName to set.
	 */
	public void setDateiName(String dateiName);
	
	/**
	 * @return Returns the fileID.
	 */
	public int getFileID();

	/**
	 * @param fileID The fileID to set.
	 */
	public void setFileID(int fileID);
	

	/**
	 * @param kategorie to set
	 */
	public void setKategorie(IPicklistEntry kategorie);
	
	/**
	 * @return the kategorie
	 */
	public IPicklistEntry getKategorie();
	
	/**
	 * @return the indirectReference
	 */
	public boolean isIndirectReference();
	
	/**
	 * @param indirectReference the indirectReference to set
	 */
	public void setIndirectReference(boolean indirectReference);	
}