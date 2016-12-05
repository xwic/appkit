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
package de.xwic.appkit.core.model.queries;

import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.model.entities.IPicklistEntry;

/**
 * @author Florian Lippisch
 */
public class PicklistTextQuery extends EntityQuery {

	private long picklistEntryID = -1l;
	private String bezeichnung = null;
	private long picklisteID = -1l;
	private boolean excludeDeletedParents = false;
	
	/**
	 * Default Constructor.
	 *
	 */
	public PicklistTextQuery() {
		super();
	}
	
	/**
	 * Query all PicklistText objects for this entry. 
	 * @param entry
	 */
	public PicklistTextQuery(IPicklistEntry entry) {
		super();
        if (entry == null) {
            throw new IllegalArgumentException("Error in constructor in class: " + getClass().getName() + "! Arguments not allowed!");
        }
		this.picklistEntryID = entry.getId();
	}

	/**
	 * Query the PicklistText object for this entry int the specified langauge. 
	 * @param entry
	 * @param languageID the specific language ID
	 */
	public PicklistTextQuery(IPicklistEntry entry, String languageID) {
        if (entry == null || 
                languageID == null || languageID.length() < 1) {
                throw new IllegalArgumentException("Error in constructor in class: " + getClass().getName() + "! Arguments not allowed!");
        }
		this.picklistEntryID = entry.getId();
		setLanguageId(languageID);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("PicklistTextQuery entry=");
		sb.append(picklistEntryID);
		sb.append(", languageID=").append(getLanguageId());		
		return sb.toString();
	}

	/**
	 * @return Returns the picklistEntryID.
	 */
	public long getPicklistEntryID() {
		return picklistEntryID;
	}

	/**
	 * @param picklistEntryID The picklistEntryID to set.
	 */
	public void setPicklistEntryID(long picklistEntryID) {
		this.picklistEntryID = picklistEntryID;
	}

	/**
	 * @return the bezeichnung
	 */
	public String getBezeichnung() {
		return bezeichnung;
	}

	/**
	 * @param bezeichnung the bezeichnung to set
	 */
	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	/**
	 * @return the picklisteID
	 */
	public long getPicklisteID() {
		return picklisteID;
	}

	/**
	 * @param picklisteID the picklisteID to set
	 */
	public void setPicklisteID(long picklisteID) {
		this.picklisteID = picklisteID;
	}

	public boolean isExcludeDeletedParents() {
		return excludeDeletedParents;
	}

	public void setExcludeDeletedParents(boolean excludeDeletedParents) {
		this.excludeDeletedParents = excludeDeletedParents;
	}
	
}
