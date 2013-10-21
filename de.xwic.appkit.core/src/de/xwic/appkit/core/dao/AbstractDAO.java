/*
 * de.xwic.appkit.core.dao.AbstractDAO
 * Created on 04.04.2005
 *
 */
package de.xwic.appkit.core.dao;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.xwic.appkit.core.ApplicationData;
import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.config.model.Property;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.core.model.util.TemplateUtil;
import de.xwic.appkit.core.security.IUser;

/**
 * Abstract DAO implementation. Real DAO Implementations should extend this
 * class to implement entity-type specific methods.
 * 
 * @author Florian Lippisch
 */
public abstract class AbstractDAO<I extends IEntity, E extends Entity> implements DAO<I> {

	private final Log log = LogFactory.getLog(getClass());
	/** Provides access to the basic DAO operations */
	protected DAOProvider provider = null;

	/**
	 * Flag to tell the DAO to create history objects if
	 * EntityDiscriptor.isHistory() is true
	 **/
	protected boolean handleHistory = false;
	private final Class<I> iClass;
	private final Class<E> eClass;

	/**
	 * @param iClass
	 * @param eClass
	 */
	public AbstractDAO(Class<I> iClass, Class<E> eClass) {
		if (iClass == null || eClass == null) {
			throw new NullPointerException("No classes provided for dao " + getClass());
		}
		if (!iClass.isInterface()) {
			throw new IllegalStateException("First parameter must be an interface.");
		}
		if (!iClass.isAssignableFrom(eClass)) {
			throw new IllegalStateException("Illegal parameters for " + getClass());
		}
		this.iClass = iClass;
		this.eClass = eClass;
	}

