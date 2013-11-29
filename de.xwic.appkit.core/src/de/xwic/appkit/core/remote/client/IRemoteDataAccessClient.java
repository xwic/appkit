/**
 * 
 */
package de.xwic.appkit.core.remote.client;

import java.io.IOException;

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
	public EntityList<EntityTransferObject> getETOs(String entityType, Limit limit, EntityQuery query) throws RemoteDataAccessException, TransportException;
	
	/**
	 * @param entityType
	 * @param eto
	 * @throws RemoteDataAccessException
	 * @throws TransportException
	 * @throws IOException
	 * @throws ConfigurationException
	 */
	public void updateETO(String entityType, EntityTransferObject eto) throws RemoteDataAccessException, TransportException, IOException, ConfigurationException;
}
