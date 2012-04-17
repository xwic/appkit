/*
 * Copyright (c) 2009 Network Appliance, Inc.
 * All rights reserved.
 */

package de.xwic.appkit.webbase.utils;

import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.model.daos.IMitarbeiterDAO;
import de.xwic.appkit.core.model.entities.IMitarbeiter;
import de.xwic.appkit.core.model.entities.IUserListProfile;


/**
 * Wrapper class for the {@link IUserListProfile} entity
 * @author Aron Cotrau
 */
public class UserProfileWrapper {

	private String baseProfileName;
	private String className;
	private String description;
	private String xmlContent;
	private int ownerId;
	private String profileId;
	
	private boolean publicProfile = false;
	
	private String sortField;
	private int sortDirection = EntityQuery.SORT_DIRECTION_UP;

	private int maxRows;
	
	/**
	 * @return the baseProfileName
	 */
	public String getBaseProfileName() {
		return baseProfileName;
	}

	/**
	 * @param baseProfileName
	 *            the baseProfileName to set
	 */
	public void setBaseProfileName(String baseProfileName) {
		this.baseProfileName = baseProfileName;
	}

	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @param className
	 *            the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the xmlContent
	 */
	public String getXmlContent() {
		return xmlContent;
	}

	/**
	 * @param xmlContent
	 *            the xmlContent to set
	 */
	public void setXmlContent(String xmlContent) {
		this.xmlContent = xmlContent;
	}

	/**
	 * @return the owner
	 */
	public int getOwnerId() {
		return ownerId;
	}

	/**
	 * @param owner
	 *            the owner to set
	 */
	public void setOwnerId(int owner) {
		this.ownerId = owner;
	}

	/**
	 * @return the profileId
	 */
	public String getProfileId() {
		return profileId;
	}

	/**
	 * @param profileId
	 *            the profileId to set
	 */
	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

	/**
	 * @return the publicProfile
	 */
	public boolean isPublicProfile() {
		return publicProfile;
	}

	/**
	 * @param publicProfile
	 *            the publicProfile to set
	 */
	public void setPublicProfile(boolean publicProfile) {
		this.publicProfile = publicProfile;
	}
	
	public IMitarbeiter getOwner() {
		return (IMitarbeiter) DAOSystem.getDAO(IMitarbeiterDAO.class).getEntity(ownerId);
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
