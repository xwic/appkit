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
	public long getEntityID();

	/**
	 * @param entityID the entityID to set
	 */
	public void setEntityID(long entityID);

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
	public long getFileID();

	/**
	 * @param fileID The fileID to set.
	 */
	public void setFileID(long fileID);
	

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