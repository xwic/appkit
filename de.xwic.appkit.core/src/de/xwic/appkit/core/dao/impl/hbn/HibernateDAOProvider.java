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
 * de.xwic.appkit.core.model.impl.AbstractHibernateDAOProvider
 * Created on 05.04.2005
 *
 */
package de.xwic.appkit.core.dao.impl.hbn;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;

import de.xwic.appkit.core.dao.DAOCallback;
import de.xwic.appkit.core.dao.DAOProvider;
import de.xwic.appkit.core.dao.DataAccessException;
import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.dao.IEntityQueryResolver;

/**
 * Base implementation of a DAOProvider that is using hibernate.
 * 
 * @author Florian Lippisch
 */
public class HibernateDAOProvider implements DAOProvider {

	/** reuse the current session */
	public final static int USE_CURRENT_SESSION = 0;
	/** create a new session for each request */
	public final static int USE_NEW_SESSION = 1;
	
	private int sessionMode = USE_CURRENT_SESSION;
	private Map<Class<? extends EntityQuery>, IEntityQueryResolver> queryResolvers = new HashMap<Class<? extends EntityQuery>, IEntityQueryResolver>();
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.DAOProvider#registerQuery(java.lang.Class, de.xwic.appkit.core.dao.IEntityQueryResolver)
	 */
	public void registerQuery(Class<? extends EntityQuery> queryClass, IEntityQueryResolver resolver) {
		queryResolvers.put(queryClass, resolver);
	}
	
	/**
	 * Returns the resolver specified.
	 * @param queryClass
	 * @return
	 */
	IEntityQueryResolver getResolver(Class<? extends EntityQuery> queryClass) throws DataAccessException {
		IEntityQueryResolver qr = queryResolvers.get(queryClass);
		if (qr == null) {
			throw new DataAccessException("No IEntityQueryResolver registerd for query '" + queryClass.getName() + "'");
		}
		return qr;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.DAOProvider#execute(de.xwic.appkit.core.dao.DAOCallback)
	 */
	public Object execute(DAOCallback operation) {
        Session session = sessionMode == USE_CURRENT_SESSION ? 
        		HibernateUtil.currentSession() :
        		HibernateUtil.openSession();
        		
        boolean startTx = session.getTransaction() == null || !session.getTransaction().isActive();
        Transaction tx = null;
        if (startTx) {
        	tx = session.beginTransaction();
        }
        boolean rollback = true;
        try {
        	Object o = operation.run(new HibernateDAOProviderAPI(this, session));
        	if (tx != null) {
        		tx.commit();
        	}
        	rollback = false;
        	return o;
        } finally {
        	// an exception occurred
        	if (rollback && tx != null && tx.isActive() && !tx.wasRolledBack()) {	
        		tx.rollback();
        	}
        	// If we use our own session, we must close it by ourself.
        	if (sessionMode != USE_CURRENT_SESSION) {
        		session.close();
        	}
        }
	}

	/**
	 * @return Returns the sessionMode.
	 */
	public int getSessionMode() {
		return sessionMode;
	}

	/**
	 * @param sessionMode The sessionMode to set.
	 */
	public void setSessionMode(int sessionMode) {
		this.sessionMode = sessionMode;
	}
    
}
