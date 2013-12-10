/**
 * 
 */
package de.xwic.appkit.core.dao.impl.remote;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.xwic.appkit.core.dao.DAOProviderAPI;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.DataAccessException;
import de.xwic.appkit.core.dao.Entity;
import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.dao.Limit;
import de.xwic.appkit.core.model.entities.IPicklistEntry;
import de.xwic.appkit.core.model.entities.IPicklistText;
import de.xwic.appkit.core.remote.client.EntityProxyFactory;
import de.xwic.appkit.core.remote.client.IRemoteDataAccessClient;
import de.xwic.appkit.core.transfer.EntityTransferObject;
import de.xwic.appkit.core.transport.xml.TransportException;

/**
 * @author lippisch
 *
 */
public class RemoteDAOProviderAPI implements DAOProviderAPI {

	private IRemoteDataAccessClient client;

	/**
	 * @param remoteServerDAOProvider
	 * @param client
	 */
	public RemoteDAOProviderAPI(RemoteServerDAOProvider remoteServerDAOProvider, IRemoteDataAccessClient client) {
		this.client = client;
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
			return eto != null ? EntityProxyFactory.createEntityProxy(eto) : null;
		} catch (TransportException e) {
			throw new DataAccessException("Error loading remote entity '" + clazz.getName() + "' #" + id, e);
		}
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.DAOProviderAPI#update(de.xwic.appkit.core.dao.IEntity)
	 */
	@Override
	public void update(IEntity entity) throws DataAccessException {
		try {
			EntityTransferObject eto = new EntityTransferObject(entity, true);
			client.updateETO(entity.type().getName(), eto);
		} catch (Exception e) {
			throw new DataAccessException("Error updating remote entity '" + entity.type().getName() + "' #" + entity.getId(), e);
		}
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.DAOProviderAPI#getEntities(java.lang.Class, de.xwic.appkit.core.dao.Limit)
	 */
	@Override
	public EntityList getEntities(Class<? extends Object> clazz, Limit limit) {
		return getEntities(clazz, limit, null);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.DAOProviderAPI#getEntities(java.lang.Class, de.xwic.appkit.core.dao.Limit, de.xwic.appkit.core.dao.EntityQuery)
	 */
	@Override
	public EntityList getEntities(Class<? extends Object> clazz, Limit limit, EntityQuery filter) {
		try {
			// unfortunately, the abstract DAO makes the call with the implementation class name, 
			// therefore we need to translate it back to the type name
			
			Class<? extends Object> trueClass = null;
			
			// PicklistEntries and PicklistText are handled by the PicklistDao, this is why we need to hardcode below 
			if (clazz.getName().endsWith("PicklistEntry")) {
				trueClass = IPicklistEntry.class;
			} else if (clazz.getName().endsWith("PicklistText")) {
				trueClass = IPicklistText.class;
			} else {
				trueClass = DAOSystem.findDAOforEntity(clazz.getName()).getEntityClass();				
			}
			
			EntityList objects = client.getList(trueClass.getName(), limit, filter);
			
			List<Object> list = new ArrayList<Object>();
			
			boolean isObjectArray = filter.getColumns() != null;
			
			for (Object obj : objects) {
				if (isObjectArray) {
					list.add(obj);
				} else {
					list.add(EntityProxyFactory.createEntityProxy((EntityTransferObject) obj));
				}
			}
			
			EntityList result = new EntityList(list, limit, objects.getTotalSize());			
			return result;
		} catch (TransportException e) {
			throw new DataAccessException("Error loading remote entities '" + clazz.getName() + "'", e);
		}
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.DAOProviderAPI#delete(de.xwic.appkit.core.dao.IEntity)
	 */
	@Override
	public void delete(IEntity entity) throws DataAccessException {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.DAOProviderAPI#softDelete(de.xwic.appkit.core.dao.IEntity)
	 */
	@Override
	public void softDelete(IEntity entity) throws DataAccessException {
		throw new UnsupportedOperationException();
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.DAOProviderAPI#getCollectionProperty(java.lang.Class, int, java.lang.String)
	 */
	@Override
	public Collection<?> getCollectionProperty(Class<? extends Entity> entityImplClass, int entityId, String propertyId) {
		throw new UnsupportedOperationException();
	}

}
