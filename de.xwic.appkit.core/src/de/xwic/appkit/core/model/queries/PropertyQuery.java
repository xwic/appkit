/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.model.queries.PropertyQuery
 * Created on 07.12.2006 by Florian Lippisch
 *
 */
package de.xwic.appkit.core.model.queries;

import static de.xwic.appkit.core.model.queries.QueryElement.AND;
import static de.xwic.appkit.core.model.queries.QueryElement.IS_EMPTY;
import static de.xwic.appkit.core.model.queries.QueryElement.IS_NOT_EMPTY;
import static de.xwic.appkit.core.model.queries.QueryElement.LIKE;
import static de.xwic.appkit.core.model.queries.QueryElement.OR;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.util.EntityUtil;
import de.xwic.appkit.core.util.CollectionUtil;

/**
 * Generic query definition based upon entity properties. 
 * 
 * @author Florian Lippisch
 */
public class PropertyQuery extends EntityQuery implements IPropertyQuery {

	private boolean hideDeleted = true;
	
	private boolean joinPicklistEntries = false;
	private List<QueryElement> elements = new ArrayList<QueryElement>();
	private Map<String, String> leftOuterJoinProperties = new LinkedHashMap<String, String>();
	
	/**
	 * If set to true then the results should be sorted by id to maintain order consistency.
	 * If a sort field already exists then the id is set as a second sort column.
	 */
	private boolean consistentOrder = false;
	
	private String wildcardPreferenceSetting = null;
	
	/**
	 * automatic Wilcard insert at end of String. <p>
	 */
	public final static String WILDCARD_VALUE_END = "wildcardEnd";
	
	/**
	 * automatic Wildcard inserts at end and beginning of String. <p>
	 */
	public final static String WILDCARD_VALUE_ALL = "wildcardAll";

	private final static char PICKLISTTEXT_INDICATOR = '#';
	
	/**
	 * Creates a PropertyQuery with the given Wildcard preference setting. <p>
	 * 
	 * Use <code>addLikeWithWildcardSetting(String Property, String value)</code> Methode
	 * to take effekt of the wildcard setting. <br>
	 * 
	 * @param wildcardPreferenceSetting
	 */
	public PropertyQuery(String wildcardPreferenceSetting) {
		this.wildcardPreferenceSetting = wildcardPreferenceSetting;
	}
	
	/**
	 * default constr. <p>
	 *
	 */
	public PropertyQuery() {
		
	}
	
	/**
	 * Constructor. Allows initializing the consistentOrder property at instantiation.
	 * 
	 * @param consistentOrder
	 */
	public PropertyQuery(boolean consistentOrder) {
		this.consistentOrder = consistentOrder;
	}
	
	/**
	 * <p>Add a property that needs to be joined using a left outer join. This
	 * is required when referenced properties are used in the query wich should
	 * not be filtered out due to the default INNER JOIN method.</p> 
	 * <p>This fixes problems in queries like this:</p>
	 * <code>(betreuer.nachname = ? OR responsible = ?)</code>
	 * 
	 * <p>In the above sample, entries where obj.betreuer is NULL are not returned at all,
	 * because of the INNER JOIN. In that case, it is required to add "betreuer" to the
	 * LEFT OUTER JOIN list to return the correct results.</p> 
	 * 
	 * <p>NOTE that the properties must be specified at the top-most PropertyQuery object:<br>
	 * <code>
	 *   PropertyQuery query = new PropertyQuery();
	 *   PropertyQuery subQuery = new PropertyQuery();
	 *   ..
	 *   subQuery.addOrLike("betreuer.nachname", value);
	 *   subQuery.addOrLike("responsible", value);
	 *   query.addSubQuery(subQuery);
	 *   query.addLeftOuterJoinProperty("betreuer");	// if it would be set to subQuery, it would not work. 
	 * </code>
	 * 
	 * @param property
	 */
	public void addLeftOuterJoinProperty(String property) {
		addLeftOuterJoinProperty(property, property);
	}