	/**
	 * Returns the classname of the entity implementation.
	 *
	 * @return
	 */
	public Class<E> getEntityImplClass() {
		return eClass;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.DAO#getEntityClass()
	 */
	@Override
	public Class<I> getEntityClass() {
		return iClass;
	}

	/**
	 * Returns the classname of the entity implementation.
	 * 
	 * @return
	 */
	public abstract Class<? extends Entity> getEntityImplClass();

	/**
	 * Returns the classname of the history implementation.
	 * 
	 * @return
	 */
	public Class<? extends IHistory> getHistoryImplClass() {
		throw new UnsupportedOperationException("History is not supported by this entity type.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.dao.DAO#createEntity(java.lang.String)
	 */
	public IEntity createEntity(String subtype) {
		if (subtype.equals(getEntityClass().getName()) || subtype.equals(getEntityImplClass().getName())) {
			return createEntity(); // return default entity
		}
		throw new IllegalArgumentException("Unknown subtype specified: " + subtype);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.dao.DAO#handlesEntity(java.lang.Class)
	 */
	public boolean handlesEntity(String entityClass) {
		if (getEntityClass().getName().equals(entityClass) || getEntityImplClass().getName().equals(entityClass)) {
			return true; // no need to instanciate if its the same classname.
		}
		try {
			// RPF 15.01.2007 : Build in another classloader because
			// Class.forName(..) didn't work...
			return getEntityClass().isAssignableFrom(this.getClass().getClassLoader().loadClass(entityClass));
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.dao.DAO#getEntity(int)
	 */
	public IEntity getEntity(final int id) throws DataAccessException {

		checkRights(ApplicationData.SECURITY_ACTION_READ);

		return (IEntity) provider.execute(new DAOCallback() {
			public Object run(DAOProviderAPI api) {
				return api.getEntity(getEntityImplClass(), id);
			}
		});
	}

	/**
	 * Returns the EntityDescriptor for the base type.
	 * 
	 * @return
	 */
	public EntityDescriptor getEntityDescriptor() {
		try {
			return DAOSystem.getEntityDescriptor(getEntityClass().getName());
		} catch (ConfigurationException e) {
			throw new DataAccessException("Can not load EntityDescriptor", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.dao.DAO#getEntityDescriptor(java.lang.String)
	 */
	public EntityDescriptor getEntityDescriptor(String subtype) {
		return getEntityDescriptor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.dao.DAO#getHistoryEntity(int)
	 */
	public IEntity getHistoryEntity(final int id) throws DataAccessException {

		checkRights(ApplicationData.SECURITY_ACTION_READ);

		return (IEntity) provider.execute(new DAOCallback() {
			public Object run(DAOProviderAPI api) {
				return api.getEntity(getHistoryImplClass(), id);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.dao.DAO#update(de.xwic.appkit.core.dao.IEntity)
	 */
	public void update(final IEntity entity) throws DataAccessException {

		checkRights(entity.getId() == 0 ? ApplicationData.SECURITY_ACTION_CREATE
				: ApplicationData.SECURITY_ACTION_UPDATE, entity);

		provider.execute(new DAOCallback() {
			public Object run(DAOProviderAPI api) {
				api.update(entity);
				handleHistory(api, entity, entity.getId() == 0 ? IHistory.REASON_CREATED : IHistory.REASON_UPDATED);
				return null;
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.dao.DAO#remove(de.xwic.appkit.core.dao.IEntity)
	 */
	public void delete(final IEntity entity) throws DataAccessException {

		checkRights(ApplicationData.SECURITY_ACTION_DELETE, entity);

		provider.execute(new DAOCallback() {
			public Object run(DAOProviderAPI api) {
				api.delete(entity);
				handleHistory(api, entity, IHistory.REASON_DELETED);
				return null;
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.xwic.appkit.core.dao.DAO#softDelete(de.xwic.appkit.core.dao.IEntity)
	 */
	public void softDelete(final IEntity entity) throws DataAccessException {

		checkRights(ApplicationData.SECURITY_ACTION_DELETE, entity);
		try {
			provider.execute(new DAOCallback() {
				public Object run(DAOProviderAPI api) {
					api.softDelete(entity);
					handleHistory(api, entity, IHistory.REASON_UPDATED);
					return null;
				}
			});
		} catch (RuntimeException re) {
			throw re;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.xwic.appkit.core.dao.DAO#getEntities(de.xwic.appkit.core.dao.Limit)
	 */
	public EntityList getEntities(final Limit limit) {

		checkRights(ApplicationData.SECURITY_ACTION_READ);

		return (EntityList) provider.execute(new DAOCallback() {
			public Object run(DAOProviderAPI api) {
				return api.getEntities(getEntityImplClass(), limit);
			}
		});
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.DAO#getEntities(de.xwic.appkit.core.dao.Limit, de.xwic.appkit.core.dao.EntityQuery)
	 */
	public EntityList getEntities(Limit limit, EntityQuery filter) {

		return getEntities(limit, filter, true);
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.DAO#getEntities(de.xwic.appkit.core.dao.Limit, de.xwic.appkit.core.dao.EntityQuery, boolean)
	 */
	public EntityList getEntities(final Limit limit, final EntityQuery filter, boolean checkReadRights) {
		
		
		if (checkReadRights) {
			checkRights(ApplicationData.SECURITY_ACTION_READ);
		}

		return (EntityList) provider.execute(new DAOCallback() {
			public Object run(DAOProviderAPI api) {
				return api.getEntities(getEntityImplClass(), limit, filter);
			}
		});
		
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.DAO#getCollectionProperty(int, java.lang.String)
	 */
	@Override
	public Collection<?> getCollectionProperty(final int entityId, final String propertyId) {
		
		checkRights(ApplicationData.SECURITY_ACTION_READ);

		return (Collection<?>) provider.execute(new DAOCallback() {
			public Object run(DAOProviderAPI api) {
				return api.getCollectionProperty(getEntityImplClass(), entityId, propertyId);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.xwic.appkit.core.dao.DAO#getHistoryEntities(de.xwic.appkit.core.dao
	 * .Limit, de.xwic.appkit.core.dao.EntityQuery)
	 */
	public EntityList getHistoryEntities(final Limit limit, final EntityQuery filter) {
		checkRights(ApplicationData.SECURITY_ACTION_READ);

		return (EntityList) provider.execute(new DAOCallback() {
			public Object run(DAOProviderAPI api) {
				return api.getEntities(getHistoryImplClass(), limit, filter);
			}
		});
	}

	/**
	 * @return Returns the provider.
	 */
	public DAOProvider getProvider() {
		return provider;
	}

	/**
	 * @param provider
	 *            The provider to set.
	 */
	public void setProvider(DAOProvider provider) {
		this.provider = provider;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.xwic.appkit.core.dao.DAO#validateEntity(de.xwic.appkit.core.dao.IEntity
	 * )
	 */
	public ValidationResult validateEntity(IEntity entity) {
		ValidationResult result = new ValidationResult(entity.type().getName());

		try {
			EntityDescriptor descriptor = DAOSystem.getEntityDescriptor(getEntityClass().getName());
			Map<String, Property> properties = descriptor.getProperties();
			for (Iterator<String> it = properties.keySet().iterator(); it.hasNext();) {
				String propertyName = it.next();
				String keyPropName = entity.type().getName() + "." + propertyName;
				Property property = properties.get(propertyName);
				
				if (property.getMaxLength() > 0) {
					Object value = getPropertyValue(entity, property, result);
					// see if the property value is of type String
					if (value instanceof String) {
						String str = (String) value;
						
						// is it longer than maxLength ? return an error
						if (str.length() > property.getMaxLength()) {
							result.addError(keyPropName, ValidationResult.FIELD_TOO_LONG);
						}
					}
				}

				if (property.getRequired()) {
					Object value = getPropertyValue(entity, property, result);
					
					if (value == null) {
						result.addError(keyPropName, ValidationResult.FIELD_REQUIRED);
					} else if (value instanceof String) {
						String s = (String) value;
						if (s.trim().length() == 0) {
							result.addError(keyPropName, ValidationResult.FIELD_REQUIRED);
						}
					} else if (value instanceof Set<?>) {
						Set<?> set = (Set<?>) value;
						if (set.size() == 0) {
							result.addError(keyPropName, ValidationResult.FIELD_REQUIRED);
						}
					}
				}
			}
		} catch (ConfigurationException e) {
			throw new DataAccessException("No EntityDescriptor found for this type - cant validate");
		} catch (Throwable e) {
			throw new DataAccessException("Error validating entity: " + e, e);
		}

		return result;
	}

	private Object getPropertyValue(IEntity entity, Property property, ValidationResult result) throws Exception {
		
		String keyPropName = entity.type().getName() + "." + property.getName();
		
		try {
			Method mRead = property.getDescriptor().getReadMethod();
			if (mRead == null) {
				// the property is not defined on the entity class. Search for the property in the superclass
				// and use that. This is needed for cases where the entity is using the history and therefore
				// extending a base implementation
				PropertyDescriptor pd = new PropertyDescriptor(property.getName(), entity.getClass().getSuperclass());
				mRead = pd.getReadMethod();
				if (mRead == null) {
					throw new ConfigurationException("The property " + property.getName() + " can not be resolved on entity " + entity.getClass().getName());
				}
			}
			Object value = mRead.invoke(entity, (Object[]) null);
			return value;
		} catch (Exception se) {
			Throwable e = se;
			while (e != null) {
				if (e instanceof SecurityException) {
					result.addWarning(keyPropName, ValidationResult.FIELD_REQUIRED_NOT_ACCESSABLE);
					break;
				} else if (e instanceof InvocationTargetException) {
					InvocationTargetException ite = (InvocationTargetException) e;
					e = ite.getTargetException();
					if (e == ite) {
						break;
					}
				} else if (e instanceof UndeclaredThrowableException) {
					UndeclaredThrowableException ute = (UndeclaredThrowableException) e;
					e = ute.getUndeclaredThrowable();
					if (e == ute) {
						break;
					}
				} else {
					throw se;
				}
			}
		}
		
		return null;
	}

	/**
	 * Create a title string for the specified entity. The default
	 * implementation is using the titlePattern that is specified within the
	 * EntityDescriptor.
	 * 
	 * @param entity
	 * @return
	 */
	public String buildTitle(IEntity entity) {

		try {
			EntityDescriptor descriptor = DAOSystem.getEntityDescriptor(getEntityClass().getName());
			if (descriptor.getTitlePattern() != null) {
				return TemplateUtil.parse(descriptor.getTitlePattern(), entity);
			} else {
				// create default title
				String cn = getEntityClass().getName();
				int idx = cn.lastIndexOf('.');
				if (idx != -1) {
					cn = cn.substring(idx + 1);
				}
				return cn + " #" + entity.getId();
			}
		} catch (ConfigurationException e) {
			throw new DataAccessException("No EntityDescriptor found for this type - cant validate: "
					+ getEntityClass().getName());
		}

	}

	/**
	 * Delegate for checkRights(rightName, entity);
	 * 
	 * @param rightName
	 */
	protected void checkRights(String rightName) {
		checkRights(rightName, null);
	}

	/**
	 * Makes a security check about the given right and the actual user.
	 * <p>
	 * 
	 * @param entity
	 * 
	 * @param rightName
	 */
	protected void checkRights(String rightName, IEntity entity) {
		ISecurityManager manager = DAOSystem.getSecurityManager();
		IUser user = manager.getCurrentUser();

		if (log.isDebugEnabled()) {
			// disabled.
			/*
			 * StringBuffer logbuffer = new StringBuffer();
			 * logbuffer.append("SECURITY CHECK: User: ").append(user != null ?
			 * user.getName() : "<no user: systemstart>");
			 * logbuffer.append(" RECHT: ").append(rightName);
			 * logbuffer.append("... Bei Entit�t: "
			 * ).append(getEntityClass().getName());
			 * log.debug(logbuffer.toString());
			 */
		}
		boolean result = entity == null ? manager.hasRight(getEntityClass().getName(), rightName) : hasRight(entity,
				rightName);

		if (user != null && !result) {
			StringBuffer sb = new StringBuffer();
			sb.append("Fehlendes Recht: ").append(rightName);
			sb.append("... Bei Entit�t: ").append(getEntityClass().getName());
			log.error("SECURITY EXCEPTION: " + sb.toString());
			throw new SecurityException(sb.toString());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.xwic.appkit.core.dao.DAO#getHistory(de.xwic.appkit.core.dao.IEntity)
	 */
	@SuppressWarnings("unchecked")
	public List<IEntity> getHistory(final IEntity entity) {

		checkRights(ApplicationData.SECURITY_ACTION_READ, entity);

		return (EntityList) provider.execute(new DAOCallback() {
			public Object run(DAOProviderAPI api) {
				PropertyQuery query = new PropertyQuery();
				query.addEquals("entityID", new Integer(entity.getId()));
				query.setSortField("entityVersion");
				query.setSortDirection(PropertyQuery.SORT_DIRECTION_UP);
				return api.getEntities(getHistoryImplClass(), null, query);
			}
		});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.xwic.appkit.core.dao.DAO#findCorrectVersionForEntityRelation(de.xwic
	 * .appkit.core.dao.IEntity, de.xwic.appkit.core.dao.IEntity)
	 */
	public IEntity findCorrectVersionForEntityRelation(IEntity mainObject, IEntity relationObject) {
		if (mainObject == null) {
			throw new IllegalArgumentException("Parameter is null in: " + getClass().getName());
		}

		if (relationObject == null) {
			return null;
		}

		// get the actual date of the main object
		Date actualVersionDate = mainObject.getLastModifiedAt();

		// find the dao for the relation object to check
		DAO relDao = DAOSystem.findDAOforEntity(relationObject.type());

		// get all history versions for the main object
		List<?> allHisObjs = relDao.getHistory(relationObject);

		IEntity element = null;
		for (Iterator<?> iter = allHisObjs.iterator(); iter.hasNext();) {
			IEntity locObj = (IEntity) iter.next();
			Date locDate = locObj.getLastModifiedAt();
			// if the date is lesser than the actual date, the element might be
			// a candidate...
			if (locDate.compareTo(actualVersionDate) < 0) {
				element = locObj;
			} else {
				// more -> get out
				break;
			}
		}

		return element == null ? relationObject : element;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.xwic.appkit.core.dao.DAO#hasRight(de.xwic.appkit.core.dao.IEntity,
	 * java.lang.String)
	 */
	public boolean hasRight(IEntity entity, String action) {
		return DAOSystem.getSecurityManager().hasRight(entity.type().getName(), action);
	}

	/**
	 * @return the handleHistory
	 */
	public boolean isHandleHistory() {
		return handleHistory;
	}

	/**
	 * @param handleHistory
	 *            the handleHistory to set
	 */
	public void setHandleHistory(boolean handleHistory) {
		this.handleHistory = handleHistory;
	}

	/**
	 * Creates a new history record if history is enabled for EntityDescriptor
	 * AND the handleHistory flag is true
	 * 
	 * @param api
	 * @param entity
	 * @param reason
	 */
	protected void handleHistory(DAOProviderAPI api, IEntity entity, int reason) {
		if (handleHistory && getEntityDescriptor().isHistory()) {
			IHistory his;
			try {
				his = getHistoryImplClass().newInstance();
				HistoryTool.createHistoryEntity(entity, his);
				his.setEntityID(entity.getId());
				his.setEntityVersion(entity.getVersion() + (reason == IHistory.REASON_CREATED ? 0 : 1));
				his.setHistoryReason(reason);
				api.update(his);
			} catch (Exception e) {
				throw new DataAccessException("Unable to create history entity for base entity '" + entity + "'", e);
			}
		}
	}

}