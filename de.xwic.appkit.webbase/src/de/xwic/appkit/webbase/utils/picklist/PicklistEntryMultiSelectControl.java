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
/**
 *
 */
package de.xwic.appkit.webbase.utils.picklist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

import de.jwic.base.IControlContainer;
import de.jwic.controls.combo.DropDown;
import de.jwic.data.ISelectElement;
import de.jwic.events.ElementSelectedEvent;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.model.daos.IPicklisteDAO;
import de.xwic.appkit.core.model.entities.IPicklistEntry;
import de.xwic.appkit.webbase.toolkit.components.IEntityListBoxMultiControl;

/**
 * Multi Select control for picklists. It's a dropdown with checkboxes showing the selected picklists as text.
 * 
 * @author Andrei Pat
 *
 */
public class PicklistEntryMultiSelectControl extends DropDown implements IEntityListBoxMultiControl<IPicklistEntry> {

	protected String picklistKey;

	protected IPicklisteDAO plDao;

	/**
	 * @param container
	 * @param name
	 * @param picklistKey
	 */
	public PicklistEntryMultiSelectControl(IControlContainer container, String name, String picklistKey) {
		super(container, name);
		this.picklistKey = picklistKey;
		loadPicklists(picklistKey);
		setMultiSelect(true);
	}

	/**
	 * @param picklistKey
	 */
	private void loadPicklists(String picklistKey) {
		String langId = getSessionContext().getLocale().getLanguage();
		plDao = DAOSystem.getDAO(IPicklisteDAO.class);
		List<IPicklistEntry> entries = plDao.getAllEntriesToList(picklistKey);
		Collections.sort(entries, new PicklistEntryComparator(langId));
		for (IPicklistEntry pe : entries) {
			String text = pe.getBezeichnung(langId);
			addElement(text, pe.getKey());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.toolkit.components.IEntityListBoxMultiControl#selectEntries(java.util.Set)
	 */
	@Override
	public void selectEntries(Set<IPicklistEntry> entities) {
		if (entities == null) {
			return;
		}
		Function<IPicklistEntry, String> peToKey = IPicklistEntry::getKey;
		List<String> peKeys = entities.stream().map(peToKey).collect(Collectors.toList());
		setSelectedKey(StringUtils.join(peKeys.iterator(), ';'));
		//combo and dropdown are not working nicely with multiselect, so workaround
		List<String> labels = new ArrayList<>();
		for (String key : peKeys) {
			labels.add(getTextForKey(key));
		}
		setText(StringUtils.join(labels, ';'));
		fireElementSelectedEvent(new ElementSelectedEvent(this, getSelectedElement()));
	}

	/**
	 * @param key
	 * @return
	 */
	private String getTextForKey(String key) {
		for (ISelectElement se : elements) {
			if (key.equals(se.getKey())) {
				return se.getTitle();
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.toolkit.components.IEntityListBoxMultiControl#getSelectedEntries()
	 */
	@Override
	public Set<IPicklistEntry> getSelectedEntries() {
		String key = getSelectedKey();
		if (key == null || key.isEmpty()) {
			return new HashSet<>();
		}
		String[] keys = key.split(";");

		Function<String, IPicklistEntry> keyToPe = new Function<String, IPicklistEntry>() {

			@Override
			public IPicklistEntry apply(String key) {
				return plDao.getPickListEntryByKey(picklistKey, key);
			}
		};

		Set<IPicklistEntry> result = Arrays.asList(keys).stream().map(keyToPe).collect(Collectors.toSet());

		return result;
	}
}