	/**
	 * <p>Add a property that needs to be joined using a left outer join. This
	 * is required when referenced properties are used in the query wich should
	 * not be filtered out due to the default INNER JOIN method.</p> 
	 * <p>This fixes problems in queries like this:</p>
	 * <code>(betreuer.nachname = ? OR responsible = ?)</code>
	 * 
	 * <p>In the above sample, entries where obj.betreuer is NULL are not returned at all,
	 * because of the INNER JOIN. In that case, it is required to add "betreuer" to the
	 * LEFT OUTER JOIN list to return the correct results.</p> 
	 * 
	 * <p>NOTE that the properties must be specified at the top-most PropertyQuery object:<br>
	 * <code>
	 *   PropertyQuery query = new PropertyQuery();
	 *   PropertyQuery subQuery = new PropertyQuery();
	 *   ..
	 *   subQuery.addOrLike("betreuer.nachname", value);
	 *   subQuery.addOrLike("responsible", value);
	 *   query.addSubQuery(subQuery);
	 *   query.addLeftOuterJoinProperty("betreuer");	// if it would be set to subQuery, it would not work. 
	 * </code>
	 * 
	 * @param property
	 * @param alias Name of the joined property
	 */
	public void addLeftOuterJoinProperty(String property, String alias) {
		if (!leftOuterJoinProperties.containsKey(alias)) {
			leftOuterJoinProperties.put(alias, property);
		}
	}
	
