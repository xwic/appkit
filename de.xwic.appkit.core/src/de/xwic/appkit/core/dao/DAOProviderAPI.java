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
/*
 * de.xwic.appkit.core.dao.DAO
 * Created on 04.04.2005
 *
 */
package de.xwic.appkit.core.dao;

import java.util.Collection;


/**
 * Implements basic CRUD (Create Read Update Delete) operations. 
 * These operations are used by the DAO implementation.
 * 
 * @author Florian Lippisch
 */
public interface DAOProviderAPI {

    /**
     * Remove the entity from the database. If the entity is unsaved, 
     * an exception is thrown.
     * @param entity
     * @return
     */
    public void delete(IEntity entity) throws DataAccessException;

    /**
     * Marks the entity as deleted. The entities "deleted" property is
     * set to true after reference checks.
     * @param entity
     * @return
     */
    public void softDelete(IEntity entity) throws DataAccessException;

    /**
     * Returns the entity with the specified ID. 
     * @param id
     * @return
     */
    public IEntity getEntity(Class<? extends Object> clazz, long id) throws DataAccessException;
    
    /**
     * Update the entity in the database. 
     * @param entity
     * @return
     */
    public void update(IEntity entity) throws DataAccessException;
 
	/**
	 * Returns the list of entities.
	 * @param limit
	 * @return
	 */
	public EntityList getEntities(Class<? extends Object> clazz, Limit limit);
	
	/**
	 * Returns a list of entities filterd by the EntityFilter. If the filter argument
	 * is <code>null</code>, all entities are returned (only limited by the Limit).
	 * @param limit
	 * @param filter
	 * @return
	 */
	public EntityList getEntities(Class<? extends Object> clazz, Limit limit, EntityQuery filter);

	/**
	 * @param entityImplClass
	 * @param entityId
	 * @param propertyId
	 * @return
	 */
	public Collection<?> getCollectionProperty(Class<? extends IEntity> entityImplClass, long entityId, String propertyId);

}
