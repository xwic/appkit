/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.model.queries.resolver.hbn.PropertyQueryResolver
 * Created on 07.12.2006 by Florian Lippisch
 *
 */
package de.xwic.appkit.core.model.queries.resolver.hbn;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;

import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.dao.impl.hbn.HibernateUtil;
import de.xwic.appkit.core.model.queries.IPropertyQuery;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.core.model.queries.QueryElement;
import de.xwic.appkit.core.util.CollectionUtil;

/**
 * @author Florian Lippisch
 */
public class PropertyQueryResolver extends QueryResolver {

	private final Log log = LogFactory.getLog(getClass());
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.queries.resolver.hbn.QueryResolver#resolve(java.lang.Class, de.xwic.appkit.core.dao.EntityQuery, boolean)
	 */
	public Object resolve(Class<? extends Object> entityClass, EntityQuery entityQuery, boolean justCount) {
		return resolve(entityClass, entityQuery, justCount, new ArrayList<String>(), new ArrayList<String>(), new ArrayList<Object>());
	}
	
	/**
	 * Does the resolve with custom settings for from and where clauses. <p>
	 * 
	 * The values are set in a list.
	 * 
	 * @param entityClass
	 * @param entityQuery
	 * @param justCount
	 * @param customFromClauses
	 * @param customWhereClauses
	 * @param customValues
	 * @return the query object
	 */
	protected Object resolve(Class<? extends Object> entityClass, EntityQuery entityQuery, boolean justCount, List<String> customFromClauses, List<String> customWhereClauses, List<Object> customValues) {
		PropertyQuery query = (PropertyQuery)entityQuery;

		rewriteQueryElements(query);

		List<QueryElement> values = new ArrayList<QueryElement>();
		String hsql = createHsqlQuery(entityClass, query, justCount, values, customFromClauses, customWhereClauses, customValues);
//		System.out.println(hsql);
		
		Session session = HibernateUtil.currentSession();
		Query q;
		try {
			q = session.createQuery(hsql);
		} catch (RuntimeException npe) {
			String qts = "null";
			if (query != null){
				qts = query.toString();
			}
			log.error(String.format("Failed to create Query!\nPQ: %s\nHSQL: %s\n", qts, hsql), npe);
			throw npe;
		}

		// add values
		int idx = 0;
		for (Iterator<QueryElement> it = values.iterator(); it.hasNext(); ) {
			QueryElement qe = it.next();
			idx += setValues(query, q, qe, idx);
		}
		for (Iterator<Object> it = customValues.iterator(); it.hasNext(); ) {
			Object value = it.next();
			if (!(value instanceof QueryElement)) {
				// wrap the value into a QueryElement.
				QueryElement qe = new QueryElement();
				qe.setValue(value);
				value = qe;
			}
			idx += setValues(query, q, (QueryElement)value, idx);
		}
		return q;
	}
	
	/**
	 * @param elements
	 */
	private static void rewriteQueryElements(final PropertyQuery query) {
		if (null == query) {
			return;
		}
		final List<QueryElement> elements = query.getElements();
		final Map<Integer, QueryElement> rewrites = new HashMap<Integer, QueryElement>();
		for (int i = 0, to = elements.size(); i < to; i++) {
			final QueryElement existingElement = elements.get(i);
			final QueryElement maybeRewrite = maybeRewrite(existingElement);
			if (existingElement != maybeRewrite) {
				rewrites.put(i, maybeRewrite);
			}
		}
		for (final Entry<Integer, QueryElement> entry : rewrites.entrySet()) {
			elements.set(entry.getKey(), entry.getValue());
		}
		for (final QueryElement queryElement : elements) {
			rewriteQueryElements(queryElement.getSubQuery());
		}
	}

