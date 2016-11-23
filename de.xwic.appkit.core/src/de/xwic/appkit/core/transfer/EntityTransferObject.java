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
package de.xwic.appkit.core.transfer;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.config.model.Property;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.DataAccessException;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.entities.IPicklistEntry;

/**
 * Acts as a container for entities to be transported via axis. The container
 * is used to transport only selected content (properties). Colletions and Entity-references
 * support lazy loading.
 * 
 * @author Florian Lippisch
 */
public class EntityTransferObject {
	
	private final Class<? extends IEntity> entityClass;
	private long entityId;
	private long entityVersion;

	private boolean modified = false;
	private final Map<String, PropertyValue> propertyValues = new HashMap<String, PropertyValue>();
	
	private final static Set<String> EXTRA_PROPERTIES = new HashSet<String>();
	static {
		EXTRA_PROPERTIES.add("entityVersion");
		EXTRA_PROPERTIES.add("entityID");
		EXTRA_PROPERTIES.add("historyReason");
	}
	
	/**
	 * @param entityId
	 * @param entityVersion
	 * @param entityClass
	 */
	public EntityTransferObject(final String entityId, final String entityVersion, final Class<? extends IEntity> entityClass) {
		this.entityId = Long.parseLong(entityId);
		this.entityVersion = Integer.parseInt(entityVersion);
		this.entityClass = entityClass;
	}
	
	/**
	 * Constructs a new EntityTransferObject from the specified bean.
	 * @param bean
	 * @throws IntrospectionException 
	 * @throws TransferException 
	 */
	public EntityTransferObject(IEntity entity) throws DataAccessException {
		this(entity, false);
	}

	/**
	 * Specify forUpdate as true to set all values to be modified.
	 * @param entity
	 * @param forUpdate
	 */
	@SuppressWarnings("unchecked")
	public EntityTransferObject(IEntity entity, boolean forUpdate) {
		if (entity == null) {
			throw new NullPointerException("Entity must not be null");
		}
		final Class<? extends IEntity> clasz;
		// hibernate has proxy classes and depending on version it contains a different text
		if (entity.getClass().getName().indexOf("EnhancerByCGLIB") != -1 || entity.getClass().getName().indexOf("_$$_jvs") != -1) {
			clasz = (Class<? extends IEntity>) entity.getClass().getSuperclass();
		} else if (Proxy.isProxyClass(entity.getClass())) {
			InvocationHandler ih = Proxy.getInvocationHandler(entity);
			if (ih instanceof IEntityInvocationHandler) {
				clasz = (Class<? extends IEntity>)((IEntityInvocationHandler)ih).getEntityImplClass();
			} else {
				clasz = null;
			}
		} else {
			clasz = entity.getClass();
		}

		entityClass = clasz;
		entityId = entity.getId();
		entityVersion = entity.getVersion();
		
		readProperties(entity, forUpdate);
	}

