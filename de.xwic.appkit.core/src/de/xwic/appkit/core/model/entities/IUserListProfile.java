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
package de.xwic.appkit.core.model.entities;

import de.xwic.appkit.core.dao.IEntity;

/**
 * 
 * @author Aron Cotrau
 */
public interface IUserListProfile extends IEntity {

	/**
	 * @return the baseProfileName
	 */
	public abstract String getBaseProfileName();

	/**
	 * @param baseProfileName
	 *            the baseProfileName to set
	 */
	public abstract void setBaseProfileName(String baseProfileName);

	/**
	 * @return the className
	 */
	public abstract String getClassName();

	/**
	 * @param className
	 *            the className to set
	 */
	public abstract void setClassName(String className);

	/**
	 * @return the description
	 */
	public abstract String getDescription();

	/**
	 * @param description
	 *            the description to set
	 */
	public abstract void setDescription(String description);

	/**
	 * @return the xmlContent
	 */
	public abstract String getXmlContent();

	/**
	 * @param xmlContent
	 *            the xmlContent to set
	 */
	public abstract void setXmlContent(String xmlContent);

	/**
	 * @return the publicProfile
	 */
	public abstract boolean isPublicProfile();

	/**
	 * @param publicProfile
	 *            the publicProfile to set
	 */
	public abstract void setPublicProfile(boolean publicProfile);
	
	/**
	 * @return the owner
	 */
	public IMitarbeiter getOwner();

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(IMitarbeiter owner);

	/**
	 * @return the profileId
	 */
	public String getProfileId();

	/**
	 * @param profileId the profileId to set
	 */
	public void setProfileId(String profileId);
	
	/**
	 * @return the sortField
	 */
	public String getSortField();

	/**
	 * @param sortField the sortField to set
	 */
	public void setSortField(String sortField);

	/**
	 * @return the sortDirection
	 */
	public int getSortDirection();

	/**
	 * @param sortDirection the sortDirection to set
	 */
	public void setSortDirection(int sortDirection);
	
	/**
	 * @return the maxRows
	 */
	public int getMaxRows();

	/**
	 * @param maxRows the maxRows to set
	 */
	public void setMaxRows(int maxRows);

}