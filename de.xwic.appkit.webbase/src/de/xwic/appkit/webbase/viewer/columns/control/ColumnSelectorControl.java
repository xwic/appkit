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
package de.xwic.appkit.webbase.viewer.columns.control;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.controls.CheckBox;
import de.jwic.controls.InputBox;
import de.jwic.controls.ListBox;
import de.jwic.controls.ListEntry;
import de.jwic.events.ElementSelectedEvent;
import de.jwic.events.ElementSelectedListener;
import de.xwic.appkit.core.config.Bundle;
import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.config.ConfigurationManager;
import de.xwic.appkit.core.config.list.ListColumn;
import de.xwic.appkit.core.config.list.ListSetup;
import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.config.model.Property;
import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.model.daos.IUserListProfileDAO;
import de.xwic.appkit.core.model.entities.IUserListProfile;
import de.xwic.appkit.webbase.utils.UserConfigXmlReader;
import de.xwic.appkit.webbase.utils.UserListUtil;
import de.xwic.appkit.webbase.utils.UserProfileWrapper;
import de.xwic.appkit.webbase.viewer.columns.IColumnSelectorHandler;
import de.xwic.appkit.webbase.viewer.columns.UserListColumn;
import de.xwic.appkit.webbase.viewer.columns.UserListSetup;

/**
 * Defines the control from the column selector dialog
 * 
 * @author Aron Cotrau
 */
public class ColumnSelectorControl extends ControlContainer implements IColumnSelectorHandler {

	private List<String> existingItems = null;
	private List<String> availableItems = null;

	private ListBox listExistingColumns = null;
	private ListBox listAvailableColumns = null;
	private InputBox ibName;
	private CheckBox chkPublic;

	private ListSetup ls;
	private Bundle bundle;

	private String userListProfileName;
	private UserProfileWrapper profile;

	private int ownerId = 0;
	
	/**
	 * @param container
	 * @param existing
	 *            the existing items
	 * @param available
	 *            the available items
	 */
	public ColumnSelectorControl(IControlContainer container) {
		super(container);
		createContents();
	}

	/**
	 * @param container
	 * @param name
	 */
	public ColumnSelectorControl(IControlContainer container, String name) {
		super(container, name);
		createContents();
	}

	private void createContents() {
		listExistingColumns = new ListBox(this, "listExisting");
		listExistingColumns.setFillWidth(true);
		listExistingColumns.setSize(20);
		listExistingColumns.setMultiSelect(true);
		// add double click listener
		listExistingColumns.setChangeNotification(true);
		listExistingColumns.addElementSelectedListener(new ElementSelectedListener() {
			
			@Override
			public void elementSelected(ElementSelectedEvent event) {
				if (event.isDblClick()) {
					handleRemoveSelection();
				}
			}
		});
		
		listAvailableColumns = new ListBox(this, "listAvailable");
		listAvailableColumns.setFillWidth(true);
		listAvailableColumns.setSize(20);
		listAvailableColumns.setMultiSelect(true);
		listAvailableColumns.setChangeNotification(true);
		// add double click listener
		listAvailableColumns.addElementSelectedListener(new ElementSelectedListener() {
			
			@Override
			public void elementSelected(ElementSelectedEvent event) {
				if (event.isDblClick()) {
					handleAddSelection();
				}
			}
		});
		

		ibName = new InputBox(this, "ibName");
		ibName.setFillWidth(true);
		
		chkPublic = new CheckBox(this, "chkPublic");
		chkPublic.setLabel("Public");

		new SelectionsButtonsControl(this, "selectionsButtons", this);
	}

	/**
	 * loads the user list setup
	 * 
	 * @param userLS
	 */
	public void loadUserListSetup(UserProfileWrapper userProfile) {
		if (null == userProfile) {
			return;
		}

		try {

			existingItems = new ArrayList<String>();
			availableItems = new ArrayList<String>();
			
			this.profile = userProfile;
			this.ownerId = profile.getOwnerId();
			
			UserListSetup userLS = UserConfigXmlReader.getUserColumnList(userProfile.getXmlContent());
			userListProfileName = userProfile.getDescription();


			ibName.setText(userListProfileName);
			chkPublic.setChecked(userProfile.isPublicProfile());

			List<?> existing = userLS.getColumns();

			ls = ConfigurationManager.getUserProfile().getListSetup(userLS.getTypeClass(),
					userProfile.getBaseProfileName());
			EntityDescriptor ed = ConfigurationManager.getSetup().getEntityDescriptor(userLS.getTypeClass());
			bundle = ed.getDomain().getBundle(getSessionContext().getLocale().getLanguage());

			int i = 0;
			for (Iterator<?> iter = existing.iterator(); iter.hasNext();) {
				UserListColumn userLC = (UserListColumn) iter.next();
				ListColumn col = getListColumn(userLC, ls);
				if (col != null) {
					String title = getColumnText(col);
					if (!existingItems.contains(title)) {
						existingItems.add(title);
					}
					i++;
				} else {
					iter.remove(); // remove missing entry from user list.
				}
			}
			
			List<?> available = ls.getColumns();
			i = 0;
			for (Iterator<?> it = available.iterator(); it.hasNext();) {
				ListColumn col = (ListColumn) it.next();
				String title = getColumnText(col);

				if (!existingItems.contains(title) && !availableItems.contains(title)) {
					availableItems.add(title);
				}
			}

			// now handle gui
			listExistingColumns.clear();
			listAvailableColumns.clear();

			for (String title : existingItems) {
				listExistingColumns.addElement(title);
			}

			for (String title : availableItems) {
				listAvailableColumns.addElement(title);
			}

		} catch (Exception e) {
			log.error("Cannot load configuration !", e);
		}
	}