	/**
	 * @param entityClass
	 * @param query
	 * @param justCount
	 * @param customFromClauses
	 * @param customWhereClauses
	 * @param customValues
	 * @return
	 */
	public String createHsqlQuery(Class<? extends Object> entityClass, PropertyQuery query, boolean justCount, List<QueryElement> values, List<String> customFromClauses, List<String> customWhereClauses, List<Object> customValues) {
		
		boolean checkPicklistJoin = query.isJoinPicklistEntries();
		
		StringBuffer sb = new StringBuffer();
		StringBuffer sbFrom = new StringBuffer();
		
		//see if we try to sort for a property1.attribute1
		Map<String, String> joinMap = new HashMap<String, String>();
		Map<String, String> remappedJoins = new HashMap<String, String>();

		String sortField = query.getSortField();
		if (null != sortField && sortField.indexOf('.') > 0) {
			// add the property to the left outer join properties
			addToJoin(sortField, joinMap, remappedJoins);
		}
		
		sbFrom.append(createFrom(query, entityClass, justCount));

		Map<String, String> joinPropertiesMap = query.getLeftOuterJoinPropertiesMap();
		for (Map.Entry<String, String> entry : joinPropertiesMap.entrySet()) {
			String alias = entry.getKey();
			String property = entry.getValue();
			add(property, alias, joinMap, remappedJoins);
		}
		
		// add all columns that are a referenced entity
		if (query.getColumns() != null && !query.getColumns().isEmpty()) {
			for (String prop : query.getColumns()) {
				addToJoin(prop, joinMap, remappedJoins);
			}
		}

		// add the joins
		for (Map.Entry<String, String> entry : joinMap.entrySet()) {
			String property = entry.getKey();
			String alias = entry.getValue();
			sbFrom.append("\n LEFT OUTER JOIN obj.")
			  .append(property)
			  .append(" ");
			if (isAliasRelevant(property, alias)) {
				// add alias
				sbFrom.append("AS ").append(alias).append(" ");
			}
		}
		
		//add custom registered from clauses 
		for (int i = 0; i < customFromClauses.size(); i++) {
			String customFrom = (String) customFromClauses.get(i);
			sbFrom.append(" ").append(customFrom).append(" ");
		}

		if (query.isHideDeleted()) {
			sb.append(" obj.deleted = 0 ");
			if (query.size() > 0) {
				sb.append("AND ");
			} 
		}
		
		// build query
		//System.out.println(sb);
		
		buildQuery(sb, values, query, remappedJoins);
		
		int size = query.size();

		// add custom where clause parts
		
		for (int i = 0; i < customWhereClauses.size(); i++) {
			String customWhere = (String) customWhereClauses.get(i);
			if (!(size == 0 && i == 0 && !query.isHideDeleted())) {
				sb.append(" AND ");
			}
			sb.append(" ").append(customWhere).append(" ");
		}

		// add the order by part
		
		String orderBy = "";
		if (!justCount) {
			boolean hasCriteria = size != 0 || query.isHideDeleted();
			orderBy = addSortingClause(query, "obj", hasCriteria, sbFrom, entityClass);
		}
			
		if(query.isConsistentOrder() && orderBy.indexOf("obj.id") == -1) {
			if(orderBy.length() != 0) {
				orderBy += ", "; 
			} else {
				orderBy = " order by ";
			}
			orderBy += "obj.id asc";
		}
				
		if (checkPicklistJoin) {
			PicklistJoinQueryParser tescht = new PicklistJoinQueryParser(entityClass.getName(), sbFrom.toString(), sb.toString(), query.getLanguageId(), justCount);
			return tescht.toString() + " " + orderBy;
		} else {
			String erg = sbFrom + "\n" +
				(sb.length() != 0 ? " WHERE " : "") +
				sb.toString() + " " + orderBy;
			return erg;
		}
	}

	/**
	 * @param property
	 * @param alias
	 * @return
	 */
	private static boolean isAliasRelevant(String property, String alias) {
		return alias != null && !alias.equals(property);
	}

	/**
	 * @param property
	 * @param alias
	 * @param joinMap
	 * @param rewrittenJoins
	 */
	private static void add(final String property, final String alias, final Map<String, String> joinMap, final Map<String, String> remappedJoins) {
		final String currentAlias = joinMap.get(property);
		if (isAliasRelevant(property, currentAlias)) {
			remappedJoins.put(alias, currentAlias);
		} else {
			joinMap.put(property, alias);
		}
	}

	/**
	 * @param joinMap
	 * @param sortField
	 */
	private static void addToJoin(String s, final Map<String, String> joinMap, final Map<String, String> remappedJoins) {
		int idx;
		while ((idx = s.lastIndexOf('.')) != -1) { 
			s= s.substring(0, idx);
			if (!joinMap.containsKey(s)) {
				add(s, null, joinMap, remappedJoins);
			}
		}
	}

