/**
 * 
 */
package de.xwic.appkit.core.model.queries.resolver.hbn;

import java.util.ArrayList;
import java.util.List;

import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.model.queries.SetRelatedQuery;

/**
 * @author Ronny Pfretzschner
 *
 */
public class SetRelatedQueryResolver extends PropertyQueryResolver {
	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.core.model.queries.resolver.hbn.PropertyQueryResolver#resolve(java.lang.Class, de.xwic.appkit.core.dao.EntityQuery, boolean)
	 */
	public Object resolve(Class<? extends Object> entityClass, EntityQuery entityQuery, boolean justCount) {
		SetRelatedQuery query = (SetRelatedQuery)entityQuery;

		List<String> customFromClauses = new ArrayList<String>();
		List<String> customWhereClauses = new ArrayList<String>();
		List<Object> customValues = new ArrayList<Object>();
		
		int entityID = query.getEntityID();
		String propertyName = query.getSetProperty();
		String relatedEntityClazzName = query.getRelatedEntityImplClazzName();
		
		StringBuffer sb = new StringBuffer();
		sb.append(" obj.id IN (select base.");
		sb.append(propertyName);
		sb.append(".id from ").append(relatedEntityClazzName);
		sb.append(" base where base.id = ?)");
		
		customWhereClauses.add(sb.toString());
		customValues.add(new Integer(entityID));

		return super.resolve(entityClass, query, justCount, customFromClauses, customWhereClauses, customValues);
	}
}
