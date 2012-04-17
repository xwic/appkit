/*
 * de.xwic.appkit.core.model.entities.impl.Pickliste
 * Created on 07.07.2005
 *
 */
package de.xwic.appkit.core.model.entities.impl;

import de.xwic.appkit.core.dao.Entity;
import de.xwic.appkit.core.model.entities.IPickliste;

/**
 * Implementation of the IPickliste.
 * @author Florian Lippisch
 */
public class Pickliste extends Entity implements IPickliste {

	private String beschreibung = "";
	private String key = "";
	private String title = "";
	
	/**
	 * ctor. Just for Hibernate and Webservice. <p>
	 * 
	 * Use this Constructor only at emergency because
	 * then you get just an empty object, <br>without check
	 * of the "must to have" properties.
	 *
	 */
	public Pickliste() {
	}
	
	/**
	 * Constructor for Pickliste. <p>
	 * Use this Constructor instead of the default!
	 * 
	 * @param key the new key for the Pickliste
	 * @param title the title of the Pickliste
	 * @param beschreibung a description for the new Pickliste
	 */
	public Pickliste(String key, String title, String beschreibung){
	    if (key == null || key.length() < 1 ||
		        title == null || title.length() < 1 ||
		        beschreibung == null) {
		        throw new IllegalArgumentException("Error in constructor in class: " + getClass().getName() + "! Arguments not allowed!");
		}
	    
	    this.key = key;
	    this.title = title;
	    this.beschreibung = beschreibung;
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.IPickliste#getBeschreibung()
	 */
	public String getBeschreibung() {
		return beschreibung;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.IPickliste#setBeschreibung(java.lang.String)
	 */
	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.IPickliste#getKey()
	 */
	public String getKey() {
		return key;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.IPickliste#setKey(java.lang.String)
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.IPickliste#getTitle()
	 */
	public String getTitle() {
		return title;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.IPickliste#setTitle(java.lang.String)
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Titel: ").append(title).append("; ");
        buffer.append("Key: ").append(key).append("; ");
        buffer.append("Beschreibung: ").append(beschreibung).append("; ");
        
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

        Pickliste p = (Pickliste) obj;
        
        if (key.equals(p.getKey()) &&
            beschreibung.equals(p.getBeschreibung()) &&
            title.equals(p.getTitle())) {
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
        
        hc = hc * multiplier + key.hashCode();
        hc = hc * multiplier + beschreibung.hashCode();
        hc = hc * multiplier + title.hashCode();
        
       return hc; 
    }
}
