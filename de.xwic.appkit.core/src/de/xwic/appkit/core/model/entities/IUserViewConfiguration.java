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
 * @author Adrian Ionescu
 */
public interface IUserViewConfiguration extends IEntity {

	/**
	 * @return the owner
	 */
	public IMitarbeiter getOwner();

	/**
	 * @param owner
	 *            the owner to set
	 */
	public void setOwner(IMitarbeiter owner);
	
	/**
	 * @return the className
	 */
	public String getClassName();

	/**
	 * @param className
	 *            the className to set
	 */
	public void setClassName(String className);
	
	/**
	 * @return the viewId
	 */
	public String getViewId();

	/**
	 * @param viewId
	 *            the viewId to set
	 */
	public void setViewId(String viewId);
	
	/**
	 * @return the listSetupId
	 */
	public String getListSetupId();

	/**
	 * @param listSetupId
	 *            the listSetupId to set
	 */
	public void setListSetupId(String listSetupId);

	/**
	 * @return the Name
	 */
	public String getName();

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name);
	
	/**
	 * @return the description
	 */
	public String getDescription();

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description);
	
	/**
	 * @return the public
	 */
	public boolean isPublic();

	/**
	 * @param isPublic
	 *            the isPublic to set
	 */
	public void setPublic(boolean isPublic);
	
	/**
	 * @return the content
	 */
	public String getColumnsConfiguration();

	/**
	 * @param columnsConfiguration
	 *            the columnsConfiguration to set
	 */
	public void setColumnsConfiguration(String columnsConfiguration);

	/**
	 * @return the sortField
	 */
	public String getSortField();

	/**
	 * @param sortField
	 *            the sortField to set
	 */
	public void setSortField(String sortField);

	/**
	 * @return the sortDirection
	 */
	public String getSortDirection();

	/**
	 * @param sortDirection
	 *            the sortDirection to set
	 */
	public void setSortDirection(String sortDirection);

	/**
	 * @return the maxRows
	 */
	public int getMaxRows();

	/**
	 * @param maxRows
	 *            the maxRows to set
	 */
	public void setMaxRows(int maxRows);
	
	/**
	 * @return the mainConfiguration
	 */
	public boolean isMainConfiguration();
	
	/**
	 * @param mainConfiguration the mainConfiguration to set
	 */
	public void setMainConfiguration(boolean mainConfiguration);
	
	/**
	 * @return the relatedConfiguration
	 */
	public IUserViewConfiguration getRelatedConfiguration();
	
	/**
	 * @param relatedConfiguration the relatedConfiguration to set
	 */
	public void setRelatedConfiguration(IUserViewConfiguration relatedConfiguration);
	
	/**
	 * @return the filtersConfiguration
	 */
	public String getFiltersConfiguration();
	
	/**
	 * @param filtersConfiguration the filtersConfiguration to set
	 */
	public void setFiltersConfiguration(String filtersConfiguration);

}