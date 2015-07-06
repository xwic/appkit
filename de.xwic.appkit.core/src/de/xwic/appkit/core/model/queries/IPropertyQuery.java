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
package de.xwic.appkit.core.model.queries;

import java.util.Collection;


/**
 * Interface for the generic PropertyQuery.
 * 
 * @author Ronny Pfretzschner
 */
public interface IPropertyQuery {

	/**
	 * Add an equals option.
	 * @param property
	 * @param value
	 */
	public abstract QueryElement addEquals(String property, Object value);

	/**
	 * Add an equals option.
	 * @param property
	 * @param value
	 */
	public abstract QueryElement addLike(String property, Object value);

	/**
	 * Add an equals option.
	 * @param property
	 * @param value
	 */
	public abstract QueryElement addNotEquals(String property, Object value);

	/**
	 * Add an OR equals option.
	 * @param property
	 * @param value
	 */
	public abstract QueryElement addOrEquals(String property, Object value);

	/**
	 * Add an OR LIKE option.
	 * @param property
	 * @param value
	 */
	public abstract QueryElement addOrLike(String property, Object value);

	/**
	 * Add an OR Not Equals option.
	 * @param property
	 * @param value
	 */
	public abstract QueryElement addOrNotEquals(String property, Object value);

	/**
	 * Add a greater option.
	 * @param property
	 * @param value
	 */
	public QueryElement addGreaterThen(String property, Object value);

	/**
	 * Add a greater equals option.
	 * @param property
	 * @param value
	 */
	public QueryElement addGreaterEqualsThen(String property, Object value);

	/**
	 * Add a lower option.
	 * @param property
	 * @param value
	 */
	public QueryElement addLowerThen(String property, Object value);

	/**
	 * Add a lower equals option.
	 * @param property
	 * @param value
	 */
	public QueryElement addLowerEqualsThen(String property, Object value);

	/**
	 * Add a query element.
	 * @param element
	 */
	public void addQueryElement(QueryElement element);

	/**
	 * Returns the number of elements.
	 * @return
	 */
	public abstract int size();

	/**
	 * Removes all elements.
	 *
	 */
	public abstract void clear();

	/**
	 * @return the hideDeleted
	 */
	public abstract boolean isHideDeleted();

	/**
	 * @param hideDeleted the hideDeleted to set
	 */
	public abstract void setHideDeleted(boolean hideDeleted);

	/**
	 * Same as <code>addLike(..)</code> but with checking clients wildcard preferences
	 * for String searches. <p>
	 * 
	 * If default constructor is used, this method acts exactly like the normal <code>addLike(..)</code> method.
	 * 
	 * @param property
	 * @param value
	 */
	QueryElement addLikeWithWildcardSetting(String property, String value);

	/**
	 * @param property
	 * @param value
	 */
	QueryElement addOrLikeWithWildcardSetting(String property, String value);

	/**
	 * @param property
	 * @param values
	 */
	QueryElement addIn(String property, Collection<?> values);

	/**
	 * @param property
	 * @param values
	 */
	QueryElement addNotIn(String property, Collection<?> values);

	/**
	 * @param property
	 * @param values
	 */
	QueryElement addOrIn(String property, Collection<?> values);

	/**
	 * @param property
	 * @param values
	 */
	QueryElement addOrNotIn(String property, Collection<?> values);

	/**
	 * Add collection empty property.
	 * @param collectionProperty
	 */
	QueryElement addEmpty(String collectionProperty);

	/**
	 * Add or collection empty property.
	 * @param collectionProperty
	 */
	QueryElement addOrEmpty(String collectionProperty);

	/**
	 * Add collection not empty property.
	 * @param collectionProperty
	 */
	QueryElement addNotEmpty(String collectionProperty);

	/**
	 * Add or collection not empty property.
	 * @param collectionProperty
	 */
	QueryElement addOrNotEmpty(String collectionProperty);

}