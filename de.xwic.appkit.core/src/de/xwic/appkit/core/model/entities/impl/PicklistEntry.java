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
	@Override
	public String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	@Override
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the sortIndex
	 */
	@Override
	public int getSortIndex() {
		return sortIndex;
	}

	/**
	 * @param sortIndex the sortIndex to set
	 */
	@Override
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
	@Override
	public IPickliste getPickliste() {
		return pickliste;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.IPicklistEntry#setPickliste(de.xwic.appkit.core.model.entities.IPickliste)
	 */
	@Override
	public void setPickliste(IPickliste pickliste) {
		this.pickliste = pickliste;
	}


	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.IPicklistEntry#getBezeichnung(java.lang.String)
	 */
	@Override
	public String getBezeichnung(String languageID) {
	    IPicklisteDAO dao = DAOSystem.getDAO(IPicklisteDAO.class);
	    IPicklistText text = dao.getPicklistText(this, languageID);
	    return text != null ? text.getBezeichnung() : "";
	}
	
	/*
	 *  (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.IPicklistEntry#setBezeichnung(java.lang.String, java.lang.String)
	 */
	@Override
	public void setBezeichnung(String languageID, String bezeichnung) {
	    IPicklisteDAO dao = DAOSystem.getDAO(IPicklisteDAO.class);
	    
	    IPicklistText text = dao.getPicklistText(this, languageID);
	    
	    if (text == null) {
	    	// this will also call update on the new entity
	    	text = dao.createBezeichnung(this, languageID, bezeichnung);	    	
	    } else {
	    	text.setBezeichnung(bezeichnung);	    	
	    	dao.update(text);
	    }
	}

	@Override
	public Set<IPicklistText> getPickTextValues() {
	  return pickTextValues;
	}
	
	@Override
	public void setPickTextValues(Set<IPicklistText> pickTextValues) {
	  this.pickTextValues= pickTextValues;
	}

	@Override
	public boolean isVeraltet() {
		return veraltet;
	}
	
	@Override
	public void setVeraltet(boolean veraltet) {
		this.veraltet = veraltet;
	}

	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.IPicklistEntry#getKommentar(java.lang.String)
	 */
	@Override
	public String getKommentar(String langID) {
	    IPicklisteDAO dao = DAOSystem.getDAO(IPicklisteDAO.class);
	    IPicklistText text = dao.getPicklistText(this, langID);
	    return text != null ? text.getBeschreibung() : "";
	}

	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.IPicklistEntry#setKommentar(java.lang.String, java.lang.String)
	 */
	@Override
	public void setKommentar(String langID, String comment) {
	    IPicklisteDAO dao = DAOSystem.getDAO(IPicklisteDAO.class);
	    
	    IPicklistText text = dao.getPicklistText(this, langID);
	    if (text != null) {
	    	text.setBeschreibung(comment);
	    	dao.update(text);
	    }
	}
}
