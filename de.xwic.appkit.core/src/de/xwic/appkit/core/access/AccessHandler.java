/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.access.AccessHandler
 * Created on 04.01.2008 by Florian Lippisch
 *
 */
package de.xwic.appkit.core.access;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.xwic.appkit.core.ApplicationData;
import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.DataAccessException;
import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.dao.IHistory;
import de.xwic.appkit.core.dao.ISecurityManager;
import de.xwic.appkit.core.dao.Limit;
import de.xwic.appkit.core.dao.impl.hbn.HibernateUtil;
import de.xwic.appkit.core.model.daos.IPicklisteDAO;
import de.xwic.appkit.core.model.entities.IPicklistEntry;
import de.xwic.appkit.core.model.entities.IPicklistText;
import de.xwic.appkit.core.model.entities.impl.PicklistEntry;
import de.xwic.appkit.core.model.entities.impl.Pickliste;
import de.xwic.appkit.core.security.IUser;
import de.xwic.appkit.core.transfer.EntityTransferObject;
import de.xwic.appkit.core.transfer.PropertyValue;
import de.xwic.appkit.core.util.ILazyEval;
import de.xwic.appkit.core.util.MapUtil;

/**
 * <p>Handles common access operations to the backend model using
 * normal entities as well as EntityTransferObjects. </p>
 * 
 * <p>This handler is used by the WebService implementation to handle all
 * basic operations. Usualy there exists only one instance of this handler,
 * allowing serverside extensions to monitor all kind of operations.</p>
 * 
 * @author Florian Lippisch
 */
public class AccessHandler {

	/** logger */
	protected final static Log log = LogFactory.getLog(AccessHandler.class);
	
	private List<AccessListener> listeners = new ArrayList<AccessListener>();
	
	private static final int EVENT_DELETED = 0;
	private static final int EVENT_SOFTDELETE = 1;
	private static final int EVENT_UPDATE = 2;
	
	private ObjectMonitoringSettings omSettings = new ObjectMonitoringSettings();
	
	private static AccessHandler instance = null;
	
	private static final ILazyEval<PropertyDescriptor, String> PROPERTY_DESCRIPTOR_NAME_EXTRACTOR = new ILazyEval<PropertyDescriptor, String>() {

		@Override
		public String evaluate(PropertyDescriptor obj) {
			return obj.getName();
		}
	};

	/**
	 * Constructor.
	 */
	private AccessHandler() {
		omSettings.initialize();
		addAccessListener(omSettings);
	}
	
	/**
	 * Returns the AccessHandler instance.
	 * @return
	 */
	public static AccessHandler getInstance() {
		if (instance == null) {
			instance = new AccessHandler();
		}
		return instance;
	}
	
