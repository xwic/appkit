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
/*
 * de.xwic.appkit.core.model.entities.IPicklistText
 * Created on 08.07.2005
 *
 */
package de.xwic.appkit.core.model.entities;

import de.xwic.appkit.core.dao.IEntity;

/**
 * Holds the languages specific text for a picklist entry.
 * 
 * @author Florian Lippisch
 */
public interface IPicklistText extends IEntity {

	/**
	 * Returns the picklist entry
	 * 
	 * @return
	 */
	public IPicklistEntry getPicklistEntry();

	/**
	 * Returns the language this entry specifies.
	 * 
	 * @return
	 */
	public String getLanguageID();

	/**
	 * Returns the value (Bezeichnung) of the picklist entry.
	 * 
	 * @return
	 */
	public String getBezeichnung();

	/**
	 * Set the value (Bezeichnung) of the picklist entry.
	 * 
	 * @param bezeichnung
	 */
	public void setBezeichnung(String bezeichnung);

	/**
	 * @return the beschreibung
	 */
	public String getBeschreibung();

	/**
	 * @param beschreibung the beschreibung to set
	 */
	public void setBeschreibung(String beschreibung);

}
