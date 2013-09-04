/**
 *
 */
package de.xwic.appkit.webbase.utils.picklist;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import de.jwic.base.IControlContainer;
import de.xwic.appkit.core.model.entities.IPicklistEntry;
import de.xwic.appkit.core.util.CollectionUtil;
import de.xwic.appkit.core.util.ILazyEval;
import de.xwic.appkit.webbase.toolkit.components.IEntityListBoxMultiControl;

/**
 * @author Alexandru Bledea
 * @since Aug 14, 2013
 */
public class PicklistEntryMultiSelectControl extends PicklistEntryControl implements IEntityListBoxMultiControl<IPicklistEntry> {

	private final ILazyEval<String, IPicklistEntry> keyToEntity;
	private final ILazyEval<IPicklistEntry, String> entityToKey;

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
		keyToEntity = new ILazyEval<String, IPicklistEntry>() {

			@Override
			public IPicklistEntry evaluate(String obj) {
				return getEntry(obj);
			}
		};
		entityToKey = new ILazyEval<IPicklistEntry, String>() {

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
		return CollectionUtil.createCollection(getSelectedKeys(), keyToEntity, new HashSet<IPicklistEntry>());
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
		HashSet<String> keys = CollectionUtil.createCollection(entities, entityToKey, new HashSet<String>());
		setSelectedKey(StringUtils.join(keys.iterator(), ';'));
	}
}
