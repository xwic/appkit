/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.dao.IEntityQueryResolver
 * Created on 11.07.2005
 *
 */
package de.xwic.appkit.core.dao;

import java.util.List;

import de.xwic.appkit.core.model.queries.QueryElement;


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

	/**
	 * @param entityClass
	 * @param query
	 * @param justCount
	 * @param values
	 * @param customFromClauses
	 * @param customWhereClauses
	 * @param customValues
	 * @return
	 */
	String generateQuery(Class<? extends Object> entityClass, EntityQuery query, boolean justCount, List<QueryElement> values,
			List<String> customFromClauses, List<String> customWhereClauses, List<Object> customValues);

}
