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
	public abstract void addEquals(String property, Object value);

	/**
	 * Add an equals option.
	 * @param property
	 * @param value
	 */
	public abstract void addLike(String property, Object value);

	/**
	 * Add an equals option.
	 * @param property
	 * @param value
	 */
	public abstract void addNotEquals(String property, Object value);

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
	public abstract void addOrLike(String property, Object value);

	/**
	 * Add an OR Not Equals option.
	 * @param property
	 * @param value
	 */
	public abstract void addOrNotEquals(String property, Object value);

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
	public void addLikeWithWildcardSetting(String property, String value);
	
	/**
	 * @param property
	 * @param value
	 */
	public void addOrLikeWithWildcardSetting(String property, String value);
	/**
	 * @param property
	 * @param values
	 */
	public void addIn(String property, Collection<?> values);

	/**
	 * @param property
	 * @param values
	 */
	public void addNotIn(String property, Collection<?> values);

	/**
	 * @param property
	 * @param values
	 */
	public void addOrIn(String property, Collection<?> values);

	/**
	 * @param property
	 * @param values
	 */
	public void addOrNotIn(String property, Collection<?> values);

	/**
	 * Add collection empty property.
	 * @param collectionProperty
	 */
	void addEmpty(String collectionProperty);

	/**
	 * Add collection not empty property.
	 * @param collectionProperty
	 */
	void addNotEmpty(String collectionProperty);

}