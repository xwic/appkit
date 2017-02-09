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
		
		long entityID = query.getEntityID();
		String propertyName = query.getSetProperty();
		String relatedEntityClazzName = query.getRelatedEntityImplClazzName();
		
		StringBuffer sb = new StringBuffer();
		sb.append(" obj.id IN (select base.");
		sb.append(propertyName);
		sb.append(".id from ").append(relatedEntityClazzName);
		sb.append(" base where base.id = ?)");
		
		customWhereClauses.add(sb.toString());
		customValues.add(new Long(entityID));

		return super.resolve(entityClass, query, justCount, customFromClauses, customWhereClauses, customValues);
	}
}
