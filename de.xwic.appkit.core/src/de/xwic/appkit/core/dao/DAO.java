/*
 * de.xwic.appkit.core.dao.DAO
 * Created on 04.04.2005
 *
 */
package de.xwic.appkit.core.dao;

import java.util.Collection;
import java.util.List;

import de.xwic.appkit.core.config.model.EntityDescriptor;



/**
 * Maps basic CRUD (Create Read Update Delete) methods to a
 * DAOProvider and implements object specific operations.
 *
 * @author Florian Lippisch
 */
public interface DAO<I extends IEntity> {

	/** The name of the trace category all DAO operations use. */
	public final static String TRACE_CAT = "DAO";

	/**
	 * Creates an unsaved entity. The final DAO implementation may
	 * implement a <code>createXY(..)</code> method that has the correct
	 * return type. This methode just creates an "empty" object
	 * with no "must have" parameters. Use this methode wisely!
	 *
	 * There should be a named methode like <code>createUnternehmen(..)</code><br>
	 * in the specific DAO for the specific business object. <br>
	 * If not, then this methode is the right way to get an entitiy instance.
	 *
	 * @return unsaved entity with no fields filled or default filled
	 */
	public I createEntity() throws DataAccessException;

    /**
	 * Creates an unsaved entity of the specified subtype. This method is
	 * used for DAOs that support subclassed entitys.
	 * @param type
	 * @return
	 */
    public IEntity createEntity(String subtype);

    /**
     * Returns the Class managed by the DAO implementation.
     */
	public Class<I> getEntityClass();

    /**
     * Returns true if the DAO implementation can handle entities of the
     * specified class.
     * @param entityClass
     * @return
     */
    public boolean handlesEntity(String entityClass);

    /**
	 * Returns the entity with the specified ID.
	 * @param id
	 * @return
	 */
	public I getEntity(int id) throws DataAccessException;

    /**
	 * Update the entity in the database.
	 * @param entity
	 * @return
	 */
	public void update(IEntity entity) throws DataAccessException;

    /**
	 * Delete the entity from the database. If the entity is unsaved,
	 * an exception is thrown.
	 * @param entity
	 * @return
	 */
    public void delete(IEntity entity) throws DataAccessException;

    /**
     * Marks the entity as deleted. The entities "deleted" property is
     * set to true after reference checks.
     * @param entity
     * @throws DataAccessException
     */
    public void softDelete(final IEntity entity) throws DataAccessException;

	/**
	 * Returns the list of entities.
	 * @param limit
	 * @return
	 */
	public EntityList<I> getEntities(Limit limit);

	/**
	 * Returns a list of entities filtered by the EntityFilter. If the filter argument
	 * is <code>null</code>, all entities are returned (only limited by the Limit).
	 * @param limit
	 * @param filter
	 * @return
	 */
	public EntityList<I> getEntities(Limit limit, EntityQuery filter);

	/**
	 * Returns a list of entities filtered by the EntityFilter. If the filter argument
	 * is <code>null</code>, all entities are returned (only limited by the Limit).
	 * Offers the possibility to bypass the read rights check. Useful for example in case we want to get the entities for a softDelete validation
	 * @param limit
	 * @param filter
	 * @param checkReadRights
	 * @return
	 */
	public EntityList<I> getEntities(Limit limit, EntityQuery filter, boolean checkReadRights);

	/**
	 * Fetches the collection specified in a single property for the given type.
	 * @param entityId
	 * @param propertyId
	 * @return
	 */
	public Collection<?> getCollectionProperty(int entityId, String propertyId);

	/**
	 * Returns a list of history entities filtered by the EntityFilter. If the filter argument
	 * is <code>null</code>, all entities are returned (only limited by the Limit).
	 * @param limit
	 * @param filter
	 * @return
	 * @throws UnsupportedOperationException if this entity does not support history.
	 */
	public EntityList getHistoryEntities(Limit limit, EntityQuery filter);

    /**
	 * Returns the history entity with the specified ID.
	 * @param id
	 * @return
	 */
	public IEntity getHistoryEntity(final int id) throws DataAccessException;

    /**
	 * Checks an entity on its validate state. <p>
	 *
	 * This checks, if all "must to have" fields are filled properly
	 * and other necessary checks. <br>
	 * The ValidationResult contains the property as Strings to be key and the
	 * value is a ResourceString key to get the proper error message
	 * from the Resource property file.<br>
	 *
	 * Should be done before an update will be made.
	 *
	 * @param entity the entity which should be validated
	 * @return ValidationResult with all properties, which have validate errors
	 */
    public abstract ValidationResult validateEntity(IEntity entity);

    /**
     * Create a title string for the specified entity. The default
     * implementation is using the titlePattern that is specified within
     * the EntityDescriptor.
     * @param entity
     * @return
     */
    public String buildTitle(IEntity entity);

    /**
     * @return Returns the provider.
     */
    public DAOProvider getProvider();

    /**
     * @param provider The provider to set.
     */
    public void setProvider(DAOProvider provider);

	/**
	 * Returns the list of history objects for the specified entity.
	 * @param unternehmen
	 * @return List of history objects implementing IEntity and IHistory
	 */
    public List<IEntity> getHistory(IEntity entity);

    /**
	 * Returns the EntityDescriptor for the base type.
	 * @return
	 */
    public EntityDescriptor getEntityDescriptor();

	/**
	 * @param classname
	 * @return
	 */
	public EntityDescriptor getEntityDescriptor(String subtype);

	/**
	 *
	 * @param mainObject
	 * @param relationObject
	 * @return
	 */
	public IEntity findCorrectVersionForEntityRelation(IEntity mainObject, IEntity relationObject);

	/**
	 * Returns true if the specified action is true for the entity.
	 * @param entity
	 * @param action
	 * @return
	 */
	public boolean hasRight(IEntity entity, String action);

}