	/**
	 * returns the new user column setup
	 */
	public UserProfileWrapper saveListProfile() {
		UserProfileWrapper userProfile = createProfile(userListProfileName);

		return userProfile;
	}

	/**
	 * returns the new user column setup
	 */
	public UserProfileWrapper saveNewListProfile() {
		UserProfileWrapper userProfile = createProfile(getNewListProfileName());

		return userProfile;
	}

	private UserProfileWrapper createProfile(String name) {
		UserListSetup userLS = new UserListSetup();
		Collection<ListEntry> selectedItems = getSelectedColumns();
		List<?> allColumns = ls.getColumns();

		for (Iterator<?> it = selectedItems.iterator(); it.hasNext();) {
			ListEntry entry = (ListEntry) it.next();
			for (Iterator<?> iter = allColumns.iterator(); iter.hasNext();) {
				ListColumn lColumn = (ListColumn) iter.next();
				String lColumnText = getColumnText(lColumn);
				if (lColumnText.equals(entry.title)) {
					UserListColumn userLC = UserListUtil.createUserListColumn(lColumn);

					userLS.addColumn(userLC);
					break;
				}
			}
		}

		userLS.setListId(ls.getListId());
		userLS.setTypeClass(ls.getTypeClass());
		try {
			userLS.setEntityDescriptor(ConfigurationManager.getSetup().getEntityDescriptor(ls.getTypeClass()));
		} catch (ConfigurationException e) {
			log.error("Cannot load configuration !", e);
		}

		IUserListProfile userProfile = UserListUtil.toUserProfile(userLS, name, ownerId);

		DAO dao = DAOSystem.getDAO(IUserListProfileDAO.class);
		userProfile.setPublicProfile(chkPublic.isChecked());

		userProfile.setSortField(null != profile ? profile.getSortField() : null);
		userProfile.setSortDirection(null != profile ? profile.getSortDirection() : 0);
		userProfile.setMaxRows(null != profile ? profile.getMaxRows() : 0);
		
		dao.update(userProfile);

		return UserListUtil.toUserProfileWrapper(userProfile);
	}

	private String getNewListProfileName() {
		return ibName.getText();
	}

	/**
	 * @param userLC
	 * @param lSetup
	 * @return the ListColumn from the ListSetup that has the same propertyId as
	 *         the given parameter
	 */
	private ListColumn getListColumn(UserListColumn userLC, ListSetup lSetup) {
		List<?> columns = lSetup.getColumns();
		for (Iterator<?> iter = columns.iterator(); iter.hasNext();) {
			ListColumn lColumn = (ListColumn) iter.next();
			if (userLC.getPropertyId().equals(lColumn.getPropertyId())) {
				return lColumn;
			}
		}

		return null;
	}

	private String getColumnText(ListColumn col) {
		String title = null;
		if (null != bundle) {
			title = bundle.getString(col.getTitleId());
		}

		if (null == title || "!!".equals(title)) {
			title = getResString(bundle, col.getFinalProperty());
		}
		return title;
	}

	/**
	 * @param key
	 * @return the resource String from the bundle
	 */
	private String getResString(Bundle bundle, Property property) {
		String key = "";
		if (null != property) {
			key = property.getEntityDescriptor().getClassname() + "." + property.getName();
		}
		if (null != bundle) {
			if (!"".equals(key)) {
				return bundle.getString(key);
			}
		} else {
			return "!" + key + "!";
		}

		return "";
	}

	/**
	 * @return the selected elements
	 */
	public Collection<ListEntry> getSelectedColumns() {
		return listExistingColumns.buildEntryList();
	}

	private void addSourceElements(String elements[]) {
		for (int i = 0; i < elements.length; i++) {
			if (null != elements[i]) {
				listAvailableColumns.addElement(elements[i]);
			}
		}
	}

