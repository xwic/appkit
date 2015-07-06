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
package de.xwic.appkit.core.model.queries.resolver.hbn;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.dao.IEntityQueryResolver;
import de.xwic.appkit.core.model.entities.IPicklistEntry;

/**
 * Abstract class for the QueryResolvers. <p>
 * This class is Hibernate dependend. 
 * 
 * @author Ronny Pfretzschner
 *
 */
public abstract class QueryResolver implements IEntityQueryResolver {

	private final static String ORDER_PROP_NAME = "pentry";
	private final static int TYPE_PROPERTY = 0;
	private final static int TYPE_PICKLIST = 1;
	private final static int TYPE_SET = 2;
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.IEntityQueryResolver#resolve(java.lang.Class, de.xwic.appkit.core.dao.EntityQuery)
	 */
	public abstract Object resolve(Class<? extends Object> entityClass, EntityQuery query, boolean justCount);

	
	/**
	 * Returns a String part which contains the "order by" clause. <p>
	 * 
	 * Give the EntityQuery as parameter, to get the sorting criterias. <br>
	 * The Alias, which was eventually used in queries, is also necessary.
	 * If no Alias was used, use null as parameter. <br>
	 * 
	 * One space is inserted, before the order by clause starts.
	 * 
	 * @param query The EntityQuery, which contains the sorting criterias
	 * @param The Alias, which was used in the queries or null to be ignored
	 * @param hasCriterias If criterias are already set to the query
	 * @param fromPart Used by the QueryResolver to join required tables 
	 * @return A String part like: " order by obj.firmenname asc", " order by firmenname desc", etc.
	 */
	public String addSortingClause(EntityQuery query, String alias, boolean hasCriterias, StringBuffer fromPart, Class<?> rootClass) {
		
		String fieldname = query.getSortField();
		
		if (fieldname == null || fieldname.length() < 1) {
			return "";
		}
		
		int sortingType = query.getSortDirection();
		
		StringBuffer buffer = new StringBuffer();
		
		int type = getPropertyType(rootClass, fieldname); 
		switch (type) {
			case TYPE_PICKLIST : 
				fromPart.append("\n\tleft outer join obj.")             //add picklist join to from clause
				.append(fieldname)
				.append(".pickTextValues as ")
				.append(ORDER_PROP_NAME);
				 
				if (!hasCriterias) {
					buffer.append(" where ");
				} else {
					buffer.append(" and ");
				}
				buffer.append("\t (")                    //add language constraint to where clause
				.append(ORDER_PROP_NAME)
				.append(".languageID = '")
				.append(query.getLanguageId())
				.append("'")
				.append(" OR ")
				.append(ORDER_PROP_NAME)
				.append(" IS NULL)")
				;
				
				buffer.append(" order by ");
				buffer.append(ORDER_PROP_NAME).append(".bezeichnung");
				break;
				
			case TYPE_PROPERTY: 
				buffer.append(" order by " + alias + ".");
				buffer.append(fieldname);
				break;
				
			case TYPE_SET: 
				return "";	// NOT SUPPORTED
				//break;
		}
		
		switch (sortingType) {
			case EntityQuery.SORT_DIRECTION_DOWN : {
				buffer.append(" desc");
				break;
			}
			case EntityQuery.SORT_DIRECTION_UP : {
				buffer.append(" asc");
				break;
			}
			default : {
				buffer.append(" asc");
			}
		}
		
		return buffer.toString();
	}


	/**
	 * @param alias
	 * @return
	 */
	private int getPropertyType(Class<?> rootClazz, String alias) {
		
		StringTokenizer stk = new StringTokenizer(alias, ".");
		Class<?> clazz = rootClazz;
		while (stk.hasMoreTokens()) {
			String propertyName = stk.nextToken();

			PropertyDescriptor desc;
			try {
				desc = new PropertyDescriptor(propertyName, clazz);
			} catch (IntrospectionException e) {
				break;
			}
			clazz = desc.getPropertyType();
		}
		if (IPicklistEntry.class.isAssignableFrom(clazz)) {
			return TYPE_PICKLIST;
		} else if (Set.class.isAssignableFrom(clazz)) {
			return TYPE_SET;
		} 
		return TYPE_PROPERTY;
	}
	
	/**
	 * Create the first part of the query including the from key.
	 * @param query
	 * @param objectClass
	 * @param justCount
	 * @return
	 */
	protected String createFrom(EntityQuery query, Class<? extends Object> objectClass, boolean justCount) {
		StringBuffer sb = new StringBuffer();
		
		if (justCount) {
			sb.append("SELECT count(*)");
		} else {
			List<String> columns = query.getColumns();
			if (columns != null) {
				//sb.append("SELECT obj");
				sb.append("select obj.id");
				for (String s : columns) {
					sb.append(", obj.").append(s);
					
				}
			}
		}
		sb.append(" FROM ")
			.append(objectClass.getName())
			.append(" AS obj ");

		
		return sb.toString();
	}
	
	/**
	 * Method replaces '*' with '%' for the database. <p>
	 * 
	 * It will also add a default '%' in front AND at the
	 * end of the searchstring.
	 * 
	 * @param querypart
	 * @return modified String
	 */
	protected String replaceWildcard(String querypart) {
		
		//do nothing, if part is null
		if (querypart == null) {
			return "";
		}
		
		//replace '*' with '%' per default
		String newStr = querypart.replace('*', '%');		
		
		if (!newStr.startsWith("%")) {
			newStr = "%" + newStr;
		}
		if (!newStr.endsWith("%")) {
			newStr = newStr + "%";
		}
		return newStr;
	}
}