	/**
	 * Add an listener for AccessEvents.
	 * @param listener
	 */
	public synchronized void addAccessListener(AccessListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * Remove a previously registered listener.
	 * @param listener
	 */
	public synchronized void removeAccessListener(AccessListener listener) {
		listeners.remove(listener);
	}
	
	/**
	 * Internally fire the event.
	 * @param type
	 * @param event
	 */
	protected void fireEvent(int type, AccessEvent event) {
		Object[] lst = listeners.toArray();
		for (int i = 0; i < lst.length; i++) {
			AccessListener listener = (AccessListener)lst[i];
			switch (type) {
			case EVENT_DELETED: 
				listener.entityDeleted(event);
				break;
			case EVENT_SOFTDELETE: 
				listener.entitySoftDeleted(event);
				break;
			case EVENT_UPDATE: 
				listener.entityUpdated(event);
				break;
			}
		}
	}

	/**
	 * Returns the name of the current user or SYSTEM if no user is set.
	 * @return
	 */
	protected String getCurrentUsername() {
		IUser currentUser = DAOSystem.getSecurityManager().getCurrentUser(); 
		return currentUser != null ? currentUser.getLogonName() : "SYSTEM"; 
	}
	
	/**
	 * Delete the specified entity.
	 * @param entity
	 */
	public void delete(IEntity entity) {

		if (entity == null) {
			throw new NullPointerException("entity must not be null.");
		}
		
		Class<? extends IEntity> type = entity.type();
		if (log.isDebugEnabled()) {
			log.debug("DELETE " + type.getName() + " ID#" + entity.getId() + " by user " + getCurrentUsername());
		}
		
		DAO dao = DAOSystem.findDAOforEntity(entity.type());
		dao.delete(entity);
		
		fireEvent(EVENT_DELETED, new AccessEvent(this, entity));
		
	}
	
	/**
	 * Delete the specified entity.
	 * @param entityType
	 * @param entityId
	 * @param version
	 * @throws RemoteException
	 * @throws DataAccessException
	 */
	 public void delete(String entityType, int entityId, long version) {

    	if (log.isDebugEnabled()) {
    		log.debug("DELETE " + entityType + " ID#" + entityId + " by user " + getCurrentUsername());
    	}
    	
    	DAO dao = DAOSystem.findDAOforEntity(entityType);
    	
    	IEntity entity;
    	if (dao instanceof IPicklisteDAO) {
    		// special treatment for picklisten
    		IPicklisteDAO plDAO = (IPicklisteDAO)dao;
    		if (entityType.equals(Pickliste.class.getName())) {
    			entity = plDAO.getEntity(entityId);
    		} else if (entityType.equals(PicklistEntry.class.getName())) {
    			entity = plDAO.getPickListEntryByID(entityId);
    		} else if (entityType.equals(Pickliste.class.getName())) {
    			entity = plDAO.getPickListTextByID(entityId);
    		} else {
    			throw new DataAccessException("Unsupported type: " + entityType);
    		}
    	} else {
    		entity = dao.getEntity(entityId);
    	}
    	
    	if (entity.getVersion() != version) {
    		throw new DataAccessException("The entity has been modified by someone else.");
    	}
    	
    	dao.delete(entity);
    	
    	fireEvent(EVENT_DELETED, new AccessEvent(this, entity));
    	
	}
	
	/**
	 * Updates the specified entity.
	 * @param entity
	 * @return
	 */
	public IEntity update(IEntity entity) {
		long start = 0;
		if (log.isDebugEnabled()) {
			start = System.currentTimeMillis();
		}
		
		Class<? extends IEntity> type = entity.type();
		DAO dao = DAOSystem.findDAOforEntity(type);
		
		dao.update(entity);
		
		if (log.isDebugEnabled()) {
			log.debug("UPDATE [" + (System.currentTimeMillis() - start) + "ms] " + type.getName() + " ID#" + entity.getId() + " by user " + getCurrentUsername());
		}

		fireEvent(EVENT_UPDATE, new AccessEvent(entity));
		
		return entity;
	 }

	/**
	 * Returns a list of entities that are wrapped into EntityTransferObjects.
	 * @param classname
	 * @param limit
	 * @param filter
	 * @return
	 */
	public EntityList getEntities(String classname, Limit limit, EntityQuery filter) {

		long start = 0;
    	if (log.isDebugEnabled()) {
    		start = System.currentTimeMillis();
    	}

    	DAO dao = DAOSystem.findDAOforEntity(classname);
    	EntityList list;
    	try {
			if (IHistory.class.isAssignableFrom(Class.forName(classname))) {
				list = dao.getHistoryEntities(limit, filter);
			} else {
				list = dao.getEntities(limit, filter);
			}
		} catch (ClassNotFoundException e) {
			throw new DataAccessException("Unknown classname specified: " + classname);
		}
        
		EntityDescriptor desc = dao.getEntityDescriptor(classname);
		
//        // generate transfer objects if it contains entites other then picklistEntries
//        if (!(dao instanceof IPicklisteDAO) && desc.isTransform()) {
		if (desc.isTransform()) {
	        if (list.size() > 0 && list.get(0) instanceof IEntity) {
	        	List<Object> data = new ArrayList<Object>(list.size());
	        	for (Iterator<Object> it = list.iterator(); it.hasNext(); ) {
	        		Object o = it.next();
	        		if (o instanceof IEntity) {
	        			data.add(new EntityTransferObject((IEntity)o, filter != null ? filter.isFetchLazySets() : false));
	        		} else {
	        			data.add(o);
	        		}
	        	}
	        	list = new EntityList(data, limit, list.getTotalSize());
	        }
        }
        
    	if (log.isDebugEnabled()) {
    		log.debug("GetEntities [" + (System.currentTimeMillis() - start) + "ms] " + classname + " Limit: " + limit + " Query: " + filter + " Size: " + list.size() + " by user " + getCurrentUsername());
    	}
        return list;

	}    
	
	/**
	 * Loads the specified entity and returns it as an EntityTransferObject.
	 * @param classname
	 * @param id
	 * @return
	 */
	public EntityTransferObject getETO(String classname, int id) {

    	if (log.isDebugEnabled()) {
    		log.debug("getETO " + classname + " ID#" + id + " by user " + getCurrentUsername());
    	}

    	DAO dao = DAOSystem.findDAOforEntity(classname);
    	IEntity entity;

    	if (classname.endsWith("PicklistEntry")) {
    		IPicklisteDAO plDAO = (IPicklisteDAO)dao;
    		entity = plDAO.getPickListEntryByID(id);
    	} else if (classname.endsWith("PicklistText")) {
    		IPicklisteDAO plDAO = (IPicklisteDAO)dao;
    		entity = plDAO.getPickListTextByID(id);
    	} else {
			try {
				if (IHistory.class.isAssignableFrom(Class.forName(classname))) {
					entity = dao.getHistoryEntity(id);
				} else {
					entity = dao.getEntity(id);
				}
			} catch (ClassNotFoundException e) {
				throw new DataAccessException("Unknown classname specified: " + classname);			
			}
    	}
    	return entity != null ?  new EntityTransferObject(entity) : null;
    	
	}
	
	/**
	 * Reads the collection property of a specified entity. Used for lazy loading of collections.
	 * @param classname
	 * @param id
	 * @param property
	 * @return
	 */
	public Object getETOCollection(String classname, int id, String property) {

    	if (log.isDebugEnabled()) {
    		log.debug("getETOCollection: " + classname + "." + property + " ID#" + id + " by user " + getCurrentUsername());
    	}
    	DAO dao = DAOSystem.findDAOforEntity(classname);
    	IEntity entity;
		try {
			if (IHistory.class.isAssignableFrom(Class.forName(classname))) {
				entity = dao.getHistoryEntity(id);
			} else {
				entity = dao.getEntity(id);
			}
		} catch (ClassNotFoundException e) {
			throw new DataAccessException("Unknown classname specified: " + classname);			
		}
    	try {
	    	// extract property value
	    	PropertyDescriptor descr = new PropertyDescriptor(property, entity.getClass());
	    	Object value = descr.getReadMethod().invoke(entity, (Object[]) null);
	    	if (!(value instanceof Collection)) {
	    		throw new DataAccessException("Property '" + property + "' is not a collection");
	    	}
	    	Collection<?> collection = (Collection<?>)value;
	    	Collection<Object> convertedCollection;
	    	if (value instanceof Set) {
	    		convertedCollection = new HashSet<Object>();
	    	} else if (value instanceof List) {
	    		convertedCollection = new ArrayList<Object>();
	    	} else {
	    		throw new DataAccessException("Property '" + property + "' is neither a Set or a List. Other types are not supported.");	    		
	    	}
	    	for (Iterator<?> it = collection.iterator(); it.hasNext(); ) {
	    		Object o = it.next();
	    		if (o instanceof IEntity) {
	    			convertedCollection.add(new EntityTransferObject((IEntity)o));
	    		} else {
	    			convertedCollection.add(o);
	    		}
	    	}
	    	return convertedCollection;
    	} catch (Exception e) {
    		if (e instanceof DataAccessException) {
    			throw (DataAccessException)e;
    		}
    		throw new DataAccessException("Error reading collection: " + e);
    	}
    	
	}	
	
	/**
	 * Update the entity specified by this EntityTransferObject.
	 * @param eto
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public EntityTransferObject updateETO(EntityTransferObject eto) {

		if (log.isDebugEnabled()) {
			log.debug("updateETO: " + eto.getEntityClass().getName() + " ID#" + eto.getEntityId() + " v" + eto.getEntityVersion() + " by user " + getCurrentUsername());
		}

		ISecurityManager secMan = DAOSystem.getSecurityManager();
		
		DAO dao = DAOSystem.findDAOforEntity(eto.getEntityClass().getName());
		IPicklisteDAO plDAO = (IPicklisteDAO)DAOSystem.getDAO(IPicklisteDAO.class);
		
		IEntity entity;
		if (eto.getEntityId() == 0) {
			// create entity
			entity = dao.createEntity(eto.getEntityClass().getName());
		} else {
			if (IPicklistEntry.class.isAssignableFrom(eto.getEntityClass())) {
				entity = ((IPicklisteDAO)dao).getPickListEntryByID(eto.getEntityId());
			} else if (IPicklistText.class.isAssignableFrom(eto.getEntityClass())) {
				entity = ((IPicklisteDAO)dao).getPickListTextByID(eto.getEntityId());
			} else {
				entity = dao.getEntity(eto.getEntityId());
			}
			// must "disconnect" the entity from the session, to prevent database 
			// synchronisation
			HibernateUtil.currentSession().evict(entity);
		}
		
		// check version
		if (entity.getVersion() != eto.getEntityVersion()) {
			throw new DataAccessException("The entity has been modified by someone else.");
		}

		String scope = entity.type().getName();

		boolean monitoring = !entity.isChanged() && !secMan.hasRight(scope, ApplicationData.SECURITY_ACTION_APPROVE);
		// always set changed flag if the entity is 'new', user has no approve rights and entity is monitored.
		boolean setChanged = monitoring && entity.getId() == 0 && omSettings.getMonitoringProperties(scope).isTypeMonitored(); 

		ObjectMonitoringTypeProperties omTp = null;
		if (monitoring) {
			omTp = omSettings.getMonitoringProperties(scope);
		}
		try {
//			AAA TODO AI use intelligence to cache this information
			BeanInfo beanInfo = Introspector.getBeanInfo(entity.getClass());
			List<PropertyDescriptor> propertyDescriptors = Arrays.asList(beanInfo.getPropertyDescriptors());
			Map<String, PropertyDescriptor> propertyMap = MapUtil.generateMap(propertyDescriptors, PROPERTY_DESCRIPTOR_NAME_EXTRACTOR);

			for (Iterator<String> it = eto.getPropertyValues().keySet().iterator(); it.hasNext(); ) {

				String propName = it.next();
				PropertyValue pValue = eto.getPropertyValue(propName);
				if (!pValue.isModified()) {
					continue;
				}

				log.debug("Modified attribute: " + propName);
				if (monitoring && omTp.isMonitored(propName)) {
					setChanged = true;
					monitoring = false; // no need to monitor any other properties
					log.debug("ObjectMonitoring detected change.");
				}

				PropertyDescriptor pd = propertyMap.get(propName);
				if (pd == null){
					log.error("Attribute modified but no such property: " + propName, new IllegalStateException());
					continue;
				}

//				PropertyDescriptor pd = new PropertyDescriptor(propName, entity.getClass());
				Method mWrite = pd.getWriteMethod();
				if (mWrite == null) {
					log.warn("No write method for property " + propName);
					continue;
				}

				if (secMan.getAccess(scope, propName) != ISecurityManager.READ_WRITE) {
					log.warn("Tried to modify attribute without rights:  " + propName);
					continue;
				}

				Object value;

				if (pValue.isLoaded()) {
					value = pValue.getValue();
					if (pd.getPropertyType().equals(Date.class) && value instanceof Calendar) {
						value = ((Calendar) value).getTime();

					// handle collections --
					// collections may contain PropertyValue 'stubs' instead of
					// entities to reduce message-size.
					} else if (value != null && value.getClass().isArray()) {

						Collection<Object> newCol;
						if (pValue.getType().isAssignableFrom(Set.class)) {
							newCol = new HashSet<Object>();
						} else if (pValue.getType().isAssignableFrom(List.class)) {
							newCol = new ArrayList<Object>();
						} else {
							throw new DataAccessException("Cant handle collection type: " + value.getClass().getName());
						}

						Object[] oArray = (Object[]) value;
						for (int i = 0; i < oArray.length; i++) {
							Object o = oArray[i];
							if (o instanceof PropertyValue) {
								PropertyValue pv = (PropertyValue) o;
								if (pv.isLoaded()) {
									o = pv.getValue();
								} else if (pv.isEntityType()) {
									if (pv.getType().equals(IPicklistEntry.class)) {
										o = plDAO.getPickListEntryByID(pv.getEntityId());
									} else {
										o = DAOSystem.findDAOforEntity(pv.getType().getName()).getEntity(pv.getEntityId());
									}
								} else {
									throw new DataAccessException("A collection can not contain another lazy collection.");
								}
							} else if (o instanceof EntityTransferObject) {
								EntityTransferObject refEto = (EntityTransferObject) o;
								o = DAOSystem.findDAOforEntity(refEto.getEntityClass().getName()).getEntity(refEto.getEntityId());
							}
							newCol.add(o);
						}
						value = newCol;
					}
				} else if (pValue.getType().equals(IPicklistEntry.class)) {
					value = plDAO.getPickListEntryByID(pValue.getEntityId());
				} else {
					if (!pValue.isEntityType()) {
						// must be a Set that has not been loaded
						log.warn("Modified but not-loaded set detected in property " + propName);
						continue;
					}
					if (pValue.getType().getName().equals(entity.type().getName()) &&
							pValue.getEntityId() == entity.getId()) {
						value = entity;
					} else {
						value = DAOSystem.findDAOforEntity(pValue.getType().getName()).getEntity(pValue.getEntityId());
						// disconnect entity from session to prevent
						// double update by hibernate
						HibernateUtil.currentSession().evict(value);
					}
				}
							
				if (value != null && value.getClass().isArray()) {
					// AXIS turns Set's into arrays - need to be converted
					if (pd.getPropertyType().equals(Set.class)) {
						Object[] array = (Object[])value;
						Set<Object> set = new HashSet<Object>();
						for (int i = 0; i < array.length; i++) {
							set.add(array[i]);
						}
						mWrite.invoke(entity, new Object[] { set });
					} else {
						throw new RuntimeException("NOT IMPLEMENTED: Handle Arrays.");
					}
				} else {
					mWrite.invoke(entity, new Object[] { value });
				}
			}
		} catch (Exception ie) {
			throw new DataAccessException("Error writing entity properties: " + ie, ie);
		}
		// FLI: Set the change property just before update, to prevent "overriding" during
		// the field update.
		if (setChanged) {
			entity.setChanged(true);
		}
		dao.update(entity);
		
		fireEvent(EVENT_UPDATE, new AccessEvent(this, entity));
		
		EntityTransferObject result = new EntityTransferObject(entity);
		
		return result;
	}

	/**
	 * SoftDelete the specified entity.
	 * @param entityType
	 * @param entityId
	 * @param version
	 */
	public void softDelete(String entityType, int entityId, long version) {
		
    	if (log.isDebugEnabled()) {
    		log.debug("softDelete: " + entityType + " ID#" + entityId + " by user " + getCurrentUsername());
    	}
    	DAO dao = DAOSystem.findDAOforEntity(entityType);
    	
    	IEntity entity;
    	if (dao instanceof IPicklisteDAO) {
    		// special treatment for picklisten
    		IPicklisteDAO plDAO = (IPicklisteDAO)dao;
    		if (entityType.equals(Pickliste.class.getName())) {
    			entity = plDAO.getEntity(entityId);
    		} else if (entityType.equals(PicklistEntry.class.getName())) {
    			entity = plDAO.getPickListEntryByID(entityId);
    		} else if (entityType.equals(Pickliste.class.getName())) {
    			entity = plDAO.getPickListTextByID(entityId);
    		} else {
    			throw new DataAccessException("Unsupported type: " + entityType);
    		}
    	} else {
    		entity = dao.getEntity(entityId);
    	}
    	
    	if (entity.getVersion() != version) {
    		throw new DataAccessException("The entity has been modified by someone else.");
    	}
    	
   		dao.softDelete(entity);

   		fireEvent(EVENT_SOFTDELETE, new AccessEvent(this, entity));
   		
	}

	
}
