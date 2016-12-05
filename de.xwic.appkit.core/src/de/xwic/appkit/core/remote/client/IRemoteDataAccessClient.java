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
package de.xwic.appkit.core.remote.client;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.dao.Limit;
import de.xwic.appkit.core.dao.UseCase;
import de.xwic.appkit.core.security.ScopeActionKey;
import de.xwic.appkit.core.transfer.EntityTransferObject;
import de.xwic.appkit.core.transport.xml.TransportException;


/**
 * @author Adrian Ionescu
 */
public interface IRemoteDataAccessClient {

	/**
	 * @param entityType
	 * @param id
	 * @return
	 * @throws RemoteDataAccessException
	 * @throws TransportException
	 */
	public EntityTransferObject getETO(String entityType, long id) throws RemoteDataAccessException, TransportException;

	/**
	 * @param entityType
	 * @param limit
	 * @param query
	 * @return
	 * @throws RemoteDataAccessException
	 * @throws TransportException
	 */
	public EntityList getList(String entityType, Limit limit, EntityQuery query) throws RemoteDataAccessException, TransportException;

	/**
	 * Returns the ID of the entity
	 *
	 * @param entityType
	 * @param eto
	 * @return
	 * @throws RemoteDataAccessException
	 * @throws TransportException
	 * @throws IOException
	 * @throws ConfigurationException
	 */
	public EntityTransferObject updateETO(EntityTransferObject eto) throws RemoteDataAccessException, TransportException, IOException, ConfigurationException;

	/**
	 * Returns the collection of an entity property.
	 * @param entityType
	 * @param entityId
	 * @param propertyName
	 * @return
	 * @throws RemoteDataAccessException
	 * @throws TransportException
	 * @throws IOException
	 * @throws ConfigurationException
	 */
	public List<?> getETOCollection(String entityType, long entityId, String propertyName) throws RemoteDataAccessException, TransportException, IOException, ConfigurationException;

	/**
	 * @param entityType
	 * @param eto
	 * @param softDelete
	 */
	public void delete(String entityType, long id, long version, boolean softDelete);

	/**
	 * @return
	 * @throws TransportException 
	 */
	public Object executeUseCase(UseCase uc) throws TransportException;
	
	/**
	 * @param userId
	 * @return
	 * @throws TransportException
	 */
	public Set<ScopeActionKey> getUserRights(long userId) throws TransportException;
	
	/**
	 * @return
	 * @throws TransportException 
	 */
	public Object executeRemoteFunctionCall(IRemoteFunctionCallConditions conditions) throws TransportException;
}
