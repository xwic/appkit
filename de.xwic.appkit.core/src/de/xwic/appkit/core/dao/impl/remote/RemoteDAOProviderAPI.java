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
package de.xwic.appkit.core.dao.impl.remote;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import de.xwic.appkit.core.dao.DAOProviderAPI;
import de.xwic.appkit.core.dao.DataAccessException;
import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.dao.Limit;
import de.xwic.appkit.core.model.util.EntityUtil;
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
			Class<? extends Object> trueClass = EntityUtil.type(clazz);
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
			
			EntityTransferObject response = client.updateETO(eto);
			
			eto.refresh(response);
			
			// TODO AI re-create the whole entity, maybe some other fields were updated!
			entity.setId(eto.getEntityId());
			entity.setLastModifiedAt((Date) eto.getPropertyValue("lastModifiedAt").getValue());
			entity.setLastModifiedFrom((String) eto.getPropertyValue("lastModifiedFrom").getValue());
			entity.setCreatedAt((Date) eto.getPropertyValue("createdAt").getValue());
			entity.setCreatedFrom((String) eto.getPropertyValue("createdFrom").getValue());
			entity.setVersion(eto.getEntityVersion());
			
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
			Class<? extends Object> trueClass = EntityUtil.type(clazz);
			
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
		performDelete(entity, false);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.DAOProviderAPI#softDelete(de.xwic.appkit.core.dao.IEntity)
	 */
	@Override
	public void softDelete(IEntity entity) throws DataAccessException {
		performDelete(entity, true);
	}
	
	/**
	 * @param entity
	 * @param softDelete
	 */
	private void performDelete(IEntity entity, boolean softDelete) {
		try {
			client.delete(entity.type().getName(), entity.getId(), entity.getVersion(), softDelete);
		} catch (Exception e) {
			throw new DataAccessException("Error soft-deleting entity: " + entity.getClass().getName() + " -> " + entity.getId(), e);
		}
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.DAOProviderAPI#getCollectionProperty(java.lang.Class, int, java.lang.String)
	 */
	@Override
	public Collection<?> getCollectionProperty(Class<? extends IEntity> entityImplClass, int entityId, String propertyId) {
        try {
            Class<? extends Object> trueClass = EntityUtil.type(entityImplClass);
            List<EntityTransferObject> entityTransferObjects = (List<EntityTransferObject>) client.getETOCollection(trueClass.getName(), entityId, propertyId);
            List<IEntity> entities = new ArrayList<IEntity>(entityTransferObjects.size());
            for(EntityTransferObject entityTransferObject : entityTransferObjects) {
                entities.add(EntityProxyFactory.createEntityProxy(entityTransferObject));
            }
            return entities;
        } catch (Exception e) {
        	throw new DataAccessException(String.format("Error getting collection property: %s of %s -> %s", propertyId,
					entityImplClass.getName(), entityId), e);
        }
	}
}
