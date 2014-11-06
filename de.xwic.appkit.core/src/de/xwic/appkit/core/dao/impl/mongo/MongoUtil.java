/*
 * de.xwic.appkit.core.util.HibernateUtil
 * Created on 04.04.2005
 *
 */
package de.xwic.appkit.core.dao.impl.mongo;

import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.hibernate.cfg.Configuration;
import org.mongodb.morphia.AdvancedDatastore;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.mapping.*;
import org.mongodb.morphia.mapping.cache.EntityCache;
import org.mongodb.morphia.query.Query;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

/**
 * @author Florian Lippisch
 */
public class MongoUtil {
    
    private static Configuration configuration;

    private static final ThreadLocal<MongoClient> MONGO_THREAD_LOCAL = new ThreadLocal<MongoClient>();
    private static MongoClient mongo = null;
    private static boolean singleSessionMode = false;

    
    /**
     * Returns true if the HibernateUtil is already initialized.
     * @return
     */
    public static boolean isInitialized() {
    	return mongo != null;
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
    	MongoUtil.configuration = config;
        mongo = createMongoPool();
    }

    private static MongoClient createMongoPool() {
        try {
            return new MongoClient("localhost");
        } catch (UnknownHostException e) {
            throw new IllegalStateException("cant configure mongo " + e.getMessage(), e);
        }
    }

    /**
     * Opens a new Session. The MONGO_THREAD_LOCAL Object is not bound to the
     * current thread. It must be closed by the user of this method.
     * @return
     */
    public static MongoClient openSession() {
    	return mongo;
    }
    
    /**
     * Returns the current MONGO_THREAD_LOCAL. If there is no MONGO_THREAD_LOCAL yet, a MONGO_THREAD_LOCAL is opened.
     * @return
     */
    public static MongoClient currentSession() {
    	if (singleSessionMode) {
    		if (mongo == null) {
                mongo = createMongoPool();
    		}
    		return mongo;
    	} else {
            MongoClient s = MONGO_THREAD_LOCAL.get();
	        
	        // Open a new Session, if this Thread has none yet
	        if (s == null) {
                s = createMongoPool();
	            MONGO_THREAD_LOCAL.set(s);
	        }
	        return s;
    	}
    }

    /**
     * Close the current MONGO_THREAD_LOCAL if there is any.
     */
    public static void closeSession() {
    	if (singleSessionMode) {
    		if (mongo != null) {
    			mongo.close();
    			mongo = null;
    		}
    	} else {
	        Mongo s = MONGO_THREAD_LOCAL.get();
	        if (s != null) {
	            try {
	            	s.close();
	            } finally {
	            	MONGO_THREAD_LOCAL.set(null);
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
		MongoUtil.singleSessionMode = singleSessionMode;
	}

	/**
	 * 
	 */
	public static void reset() {
    	closeSession();
	}


    public static AdvancedDatastore getDatastore(Class<? extends Object> clazz, MongoClient mongoClient) {
        Morphia morphia = new Morphia();
        Mapper mapper = morphia.getMapper();
        mapper.getOptions().objectFactory = new CustomMorphiaObjectFactory();
        mapper.getOptions().setActLikeSerializer(true);
        mapper.getOptions().setStoreEmpties(true);
        morphia.map(EntityWrapper.class);
        if(mongoClient == null) {
            mongoClient = createMongoPool();
        }
        return (AdvancedDatastore) morphia.createDatastore(mongoClient, "test");
    }

    public static Query createQuery(Class<? extends Object> entityClass) {
        MongoClient mongoClient = MongoUtil.currentSession();
        AdvancedDatastore datastore = MongoUtil.getDatastore(entityClass, mongoClient);
        return datastore.createQuery(entityClass.getName(), EntityWrapper.class).disableValidation();
    }

    private static class CustomMorphiaObjectFactory extends DefaultCreator {
        @Override
        public Object createInstance(Class clazz) {
            try {
                final Constructor constructor = getNoArgsConstructor(clazz);
                if(constructor != null) {
                    return constructor.newInstance();
                }
                try {
                    return ReflectionFactory.getReflectionFactory().newConstructorForSerialization(clazz, Object.class.getDeclaredConstructor(null)).newInstance(null);
                } catch (Exception e) {
                    throw new MappingException("Failed to instantiate " + clazz.getName(), e);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private Constructor getNoArgsConstructor(final Class ctorType) {
            try {
                Constructor ctor = ctorType.getDeclaredConstructor();
                ctor.setAccessible(true);
                return ctor;
            } catch (NoSuchMethodException e) {
                return null;
            }
        }
    }

}
