/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.model.queries.PicklistTextQuery
 * Created on 11.07.2005
 *
 */
package de.xwic.appkit.core.model.queries;

import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.model.entities.IPicklistEntry;

/**
 * @author Florian Lippisch
 */
public class PicklistTextQuery extends EntityQuery {

	private int picklistEntryID = -1;
	private String bezeichnung = null;
	private int picklisteID = -1;
	
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
	public int getPicklistEntryID() {
		return picklistEntryID;
	}

	/**
	 * @param picklistEntryID The picklistEntryID to set.
	 */
	public void setPicklistEntryID(int picklistEntryID) {
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
	public int getPicklisteID() {
		return picklisteID;
	}

	/**
	 * @param picklisteID the picklisteID to set
	 */
	public void setPicklisteID(int picklisteID) {
		this.picklisteID = picklisteID;
	}
	
}
