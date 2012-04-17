/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.webbase.utils.picklist.PicklistEntryCheckboxControl
 * Created on Jan 26, 2009 by Aron Cotrau
 *
 */
package de.xwic.appkit.webbase.utils.picklist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.jwic.base.IControlContainer;
import de.jwic.controls.CheckboxControl;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.model.daos.IPicklisteDAO;
import de.xwic.appkit.core.model.entities.IPicklistEntry;

/**
 * Custom control for displaying picklist entries from a pickliste using
 * checkboxes, for multiselection
 * 
 * @author Aron Cotrau
 */
public class PicklistEntryCheckboxControl extends CheckboxControl {

	private String lang = "DE";

	private Map<String, IPicklistEntry> entries;
	private IPicklisteDAO plDao = null;
	private Comparator<IPicklistEntry> comparator = null;

	/**
	 * @param container
	 * @param name
	 */
	public PicklistEntryCheckboxControl(IControlContainer container, String name, String picklistKey) {
		this(container, name, picklistKey, null);
	}

	/**
	 * @param container
	 * @param name
	 * @param picklistKey
	 * @param comparator
	 */
	public PicklistEntryCheckboxControl(IControlContainer container, String name, String picklistKey,
			Comparator<IPicklistEntry> comparator) {
		this(container, name, picklistKey, comparator, 0);

	}

	/**
	 * @param container
	 * @param name
	 * @param picklistKey
	 * @param comparator
	 * @param colsNr
	 */
	public PicklistEntryCheckboxControl(IControlContainer container, String name, String picklistKey,
			Comparator<IPicklistEntry> comparator, int colsNr) {
		super(container, name);

		plDao = (IPicklisteDAO) DAOSystem.getDAO(IPicklisteDAO.class);
		setTemplateName(CheckboxControl.class.getName());
		this.lang = getSessionContext().getLocale().getLanguage();
		this.comparator = comparator == null ? new PicklistEntryComparator(lang) : comparator;

		if (colsNr > 0) {
			setColumns(colsNr);
		}

		setupEntries(lang, picklistKey);
	}

	/**
	 * set an internal array for the given list of entries
	 * 
	 * @param lang
	 * @param entryList
	 */
	@SuppressWarnings("unchecked")
	private void setupEntries(String lang, String picklistKey) {

		this.lang = lang;

		List<IPicklistEntry> entryList = plDao.getAllEntriesToList(picklistKey);

		if (null != entryList) {

			entries = new HashMap<String, IPicklistEntry>();

			for (Iterator<?> it = entryList.iterator(); it.hasNext();) {
				IPicklistEntry entry = (IPicklistEntry) it.next();
				if (!entry.isVeraltet()) {
					Object key = entry.getKey() != null ? entry.getKey() : new Integer(entry.getId());
					entries.put(key.toString(), entry);
				}
			}

			// sort the list
			Collections.sort(entryList, comparator);

			// add entries
			for (int i = 0; i < entryList.size(); i++) {
				IPicklistEntry entry = (IPicklistEntry) entryList.get(i);
				if (!entry.isVeraltet()) {
					Object key = entry.getKey() != null ? entry.getKey() : new Integer(entry.getId());
					addElement(entry.getBezeichnung(lang), key.toString());
				}
			}
		}
	}

	/**
	 * selects the given entry
	 * 
	 * @param entry
	 */
	public void selectEntry(IPicklistEntry entry) {
		Object key = entry.getKey() != null ? entry.getKey() : new Integer(entry.getId());
		setSelectedKey(key.toString());
	}

	/**
	 * selects the entries from the list
	 * 
	 * @param entries
	 */
	public void selectEntries(List<IPicklistEntry> plEntries) {
		if (null != plEntries) {
			String key = "";
			for (IPicklistEntry entry : plEntries) {
				key += entry.getKey() != null ? entry.getKey() : new Integer(entry.getId()).toString();
				key +=";";
			}
			
			setSelectedKey(key);
		}
	}

	/**
	 * @return a list of selected entries
	 */
	public List<IPicklistEntry> getSelectedEntries() {
		List<IPicklistEntry> selectedEntries = new ArrayList<IPicklistEntry>();
		String[] keys = getSelectedKeys();

		for (String key : keys) {
			IPicklistEntry entry = entries.get(key);
			if (null != entry) {
				selectedEntries.add(entry);
			}
		}

		return selectedEntries;
	}

}
