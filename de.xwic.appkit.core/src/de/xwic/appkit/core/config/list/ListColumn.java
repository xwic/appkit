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
/*
 * de.pol.entitydescriptor.ListColumn
 * Created on 31.05.2005
 *
 */
package de.xwic.appkit.core.config.list;

import de.xwic.appkit.core.config.model.Property;

/**
 * Defines a column in a list.
 * 
 * @author Florian Lippisch
 */
public class ListColumn {

	/** Not sortable * */
	public final static int SORT_NONE = 0;

	/** Sorted on the client side */
	public final static int SORT_CLIENT = 1;

	/** Sorted on the server side (new query) */
	public final static int SORT_SERVER = 2;

	private String titleId = null;

	private String propertyId = null;

	private Property[] property = null;

	private int defaultWidth = 0; // no width set

	private boolean defaultVisible = false;

	private String descriptionId = "";

	private String customLabelProviderClass = null;

	private int sortMode = SORT_CLIENT;
	
	private boolean hideFilter = false;

	/**
	 * Default constructor.
	 * 
	 * @param titleId
	 * @param property
	 */
	public ListColumn(String titleId, String propertyId) {
		this.titleId = titleId;
		this.propertyId = propertyId;
	}

	/**
	 * Default constructor.
	 * 
	 * @param titleId
	 * @param property
	 */
	public ListColumn(String titleId, String property, int defWidth) {
		this.titleId = titleId;
		this.propertyId = property;
		this.defaultWidth = defWidth;
	}

	/**
	 * @return Returns the defaultVisible.
	 */
	public boolean isDefaultVisible() {
		return defaultVisible;
	}

	/**
	 * @param defaultVisible
	 *            The defaultVisible to set.
	 */
	public void setDefaultVisible(boolean defaultVisible) {
		this.defaultVisible = defaultVisible;
	}

	/**
	 * @return Returns the defaultWidth.
	 */
	public int getDefaultWidth() {
		return defaultWidth;
	}

	/**
	 * @param defaultWidth
	 *            The defaultWidth to set.
	 */
	public void setDefaultWidth(int defaultWidth) {
		this.defaultWidth = defaultWidth;
	}

	/**
	 * @return Returns the descriptionId.
	 */
	public String getDescriptionId() {
		return descriptionId;
	}

	/**
	 * @param descriptionId
	 *            The descriptionId to set.
	 */
	public void setDescriptionId(String descriptionId) {
		this.descriptionId = descriptionId;
	}

	/**
	 * @return Returns the property.
	 */
	public String getPropertyId() {
		return propertyId;
	}

	/**
	 * @param property
	 *            The property to set.
	 */
	public void setPropertyId(String property) {
		this.propertyId = property;
	}

	/**
	 * @return Returns the titleId.
	 */
	public String getTitleId() {
		return titleId;
	}

	/**
	 * @param titleId
	 *            The titleId to set.
	 */
	public void setTitleId(String titleId) {
		this.titleId = titleId;
	}

	/**
	 * @return Returns the customLabelProviderClass.
	 */
	public String getCustomLabelProviderClass() {
		return customLabelProviderClass;
	}

	/**
	 * @param customLabelProviderClass
	 *            The customLabelProviderClass to set.
	 */
	public void setCustomLabelProviderClass(String customLabelProviderClass) {
		this.customLabelProviderClass = customLabelProviderClass;
	}
	
	/**
	 * @return the hideFilter
	 */
	public boolean isHideFilter() {
		return hideFilter;
	}

	
	/**
	 * @param hideFilter the hideFilter to set
	 */
	public void setHideFilter(boolean hideFilter) {
		this.hideFilter = hideFilter;
	}

	/**
	 * @return
	 */
	public int getSortMode() {
		return sortMode;
	}

	/**
	 * @param sortMode
	 */
	public void setSortMode(int sortMode) {
		this.sortMode = sortMode;
	}

	/**
	 * @return the property
	 */
	public Property[] getProperty() {
		return property;
	}

	/**
	 * @param property
	 *            the property to set
	 */
	public void setProperty(Property[] property) {
		this.property = property;
	}

	/**
	 * Returns the property that is the leaf.
	 * 
	 * @return
	 */
	public Property getFinalProperty() {
		if (property != null && property.length >= 1) {
			return property[property.length - 1];
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((propertyId == null) ? 0 : propertyId.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ListColumn other = (ListColumn) obj;
		if (propertyId == null) {
			if (other.propertyId != null)
				return false;
		} else if (!propertyId.equals(other.propertyId))
			return false;
		return true;
	}

	/**
	 * @return a clone of the list column
	 */
	public ListColumn cloneListColumn() {
		ListColumn column = new ListColumn(titleId, propertyId);
		
		column.setCustomLabelProviderClass(customLabelProviderClass);
		column.setDefaultVisible(defaultVisible);
		column.setDefaultWidth(defaultWidth);
		column.setDescriptionId(descriptionId);
		column.setProperty(property);
		column.setSortMode(sortMode);
		column.setTitleId(titleId);
		column.setHideFilter(hideFilter);
		
		return column;
	}
	
}
