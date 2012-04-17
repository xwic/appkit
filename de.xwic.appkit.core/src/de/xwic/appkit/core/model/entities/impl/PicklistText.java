/*
 * de.xwic.appkit.core.model.entities.impl.PicklistText
 * Created on 08.07.2005
 *
 */
package de.xwic.appkit.core.model.entities.impl;

import de.xwic.appkit.core.dao.Entity;
import de.xwic.appkit.core.model.entities.IPicklistEntry;
import de.xwic.appkit.core.model.entities.IPicklistText;

/**
 * @author Florian Lippisch
 */
public class PicklistText extends Entity implements IPicklistText {

	private String bezeichnung = "";
	private String languageID = "";
	private String beschreibung = null;
	private IPicklistEntry picklistEntry = null;
	
	/**
	 * ctor. Just for Hibernate and Webservice. <p>
	 * 
	 * Use this Constructor only at emergency because
	 * then you get just an empty object, <br>without check
	 * of the "must to have" properties.
	 *
	 */
	public PicklistText() {
		
	}

	/**
	 * Constructor for PicklistText. <p>
	 * Use this Constructor instead of the default!
	 * 
	 * @param entry The PicklistEntry of this PicklistText
	 * @param langID the specific language ID
	 * @param bezeichnung the new text for the given language ID 
	 */
	public PicklistText(IPicklistEntry entry, String langID, String bezeichnung) {
        if (entry == null || 
            langID == null || langID.length() < 1 || 
            bezeichnung == null) {
        throw new IllegalArgumentException("Error in constructor in class: " + getClass().getName() + "! Arguments not allowed!");
        }

	    this.picklistEntry = entry;
	    this.languageID = langID;
	    this.bezeichnung = bezeichnung;
	}
	
	/**
	 * For Hibernate only. <p>
	 * 
	 * @param entry
	 */
	public void setPicklistEntry(IPicklistEntry picklistEntry) {
	    this.picklistEntry = picklistEntry;
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.IPicklistText#getPicklistEntry()
	 */
	public IPicklistEntry getPicklistEntry() {
		return picklistEntry;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.IPicklistEntry#getLanguageID()
	 */
	public String getLanguageID() {
		return languageID;
	}

	
	/**
	 * Sets the given language ID
	 * @param languageID
	 */
	public void setLanguageID(String languageID) {
		this.languageID = languageID;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.IPicklistEntry#getBezeichnung()
	 */
	public String getBezeichnung() {
		return bezeichnung;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.IPicklistEntry#setBezeichnung(java.lang.String)
	 */
	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
	    StringBuffer buffer = new StringBuffer();
	    buffer.append("LANG_ID: ").append(getLanguageID());
	    buffer.append(", Bezeichnung: ").append(getBezeichnung());
	    return buffer.toString();
	}
	
	   /* (non-Javadoc)
     * @see de.xwic.appkit.core.dao.Entity#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        if (obj == null) {
            return false;
        }
        
        if (obj.getClass() != this.getClass()) {
            return false;
        }

        PicklistText p = (PicklistText) obj;
        
        if (bezeichnung.equals(p.getBezeichnung()) &&
            languageID.equals(p.getLanguageID()) &&
            picklistEntry.equals(p.getPicklistEntry())) {
            return true;
        }
        
        return false;
    }
    
    /* (non-Javadoc)
     * @see de.xwic.appkit.core.dao.Entity#hashCode()
     */
    public int hashCode() {
        int hc = 17;
        int multiplier = 7;
        
        hc = hc * multiplier + bezeichnung.hashCode();
        hc = hc * multiplier + languageID.hashCode();
        
       return hc; 
    }

	/**
	 * @return the beschreibung
	 */
	public String getBeschreibung() {
		return beschreibung;
	}

	/**
	 * @param beschreibung the beschreibung to set
	 */
	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}

}