	/**
	 * @param entity
	 * @param loadCollections 
	 * @throws IntrospectionException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	private void readProperties(IEntity entity, boolean loadCollections) throws DataAccessException {
		
		try {
			BeanInfo bi = Introspector.getBeanInfo(entity.getClass());
			PropertyDescriptor descriptors[] = bi.getPropertyDescriptors();
			EntityDescriptor entityDescr = DAOSystem.getEntityDescriptor(entity.type().getName());
			
//			ISecurityManager secMan = DAOSystem.getSecurityManager();
//			String scope = entity.type().getName();
			
			// read the properties
			for (int i = 0; i < descriptors.length; i++) {
				PropertyDescriptor descriptor = descriptors[i];
				
				if (!descriptor.getName().equals("class")) {
					Property property = entityDescr.getProperty(descriptor.getName());
					Class<?> type = descriptor.getPropertyType();
					Method mRead = descriptor.getReadMethod();
					
					if (mRead == null) {
						// if we have a Boolean field, the java sepcification is that the getter starts with
						// 'get', not 'is', therefore the read method is not located
						// by instantiating the property descriptor manually, the read method is located successfully
						PropertyDescriptor pd = new PropertyDescriptor(descriptor.getName(), entity.getClass());
						mRead = pd.getReadMethod();
					}
					
					if (property == null && !EXTRA_PROPERTIES.contains(descriptor.getName())) {
						// if the property is not specified by the entity and it
						// is not a special extra property, it is skiped.
						continue;
					}
					
					PropertyValue value = new PropertyValue();
					value.setType(type);
					value.setModified(loadCollections);
					
					// do not check the rights.. the checks are already done on the server
//					value.setAccess(secMan.getAccess(scope, descriptor.getName()));
//					if (value.getAccess() != ISecurityManager.NONE) {
					
						Object data = mRead.invoke(entity, (Object[]) null);
						
						if (IEntity.class.isAssignableFrom(type)) {
							value.setEntityType(true);
							if (data != null) {
								IEntity refEntity = (IEntity)data;
								if (refEntity.getId() == 0) { // new entity 
									value.setEntityId(0);
									value.setLoaded(true);
									value.setValue(refEntity);
								} else {
									value.setEntityId(refEntity.getId());
									// do not store the ref entity when lazy loading is on or the ETO
									// is constructed for an update.
									if (property != null && (property.isLazy() || loadCollections)) {
										value.setLoaded(false);
									} else {
										value.setLoaded(true);
										value.setValue(new EntityTransferObject(refEntity));
									}
								}
								// check picklist entry -> setLoaded is false in any case!
								boolean isPlEntry = refEntity instanceof IPicklistEntry;
								
								if (isPlEntry) {
									value.setLoaded(false);
								}
								
							}
						} else if (data instanceof Collection) {
							if (loadCollections) {
								value.setLoaded(true);
								Collection<Object> col;
								if (data instanceof Set) {
									col = new HashSet<Object>();
								} else if (data instanceof List) {
									col = new ArrayList<Object>();
								} else {
									throw new DataAccessException("Can't handle collection type: " + data.getClass().getName());
								}
								for (Iterator<?> it = ((Collection<?>)data).iterator(); it.hasNext(); ) {
									Object obj = it.next();
									if (obj instanceof IEntity) {
										IEntity ent = (IEntity)obj;
										PropertyValue pv = new PropertyValue();
										pv.setEntityId(ent.getId());
										pv.setType(ent.type());
										pv.setEntityType(true);
										pv.setLoaded(false);
										obj = pv;
									}
									col.add(obj);
								}
								value.setValue(col);
							} else {
								value.setLoaded(false);
							}
						} else {
							value.setValue(data);
						}
						
//					}
						
					propertyValues.put(descriptor.getName(), value);
				}
			}
			
		} catch (Exception e) {
			throw new DataAccessException("Error reading properties:" + e, e);
		}
		
	}
	
	/**
	 * Returns the PropertyValue of the specified property.
	 * @param property
	 * @return
	 */
	public PropertyValue getPropertyValue(String property) {
		return propertyValues.get(property);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("ETO [" + entityClass.getName() + "]: \n");
		for (Iterator<String> it = propertyValues.keySet().iterator(); it.hasNext(); ) {
			String property = it.next();
			sb.append(property).append("=");
			sb.append(propertyValues.get(property));
			if (it.hasNext()) {
				sb.append("\n");
			}
		}
		return sb.toString();
	}

	/**
	 * @return Returns the entityClass.
	 */
	public Class<? extends IEntity> getEntityClass() {
		return entityClass;
	}

	/**
	 * @return Returns the entityId.
	 */
	public long getEntityId() {
		return entityId;
	}

	/**
	 * @return Returns the entityVersion.
	 */
	public long getEntityVersion() {
		return entityVersion;
	}

	/**
	 * @return Returns the propertyValues.
	 */
	public Map<String, PropertyValue> getPropertyValues() {
		return propertyValues;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((entityClass == null) ? 0 : entityClass.hashCode());
		result = PRIME * result + (int) (entityId ^ (entityId >>> 32));
		result = PRIME * result + (int) (entityVersion ^ (entityVersion >>> 32));
		//result = PRIME * result + ((propertyValues == null) ? 0 : propertyValues.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final EntityTransferObject other = (EntityTransferObject) obj;
		if (entityClass == null) {
			if (other.entityClass != null) {
				return false;
			}
		} else if (!entityClass.equals(other.entityClass)) {
			return false;
		}
		if (entityId != other.entityId) {
			return false;
		}
		if (modified != other.modified) {
			return false;
		}
		if (entityVersion != other.entityVersion) {
			return false;
		}
		if (propertyValues == null) {
			if (other.propertyValues != null) {
				return false;
			}
		} else if (!propertyValues.equals(other.propertyValues)) {
			return false;
		}
		return true;
	}

	/**
	 * Rereshes the content if the specified ETO is a newer version.
	 * This method is used to update the ETO instance after it has been
	 * updated on the server side.
	 * @param response
	 */
	public void refresh(EntityTransferObject response) {
		if (!response.entityClass.equals(entityClass)) {
			throw new IllegalArgumentException("The entity type is different.");
		}
		// not new but different
		if (entityId != 0l && response.entityId != entityId) {
			throw new IllegalArgumentException("The entity ID is not the same.");
		}
		
		// update
		entityId = response.entityId;
		entityVersion = response.entityVersion;
		propertyValues.clear();
		propertyValues.putAll(response.propertyValues);
	}

	/**
	 * @return the modified
	 */
	public boolean isModified() {
		return modified;
	}

	/**
	 * @param modified the modified to set
	 */
	public void setModified(boolean modified) {
		this.modified = modified;
	}
	
}
