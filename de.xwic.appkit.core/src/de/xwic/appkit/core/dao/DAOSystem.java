/*
 * de.xwic.appkit.core.dao.ProviderFactory
 * Created on 05.04.2005
 *
 */
package de.xwic.appkit.core.dao;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.config.ConfigurationManager;
import de.xwic.appkit.core.config.Setup;
import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.config.model.Model;



/**
 * Manages the DataAccess layer. The DAOSystem is configured during startup
 * to a specific provider like hibernate or WSClient. The system can be
 * "reseted" to switch the factory (i.e. switch Online to Offline mode in 
 * a client).
 * 
 * @author Florian Lippisch
 */
public class DAOSystem {

    private static DAOFactory daoFactory = null;
    private static IFileHandler fileHandler = null;
    private static ISecurityManager securityManager = null;
    private static IUseCaseService useCaseService = null;
    
    private static Map<String, String> shortNames = new HashMap<String, String>();
    private static boolean oracleCaseInsensitve = false;
    
    private static ClassLoader modelClassLoader = new DAOSystem().getClass().getClassLoader();
	
    /**
     * Set the DAOFactory.
     * @param daoFactory
     */
    public static void setDAOFactory(DAOFactory daoFactory) {
    	// prevents start of both remote and offline providers. 
    	if (daoFactory != null && DAOSystem.daoFactory != null) {
    		throw new IllegalStateException("A DAOFactory is already registered at the DAOSystem.");
    	}
    	DAOSystem.daoFactory = daoFactory;
    }
    
    /**
	 * @return Returns the securityManager.
	 */
	public static ISecurityManager getSecurityManager() {
		return securityManager;
	}

	/**
	 * @param securityManager The securityManager to set.
	 */
	public static void setSecurityManager(ISecurityManager securityManager) {
		DAOSystem.securityManager = securityManager;
	}

	/**
     * Returns the DAO implementation of thes pecified DAO type.
     * @param daoType
     * @return
     */
    public static <D extends DAO> D getDAO(Class<D> daoType) {
    	
    	if (daoFactory != null) {
    		D dao = (D)daoFactory.getDAO(daoType);
    		if (dao == null) {
    			throw new IllegalArgumentException("A DAO for the specified type is not registered: " + daoType.getName());
    		}
    		return dao;
    	}
    	throw new IllegalStateException("The DAOSystem is not initilaized properly yet");
    	
    }
    
    /**
     * Returns the DAO implementation of thes pecified DAO type.
     * @param daoType
     * @return
     */
    public static <I extends IEntity> DAO<I> findDAOforEntity(Class<I> entityClass) {
		return findDAOforEntity(entityClass.getName());
    }

	/**
	 * @param classname
	 * @return
	 */
	public static DAO findDAOforEntity(String classname) {
    	if (daoFactory != null) {
    		return (DAO)daoFactory.findDAOforEntity(classname);
    	}
    	return null;
	}

    /**
     * Returns the fileHandler.
     * @return
     */
	public static IFileHandler getFileHandler() {
		return fileHandler;
	}

	/**
	 * Set the fileHandler.
	 * @param fileHandler
	 */
	public static void setFileHandler(IFileHandler fileHandler) {
		DAOSystem.fileHandler = fileHandler;
	}

	/**
	 * @return Returns the useCaseService.
	 */
	public static IUseCaseService getUseCaseService() {
		return useCaseService;
	}

	/**
	 * @param useCaseService The useCaseService to set.
	 */
	public static void setUseCaseService(IUseCaseService useCaseService) {
		DAOSystem.useCaseService = useCaseService;
	}
    
	/**
	 * Returns true if the DAOSystem is initialized.
	 * @return
	 */
    public static boolean isInitialized() {
    	return daoFactory != null;
    }

	/**
	 * Returns the EntityDescriptor for the specified type.
	 * @param classname
	 * @return
	 * @throws ConfigurationException 
	 */
	public static EntityDescriptor getEntityDescriptor(String classname) throws ConfigurationException {
		return ConfigurationManager.getSetup().getEntityDescriptor(classname);
	}

	/**
	 * Searches for the best matching EntityDescriptor. Supports short names like
	 * IUnternehmen instead of the full classname. If no descriptor is found, 
	 * null is returned.
	 * @param type
	 * @return
	 * @throws ConfigurationException 
	 */
	public static EntityDescriptor findEntityDescriptor(String type) throws ConfigurationException {
		Setup setup = ConfigurationManager.getSetup();
		String fullName = shortNames.get(type);
		if (!shortNames.containsKey(type)) {
			if (type.indexOf('.') != -1) {
				fullName = type;
			} else {
				for (Iterator<?> it = setup.getModels().iterator(); it.hasNext() && fullName == null; ) {
					Model model = (Model)it.next();
					for (Iterator<?> itNames = model.getManagedEntities().iterator(); itNames.hasNext(); ) {
						String name = (String)itNames.next();
						int idxDot = name.lastIndexOf('.');
						if (name.equals(type)) {
							// we have a MATCH! 
							fullName = name;
							break;
						} else if (idxDot != -1) {
							String shortName = name.substring(idxDot + 1);
							if (type.equals(shortName)) {
								fullName = name;
								break;
							}
						}
						
					}
				}
			}
			shortNames.put(type, fullName);
		}
		if (fullName != null) {
			try {
				return ConfigurationManager.getSetup().getEntityDescriptor(fullName);
			} catch (ConfigurationException ce) {
				// try via DAO.
				DAO dao = findDAOforEntity(fullName);
				if (dao != null) {
					return dao.getEntityDescriptor(fullName);
				}
			}
			
		}
		return null;
	}

	/**
	 * @return the modelClassLoader
	 */
	public static ClassLoader getModelClassLoader() {
		return modelClassLoader;
	}

	/**
	 * @param modelClassLoader the modelClassLoader to set
	 */
	public static void setModelClassLoader(ClassLoader modelClassLoader) {
		DAOSystem.modelClassLoader = modelClassLoader;
	}

	/**
	 * @return the oracleCaseInsensitve
	 */
	public static boolean isOracleCaseInsensitve() {
		return oracleCaseInsensitve;
	}

	/**
	 * @param oracleCaseInsensitve the oracleCaseInsensitve to set
	 */
	public static void setOracleCaseInsensitve(boolean oracleCaseInsensitve) {
		DAOSystem.oracleCaseInsensitve = oracleCaseInsensitve;
	}
	
}
