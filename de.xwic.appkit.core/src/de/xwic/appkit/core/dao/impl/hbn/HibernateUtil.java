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
 * de.xwic.appkit.core.util.HibernateUtil
 * Created on 04.04.2005
 *
 */
package de.xwic.appkit.core.dao.impl.hbn;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * @author Florian Lippisch
 */
public class HibernateUtil {
    
    private static SessionFactory sessionFactory = null;
    private static Configuration configuration;

    private static final ThreadLocal<Session> session = new ThreadLocal<Session>();
    private static Session singleSession = null;
    private static boolean singleSessionMode = false;

    
    /**
     * Returns true if the HibernateUtil is already initialized.
     * @return
     */
    public static boolean isInitialized() {
    	return sessionFactory != null;
    }
    
    /**
     * Initialize the sessionFactory with the default configuration.
     *
     */
    public static void initialize() {
    	initialize(new Configuration().configure());
    }
    
    /**
     * Initilaize with a custom configuration.
     * @param configuration
     */
    public static void initialize(Configuration config) {
        // Create the SessionFactory
    	if (sessionFactory != null) {
    		throw new IllegalStateException("HibernateUtil already initialized.");
    	}
    	HibernateUtil.configuration = config;
    	
        sessionFactory = config.buildSessionFactory();
    }
    
    /**
     * Opens a new Session. The session Object is not bound to the
     * current thread. It must be closed by the user of this method.
     * @return
     */
    public static Session openSession() {
    	return sessionFactory.openSession();
    }
    
    /**
     * Returns the current session. If there is no session yet, a session is opened.
     * @return
     */
    public static Session currentSession() {
    	if (sessionFactory == null) {
    		initialize();
    	}
    	if (singleSessionMode) {
    		if (singleSession == null) {
    			singleSession = sessionFactory.openSession();
    		}
    		return singleSession;
    	} else {
	        Session s = session.get();
	        
	        // Open a new Session, if this Thread has none yet
	        if (s == null || !s.isOpen()) {
	            s = sessionFactory.openSession();
	            session.set(s);
	        }
	        return s;
    	}
    }

    /**
     * Close the current session if there is any.
     */
    public static void closeSession() {
    	if (singleSessionMode) {
    		if (singleSession != null) {
    			singleSession.close();
    			singleSession = null;
    		}
    	} else {
	        Session s = session.get();
	        if (s != null && s.isOpen()) {
	            try {
	            	s.close();
	            } finally {
	            	session.set(null);    	
	            }
	        }
	        
    	}
    }

    /**
     * Returns the configuration.
     * @return
     */
    public static Configuration getConfiguration() {
    	return configuration;
    }

	/**
	 * @return Returns the singleSessionMode.
	 */
	public static boolean isSingleSessionMode() {
		return singleSessionMode;
	}

	/**
	 * @param singleSessionMode The singleSessionMode to set.
	 */
	public static void setSingleSessionMode(boolean singleSessionMode) {
		HibernateUtil.singleSessionMode = singleSessionMode;
	}

	/**
	 * 
	 */
	public static void reset() {
		
    	closeSession();
    	sessionFactory = null;
    	
	}
    
    
}
