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
 * de.xwic.appkit.core.model.daos.IPicklisteDAO
 * Created on 07.07.2005
 *
 */
package de.xwic.appkit.core.model.daos;

import java.util.Collection;
import java.util.Set;

import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.dao.event.DAOWithEvent;
import de.xwic.appkit.core.model.entities.IPicklistEntry;
import de.xwic.appkit.core.model.entities.IPicklistText;
import de.xwic.appkit.core.model.entities.IPickliste;

/**
 * DAO implementation for the IPickliste entity type.
 * All necessary methods for creating Picklist parts like
 * entries, texts are provided with this DAO.
 *
 * @author Florian Lippisch
 */
public interface IPicklisteDAO extends DAOWithEvent<IPickliste> {

	/**
 	 * Create a new IPickliste instance. <p>
	 * An update is not necessary, because it is done already
	 * in this methode.
	 *
	 * @param key for the Pickliste
	 * @param title
	 * @param beschreibung
	 * @return new IPickListe instance
	 */
    public IPickliste create(String key, String title, String beschreibung);

	/**
 	 * Create a new IPicklistEntry instance for the given list. <p>
	 * An update is not necessary, because it is done already
	 * in this methode.
	 *
	 * @param the list, for which the Entry has to be created for
	 * @return new IPicklistEntry instance
	 */
    public IPicklistEntry createPickListEntry(IPickliste list);

    /**
 	 * Create a new IPicklistText instance for the given PickListEntry. <p>
	 * An update is not necessary, because it is done already
	 * in this methode.
     *
     * @param the entry, for which the Bezeichnung has to be created for
     * @param langID
     * @param bezeichnung for the given lang_id to the specific entry
     * @return new IPicklistText instance
     */
    public IPicklistText createBezeichnung(IPicklistEntry entry, String langID, String bezeichnung);

    /**
     * Returns the IPicklistText to a specific PicklistEntry and the given language ID. <p>
     *
     * @param entry
     * @param langID
     * @return IPicklistText
     */
    public IPicklistText getPicklistText(IPicklistEntry entry, String langID);

    /**
     * Returns all Entries for one given Pickliste. <p>
     * If nothing is found, the Entitylist is empty.
     *
     * @param list
     * @return EntityList
     */
    public EntityList<IPicklistEntry> getAllEntriesToList(IPickliste list);

    /**
     * Get all entries to a List by Picklistkey. <p>
     *
     * @param pickListKey as a String
     * @return all Entries to the list
     */
    public EntityList<IPicklistEntry> getAllEntriesToList(String pickListKey);

    /**
     * This methode should be used, when you want to search for PickListEntries. <p>
     *
     * The methode getEntityById() does not work in this case, because it holds
     * the PickListe as class identifier. <br>
     *
     * @param id
     * @return IPicklistEntry
     */
    public IPicklistEntry getPickListEntryByID(long id);

    /**
     * @param id
     * @return
     */
    Set<IPicklistEntry> getPicklistEntriesByID(Collection<Long> id);

    /**
     * Returns the picklistText object with the specified id.
     * @param id
     * @return
     */
    public IPicklistText getPickListTextByID(long id);

    /**
     * Get a Pickliste from the database by the given key. <p>
     *
     * The keys are public variables of the IPickliste interface.
     *
     * @param key of the Pickliste type
     * @return IPickliste
     */
    public IPickliste getPicklisteByKey(String key);

    /**
     * Get a PicklistEntry from the database by the given key. <p>
     *
     * @param key of the Pickliste type
     * @return IPicklistEntry
     */
    public IPicklistEntry getPickListEntryByKey(String listKey, String key);


	/**
	 * Drops the cache. <p>
	 */
    public void dropCache();

    /**
     * Loads all Picklist, PicklistEntry and PicklistText objects into the cache.
     */
    public void cacheAll();

    /**
     * Creates a Picklistentry without update and without picklist relation.
     *
     * @return an unsaved entity!
     */
    public IPicklistEntry createPicklistEntry();

}
