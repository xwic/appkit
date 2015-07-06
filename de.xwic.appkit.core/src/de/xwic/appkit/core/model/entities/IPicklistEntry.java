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
 * de.xwic.appkit.core.model.entities.IPicklistEntry
 * Created on 07.07.2005
 *
 */
package de.xwic.appkit.core.model.entities;

import java.util.Set;

import de.xwic.appkit.core.dao.IEntity;

/**
 * PL - PicklistenEntry. Defines an entry in a Pickliste.
 * @author Florian Lippisch
 */
public interface IPicklistEntry extends IEntity {

	/**
	 * Returns the Pickliste this entry belongs to.
	 * @return
	 */
	public IPickliste getPickliste();
	/**
	 * Set the Pickliste this entry belongs to.
	 * @param pickliste
	 */
	public void setPickliste(IPickliste pickliste);

	/**
	 * Returns the bezeichnung of this entry in the specified language.
	 * @param languageID
	 * @return
	 */
	public String getBezeichnung(String languageID);
	
	/**
	 * Returns true if the entry is deprecated and should be "hidden" in
	 * certain cases.
	 * @return
	 */
	public boolean isVeraltet();

	/**
	 * Sets a bezeichnung to the given languageID. <p>
	 * 
	 * In the back, at the moment the PickListeText is requested from
	 * the database. <br>There, the bezeichnung is set AND the PicklisteText
	 * is updated there. 
	 * 
	 * @param languageID from the user
	 * @param bezeichnung the new bezeichnung for the languageID
	 */
	public void setBezeichnung(String languageID, String bezeichnung);
	
	/**
	 * Return PicklistText values.
	 * @return Set of PicklistText
	 */
	public Set<IPicklistText> getPickTextValues();
	
	/**
	 * Set PickListText values.
	 * @param pickTextValues to set
	 */
	public void setPickTextValues(Set<IPicklistText> pickTextValues);
	
	/**
	 * Set to true if the entry is deprecated.
	 * @param veraltet
	 */
	public void setVeraltet(boolean veraltet);
	
	/**
	 * Returns the optional key value for this entry.
	 * @return
	 */
	public String getKey();
	
	/**
	 * Set a key value for this entry.
	 * @param newKey
	 */
	public void setKey(String newKey);
	
	/**
	 * Returns the sortIndex property.
	 * @return
	 */
	public int getSortIndex();
	
	/**
	 * Set the SortIndex Property.
	 * @param newSortIndex
	 */
	public void setSortIndex(int newSortIndex);
	
	/**
	 * Sets the comment on the picklist text entry.
	 * 
	 * @param langID
	 * @param comment
	 */
	public void setKommentar(String langID, String comment);
	
	/**
	 * Tries to get the description of this entry by the given language.
	 * 
	 * @param langID
	 * @return 
	 */
	public String getKommentar(String langID);
}
