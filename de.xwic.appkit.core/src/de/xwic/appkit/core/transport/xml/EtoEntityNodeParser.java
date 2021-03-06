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
/**
 * 
 */
package de.xwic.appkit.core.transport.xml;

import java.beans.IntrospectionException;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.config.model.Property;
import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.EntityKey;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.export.XmlExport;
import de.xwic.appkit.core.model.entities.IPicklistEntry;
import de.xwic.appkit.core.transfer.EntityTransferObject;
import de.xwic.appkit.core.transfer.PropertyValue;

/**
 * @author Adrian Ionescu
 */
public final class EtoEntityNodeParser implements IEntityNodeParser {

	private static final Log log = LogFactory.getLog(EtoEntityNodeParser.class);

	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.core.transport.xml.IEntityNodeParser#parseElement(org.dom4j.Element,
	 * java.util.Map, de.xwic.appkit.core.config.model.EntityDescriptor,
	 * de.xwic.appkit.core.transport.xml.XmlBeanSerializer)
	 */
	@Override
	public Object parseElement(Element elmEntity, Map<EntityKey, Long> context, Class entityClass,
			EntityDescriptor descr, XmlBeanSerializer xmlBeanSerializer, Map<EntityKey, EntityTransferObject> sessionCache, boolean forceLoadCollection) throws TransportException {
		String sId = elmEntity.attributeValue("id");
		String sVersion = elmEntity.attributeValue("version");
		if (sId == null || sId.length() == 0 || sVersion == null || sVersion.length() == 0) {
			throw new TransportException("Missing id and version.");
		}

		// etoClass can differ, this means that the descriptor can also differ, friend
		EntityDescriptor descriptor = descr;
		Class etoClass = entityClass;

		String sType = elmEntity.attributeValue("type");
		if (etoClass != null && !etoClass.getName().equals(sType) && sType != null && sType.length() != 0) {
			try {
				etoClass = DAOSystem.getModelClassLoader().loadClass(sType);
				if (requiresNewDescriptor(entityClass, etoClass)) {
					descriptor = new EntityDescriptor(etoClass);
				}
			} catch (ClassNotFoundException e) {
				throw new TransportException("Wront type specified " + sType, e);
			} catch (final IntrospectionException e) {
				throw new TransportException("Failed to read type " + sType, e);
			}
		}
		
		EntityKey key = new EntityKey(descr.getClassname(), Long.parseLong(sId));
		if(sessionCache.containsKey(key)){
			log.debug("Cache hit for Entity: " + key.toString());
			return sessionCache.get(key);
		}

		EntityTransferObject eto = new EntityTransferObject(sId, sVersion, etoClass);
		sessionCache.put(key, eto);
		eto.setModified(false);
		Map<String, PropertyValue> values = eto.getPropertyValues();

		// add deleted property value
		PropertyValue pvDeleted = new PropertyValue();
		pvDeleted.setValue(XmlBeanSerializer.ATTRVALUE_TRUE.equals(elmEntity.attributeValue("deleted")));
		pvDeleted.setType(boolean.class);
		values.put("deleted", pvDeleted);

		for (Iterator<Element> itP = elmEntity.elementIterator(); itP.hasNext();) {
			Element elmProp = itP.next();
			String x = elmProp.getName();
			Property prop = descriptor.getProperty(x);
			if (null == prop) {
				log.debug("Unrecognized property: " + x);
				continue;
			}
			Class<?> propertyType = prop.getDescriptor().getPropertyType();
			boolean isEntityRef = IEntity.class.isAssignableFrom(propertyType);
			boolean isPicklistRef = IPicklistEntry.class.isAssignableFrom(propertyType);
			boolean isNull = elmProp.element(XmlExport.ELM_NULL) != null
					|| XmlBeanSerializer.ATTRVALUE_TRUE.equals(elmProp.attributeValue("null"));

			PropertyValue pv = new PropertyValue();
			pv.setType(propertyType);
			if (isNull) {
				pv.setValue(null);
				pv.setLoaded(true);
			} else {
				if ((isEntityRef && !isPicklistRef)) {
					String id = elmProp.attributeValue("id");
					if (id != null && !id.isEmpty()) {
						long refId = Long.parseLong(id);
						pv.setEntityId(refId);

						// String etoStr = elmProp.attributeValue("eto");

						if (elmProp.element(XmlEntityTransport.ELM_ENTITY) != null) {

							Element elmRef = elmProp.element(XmlEntityTransport.ELM_ENTITY);
							String type = elmRef.attributeValue("type");
							if (type == null || type.length() == 0) {
								// this is a direct entity reference, not an ETO
								type = elmProp.attributeValue("type");
							}
							
							String cached = elmRef.attributeValue("cached");
							
							EntityDescriptor descrRef;
							Class entityClassRef;
							DAO<?> daoRef;
							try {
								daoRef = DAOSystem.findDAOforEntity(type);
								descrRef = daoRef.getEntityDescriptor();
								entityClassRef = DAOSystem.getModelClassLoader().loadClass(descr.getClassname());
							} catch (Exception e) {
								throw new TransportException("Specified type not available/configured: " + type, e);
							}
							
							if("true".equals(cached)){
								EntityKey keyRef = new EntityKey(descr.getClassname(), refId);
								EntityTransferObject etoRef = sessionCache.get(keyRef);
								if(etoRef == null){
									keyRef = new EntityKey(daoRef.getEntityClass().getName(), refId);
									etoRef = sessionCache.get(keyRef );
								}
								pv.setValue(etoRef);
							}else {
							
							pv.setValue(parseElement(elmProp.element(XmlEntityTransport.ELM_ENTITY), context,
									entityClassRef, descrRef, xmlBeanSerializer, sessionCache, forceLoadCollection));
							// pv.setValue(EtoSerializer.deserialize(elmProp.elementText(XmlEntityTransport.ELM_ENTITY)));
							}
							pv.setLoaded(true);
						} else {
							pv.setLoaded(false);
						}
					} else {
						pv.setLoaded(true);

					}
				} else if (prop.isCollection()
						&& (XmlBeanSerializer.ATTRVALUE_TRUE.equals(elmProp.attributeValue("lazy")) && !forceLoadCollection)) {
					pv.setLoaded(false);
				} else {
					pv.setValue(xmlBeanSerializer.readValue(context, elmProp, prop.getDescriptor(), forceLoadCollection));
				}
			}

			if (XmlBeanSerializer.ATTRVALUE_TRUE.equals(elmProp.attributeValue("modified"))) {
				pv.setModified(true);
			}

			if (XmlBeanSerializer.ATTRVALUE_TRUE.equals(elmProp.attributeValue("entityType"))) {
				pv.setEntityType(true);
			}

			values.put(elmProp.getName(), pv);
		}

		return eto;
	}

	/**
	 * @param entityClass
	 * @param etoClass
	 * @return
	 */
	private static boolean requiresNewDescriptor(final Class<?> entityClass, final Class<?> etoClass) {
		if (etoClass == null) {
			return false;
		}
		return !etoClass.getName().equals(entityClass.getName());
	}

}
