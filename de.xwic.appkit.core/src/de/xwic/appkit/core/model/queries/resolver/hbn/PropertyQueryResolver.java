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

import org.apache.commons.lang.Validate;
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

		String sortField = query.getSortField();
		if (null != sortField && sortField.indexOf('.') > 0) {
			// add the property to the left outer join properties
			addToJoin(joinMap, sortField);
		}
		
		sbFrom.append(createFrom(query, entityClass, justCount));

		Map<String, String> joinPropertiesMap = query.getLeftOuterJoinPropertiesMap();
		for (Map.Entry<String, String> entry : joinPropertiesMap.entrySet()) {
			String alias = entry.getKey();
			String property = entry.getValue();
			joinMap.put(property, alias);
		}
		
		// add all columns that are a referenced entity
		if (query.getColumns() != null && !query.getColumns().isEmpty()) {
			for (String prop : query.getColumns()) {
				addToJoin(joinMap, prop);
			}
		}

		// add the joins
		for (Map.Entry<String, String> entry : joinMap.entrySet()) {
			String property = entry.getKey();
			String alias = entry.getValue();
			sbFrom.append("\n LEFT OUTER JOIN obj.")
			  .append(property)
			  .append(" ");
			if (alias != null && !alias.equals(property)) {
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
		
		buildQuery(sb, values, query);
		
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
	 * @param joinMap
	 * @param sortField
	 */
	private void addToJoin(Map<String, String> joinMap, String s) {
		int idx;
		while ((idx = s.lastIndexOf('.')) != -1) { 
			s= s.substring(0, idx);
			if (!joinMap.containsKey(s)) {
				joinMap.put(s, null);
			}
		}
	}

	/**
	 * @param sb
	 * @param values
	 * @param query
	 */
	private void buildQuery(StringBuffer sb, List<QueryElement> values, PropertyQuery query) {

		boolean first = true;
		
		for (Iterator<QueryElement> it = query.getElements().iterator(); it.hasNext(); ) {
			QueryElement qe = it.next();
			
			String aliasPrefix = null;
			if (qe.getAlias() != null) {
				aliasPrefix = qe.getAlias() + ".";
			} else {
				aliasPrefix = "";
			}
			
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
				
				sb.append("(");
				buildQuery(sb, values, qe.getSubQuery());
				sb.append(")");
				
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

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.EntityQueryAdapter#generateQuery(java.lang.Class, de.xwic.appkit.core.dao.EntityQuery, boolean, java.util.List, java.util.List, java.util.List, java.util.List)
	 */
	@Override
	public String generateQuery(final Class<? extends Object> entityClass, final EntityQuery query, final boolean justCount,
			final List<QueryElement> values, final List<String> customFromClauses, final List<String> customWhereClauses, 
			final List<Object> customValues) {
		Validate.notNull(query);
		if (!(query instanceof PropertyQuery)) {
			throw new IllegalArgumentException();
		}

		return createHsqlQuery(entityClass, (PropertyQuery) query, justCount, values, customFromClauses, customWhereClauses, customValues);
	}

}
