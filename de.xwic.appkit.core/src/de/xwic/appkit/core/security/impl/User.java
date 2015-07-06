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
package de.xwic.appkit.core.security.impl;

import java.util.HashSet;
import java.util.Set;

import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.Entity;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.security.IUser;
import de.xwic.appkit.core.security.ScopeActionKey;
import de.xwic.appkit.core.security.daos.IUserDAO;
import de.xwic.appkit.core.util.DigestTool;

/**
 * Implementation of the IUser interface.
 * @see de.xwic.appkit.core.security.IUser
 * @author Florian Lippisch
 */
public class User extends Entity implements IUser {

	private String name = null;
	private String logonName = null;
	private String passwordHash = null;
	private Set<IEntity> roles = null;
	private String profileName = null;
	private String language = null;
	

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.IUser#getName()
	 */
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.IUser#setName(java.lang.String)
	 */
	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.IUser#getLogonName()
	 */
	public String getLogonName() {
		return logonName;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.IUser#setLogonName(java.lang.String)
	 */
	public void setLogonName(String logonName) {
		this.logonName = logonName;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.IUser#getRoles()
	 */
	public Set<IEntity> getRoles() {
		if (roles == null) {
			roles = new HashSet<IEntity>();
		}
		return roles;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.IUser#setRoles(java.util.Set)
	 */
	public void setRoles(Set<IEntity> roles) {
		this.roles = roles;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.IUser#buildAllRights()
	 */
	public Set<ScopeActionKey> buildAllRights() {
		IUserDAO userDAO = (IUserDAO)DAOSystem.getDAO(IUserDAO.class);
		return userDAO.buildAllRights(this);
	}

	/**
	 * @return the profileName
	 */
	public String getProfileName() {
		return profileName;
	}

	/**
	 * @param profileName the profileName to set
	 */
	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	/**
	 * @return the passwordHash
	 */
	public String getPasswordHash() {
		return passwordHash;
	}

	/**
	 * @param passwordHash the passwordHash to set
	 */
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
	
	/**
	 * Changes the password to the specified password. The password is
	 * encoded using this method.
	 * @param newPassword
	 */
	public void changePassword(String newPassword) {
		setPasswordHash(DigestTool.encodeString(newPassword));
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.IUser#validatePassword(java.lang.String)
	 */
	public boolean validatePassword(String password) {
		if (password == null || getPasswordHash() == null) {
			return false;
		}
		return DigestTool.encodeString(password).equals(getPasswordHash());
	}

	
	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	
	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}
	
}
