/**
 * 
 */
package de.xwic.appkit.core.dao.impl.remote;

import java.util.Collection;

import de.xwic.appkit.core.dao.DAOProviderAPI;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.DataAccessException;
import de.xwic.appkit.core.dao.Entity;
import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.dao.Limit;
import de.xwic.appkit.core.remote.client.EntityProxyFactory;
import de.xwic.appkit.core.remote.client.RemoteDataAccessClient;
import de.xwic.appkit.core.transfer.EntityTransferObject;
import de.xwic.appkit.core.transport.xml.TransportException;

/**
 * @author lippisch
 *
 */
public class RemoteDAOProviderAPI implements DAOProviderAPI {

	private RemoteDataAccessClient client;
	private RemoteServerDAOProvider daoProvider;

	/**
	 * @param remoteServerDAOProvider
	 * @param client
	 */
	public RemoteDAOProviderAPI(RemoteServerDAOProvider remoteServerDAOProvider, RemoteDataAccessClient client) {
		this.daoProvider = remoteServerDAOProvider;
		this.client = client;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.DAOProviderAPI#delete(de.xwic.appkit.core.dao.IEntity)
	 */
	@Override
	public void delete(IEntity entity) throws DataAccessException {

	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.DAOProviderAPI#softDelete(de.xwic.appkit.core.dao.IEntity)
	 */
	@Override
	public void softDelete(IEntity entity) throws DataAccessException {

	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.DAOProviderAPI#getEntity(java.lang.Class, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public IEntity getEntity(Class<? extends Object> clazz, int id) throws DataAccessException {
		try {
			// unfortunately, the abstract DAO makes the call with the implementation class name, 
			// therefore we need to translate it back to the type name
			Class<? extends Object> trueClass = DAOSystem.findDAOforEntity(clazz.getName()).getEntityClass();
			EntityTransferObject eto = client.getETO(trueClass.getName(), id);
			return EntityProxyFactory.createEntityProxy(eto);
		} catch (TransportException e) {
			throw new DataAccessException("Error loading remote entity '" + clazz.getName() + "' #" + id, e);
		}
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.DAOProviderAPI#update(de.xwic.appkit.core.dao.IEntity)
	 */
	@Override
	public void update(IEntity entity) throws DataAccessException {

	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.DAOProviderAPI#getEntities(java.lang.Class, de.xwic.appkit.core.dao.Limit)
	 */
	@Override
	public EntityList getEntities(Class<? extends Object> clazz, Limit limit) {
		return null;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.DAOProviderAPI#getEntities(java.lang.Class, de.xwic.appkit.core.dao.Limit, de.xwic.appkit.core.dao.EntityQuery)
	 */
	@Override
	public EntityList getEntities(Class<? extends Object> clazz, Limit limit, EntityQuery filter) {
		return null;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.DAOProviderAPI#getCollectionProperty(java.lang.Class, int, java.lang.String)
	 */
	@Override
	public Collection<?> getCollectionProperty(Class<? extends Entity> entityImplClass, int entityId, String propertyId) {
		return null;
	}

}