	private void addDestinationElements(String elements[]) {
		for (int i = 0; i < elements.length; i++) {
			if (null != elements[i]) {
				listExistingColumns.addElement(elements[i]);
			}
		}
	}

	public void handleAddAll() {
		Collection<ListEntry> allExisting = listAvailableColumns.buildEntryList();
		String[] titles = new String[allExisting.size()];

		int i = 0;
		for (Iterator<ListEntry> it = allExisting.iterator(); it.hasNext();) {
			ListEntry entry = (ListEntry) it.next();
			titles[i++] = entry.title;
		}
		listAvailableColumns.clear();

		addDestinationElements(titles);
	}

	public void handleAddSelection() {
		String[] selections = listAvailableColumns.getSelectedKeys();
		String[] toAdd = new String[selections.length];

		for (int i = 0; i < selections.length; i++) {
			if (!"".equals(selections[i])) {
				toAdd[i] = getElements(listAvailableColumns, selections[i])[0];
				listAvailableColumns.removeElementByKey(selections[i]);
			}
		}

		addDestinationElements(toAdd);
	}

	public void handleDown() {
		String[] selectedKeys = listExistingColumns.getSelectedKeys();
		Collection<ListEntry> allItems = listExistingColumns.buildEntryList();

		LinkedList<ListEntry> entries = new LinkedList<ListEntry>();
		entries.addAll(allItems);

		if (!"".equals(selectedKeys[0])) {

			listExistingColumns.clear();

			int j = 0;

			int[] indexes = new int[selectedKeys.length];

			for (String key : selectedKeys) {
				int i = 0;
				for (ListEntry entry : entries) {
					if (entry.getKey().equals(key)) {
						indexes[j++] = i;
					}

					i++;
				}
			}

			for (int k = indexes.length - 1; k >= 0; k--) {
				ListEntry entry = entries.get(indexes[k]);
				if (indexes[k] < entries.size() - 1) {
					entries.set(indexes[k], entries.get(indexes[k] + 1));
					entries.set(indexes[k] + 1, entry);
				}
			}

			for (ListEntry listEntry : entries) {
				listExistingColumns.addElement(listEntry.getTitle(), listEntry.getKey());
			}

			listExistingColumns.setSelectedKey(selectedKeys[0]);
		}

	}

	public void handleRemoveSelection() {
		String[] selections = listExistingColumns.getSelectedKeys();
		String[] toRemove = new String[selections.length];

		for (int i = 0; i < selections.length; i++) {
			if (!"".equals(selections[i])) {
				toRemove[i] = getElements(listExistingColumns, selections[i])[0];
				listExistingColumns.removeElementByKey(selections[i]);
			}
		}

		addSourceElements(toRemove);
	}

	public void handleRemoveAll() {
		Collection<ListEntry> allExisting = listExistingColumns.buildEntryList();
		String[] titles = new String[allExisting.size()];

		int i = 0;
		for (Iterator<ListEntry> it = allExisting.iterator(); it.hasNext();) {
			ListEntry entry = (ListEntry) it.next();
			titles[i++] = entry.title;
		}

		listExistingColumns.clear();
		addSourceElements(titles);
	}

	/**
	 * @param key
	 * @return the elements with the given key from the list as a String[]
	 */
	private String[] getElements(ListBox sourceList, String key) {
		List<String> list = new ArrayList<String>();
		for (Iterator<ListEntry> it = sourceList.buildEntryList().iterator(); it.hasNext();) {
			ListEntry listEntry = (ListEntry) it.next();
			if (listEntry.key.equals(key)) {
				list.add(listEntry.title);
			}
		}

		return (String[]) list.toArray(new String[list.size()]);
	}

	public void handleUp() {
		String[] selectedKeys = listExistingColumns.getSelectedKeys();
		Collection<ListEntry> allItems = listExistingColumns.buildEntryList();

		LinkedList<ListEntry> entries = new LinkedList<ListEntry>();
		entries.addAll(allItems);

		if (!"".equals(selectedKeys[0])) {

			listExistingColumns.clear();

			int j = 0;

			int[] indexes = new int[selectedKeys.length];

			for (String key : selectedKeys) {
				int i = 0;
				for (ListEntry entry : entries) {
					if (entry.getKey().equals(key)) {
						indexes[j++] = i;
					}

					i++;
				}
			}

			for (int k = 0; k < indexes.length; k++) {
				if (indexes[k] != 0) {
					ListEntry entry = entries.get(indexes[k] - 1);
					if (indexes[k] < entries.size()) {
						entries.set(indexes[k] - 1, entries.get(indexes[k]));
						entries.set(indexes[k], entry);
					}
				}
			}

			for (ListEntry listEntry : entries) {
				listExistingColumns.addElement(listEntry.getTitle(), listEntry.getKey());
			}

			listExistingColumns.setSelectedKey(selectedKeys[0]);
		}
	}
}
