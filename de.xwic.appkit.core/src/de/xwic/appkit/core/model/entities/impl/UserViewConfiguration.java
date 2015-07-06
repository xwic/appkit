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
package de.xwic.appkit.core.model.entities.impl;

import de.xwic.appkit.core.dao.Entity;
import de.xwic.appkit.core.model.entities.IMitarbeiter;
import de.xwic.appkit.core.model.entities.IUserViewConfiguration;

/**
 * @author Adrian Ionescu
 */
public class UserViewConfiguration extends Entity implements IUserViewConfiguration {
	
	private IMitarbeiter owner;
	private String className;
	private String viewId;
	private String listSetupId;
	private String name;
	private String description;
	private boolean isPublic;
	private String columnsConfiguration;
	private String sortField;
	private String sortDirection;
	private int maxRows;
	private boolean mainConfiguration;
	private IUserViewConfiguration relatedConfiguration;
	private String filtersConfiguration;
	
	
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
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}
	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	/**
	 * @return the viewId
	 */
	public String getViewId() {
		return viewId;
	}
	/**
	 * @param viewId the viewId to set
	 */
	public void setViewId(String viewId) {
		this.viewId = viewId;
	}
	/**
	 * @return the listSetupId
	 */
	public String getListSetupId() {
		return listSetupId;
	}
	/**
	 * @param listSetupId the listSetupId to set
	 */
	public void setListSetupId(String listSetupId) {
		this.listSetupId = listSetupId;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the isPublic
	 */
	public boolean isPublic() {
		return isPublic;
	}
	/**
	 * @param isPublic the isPublic to set
	 */
	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}
	/**
	 * @return the columnsConfiguration
	 */
	public String getColumnsConfiguration() {
		return columnsConfiguration;
	}
	/**
	 * @param columnsConfiguration the columnsConfiguration to set
	 */
	public void setColumnsConfiguration(String columnsConfiguration) {
		this.columnsConfiguration = columnsConfiguration;
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
	public String getSortDirection() {
		return sortDirection;
	}
	/**
	 * @param sortDirection the sortDirection to set
	 */
	public void setSortDirection(String sortDirection) {
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
	/**
	 * @return the mainConfiguration
	 */
	public boolean isMainConfiguration() {
		return mainConfiguration;
	}
	/**
	 * @param mainConfiguration the mainConfiguration to set
	 */
	public void setMainConfiguration(boolean mainConfiguration) {
		this.mainConfiguration = mainConfiguration;
	}
	/**
	 * @return the relatedConfiguration
	 */
	public IUserViewConfiguration getRelatedConfiguration() {
		return relatedConfiguration;
	}
	/**
	 * @param relatedConfiguration the relatedConfiguration to set
	 */
	public void setRelatedConfiguration(IUserViewConfiguration relatedConfiguration) {
		this.relatedConfiguration = relatedConfiguration;
	}
	/**
	 * @return the filtersConfiguration
	 */
	public String getFiltersConfiguration() {
		return filtersConfiguration;
	}
	/**
	 * @param filtersConfiguration the filtersConfiguration to set
	 */
	public void setFiltersConfiguration(String filtersConfiguration) {
		this.filtersConfiguration = filtersConfiguration;
	}
}