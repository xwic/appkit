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
package de.xwic.appkit.webbase.utils.picklist;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.jwic.base.IControlContainer;
import de.jwic.controls.ListBox;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.model.daos.IPicklisteDAO;
import de.xwic.appkit.core.model.entities.IPicklistEntry;
import de.xwic.appkit.webbase.toolkit.components.IEntityListBoxControl;

/**
 * PicklistSingleSelection control.
 *
 * Created on 19.02.2008
 * @author Ronny Pfretzschner
 */
public class PicklistEntryControl extends ListBox implements IPicklistEntryControl, IEntityListBoxControl<IPicklistEntry> {

	private String lang = "DE";
	private boolean allowEmptySelection = true;
	protected Map<Integer, IPicklistEntry> entries;

	private IPicklisteDAO plDao = null;
	private String picklistKey = null;

	private Comparator<IPicklistEntry> comparator = null;

	private String emptySelectionText = "";

	/**
	 * @param container
	 * @param name
	 * @param picklistKey
	 */
	public PicklistEntryControl(IControlContainer container, String name, String picklistKey) {
		this(container, name, picklistKey, true, null);
	}

	/**
	 * @param container
	 * @param name
	 * @param picklistKey
	 * @param allowEmptySelection
	 * @param comparator
	 */
	public PicklistEntryControl(IControlContainer container, String name, String picklistKey, boolean allowEmptySelection, Comparator<IPicklistEntry> comparator) {
		this(container, name, allowEmptySelection, comparator);
		this.picklistKey = picklistKey;
		setupEntries(lang, picklistKey);
	}

	/**
	 * @param container
	 * @param name
	 * @param entryList
	 * @param allowEmptySelection
	 */
	public PicklistEntryControl(IControlContainer container, String name, List<IPicklistEntry> entryList, boolean allowEmptySelection) {
		this(container, name, allowEmptySelection, null);
		setupEntries(lang, entryList);
	}

	/**
	 * @param container
	 * @param name
	 * @param allowEmptySelection
	 * @param comparator
	 */
	private PicklistEntryControl(IControlContainer container, String name, boolean allowEmptySelection, Comparator<IPicklistEntry> comparator) {
		super(container, name);
		plDao = DAOSystem.getDAO(IPicklisteDAO.class);
		this.lang = getSessionContext().getLocale().getLanguage();
		this.comparator = comparator == null ? new PicklistEntryComparator(lang) : comparator;
		setTemplateName(ListBox.class.getName());
	}

	/**
	 * set an internal array for the given list of entries
	 * @param lang
	 * @param entryList
	 */
	protected void setupEntries(String lang, List<IPicklistEntry> entryList){
		clear();
		this.lang = lang;


		if (null != entryList){
			entries = new HashMap<Integer, IPicklistEntry>();

			for (IPicklistEntry iPicklistEntry : entryList) {
				IPicklistEntry entry = iPicklistEntry;
				if (!entry.isVeraltet()) {
					entries.put(new Integer(entry.getId()), entry);
					this.picklistKey = entry.getPickliste().getKey();
				}
			}

			// sort the list
			Collections.sort(entryList, comparator);

			//add empty selection
			if (allowEmptySelection){
				entries.put(new Integer(0), null);
				addElement(emptySelectionText, "0");
			}

			String preSelection = null;

			// add entries into combo
			for (int i = 0; i < entryList.size(); i++) {
				IPicklistEntry entry = entryList.get(i);
				if (!entry.isVeraltet()) {
					addElement(entry.getBezeichnung(lang), Integer.toString(entry.getId()));
				}

				if (i == 0) {
					preSelection = Integer.toString(entry.getId());
				}

			}

			if (allowEmptySelection) {
				setSelectedKey("0");
			} else if (preSelection != null){
				setSelectedKey(preSelection);
			}
		}
	}


	/**
	 * set an internal array for the given list of entries
	 * @param lang
	 * @param entryList
	 */
	@SuppressWarnings("unchecked")
	protected void setupEntries(String lang, String picklistKey){
		clear();
		this.lang = lang;

		List<IPicklistEntry> entryList = plDao.getAllEntriesToList(picklistKey);

		setupEntries(lang, entryList);
	}


	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.components.IEntityListBoxControl#selectEntry(de.xwic.appkit.core.dao.IEntity)
	 */
	@Override
	public void selectEntry(IPicklistEntry pEntry){
		String key = getKey(pEntry);
		if (key != null) {
			setSelectedKey(key);
		}
	}

	/**
	 * @param pEntry
	 * @return
	 */
	protected String getKey(IPicklistEntry pEntry) {
		String key = null;
		if (null != entries && null != pEntry) {
			boolean found = false;

			int id = pEntry.getId();
			if (entries.containsKey(new Integer(id))) {
				found = true;
				key = Integer.toString(id);
			}

			if (!found && pEntry.isVeraltet()) {
				entries.put(new Integer(id), pEntry);
				addElement("[" + pEntry.getBezeichnung(lang) + "]", Integer.toString(id));
				key = Integer.toString(entries.size() - 1);
			}

		} else {
			if (entries != null && pEntry == null && allowEmptySelection) {
				key = "0";
			}
		}
		return key;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.components.IEntityListBoxControl#getSelectedEntry()
	 */
	@Override
	public IPicklistEntry getSelectedEntry() {
		return getEntry(getSelectedKey());
	}

	/**
	 * @param picklistKey
	 */
	public void selectEntryByKey(String picklistKey) {
		if (picklistKey != null) {
			Collection<IPicklistEntry> values = entries.values();
			for (IPicklistEntry pe : values) {
				if (pe.getKey().equals(picklistKey)) {
					selectEntry(pe);
					return;
				}
			}
		}
	}

	/**
	 * @param idString
	 * @return
	 */
	protected IPicklistEntry getEntry(String idString){
		if (idString == null || idString.length() < 1) {
			return null;
		}

		int i = Integer.parseInt(idString);

		if (null != entries){
			if (entries.containsKey(new Integer(i))){
				IPicklistEntry entry = entries.get(new Integer(i));
				return entry;
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.utils.picklist.IPicklistEntryControl#getPicklistKey()
	 */
	@Override
	public String getPicklistKey() {
		return picklistKey;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.utils.picklist.IPicklistEntryControl#setPicklistKey(java.lang.String)
	 */
	@Override
	public void setPicklistKey(String picklistKey) {
		this.picklistKey = picklistKey;
	}

	/**
	 * @param allowEmptySelection the allowEmptySelection to set
	 */
	public void setAllowEmptySelection(boolean allowEmptySelection) {
		this.allowEmptySelection = allowEmptySelection;
		setupEntries(lang, picklistKey);
	}

	/**
	 * @return the emptySelectionText
	 */
	public String getEmptySelectionText() {
		return emptySelectionText;
	}

	/**
	 * @param emptySelectionText the emptySelectionText to set
	 */
	public void setEmptySelectionText(String emptySelectionText) {
		this.emptySelectionText = emptySelectionText;
		setupEntries(lang, picklistKey);
	}

}
