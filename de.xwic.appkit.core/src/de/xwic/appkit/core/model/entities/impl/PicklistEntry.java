/*
 * de.xwic.appkit.core.model.entities.impl.PicklistEntry
 * Created on 07.07.2005
 *
 */
package de.xwic.appkit.core.model.entities.impl;

import java.util.Set;

import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.Entity;
import de.xwic.appkit.core.model.daos.IPicklisteDAO;
import de.xwic.appkit.core.model.entities.IPicklistEntry;
import de.xwic.appkit.core.model.entities.IPicklistText;
import de.xwic.appkit.core.model.entities.IPickliste;

/**
 * Implementation of the IPicklistEntry interface.
 * @author Florian Lippisch
 */
public class PicklistEntry extends Entity implements IPicklistEntry {

	private IPickliste pickliste = null;
	private Set<IPicklistText> pickTextValues;
	private boolean veraltet = false;
	private String key = null;
	private int sortIndex = 0;
	
	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the sortIndex
	 */
	public int getSortIndex() {
		return sortIndex;
	}

	/**
	 * @param sortIndex the sortIndex to set
	 */
	public void setSortIndex(int sortIndex) {
		this.sortIndex = sortIndex;
	}

	/**
	 * ctor. Just for Hibernate and Webservice. <p>
	 * 
	 * Use this Constructor only at emergency because
	 * then you get just an empty object, <br>without check
	 * of the "must to have" properties.
	 *
	 */
	public PicklistEntry() {
		
	}

	/**
	 * Constructor for PicklistEntry. <p>
	 * Use this Constructor instead of the default!
	 * 
	 * @param list The Pickliste of this PicklistEntry
	 */
	public PicklistEntry(IPickliste list) {
        if (list == null) {
            throw new IllegalArgumentException("Error in constructor in class: " + getClass().getName() + "! Arguments not allowed!");
        }

	    this.pickliste = list;
	}
	
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.IPicklistEntry#getPickliste()
	 */
	public IPickliste getPickliste() {
		return pickliste;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.IPicklistEntry#setPickliste(de.xwic.appkit.core.model.entities.IPickliste)
	 */
	public void setPickliste(IPickliste pickliste) {
		this.pickliste = pickliste;
	}


	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.IPicklistEntry#getBezeichnung(java.lang.String)
	 */
	public String getBezeichnung(String languageID) {
	    IPicklisteDAO dao = (IPicklisteDAO)DAOSystem.getDAO(IPicklisteDAO.class);
	    IPicklistText text = (IPicklistText)dao.getPicklistText(this, languageID);
	    return text != null ? text.getBezeichnung() : "";
	}
	
	/*
	 *  (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.IPicklistEntry#setBezeichnung(java.lang.String, java.lang.String)
	 */
	public void setBezeichnung(String languageID, String bezeichnung) {
	    IPicklisteDAO dao = (IPicklisteDAO)DAOSystem.getDAO(IPicklisteDAO.class);
	    
	    IPicklistText text = dao.getPicklistText(this, languageID);
	    text.setBezeichnung(bezeichnung);
	    dao.update(text);
	}

	public Set<IPicklistText> getPickTextValues() {
	  return pickTextValues;
	}
	
	public void setPickTextValues(Set<IPicklistText> pickTextValues) {
	  this.pickTextValues= pickTextValues;
	}

	public boolean isVeraltet() {
		return veraltet;
	}
	
	public void setVeraltet(boolean veraltet) {
		this.veraltet = veraltet;
	}

	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.IPicklistEntry#getKommentar(java.lang.String)
	 */
	public String getKommentar(String langID) {
	    IPicklisteDAO dao = (IPicklisteDAO)DAOSystem.getDAO(IPicklisteDAO.class);
	    IPicklistText text = (IPicklistText)dao.getPicklistText(this, langID);
	    return text != null ? text.getBeschreibung() : "";
	}

	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.IPicklistEntry#setKommentar(java.lang.String, java.lang.String)
	 */
	public void setKommentar(String langID, String comment) {
	    IPicklisteDAO dao = (IPicklisteDAO)DAOSystem.getDAO(IPicklisteDAO.class);
	    
	    IPicklistText text = dao.getPicklistText(this, langID);
	    text.setBeschreibung(comment);
	    dao.update(text);
		
	}
}
