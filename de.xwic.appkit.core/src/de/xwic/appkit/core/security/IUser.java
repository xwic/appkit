/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.security.IUser
 * Created on 02.08.2005
 *
 */
package de.xwic.appkit.core.security;

import java.util.Set;

import de.xwic.appkit.core.dao.IEntity;

/**
 * Defines an user. A user can have one or more roles.
 * @author Florian Lippisch
 */
public interface IUser extends IEntity {

	/**
	 * Returns the name the user uses to logon to the system.
	 * @return
	 */
	public String getLogonName();
	
	/**
	 * Returns the logon name.
	 * @param logonName
	 */
	public void setLogonName(String logonName);
	
	/**
	 * Returns the full name of the user.
	 * @return
	 */
	public String getName();
	
	/**
	 * Set the full name of the user. 
	 */
	public void setName(String name);
	
	/**
	 * Returns the set of assigned roles.
	 * @return
	 */
	public Set<IEntity> getRoles();
	
	/**
	 * Sets the roles assigned to the user.
	 * @param roles
	 */
	public void setRoles(Set<IEntity> roles);

	/**
	 * Creates a set of all Rights.
	 * @return
	 */
	public Set<ScopeActionKey> buildAllRights();
	
	/**
	 * Returns the name of the configuration profile this user uses. This
	 * property might have a NULL value if the default profile should be used. 
	 * @return
	 */
	public String getProfileName();
	
	/**
	 * Set the name of the configuration profile this user uses. If the default
	 * profile should be used, the value must be set to NULL.
	 * @param profileName
	 */
	public void setProfileName(String profileName);

	/**
	 * @return the passwordHash
	 */
	public String getPasswordHash();

	/**
	 * @param passwordHash the passwordHash to set
	 */
	public void setPasswordHash(String passwordHash);
	
	/**
	 * Changes the password to the specified password. The password is
	 * encoded using this method.
	 * @param newPassword
	 */
	public void changePassword(String newPassword);	
	
	/**
	 * Returns true if the given password matches the users 
	 * password. The password is hashed and then compared
	 * with the passwordHash.
	 * 
	 * @param password in clear text.
	 * @return
	 */
	public boolean validatePassword(String password);
	
	/**
	 * @return the language
	 */
	public String getLanguage();
	
	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language);
}
