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

import java.util.Date;

import de.xwic.appkit.core.dao.IEntity;


/**
 * The mitarbeiter relation entity.
 * @author Aron Cotrau
 */
public interface IMitarbeiterRelation extends IEntity {

	/** picklist key for the rolle property */
	public static final String MIT_RELATION_ROLLE_PL_KEY = "mitRelation.rolle";
	
	/**
	 * @return the mitarbeiter
	 */
	public IMitarbeiter getMitarbeiter();

	/**
	 * @param mitarbeiter the mitarbeiter to set
	 */
	public void setMitarbeiter(IMitarbeiter mitarbeiter);

	/**
	 * @return the relatedMitarbeiter
	 */
	public IMitarbeiter getRelatedMitarbeiter();

	/**
	 * @param relatedMitarbeiter the relatedMitarbeiter to set
	 */
	public void setRelatedMitarbeiter(IMitarbeiter relatedMitarbeiter);

	/**
	 * @return the rolle
	 */
	public IPicklistEntry getRolle();

	/**
	 * @param rolle the rolle to set
	 */
	public void setRolle(IPicklistEntry rolle);

	/**
	 * @return the startDate
	 */
	public Date getStartDate();

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate);

	/**
	 * @return the endDate
	 */
	public Date getEndDate();

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate);

}