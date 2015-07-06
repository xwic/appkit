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
package de.xwic.appkit.webbase.utils;

import java.util.Iterator;
import java.util.List;

import de.xwic.appkit.core.config.list.ListColumn;
import de.xwic.appkit.core.config.list.ListSetup;
import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.dao.Limit;
import de.xwic.appkit.core.model.daos.IMitarbeiterDAO;
import de.xwic.appkit.core.model.daos.IUserListProfileDAO;
import de.xwic.appkit.core.model.entities.IMitarbeiter;
import de.xwic.appkit.core.model.entities.IUserListProfile;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.webbase.viewer.columns.UserListColumn;
import de.xwic.appkit.webbase.viewer.columns.UserListSetup;

/**
 * Util class or the User List setups operations
 * 
 * @author Aron Cotrau
 */
public class UserListUtil {

	private final static int UPPER_DIF = 'A' - 'a';

	/**
	 * @param listColumn
	 * @return a {@link UserListColumn} with the infos of the given listColumn
	 */
	public static UserListColumn createUserListColumn(ListColumn listColumn) {
		UserListColumn column = new UserListColumn();
		column.setPropertyId(listColumn.getPropertyId());
		column.setWidth(listColumn.getDefaultWidth());

		return column;
	}

	/**
	 * @param setup
	 * @param userColumn
	 * @return a list column from the given list setup which has the same
	 *         propertyId as the one of the user list column
	 */
	public static ListColumn getListColumn(ListSetup setup, UserListColumn userColumn) {
		List<ListColumn> columns = setup.getColumns();
		return getListColumn(columns, userColumn);
	}
	
	/**
	 * @param columns
	 * @param userColumn
	 * @return a list column from the given list setup which has the same
	 *         propertyId as the one of the user list column
	 */
	public static ListColumn getListColumn(List<ListColumn> columns, UserListColumn userColumn){
		for (ListColumn listColumn : columns) {
			if (listColumn.getPropertyId().equals(userColumn.getPropertyId())) {
				return listColumn;
			}
		}

		return null;
	}

	/**
	 * @param userLS
	 * @param description
	 * @return a {@link UserProfileWrapper} that contains the infos from the
	 *         user list setup
	 */
	public static UserProfileWrapper toUserProfileWrapper(UserListSetup userLS, String description) {
		UserProfileWrapper wrapper = new UserProfileWrapper();

		String profileId = createGenericKey(description);

		// update the entity content
		wrapper.setBaseProfileName(userLS.getListId());
		wrapper.setClassName(userLS.getTypeClass());
		wrapper.setProfileId(profileId);
		wrapper.setXmlContent(UserConfigXmlReader.generateXmlContent(userLS));
		wrapper.setDescription(description);

		IMitarbeiter mit = ((IMitarbeiterDAO) DAOSystem.getDAO(IMitarbeiterDAO.class)).getByCurrentUser();
		wrapper.setOwnerId(null != mit ? mit.getId() : 0);

		return wrapper;
	}

	/**
	 * @param userProfile
	 * @return a {@link UserProfileWrapper} that contains the infos from the
	 *         user list profile entity
	 */
	public static UserProfileWrapper toUserProfileWrapper(IUserListProfile userProfile) {
		UserProfileWrapper wrapper = new UserProfileWrapper();

		String profileId = createGenericKey(userProfile.getDescription());

		wrapper.setBaseProfileName(userProfile.getBaseProfileName());
		wrapper.setClassName(userProfile.getClassName());
		wrapper.setProfileId(profileId);

		// update the entity content
		wrapper.setXmlContent(userProfile.getXmlContent());
		wrapper.setDescription(userProfile.getDescription());

		wrapper.setSortField(userProfile.getSortField());
		wrapper.setSortDirection(userProfile.getSortDirection());
		wrapper.setMaxRows(userProfile.getMaxRows());
		
		wrapper.setPublicProfile(userProfile.isPublicProfile());
		
		IMitarbeiter mit = userProfile.getOwner();
		wrapper.setOwnerId(null != mit ? mit.getId() : 0);

		return wrapper;
	}

