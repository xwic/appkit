/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.dao.IEntityQueryResolver
 * Created on 11.07.2005
 *
 */
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