	/**
	 * @param element
	 * @param remappedJoins
	 * @return
	 */
	private static String getAliasPrefix(final QueryElement element, final Map<String, String> remappedJoins) {
		String alias = element.getAlias();
		final String renamedAlias = remappedJoins.get(alias);
		if (renamedAlias != null) {
			alias = renamedAlias;
		}
		if (alias == null) {
			return "";
		}
		return alias + ".";
	}
	/**
	 * @param sb
	 * @param values
	 * @param query
	 */
	private void buildQuery(StringBuffer sb, List<QueryElement> values, PropertyQuery query, final Map<String, String> remappedJoins) {

		boolean first = true;
		
		final List<QueryElement> elements = query.getElements();
		final boolean requiresWrapping = elements.size() > 1;
		if (requiresWrapping) {
			sb.append("(");
		}
		for (final QueryElement raw : elements) {
			final QueryElement qe = maybeRewrite(raw);
			final String aliasPrefix = getAliasPrefix(qe, remappedJoins);
			
			if (first) {
				first = false;
			} else {
				switch (qe.getLinkType()) {
				case QueryElement.AND: 
					sb.append(" AND ");
					break;
				case QueryElement.OR: 
					sb.append(" OR ");
					break;
				default: 
					throw new IllegalArgumentException("Illegal linkType specified - only AND or OR supported");
				}
			}
			if (qe.getSubQuery() != null) {
				
				buildQuery(sb, values, qe.getSubQuery(), remappedJoins);
				
			} else {
				if (qe.getValue() == null) {
					final String operation = qe.getOperation();
					sb.append(aliasPrefix).append(qe.getPropertyName());
					if (qe.getOperation().equals(QueryElement.EQUALS)) {
						sb.append(" IS NULL");
					} else if (qe.getOperation().equals(QueryElement.NOT_EQUALS)) {
						sb.append(" IS NOT NULL");
					} else if (operation.equals(QueryElement.IS_EMPTY)) {
						sb.append(" IS EMPTY");
					} else if (operation.equals(QueryElement.IS_NOT_EMPTY)) {
						sb.append(" IS NOT EMPTY");
					} else {
						throw new IllegalArgumentException("NULL values are only allowed for EQUALS, NOT_EQUALS, IS_EMPTY or IS_NOT_EMPTY operations.");
					}
				} else {
					if(DAOSystem.isOracleCaseInsensitve() && QueryElement.LIKE.equals(qe.getOperation())){
						sb.append("UPPER(").append(aliasPrefix)
						  .append(qe.getPropertyName())
						  .append(") ")
						  .append(qe.getOperation())
						  .append(" UPPER(?)");
					} else if (QueryElement.EQUALS.equals(qe.getOperation()) && qe.isCollectionElement()) {
						sb.append("? IN ELEMENTS(")
						  .append(aliasPrefix)
						  .append(qe.getPropertyName())
						  .append(")");
					} else if (QueryElement.NOT_EQUALS.equals(qe.getOperation()) && qe.isCollectionElement()) {
						sb.append("? NOT IN ELEMENTS(")
						  .append(aliasPrefix)
						  .append(qe.getPropertyName())
						  .append(")");
					}else{
						sb.append(aliasPrefix)
						  .append(qe.getPropertyName())
						  .append(" ")
						  .append(qe.getOperation());
						if (qe.getOperation().equals(QueryElement.IN) || qe.getOperation().equals(QueryElement.NOT_IN)) {
							// get array/collection size
							int size = 1;
							if (qe.getValue().getClass().isArray()) {
								size = ((Object[])qe.getValue()).length;
							} else if (Collection.class.isAssignableFrom(qe.getValue().getClass())) {
								size = ((Collection<?>)qe.getValue()).size();
							}
							if (size < 1) {
								throw new IllegalArgumentException("IN operator requires Array or Collection size > 0.");
							}
							sb.append(" ");
							String delimiter = "(";
							for (int i = 0; i < size; i++) {
								sb.append(delimiter).append("?");
								delimiter = ",";
							}
							sb.append(")");
						} else {
							sb.append(" ?");
						}
					}
					values.add(qe);
				}
			}
		}
		if (requiresWrapping) {
			sb.append(")");
		}
		
	}

