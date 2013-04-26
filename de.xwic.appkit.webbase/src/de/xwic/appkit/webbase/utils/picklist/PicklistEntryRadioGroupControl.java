/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.webbase.utils.picklist.PicklistEntryRadioGroup
 * Created on Sep 9, 2008 by Aron Cotrau
 *
 */
package de.xwic.appkit.webbase.utils.picklist;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.jwic.base.IControlContainer;
import de.jwic.controls.RadioGroup;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.model.daos.IPicklisteDAO;
import de.xwic.appkit.core.model.entities.IPicklistEntry;

/**
 * Custom radio group control which displays the entries of a pickliste
 * 
 * @author Aron Cotrau
 */
public class PicklistEntryRadioGroupControl extends RadioGroup {

	private String lang = "DE";

	private Map<Integer, IPicklistEntry> entries;

	private IPicklisteDAO plDao = null;
	private String picklistKey = null;

	private Comparator<IPicklistEntry> comparator = null;

	/**
	 * @param container
	 * @param name
	 */
	public PicklistEntryRadioGroupControl(IControlContainer container, String name, String picklistKey) {
		this(container, name, picklistKey, null);
	}

	/**
	 * @param container
	 * @param name
	 * @param picklistKey
	 * @param comparator
	 */
	public PicklistEntryRadioGroupControl(IControlContainer container, String name, String picklistKey,
			Comparator<IPicklistEntry> comparator) {
		super(container, name);

		plDao = (IPicklisteDAO) DAOSystem.getDAO(IPicklisteDAO.class);
		this.picklistKey = picklistKey;
		setTemplateName(RadioGroup.class.getName());
		this.lang = getSessionContext().getLocale().getLanguage();
		this.comparator = comparator == null ? new PicklistEntryComparator(lang) : comparator;
		setupEntries(picklistKey);
	}

	/**
	 * set an internal array for the given list of entries
	 * 
	 * @param picklistKey
	 */
	@SuppressWarnings("unchecked")
	private void setupEntries(String picklistKey) {

		List<IPicklistEntry> entryList = plDao.getAllEntriesToList(picklistKey);

		if (null != entryList) {
			entries = new HashMap<Integer, IPicklistEntry>();
			for (Iterator<IPicklistEntry> it = entryList.iterator(); it.hasNext();) {
				IPicklistEntry entry = (IPicklistEntry) it.next();
				if (!entry.isVeraltet()) {
					entries.put(new Integer(entry.getId()), entry);
				}
			}
			// sort the list
			Collections.sort(entryList, comparator);

			// add entries into combo
			for (int i = 0; i < entryList.size(); i++) {
				IPicklistEntry entry = (IPicklistEntry) entryList.get(i);
				if (!entry.isVeraltet()) {
					addElement(entry.getBezeichnung(lang), Integer.toString(entry.getId()));
				}
			}
		}
	}

	/**
	 * select the item corresponding to the given entry id
	 * 
	 * @param pEntry
	 */
	public void selectEntry(IPicklistEntry pEntry) {
		if (null != entries && null != pEntry) {
			boolean found = false;

			if (entries.containsKey(new Integer(pEntry.getId()))) {
				found = true;
				setSelectedKey(Integer.toString(pEntry.getId()));
			}

			if (!found && pEntry.isVeraltet()) {
				entries.put(new Integer(pEntry.getId()), pEntry);
				addElement("[" + pEntry.getBezeichnung(lang) + "]", Integer.toString(pEntry.getId()));
				setSelectedKey(Integer.toString(entries.size() - 1));
			}

		} 

	}
	
	/**
	 * @param pEntryKey
	 */
	public void selectEntry(String pEntryKey) {
		
		IPicklistEntry plEntry = plDao.getPickListEntryByKey(picklistKey, pEntryKey);
		selectEntry(plEntry);
		
	}

	/**
	 * @return the id of the selected entry
	 */
	public IPicklistEntry getSelectionEntry() {
		if (getSelectedKey() == null || getSelectedKey().length() < 1) {
			return null;
		}

		int i = Integer.parseInt(getSelectedKey());

		if (null != entries) {
			if (entries.containsKey(new Integer(i))) {
				IPicklistEntry entry = (IPicklistEntry) entries.get(new Integer(i));
				return entry;
			}
		}
		
		return null;
	}

	/**
	 * @return Returns the picklistKey.
	 */
	public String getPicklistKey() {
		return picklistKey;
	}

	/**
	 * @param picklistKey
	 *            The picklistKey to set.
	 */
	public void setPicklistKey(String picklistKey) {
		this.picklistKey = picklistKey;
	}

}
