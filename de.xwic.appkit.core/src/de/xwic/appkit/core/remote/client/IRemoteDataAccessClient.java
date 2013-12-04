/**
 * 
 */
package de.xwic.appkit.core.remote.client;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.dao.Limit;
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
	public EntityTransferObject getETO(String entityType, int id) throws RemoteDataAccessException, TransportException;

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
	 * @param entityType
	 * @param eto
	 * @throws RemoteDataAccessException
	 * @throws TransportException
	 * @throws IOException
	 * @throws ConfigurationException
	 */
	public void updateETO(String entityType, EntityTransferObject eto) throws RemoteDataAccessException, TransportException, IOException, ConfigurationException;
	
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
	public List<?> getETOCollection(String entityType, int entityId, String propertyName) throws RemoteDataAccessException, TransportException, IOException, ConfigurationException;
	
}
