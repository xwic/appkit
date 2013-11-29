/**
 * 
 */
package de.xwic.appkit.core.dao.impl.remote;

import de.xwic.appkit.core.dao.DAOCallback;
import de.xwic.appkit.core.dao.DAOProvider;
import de.xwic.appkit.core.dao.DataAccessException;
import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.dao.IEntityQueryResolver;
import de.xwic.appkit.core.remote.client.IRemoteDataAccessClient;

/**
 * @author lippisch
 *
 */
public class RemoteServerDAOProvider implements DAOProvider {

	
	private IRemoteDataAccessClient client;
	

	/**
	 * @param client
	 */
	public RemoteServerDAOProvider(IRemoteDataAccessClient client) {
		super();
		this.client = client;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.DAOProvider#execute(de.xwic.appkit.core.dao.DAOCallback)
	 */
	@Override
	public Object execute(DAOCallback operation) {

        try {
        	Object o = operation.run(new RemoteDAOProviderAPI(this, client));
        	return o;
        } catch (Exception e) {
        	throw new DataAccessException("Error executing DAO operation", e);
        }
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.DAOProvider#registerQuery(java.lang.Class, de.xwic.appkit.core.dao.IEntityQueryResolver)
	 */
	@Override
	public void registerQuery(Class<? extends EntityQuery> queryClass, IEntityQueryResolver resolver) {
		
	}

}
