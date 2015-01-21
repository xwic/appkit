/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.model.entities.impl.PicklistCache
 * Created on 05.06.2008 by Florian Lippisch
 *
 */
package de.xwic.appkit.core.model.daos.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import de.xwic.appkit.core.cluster.ClusterManager;
import de.xwic.appkit.core.cluster.util.ClusterCollections;
import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.model.daos.LangKey;
import de.xwic.appkit.core.model.entities.IPicklistEntry;
import de.xwic.appkit.core.model.entities.IPicklistText;
import de.xwic.appkit.core.model.entities.IPickliste;

/**
 * Encapsulates the picklist entities.
 * 
 * @author Florian Lippisch
 */
public class PicklistCache {

	private Map<String, IPickliste> allLists = new HashMap<String, IPickliste>();
	/** map of all entries - is needed in local dao */
	protected Map<Integer, IPicklistEntry> allEntries = new HashMap<Integer, IPicklistEntry>();
	private Map<LangKey, IPicklistText> allTexts = new HashMap<LangKey, IPicklistText>();
	private Map<String, EntityList<IPicklistEntry>> allListsWithEntries = new HashMap<String, EntityList<IPicklistEntry>>();
	private boolean convertedToClusterMap = false;

	/**
	 * @param id
	 * @return
	 */
	public IPicklistText getPicklistTextById(int id) {

		for (Iterator<IPicklistText> it = allTexts.values().iterator(); it.hasNext();) {
			IPicklistText pt = it.next();
			if (pt.getId() == id) {
				return pt;
			}
		}
		return null;
	}

	/**
	 * Write PicklistText.
	 * 
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
	public IPicklistText getPicklistText(int entryId, String langId) {
		IPicklistText text = allTexts.get(new LangKey(entryId, langId));
		return text;
	}

	/**
	 * @param key
	 * @return
	 */
	public EntityList<IPicklistEntry> getEntryList(String key) {
		return allListsWithEntries.get(key);
	}

	/**
	 * @param key
	 * @param erg
	 */
	public void putEntryList(String key, EntityList<IPicklistEntry> erg) {
		allListsWithEntries.put(key, erg);
	}

	/**
	 * @param e
	 */
	public void putPicklistEntry(IPicklistEntry e) {
		allEntries.put(new Integer(e.getId()), e);

		String key = e.getPickliste().getKey();
		EntityList<IPicklistEntry> sublist = allListsWithEntries.get(key);
		// if the list does not exist, the list can not be created here. If so, a single request for
		// an
		// entry would create a list that does not contain all entries.
		// if (sublist == null) {
		// sublist = new EntityList(new ArrayList(), null, 0);
		// allListsWithEntries.put(key, sublist);
		// }
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
	public IPicklistEntry getPicklistEntry(int id) {
		return allEntries.get(new Integer(id));
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
		allEntries.remove(new Integer(entry.getId()));
		String key = entry.getPickliste().getKey();
		EntityList<IPicklistEntry> sublist = allListsWithEntries.get(key);
		if (sublist != null) {
			sublist.remove(entry);
		}

		for (Iterator<LangKey> it = allTexts.keySet().iterator(); it.hasNext();) {
			LangKey lk = it.next();
			if (lk.getId() == entry.getId()) {
				it.remove();
			}
		}

	}

	/**
	 * 
	 */
	public void toClusterCache() {
		if (!convertedToClusterMap) {
			allLists = ClusterCollections.toCacheMap(allLists, "allPicklists");
			allEntries = ClusterCollections.toCacheMap(allEntries, "allPicklistEntries");
			allTexts = ClusterCollections.toCacheMap(allTexts, "allTexts");
			allListsWithEntries = ClusterCollections.toCacheMap(allListsWithEntries,
					"allPicklistsWithPicklistEntries");
			convertedToClusterMap = true;
		}
	}

}
