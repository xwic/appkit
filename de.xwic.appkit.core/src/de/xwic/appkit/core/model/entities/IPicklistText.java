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
