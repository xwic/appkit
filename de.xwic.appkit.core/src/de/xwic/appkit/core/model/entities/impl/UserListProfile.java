/*
 * Copyright (c) 2009 Network Appliance, Inc.
 * All rights reserved.
 */

package de.xwic.appkit.core.model.entities.impl;

import de.xwic.appkit.core.dao.Entity;
import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.model.entities.IMitarbeiter;
import de.xwic.appkit.core.model.entities.IUserListProfile;

/**
 * Defines a User List Profile
 * 
 * @author Aron Cotrau
 */
public class UserListProfile extends Entity implements IUserListProfile {

	private String baseProfileName;
	private String className;
	private String description;
	private String xmlContent;
	private IMitarbeiter owner;
	private String profileId;
	
	private boolean publicProfile = false;

	private String sortField;
	private int sortDirection = EntityQuery.SORT_DIRECTION_UP;
	
	private int maxRows;
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.IUserListProfile#getBaseProfileName()
	 */
	public String getBaseProfileName() {
		return baseProfileName;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.IUserListProfile#setBaseProfileName(java.lang.String)
	 */
	public void setBaseProfileName(String baseProfileName) {
		this.baseProfileName = baseProfileName;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.IUserListProfile#getClassName()
	 */
	public String getClassName() {
		return className;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.IUserListProfile#setClassName(java.lang.String)
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.IUserListProfile#getDescription()
	 */
	public String getDescription() {
		return description;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.IUserListProfile#setDescription(java.lang.String)
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.IUserListProfile#getXmlContent()
	 */
	public String getXmlContent() {
		return xmlContent;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.IUserListProfile#setXmlContent(java.lang.String)
	 */
	public void setXmlContent(String xmlContent) {
		this.xmlContent = xmlContent;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.IUserListProfile#isPublicProfile()
	 */
	public boolean isPublicProfile() {
		return publicProfile;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.IUserListProfile#setPublicProfile(boolean)
	 */
	public void setPublicProfile(boolean publicProfile) {
		this.publicProfile = publicProfile;
	}

	/**
	 * @return the owner
	 */
	public IMitarbeiter getOwner() {
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(IMitarbeiter owner) {
		this.owner = owner;
	}

	/**
	 * @return the profileId
	 */
	public String getProfileId() {
		return profileId;
	}

	/**
	 * @param profileId the profileId to set
	 */
	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

	/**
	 * @return the sortField
	 */
	public String getSortField() {
		return sortField;
	}

	/**
	 * @param sortField the sortField to set
	 */
	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	/**
	 * @return the sortDirection
	 */
	public int getSortDirection() {
		return sortDirection;
	}

	/**
	 * @param sortDirection the sortDirection to set
	 */
	public void setSortDirection(int sortDirection) {
		this.sortDirection = sortDirection;
	}

	/**
	 * @return the maxRows
	 */
	public int getMaxRows() {
		return maxRows;
	}

	/**
	 * @param maxRows the maxRows to set
	 */
	public void setMaxRows(int maxRows) {
		this.maxRows = maxRows;
	}

}
