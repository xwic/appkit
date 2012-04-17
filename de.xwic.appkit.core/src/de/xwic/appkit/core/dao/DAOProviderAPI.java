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
    public IEntity getEntity(Class<? extends Object> clazz, int id) throws DataAccessException;
    
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
	public Collection<?> getCollectionProperty(Class<? extends Entity> entityImplClass, int entityId, String propertyId);

}
