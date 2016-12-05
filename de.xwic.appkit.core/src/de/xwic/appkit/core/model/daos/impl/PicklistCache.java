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
package de.xwic.appkit.core.model.daos.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.model.daos.LangKey;
import de.xwic.appkit.core.model.entities.IPicklistEntry;
import de.xwic.appkit.core.model.entities.IPicklistText;
import de.xwic.appkit.core.model.entities.IPickliste;

/**
 * Encapsulates the picklist entities.
 * @author Florian Lippisch
 */
public class PicklistCache {

	private Map<String, IPickliste> allLists = new HashMap<String, IPickliste>();
	/** map of all entries - is needed in local dao */
	protected Map<Long, IPicklistEntry> allEntries = new HashMap<Long, IPicklistEntry>();
	private Map<LangKey, IPicklistText> allTexts = new HashMap<LangKey, IPicklistText>();
	private Map<String, EntityList> allListsWithEntries = new HashMap<String, EntityList>();
	
	/**
	 * @param id
	 * @return
	 */
	public IPicklistText getPicklistTextById(long id) {
    	
		for (Iterator<IPicklistText> it = allTexts.values().iterator(); it.hasNext(); ) {
    		IPicklistText pt = it.next();
    		if (pt.getId() == id) {
    			return pt;
    		}
    	}
    	return null;
	}

	/**
	 * Write PicklistText.
	 * @param e
	 */
	public void putPicklistText(IPicklistText e) {
		allTexts.put(new LangKey(e.getPicklistEntry().getId(), e.getLanguageID()), e);
	}

	/**
	 * @param id
	 * @param langID
	 * @return
	 */
	public IPicklistText getPicklistText(long entryId, String langId) {
		IPicklistText text = allTexts.get(new LangKey(entryId, langId));
		return text;
	}

	/**
	 * @param key
	 * @return
	 */
	public EntityList getEntryList(String key) {
		return allListsWithEntries.get(key);
	}

	/**
	 * @param key
	 * @param erg
	 */
	public void putEntryList(String key, EntityList erg) {
		allListsWithEntries.put(key, erg);
	}

	/**
	 * @param e
	 */
	public void putPicklistEntry(IPicklistEntry e) {
		allEntries.put(new Long(e.getId()), e);
		
		String key = e.getPickliste().getKey();
		EntityList sublist = allListsWithEntries.get(key);
		// if the list does not exist, the list can not be created here. If so, a single request for an 
		// entry would create a list that does not contain all entries.
//		if (sublist == null) {
//			sublist = new EntityList(new ArrayList(), null, 0);
//			allListsWithEntries.put(key, sublist);
//		}
		if (null != sublist && !contains(e, sublist)) {
			sublist.add(e);
		}

	}
	
	/**
	 * @param e
	 * @param sublist
	 * @return
	 */
	private boolean contains(IPicklistEntry e, EntityList<IPicklistEntry> sublist) {
		for (IPicklistEntry pe : sublist) {
			if (pe.getId() == e.getId()) {
				return true;
			}
		}
		
		return false;
	}

	/**
	 * @param id
	 * @return
	 */
	public IPicklistEntry getPicklistEntry(long id) {
		return allEntries.get(new Long(id));
	}

	/**
	 * @param key
	 * @return
	 */
	public IPickliste getPicklisteByKey(String key) {
		return allLists.get(key);	
	}

	/**
	 * @param p
	 */
	public void putPickliste(IPickliste p) {
		allLists.put(p.getKey(), p);
	}

	/**
	 * 
	 */
	public void clear() {
		allLists.clear();
		allEntries.clear();
		allListsWithEntries.clear();
		allTexts.clear();
	}

	/**
	 * @param entry
	 */
	public void removePicklistEntry(IPicklistEntry entry) {
		allEntries.remove(new Long(entry.getId()));
		String key = entry.getPickliste().getKey();
		EntityList sublist = allListsWithEntries.get(key);
		if (sublist != null) {
			sublist.remove(entry);
		}
		
		for (Iterator<LangKey> it = allTexts.keySet().iterator(); it.hasNext(); ) {
			LangKey lk = it.next();
			if (lk.getId() == entry.getId()) {
				it.remove();
			}
		}

	}

	
}
