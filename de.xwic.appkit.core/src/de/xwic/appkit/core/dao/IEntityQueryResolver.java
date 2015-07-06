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
package de.xwic.appkit.core.dao;



/**
 * Transforms a generic EntityQuery into a implementation specific query. DAOProvider
 * implementations use the resolvers to generate type specific queries.
 * @author Florian Lippisch
 */
public interface IEntityQueryResolver {

	/**
	 * Create a query String or Query object used by the DAO implementation to filter
	 * the objects.
	 * @param entityClass
	 * @param query
	 * @param justCount
	 * @return
	 */
	public Object resolve(Class<? extends Object> entityClass, EntityQuery query, boolean justCount);

}
