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
 * de.xwic.appkit.core.dao.EntityFilter
 * Created on 04.04.2005
 *
 */
package de.xwic.appkit.core.dao;

import java.util.List;
import java.util.Locale;

/**
 * Used to specify an entity-type specific filter.
 * 
 * @author Florian Lippisch
 */
public abstract class EntityQuery {

	/**
	 * sort direction up constant - ASCENDING
	 */
	public final static int SORT_DIRECTION_UP = 0;

	/**
	 * sort direction down constant - DESCENDING
	 */
	public final static int SORT_DIRECTION_DOWN = 1;

	private int sortDirection = 0;
	
	private boolean fetchLazySets = false;
	
	private boolean returnEntity = true;

	private String sortField = null;

	private String languageId = null;

	private List<String> columns = null;
	
	private boolean cacheQuery = false;

	/**
	 * Default constructor.
	 */
	public EntityQuery() {
		languageId = Locale.getDefault().getLanguage();
	}

	/**
	 * Sets the sorting direction
	 * 
	 * @param direction -
	 *            the direction to be set
	 */
	public void setSortDirection(int direction) {
		this.sortDirection = direction;
	}

	/**
	 * Sets the sort field
	 * 
	 * @param fieldName -
	 *            the field name to be set
	 */
	public void setSortField(String fieldName) {
		this.sortField = fieldName;
	}

	/**
	 * @return Returns the sortDirection.
	 */
	public int getSortDirection() {
		return sortDirection;
	}

	/**
	 * @return Returns the sortField.
	 */
	public String getSortField() {
		return sortField;
	}

	/**
	 * @return Returns the language id
	 */
	public String getLanguageId() {
		return languageId;
	}

	/**
	 * Sets the language id
	 * 
	 * @param languageId
	 *            the language id to be set
	 */
	public void setLanguageId(String languageId) {
		this.languageId = languageId;
	}

	/**
	 * @return Returns the columns list (list of strings)
	 */
	public List<String> getColumns() {
		return columns;
	}

	/**
	 * A list of properties to be returned. The query will then return an
	 * Object[] with the specified properties per row. The ID of the entity will
	 * be always in the element #0.
	 * 
	 * @param columns -
	 *            List of String objects.
	 */
	public void setColumns(List<String> columns) {
		this.columns = columns;
		if (columns != null) {
			returnEntity = false;
		}
	}

	/**
	 * Indicates that the underlying data-layer should return objects
	 * with fully initialized/loaded sets. By default, this value is set to
	 * false, so that most of the sets are lazy initialized.
	 * This features is supported by the remote WebService API of the ISIS
	 * Server. It was implemented for better replication performance.
	 * @return the fetchLazySets
	 */
	public boolean isFetchLazySets() {
		return fetchLazySets;
	}

	/**
	 * @param fetchLazySets the fetchLazySets to set
	 */
	public void setFetchLazySets(boolean fetchLazySets) {
		this.fetchLazySets = fetchLazySets;
	}

	/**
	 * If returnEntity is true, the DAOProviderAPI ensures that the list of requested entities contains the requested object
	 * type (by default true). 
	 * @return the returnEntity
	 */
	public boolean isReturnEntity() {
		return returnEntity;
	}

	/**
	 * @param returnEntity the returnEntity to set
	 */
	public void setReturnEntity(boolean returnEntity) {
		this.returnEntity = returnEntity;
	}
	
	
	/**
	 * set to true if the query and it's results should be cached . 
	 * 
	 * Default is false.
	 * 
	 * @param cacheQuery the cacheQuery to set
	 */
	public void setCacheQuery(boolean cacheQuery) {
		this.cacheQuery = cacheQuery;
	}

	
	/**
	 * return true if the query and it's results should be cached
	 * @return the cacheQuery
	 */
	public boolean isCacheQuery() {
		return cacheQuery;
	}
	
}