	/**
	 * @param ls
	 * @param description
	 * @return
	 */
	public static int toUserProfile(ListSetup ls, String description) {
		UserListSetup userLS = toUserListSetup(ls);

		String profileId = createGenericKey(description);

		IMitarbeiterDAO mitDAO = (IMitarbeiterDAO) DAOSystem.getDAO(IMitarbeiterDAO.class);
		IMitarbeiter currentUser = mitDAO.getByCurrentUser();
		
		// see if we already have a profile which matches the user list setup
		PropertyQuery query = new PropertyQuery();
		query.addEquals("baseProfileName", ls.getListId());
		query.addEquals("className", ls.getTypeClass());
		query.addEquals("profileId", profileId);
		query.addEquals("owner", currentUser);

		DAO dao = DAOSystem.getDAO(IUserListProfileDAO.class);
		EntityList list = dao.getEntities(new Limit(0, 1), query);
		if (list.size() > 0 && list.get(0) != null) {
			IUserListProfile profile = (IUserListProfile) list.get(0);
			// return it if we found it. for list setups, we don't create new
			// ones, nor do we update them, we update at UserListSetups
			return profile.getId();
		}

		IUserListProfile profile = toUserProfile(userLS, description, currentUser.getId());
		dao.update(profile);
		return profile.getId();
	}

	/**
	 * @param setup
	 * @return
	 */
	public static UserListSetup toUserListSetup(ListSetup setup) {
		UserListSetup userList = new UserListSetup();

		userList.setEntityDescriptor(setup.getEntityDescriptor());
		userList.setTypeClass(setup.getTypeClass());
		userList.setListId(setup.getListId());

		List<ListColumn> columns = setup.getColumns();
		for (Iterator<ListColumn> iter = columns.iterator(); iter.hasNext();) {
			ListColumn element = iter.next();
			if (element.isDefaultVisible()) {
				UserListColumn column = UserListUtil.createUserListColumn(element);
				userList.addColumn(column);
			}
		}

		return userList;
	}

	/**
	 * Returns an updated userListProfile which is not saved! This allows to add additional information 
	 * before saving. (avoid double save)
	 * @param userLS
	 * @return the updated or created, but unsaved IUserListProfile. 
	 */
	public static IUserListProfile toUserProfile(UserListSetup userLS, String description, int ownerId) {
		IUserListProfileDAO dao = (IUserListProfileDAO) DAOSystem.getDAO(IUserListProfileDAO.class);
		IUserListProfile profile = null;

		if (null == description || description.length() == 0) {
			description = userLS.getListId();
		}

		String profileId = createGenericKey(description);

		IMitarbeiterDAO mitDAO = (IMitarbeiterDAO) DAOSystem.getDAO(IMitarbeiterDAO.class);
		IMitarbeiter mit = (IMitarbeiter) mitDAO.getEntity(ownerId);
		
		// see if we already have a profile which matches the user list setup
		PropertyQuery query = new PropertyQuery();
		query.addEquals("baseProfileName", userLS.getListId());
		query.addEquals("className", userLS.getTypeClass());
		query.addEquals("profileId", profileId);
		query.addEquals("owner", mit);

		EntityList list = dao.getEntities(new Limit(0, 1), query);
		if (list.size() > 0 && list.get(0) != null) {
			profile = (IUserListProfile) list.get(0);
		} else {
			// create a new one, since we don't have the entity in our DB
			profile = (IUserListProfile) dao.createEntity();
		}

		// update the entity content
		profile.setBaseProfileName(userLS.getListId());
		profile.setClassName(userLS.getTypeClass());
		profile.setProfileId(profileId);
		profile.setXmlContent(UserConfigXmlReader.generateXmlContent(userLS));
		profile.setDescription(description);

		profile.setOwner(mit);

		return profile;
	}

	/**
	 * @param name
	 * @return a unique string representation of the given property. all letters
	 *         are upper cased, spaces are removed
	 */
	public static String createGenericKey(String name) {

		if (name == null) {
			return "";
		}

		StringBuilder sb = new StringBuilder(name.length() + 3);

		int l = name.length();
		for (int i = 0; i < l; i++) {
			char c = name.charAt(i);
			if (c == '\u00D6' || c == '\u00F6') {
				sb.append("OE");
			} else if (c == '\u00C4' || c == '\u00E4') {
				sb.append("AE");
			} else if (c == '\u00DC' || c == '\u00FC') {
				sb.append("UE");
			} else if (c == '\u00DF') {
				sb.append("SS");
			} else if (c >= 'a' && c <= 'z') {
				sb.append((char) (c + UPPER_DIF));
			} else if ((c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z')) {
				// numerics are allowed
				sb.append(c);
			}
		}

		return sb.toString();
	}
}