	/**
	 * Add a subquery.
	 * @param query
	 */
	public void addSubQuery(PropertyQuery query) {
		if (!joinPicklistEntries && query.isJoinPicklistEntries()) {
			joinPicklistEntries = true;
		}
		elements.add(new QueryElement(QueryElement.AND, query));
		// pass joins
		leftOuterJoinProperties.putAll(query.leftOuterJoinProperties);
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.queries.IPropertyQuery#addEquals(java.lang.String, java.lang.Object)
	 */
	@Override
	public QueryElement addEquals(String property, Object value) {
		if (value instanceof IEntity) {
			// store the id instead of the entity.
			value = new Integer(((IEntity)value).getId());
			if (!property.endsWith(".id")) {
				property += ".id";
			}
		}
		if (property.indexOf(PICKLISTTEXT_INDICATOR) != -1) {
			joinPicklistEntries = true;
		}
		return addAux(QueryElement.AND, property, QueryElement.EQUALS, value);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.queries.IPropertyQuery#addOrEquals(java.lang.String, java.lang.Object)
	 */
	public QueryElement addOrEquals(String property, Object value) {
		if (value instanceof IEntity) {
			// store the id instead of the entity.
			value = new Integer(((IEntity)value).getId());
			if (!property.endsWith(".id")) {
				property += ".id";
			}
		}
		if (property.indexOf(PICKLISTTEXT_INDICATOR) != -1) {
			joinPicklistEntries = true;
		}
		QueryElement qe = new QueryElement(QueryElement.OR, property, QueryElement.EQUALS, value);
		elements.add(qe);
		return qe;
	}

	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.queries.IPropertyQuery#addLike(java.lang.String, java.lang.Object)
	 */
	public QueryElement addLike(String property, Object value) {
		if (property.indexOf(PICKLISTTEXT_INDICATOR) != -1) {
			joinPicklistEntries = true;
		}
		return addAux(AND, property, LIKE, value);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.queries.IPropertyQuery#addOrLike(java.lang.String, java.lang.Object)
	 */
	public QueryElement addOrLike(String property, Object value) {
		if (property.indexOf(PICKLISTTEXT_INDICATOR) != -1) {
			joinPicklistEntries = true;
		}
		return addAux(OR, property, LIKE, value);
	}

	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.core.model.queries.IPropertyQuery#addLikeWithWildcardSetting(java.lang.String, java.lang.String)
	 */
	public QueryElement addLikeWithWildcardSetting(String property, String value) {
		String finalValue = null;
		if (WILDCARD_VALUE_END.equals(wildcardPreferenceSetting)) {
			//replace '*' with '%' per default
			finalValue = value.replace('*', '%');		
			
			if (!finalValue.endsWith("%")) {
				finalValue = finalValue + "%";
			}
		}
		else if (WILDCARD_VALUE_ALL.equals(wildcardPreferenceSetting)) {
			//replace '*' with '%' per default
			finalValue = value.replace('*', '%');		
			
			if (!finalValue.endsWith("%")) {
				finalValue = finalValue + "%";
			}
			if (!finalValue.startsWith("%")) {
				finalValue = "%" + finalValue;
			}
		}
		else {
			finalValue = value;
		}
		return addLike(property, finalValue);
	}

	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.queries.IPropertyQuery#addNotEquals(java.lang.String, java.lang.Object)
	 */
	public QueryElement addNotEquals(String property, Object value) {
		
		if (property.indexOf(PICKLISTTEXT_INDICATOR) != -1) {
			joinPicklistEntries = true;
		}
		return addAux(QueryElement.AND, property, QueryElement.NOT_EQUALS, value);

		
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.queries.IPropertyQuery#addOrNotEquals(java.lang.String, java.lang.Object)
	 */
	public QueryElement addOrNotEquals(String property, Object value) {
		
		if (property.indexOf(PICKLISTTEXT_INDICATOR) != -1) {
			joinPicklistEntries = true;
		}
		return addAux(QueryElement.OR, property, QueryElement.NOT_EQUALS, value);
		
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.queries.IPropertyQuery#addGreaterThen(java.lang.String, java.lang.Object)
	 */
	public QueryElement addGreaterThen(String property, Object value) {
		
		QueryElement qe = new QueryElement(property, QueryElement.GREATER_THEN, value); 
		elements.add(qe);
		return qe;
		
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.queries.IPropertyQuery#addGreaterEqualsThen(java.lang.String, java.lang.Object)
	 */
	public QueryElement addGreaterEqualsThen(String property, Object value) {
		
		QueryElement qe = new QueryElement(property, QueryElement.GREATER_EQUALS_THEN, value); 
		elements.add(qe);
		return qe;
		
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.queries.IPropertyQuery#addLowerThen(java.lang.String, java.lang.Object)
	 */
	public QueryElement addLowerThen(String property, Object value) {

		QueryElement qe = new QueryElement(property, QueryElement.LOWER_THEN, value); 
		elements.add(qe);
		return qe;
		
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.queries.IPropertyQuery#addLowerEqualsThen(java.lang.String, java.lang.Object)
	 */
	public QueryElement addLowerEqualsThen(String property, Object value) {
		
		QueryElement qe = new QueryElement(property, QueryElement.LOWER_EQUALS_THEN, value);
		elements.add(qe);
		return qe;
		
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.queries.IPropertyQuery#size()
	 */
	public int size() {
		return elements.size();
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		
		StringBuffer sb = new StringBuffer();
		sb.append("PropertyQuery (");
		boolean first = true;
		for (Iterator<?> it = elements.iterator(); it.hasNext(); ) {
			QueryElement element = (QueryElement)it.next();
			if (first) {
				first = false;
			} else {
				switch (element.getLinkType()) {
				case QueryElement.AND: 
					sb.append(" AND ");
					break;
				case QueryElement.OR: 
					sb.append(" OR ");
					break;
				default:
					sb.append(" [Link=" + element.getLinkType() + "] ");
				}
			}
			sb.append(element.toString());
		}
		sb.append(")");
		return sb.toString();
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.queries.IPropertyQuery#isHideDeleted()
	 */
	public boolean isHideDeleted() {
		return hideDeleted;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.queries.IPropertyQuery#setHideDeleted(boolean)
	 */
	public void setHideDeleted(boolean hideDeleted) {
		this.hideDeleted = hideDeleted;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.queries.IPropertyQuery#addQueryElement(de.xwic.appkit.core.model.queries.QueryElement)
	 */
	public void addQueryElement(QueryElement element) {
		elements.add(element);
		// pass join
		if (element.getSubQuery() != null) {
			leftOuterJoinProperties.putAll(element.getSubQuery().leftOuterJoinProperties);
		}
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.queries.IPropertyQuery#clear()
	 */
	public void clear() {
		elements.clear();
	}

	/**
	 * @return the elements
	 */
	public List<QueryElement> getElements() {
		return elements;
	}

	/**
	 * @param elements the elements to set
	 */
	public void setElements(List<QueryElement> elements) {
		this.elements = elements;
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.queries.IPropertyQuery#addOrLikeWithWildcardSetting(java.lang.String, java.lang.String)
	 */
	public QueryElement addOrLikeWithWildcardSetting(String property, String value) {
		String finalValue = null;
		if (WILDCARD_VALUE_END.equals(wildcardPreferenceSetting)) {
			//replace '*' with '%' per default
			finalValue = value.replace('*', '%');		
			
			if (!finalValue.endsWith("%")) {
				finalValue = finalValue + "%";
			}
		}
		else if (WILDCARD_VALUE_ALL.equals(wildcardPreferenceSetting)) {
			//replace '*' with '%' per default
			finalValue = value.replace('*', '%');		
			
			if (!finalValue.endsWith("%")) {
				finalValue = finalValue + "%";
			}
			if (!finalValue.startsWith("%")) {
				finalValue = "%" + finalValue;
			}
		}
		else {
			finalValue = value;
		}
		
		return addOrLike(property, finalValue);
	}

	/**
	 * @return the joinPicklistEntries
	 */
	public boolean isJoinPicklistEntries() {
		return joinPicklistEntries;
	}

	/**
	 * @param joinPicklistEntries the joinPicklistEntries to set
	 */
	public void setJoinPicklistEntries(boolean joinPicklistEntries) {
		this.joinPicklistEntries = joinPicklistEntries;
	}

	/**
	 * @return the leftOuterJoinProperties as List
	 */
	public List<String> getLeftOuterJoinProperties() {
		return new ArrayList<String>(leftOuterJoinProperties.values());
	}

	/**
	 * @return the leftOuterJoinProperties as Map
	 */
	public Map<String, String> getLeftOuterJoinPropertiesMap() {
		return leftOuterJoinProperties;
	}
	
	/**
	 * @param leftOuterJoinProperties the leftOuterJoinProperties to set
	 */
	public void setLeftOuterJoinPropertiesMap(Map<String, String> leftOuterJoinProperties) {
		this.leftOuterJoinProperties = leftOuterJoinProperties;
	}
	
	/**
	 * @return the wildcardPreferenceSetting
	 */
	public String getWildcardPreferenceSetting() {
		return wildcardPreferenceSetting;
	}

	
	/**
	 * @param wildcardPreferenceSetting the wildcardPreferenceSetting to set
	 */
	public void setWildcardPreferenceSetting(String wildcardPreferenceSetting) {
		this.wildcardPreferenceSetting = wildcardPreferenceSetting;
	}
	
	/**
	 * clones the query
	 */
	public PropertyQuery cloneQuery() {
		return cloneQuery(true);
	}
	
	/**
	 * Create new instance for extended classes to overview.
	 * @return
	 */
	protected PropertyQuery newInstance() {
		return new PropertyQuery();
	}
	
	/**
	 * clones the query
	 * @param cloneElements
	 */
	public PropertyQuery cloneQuery(boolean cloneElements) {
		
		PropertyQuery query = newInstance();
		
		query.setWildcardPreferenceSetting(this.getWildcardPreferenceSetting());
		List<QueryElement> elements = new ArrayList<QueryElement>();
		if (cloneElements) {
			for (QueryElement qe : getElements()) {
				elements.add(qe.cloneElement());				
			}
		}
		query.setElements(elements);
		query.setColumns(null != this.getColumns() ? new ArrayList<String>(this.getColumns()) : null);
		query.leftOuterJoinProperties = new LinkedHashMap<String, String>(leftOuterJoinProperties);
		
		query.setHideDeleted(this.isHideDeleted());
		query.setFetchLazySets(this.isFetchLazySets());
		query.setReturnEntity(this.isReturnEntity());
		query.setJoinPicklistEntries(this.isJoinPicklistEntries());
		
		query.setLanguageId(this.getLanguageId());
		query.setSortField(this.getSortField());
		
		query.setSortDirection(this.getSortDirection());
		
		return query;
		
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((elements == null) ? 0 : elements.hashCode());
		result = prime * result + (hideDeleted ? 1231 : 1237);
		result = prime * result + (joinPicklistEntries ? 1231 : 1237);
		result = prime
				* result
				+ ((leftOuterJoinProperties == null) ? 0
						: leftOuterJoinProperties.hashCode());
		result = prime
				* result
				+ ((wildcardPreferenceSetting == null) ? 0
						: wildcardPreferenceSetting.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.queries.IPropertyQuery#addIn(java.lang.String, java.util.Collection)
	 */
	@Override
	public QueryElement addIn(final String property, final Collection<?> values) {
		return addInAux(AND, property, QueryElement.IN, values);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.queries.IPropertyQuery#addNotIn(java.lang.String, java.util.Collection)
	 */
	@Override
	public QueryElement addNotIn(final String property, final Collection<?> values) {
		return addInAux(AND, property, QueryElement.NOT_IN, values);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.queries.IPropertyQuery#addOrIn(java.lang.String, java.util.Collection)
	 */
	@Override
	public QueryElement addOrIn(final String property, final Collection<?> values) {
		return addInAux(OR, property, QueryElement.IN, values);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.queries.IPropertyQuery#addOrNotIn(java.lang.String, java.util.Collection)
	 */
	@Override
	public QueryElement addOrNotIn(final String property, final Collection<?> values) {
		return addInAux(OR, property, QueryElement.NOT_IN, values);
	}

	/**
	 * @param property
	 * @param values
	 */
	public void oldaddIn(final String property, final Collection<?> values) {
		oldaddInAux(property, values, QueryElement.AND, QueryElement.OR, QueryElement.IN);
	}

	/**
	 * @param property
	 * @param values
	 */
	public void oldaddNotIn(final String property, final Collection<?> values) {
		oldaddInAux(property, values, QueryElement.AND, QueryElement.AND, QueryElement.NOT_IN);
	}

	/**
	 * @param property
	 * @param values
	 */
	public void oldaddOrIn(final String property, final Collection<?> values) {
		oldaddInAux(property, values, QueryElement.OR, QueryElement.OR, QueryElement.IN);
	}

	/**
	 * @param property
	 * @param values
	 */
	public void oldaddOrNotIn(final String property, final Collection<?> values) {
		oldaddInAux(property, values, QueryElement.OR, QueryElement.AND, QueryElement.NOT_IN);
	}

	/**
	 * @param property
	 * @param values
	 * @param linkTypeSubQuery
	 * @param linkTypeElement
	 * @param operation
	 */
	private void oldaddInAux(final String property, final Collection<?> values, final int linkTypeSubQuery, final int linkTypeElement, final String operation) {
		List<? extends Collection<?>> sets = CollectionUtil.breakInSets(idsIfEntities(values), 1000);
		PropertyQuery subQuery = new PropertyQuery();
		for (Collection ids : sets) {
			subQuery.addQueryElement(new QueryElement(linkTypeElement, property, operation, ids));
		}
		if (sets.isEmpty()) {
			subQuery.addEquals("id", null);
		}
		addQueryElement(new QueryElement(linkTypeSubQuery, subQuery));
	}

	/**
	 * @param linkTypeElement
	 * @param property
	 * @param operation
	 * @param values
	 * @return
	 */
	private QueryElement addInAux(final int linkTypeElement, final String property, final String operation, final Collection<?> values) {
		final QueryElement addAux = addAux(linkTypeElement, property, operation, idsIfEntities(values));
		addAux.setRewriteIn(true);
		return addAux;
	}

	/**
	 * @param collection
	 * @return
	 */
	private static Set<?> idsIfEntities(final Collection<?> collection) {
		if (CollectionUtil.isEmpty(collection)) {
			return Collections.emptySet();
		}
		if (CollectionUtil.isOf(collection, IEntity.class)) {
			return EntityUtil.getIds((Collection<? extends IEntity>) collection);
		}
		return CollectionUtil.cloneToSet(collection);
	}

	/**
	 * @return consistentOrder
	 */
	public boolean isConsistentOrder() {
		return consistentOrder;
	}

	/**
	 * @param consistentOrder the value to set for consistentOrder
	 */
	public void setConsistentOrder(boolean consistentOrder) {
		this.consistentOrder = consistentOrder;
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
		final PropertyQuery other = (PropertyQuery) obj;
		if (elements == null) {
			if (other.elements != null)
				return false;
		} else if (!elements.equals(other.elements))
			return false;
		if (hideDeleted != other.hideDeleted)
			return false;
		if (joinPicklistEntries != other.joinPicklistEntries)
			return false;
		if (leftOuterJoinProperties == null) {
			if (other.leftOuterJoinProperties != null)
				return false;
		} else if (!leftOuterJoinProperties
				.equals(other.leftOuterJoinProperties))
			return false;
		if (wildcardPreferenceSetting == null) {
			if (other.wildcardPreferenceSetting != null)
				return false;
		} else if (!wildcardPreferenceSetting
				.equals(other.wildcardPreferenceSetting))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.queries.IPropertyQuery#addEmpty(java.lang.String)
	 */
	@Override
	public QueryElement addEmpty(final String collectionProperty) {
		return addAux(AND, collectionProperty, IS_EMPTY, null);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.queries.IPropertyQuery#addNotEmpty(java.lang.String)
	 */
	@Override
	public QueryElement addNotEmpty(final String collectionProperty) {
		return addAux(AND, collectionProperty, IS_NOT_EMPTY, null);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.queries.IPropertyQuery#addEmpty(java.lang.String)
	 */
	@Override
	public QueryElement addOrEmpty(final String collectionProperty) {
		return addAux(OR, collectionProperty, IS_EMPTY, null);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.queries.IPropertyQuery#addNotEmpty(java.lang.String)
	 */
	@Override
	public QueryElement addOrNotEmpty(final String collectionProperty) {
		return addAux(OR, collectionProperty, IS_NOT_EMPTY, null);
	}

	/**
	 * @param linkTypeElement
	 * @param property
	 * @param value
	 * @param operation
	 * @return 
	 */
	private QueryElement addAux(final int linkTypeElement, final String property, final String operation, final Object value) {
		final QueryElement element = new QueryElement(linkTypeElement, property, operation, value);
		elements.add(element);
		return element;
	}

	/**
	 * @param alias
	 * @return
	 */
	public String getAliasMapping(final String alias) {
		if (leftOuterJoinProperties == null) {
			return null;
		}
		return leftOuterJoinProperties.get(alias);
	}

}