	/**
	 * Set the value for the query.
	 * @return number of values set in query, default is 1
	 * @param query
	 * @param q
	 * @param element
	 * @param index
	 */
	private int setValues(IPropertyQuery query, Query q, QueryElement element, int index) {
		int i = 1;
		Object value = element.getValue();
		if (value == null) {
			q.setEntity(index, null);
			
		} else if (value instanceof String) { // String type
			String s = (String)value;
			
			if (element.getOperation().equals(QueryElement.LIKE)) {
				q.setString(index, s.replace('*', '%'));
			} else {
				q.setString(index, s);
			}
		} else if (value instanceof Integer) { // int
			q.setInteger(index, ((Integer)value).intValue());

		} else if (value instanceof Long) {
			q.setLong(index, ((Long)value).longValue());
		}
		else if (value instanceof Double) { // int
			q.setDouble(index, ((Double)value).doubleValue());

		} else if (value instanceof Boolean) { // boolean
			q.setBoolean(index, ((Boolean)value).booleanValue());
			
		} else if (value instanceof GregorianCalendar) { // Date that got transformed by Axis into GregorianCalendar
			if (element.isTimestamp()) {
				q.setTimestamp(index, ((GregorianCalendar)value).getTime());
			} else {
				q.setDate(index, ((GregorianCalendar)value).getTime());
			}
		} else if (value instanceof Date) { // Date
			if (element.isTimestamp()) {
				q.setTimestamp(index, (Date)value);
			} else {
				q.setDate(index, (Date)value);
			}
		} else if (value instanceof IEntity) { // Entity
			q.setEntity(index, value);
		} else if (element.getOperation().equals(QueryElement.IN) || element.getOperation().equals(QueryElement.NOT_IN)) { 
			if (value.getClass().isArray()) { // array
				i--;
				for (Object v : (Object[])value) {
					QueryElement e = element.cloneElement();
					e.setValue(v);
					i += setValues(query, q, e, index + i);
				}
			} else if (Collection.class.isAssignableFrom(value.getClass())) { // Collection
				i--;
				for (Object v : (Collection<?>)value) {
					QueryElement e = element.cloneElement();
					e.setValue(v);
					i += setValues(query, q, e, index + i);
				}
			}
		} else {
			throw new IllegalArgumentException("Invalid value type: " + value.getClass());
		}
		return i;
	}

	/**
	 * @param element
	 * @return
	 */
	private static QueryElement maybeRewrite(final QueryElement element) {
		if (!element.requiresRewrite()) {
			return element;
		}

		final Collection<?> collection = (Collection<?>) element.getValue();

		if (collection.isEmpty()) {
//			this is a problem when we have no id, 
			return new QueryElement(element.getLinkType(), "id", QueryElement.EQUALS, null); // this kills this element
		}
		if (element.isCollectionElement()) {
			return rewriteCollectionInCollection(element, collection);
		}
		if (collection.size() <= QueryElement.MAXIMUM_ELEMENTS_IN) {
			return element;
		}
		final String operation = element.getOperation();
		final PropertyQuery subQuery = new PropertyQuery();

//		if you want to search in 1000 elements you want "if x is in () or in ()"
//		if you want to search not in 100 elements, you want "if x not in () and not in ()"
		final int link = QueryElement.IN.equals(operation) ? QueryElement.OR : QueryElement.AND;

		element.setValue(null); // don't clome the value
		final int max = QueryElement.MAXIMUM_ELEMENTS_IN;
		for (final Collection<?> safeCollection : CollectionUtil.breakInSets(collection, max)) {
			final QueryElement clome = element.cloneElement();
			clome.setLinkType(link);
			clome.setValue(safeCollection);
			clome.setRewriteIn(false); // no need to try again
			subQuery.addQueryElement(clome);
		}
		return new QueryElement(element.getLinkType(), subQuery);
	}

	/**
	 * @param element
	 * @param collection
	 * @return
	 */
	private static QueryElement rewriteCollectionInCollection(final QueryElement element, final Collection<?> collection) {
		final PropertyQuery pq = new PropertyQuery();
		final int inLinkType = element.getInLinkType();
		element.setValue(null); // don't clome the value
		for (final Object object : collection) {
			final QueryElement clome = element.cloneElement();
			clome.setValue(object);
			clome.setOperation(QueryElement.EQUALS);
			clome.setLinkType(inLinkType);
			pq.addQueryElement(clome);
		}
		return new QueryElement(element.getLinkType(), pq);
	}

}
