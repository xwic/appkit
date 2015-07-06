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

import java.util.Arrays;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import de.jwic.base.IControlContainer;
import de.xwic.appkit.core.model.entities.IPicklistEntry;
import de.xwic.appkit.core.util.CollectionUtil;
import de.xwic.appkit.core.util.Function;
import de.xwic.appkit.webbase.toolkit.components.IEntityListBoxMultiControl;

/**
 * @author Alexandru Bledea
 * @since Aug 14, 2013
 */
public class PicklistEntryMultiSelectControl extends PicklistEntryControl implements IEntityListBoxMultiControl<IPicklistEntry> {

	private final Function<String, IPicklistEntry> keyToEntity;
	private final Function<IPicklistEntry, String> entityToKey;

	/**
	 * @param container
	 * @param name
	 * @param picklistKey
	 */
	public PicklistEntryMultiSelectControl(IControlContainer container, String name, String picklistKey) {
		this(container, name, picklistKey, true);
	}

	/**
	 * @param container
	 * @param name
	 * @param picklistKey
	 * @param noScroll
	 */
	public PicklistEntryMultiSelectControl(IControlContainer container, String name, String picklistKey, boolean noScroll) {
		super(container, name, picklistKey);
		setMultiSelect(true);
		setChangeNotification(true);
		if (noScroll && entries != null) {
			setSize(entries.size());
		}
		keyToEntity = new Function<String, IPicklistEntry>() {

			@Override
			public IPicklistEntry evaluate(String obj) {
				return getEntry(obj);
			}
		};
		entityToKey = new Function<IPicklistEntry, String>() {

			@Override
			public String evaluate(IPicklistEntry obj) {
				return getKey(obj);
			}
		};
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.utils.picklist.PicklistEntryControl#selectEntry(de.xwic.appkit.core.model.entities.IPicklistEntry)
	 */
	@Override
	@Deprecated
	public void selectEntry(IPicklistEntry pEntry) {
		super.selectEntry(pEntry);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.utils.picklist.PicklistEntryControl#getSelectedEntry()
	 */
	@Override
	@Deprecated
	public IPicklistEntry getSelectedEntry() {
		return super.getSelectedEntry();
	}

	/**
	 * @return
	 */
	@Override
	public Set<IPicklistEntry> getSelectedEntries() {
		return CollectionUtil.createSet(Arrays.asList(getSelectedKeys()), keyToEntity);
	}

	/* (non-Javadoc)
	 * @see de.jwic.controls.AbstractListControl#getSelectedKeys()
	 */
	@Override
	public String[] getSelectedKeys() {
		String[] selectedKeys = super.getSelectedKeys();
		return selectedKeys == null ? new String[0] : selectedKeys;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.components.IEntityListBoxMultiControl#selectEntries(java.util.Set)
	 */
	@Override
	public void selectEntries(Set<IPicklistEntry> entities) {
		final Set<String> keys = CollectionUtil.createSet(entities, entityToKey);
		setSelectedKey(StringUtils.join(keys.iterator(), ';'));
	}
}
