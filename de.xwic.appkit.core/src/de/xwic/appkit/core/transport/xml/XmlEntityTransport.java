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
package de.xwic.appkit.core.transport.xml;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.config.model.Property;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.EntityKey;
import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.dao.Limit;
import de.xwic.appkit.core.remote.client.EntityProxyFactory;
import de.xwic.appkit.core.transfer.EntityTransferObject;
import de.xwic.appkit.core.transfer.PropertyValue;

/**
 * Writes a list of entities into a writer.
 * 
 * @author Florian Lippisch
 */
public class XmlEntityTransport {

	/**
	 * 
	 */
	private static final String ATTR_CHANGED = "changed";
	/**
	 * 
	 */
	public static final String ELM_ENTITY = "entity";
	/**
	 * 
	 */
	public static final String ELM_ENTITIES = "entities";
	/**
	 * 
	 */
	public static final String ELM_DATA = "data";
	/**
	 * 
	 */
	public static final String ELM_EXPORTDDATE = "exportddate";
	/**
	 * 
	 */
	public static final String ELM_EXPORT = "export";

	private static Set<String> SKIP_PROPS = new HashSet<String>();
	static {
		// SKIP_PROPS.add("id");
		// SKIP_PROPS.add("version");
		// SKIP_PROPS.add("changed");
		// SKIP_PROPS.add("deleted");
		// SKIP_PROPS.add("lastModifiedAt");
		// SKIP_PROPS.add("lastModifiedFrom");
		// SKIP_PROPS.add("createdAt");
		// SKIP_PROPS.add("createdFrom");
		// SKIP_PROPS.add("serverEntityId");
		// SKIP_PROPS.add("downloadVersion");
	}

	private boolean exportAll = false;

	/**
	 * Cache used on the server, to avoid transferring an ETO twice.
	 */
	Map<EntityKey, Integer> serverContext = new HashMap<EntityKey, Integer>();
	
	/**
	 * Used on client side. Contains all ETOs transferred during request / response cycle.
	 * That way we can use already serialized ETOs and don't have to make another server call.
	 */
	Map<EntityKey, EntityTransferObject> sessionCache;
	
	private XmlBeanSerializer xmlBeanSerializer = new XmlBeanSerializer(this);

	/**
	 * Constructor.
	 */
	public XmlEntityTransport(Map<EntityKey, EntityTransferObject> sessionCache) {
		this.sessionCache = sessionCache;
	}

	/**
	 * Export data with all properties.
	 * 
	 * @param exportAll
	 */
	public XmlEntityTransport(boolean exportAll, Map<EntityKey, EntityTransferObject> sessionCache) {
		this(sessionCache);
		this.exportAll = exportAll;
	}

	/**
	 * Export a list of entities to a xml file.
	 * 
	 * @param file
	 * @param entities
	 * @throws IOException
	 * @throws ConfigurationException
	 */
	public void write(Writer writer, IEntity entity, EntityDescriptor descr, boolean lazy) throws IOException,
			ConfigurationException {
		List<IEntity> l = new ArrayList<IEntity>();
		l.add(entity);
		write(writer, l, descr, lazy);
	}

	/**
	 * Export a list of entities to a xml file.
	 * 
	 * @param file
	 * @param entities
	 * @throws IOException
	 * @throws ConfigurationException
	 */
	public void write(Writer writer, EntityTransferObject eto, EntityDescriptor descr, boolean lazy) throws IOException,
			ConfigurationException {
		List<EntityTransferObject> l = new ArrayList<EntityTransferObject>();
		if (eto != null) {
			l.add(eto);
		}
		write(writer, l, descr, lazy);
	}

	/**
	 * @param writer
	 * @param entities
	 * @param descr
	 * @throws IOException
	 * @throws ConfigurationException
	 */
	public void write(Writer writer, List<?> entities, EntityDescriptor descr, boolean lazy) throws IOException,
			ConfigurationException {
		write(writer, entities, descr, null, lazy);
	}

	/**
	 * Export a list of entities to a xml file.
	 * 
	 * @param file
	 * @param entities
	 * @throws IOException
	 * @throws ConfigurationException
	 */
	public void write(Writer writer, List<?> entities, EntityDescriptor descr, List<String> columnNames, boolean lazy)
			throws IOException, ConfigurationException {

		boolean isETO = false;
		boolean isEntity = false;
		if (entities.size() > 0) {
			Object first = entities.get(0);
			isETO = first instanceof EntityTransferObject;
			if (!isETO) {
				isEntity = first instanceof IEntity;
			}
		}

		Document doc = DocumentFactory.getInstance().createDocument();
		Element root = doc.addElement(ELM_EXPORT);
		root.addAttribute("type", "entities");

		Element info = root.addElement(ELM_EXPORTDDATE);
		info.setText(DateFormat.getDateTimeInstance().format(new Date()));

		Element data = root.addElement(ELM_DATA);

		Element elmEntities = data.addElement(ELM_ENTITIES);
		elmEntities.addAttribute("type", descr.getClassname());
		elmEntities.addAttribute("size", Integer.toString(entities.size()));
		if (entities instanceof EntityList) {
			elmEntities.addAttribute("totalSize", Integer.toString(((EntityList) entities).getTotalSize()));
		}

		for (Iterator<?> it = entities.iterator(); it.hasNext();) {
			Object o = it.next();
			if (isETO) {
				EntityTransferObject eto = (EntityTransferObject) o;
				addEntity(elmEntities, descr, eto, lazy);
			} else if (isEntity) {
				IEntity entity = (IEntity) o;
				addEntity(elmEntities, descr, entity);
			} else {
				// it's an object array - the list comes from a query for columns, usually for list
				// views
				if (columnNames == null) {
					throw new IllegalArgumentException(
							"Error attempting to serialize Object array with no column information");
				}
				Object[] arr = (Object[]) o;
				addObjectArray(elmEntities, descr, arr, columnNames, lazy);
			}
		}

		OutputFormat prettyFormat = OutputFormat.createCompactFormat();// PrettyPrint();
		// this "hack" is required to preserve the LBCR's in strings...
		XMLWriter xmlWriter = new XMLWriter(writer, prettyFormat) {
			public void write(Document arg0) throws IOException {
				// this.preserve = true;
				super.write(arg0);
			}
		};
		xmlWriter.write(doc);
		xmlWriter.flush();

	}

	/**
	 * @param elmEntities
	 * @param descr
	 * @param eto
	 */
	void addEntity(Element entities, EntityDescriptor descr, EntityTransferObject eto, boolean lazy) {

		try {
			String entityClassName = DAOSystem.findDAOforEntity(descr.getClassname()).getEntityClass().getName();
			EntityKey key = new EntityKey(entityClassName, eto.getEntityId());
			Element elm = entities.addElement(ELM_ENTITY);
			elm.addAttribute("id", Integer.toString(eto.getEntityId()));
			elm.addAttribute("version", Long.toString(eto.getEntityVersion()));
			elm.addAttribute("type", eto.getEntityClass().getName());
			if (Boolean.TRUE.equals(eto.getPropertyValue("deleted").getValue())) {
				elm.addAttribute("deleted", XmlBeanSerializer.ATTRVALUE_TRUE);
			}
			if (Boolean.TRUE.equals(eto.getPropertyValue("changed").getValue())) {
				elm.addAttribute(ATTR_CHANGED, XmlBeanSerializer.ATTRVALUE_TRUE);
			}

			if (serverContext.containsKey(key)) {
				elm.addAttribute("cached", "true");
			} else {

				for (Iterator<?> it = eto.getPropertyValues().keySet().iterator(); it.hasNext();) {

					String propertyName = (String) it.next();

					if (exportAll || !SKIP_PROPS.contains(propertyName)) {
						// Property property = (Property)descr.getProperty(propertyName);
						PropertyValue val = eto.getPropertyValue(propertyName);

						Element pValue = elm.addElement(propertyName);

						xmlBeanSerializer.addValue(pValue, val, true, lazy);

					}

				}
				serverContext.put(key, eto.getEntityId());
			}
		} catch (Exception e) {
			throw new RuntimeException("Error transforming entity into XML: " + descr.getClassname() + ", #"
					+ eto.getEntityId() + " :" + e, e);
		}

	}

	/**
	 * @param entities
	 * @param descr
	 * @param entity
	 */
	public void addEntity(Element entities, EntityDescriptor descr, IEntity entity) {

		try {
			EntityKey key = new EntityKey(entity.type().getName(), entity.getId());
			Element elm = entities.addElement(ELM_ENTITY);
			elm.addAttribute("id", Integer.toString(entity.getId()));
			elm.addAttribute("version", Long.toString(entity.getVersion()));
			
			Class clasz = null;
			// hibernate has proxy classes and depending on version it contains a different text
			if (entity.getClass().getName().indexOf("EnhancerByCGLIB") != -1 || entity.getClass().getName().indexOf("_$$_jvs") != -1) {
				clasz = (Class<? extends IEntity>) entity.getClass().getSuperclass();
			}else {
				clasz = entity.getClass();
			}
			
			elm.addAttribute("type", clasz.getName());
			if (entity.isDeleted()) {
				elm.addAttribute("deleted", XmlBeanSerializer.ATTRVALUE_TRUE);
			}
			if (entity.isChanged()) {
				elm.addAttribute(ATTR_CHANGED, XmlBeanSerializer.ATTRVALUE_TRUE);
			}

			if (serverContext.containsKey(key)) {
				elm.addAttribute("cached", "true");
			} else {

				for (Iterator<?> it = descr.getProperties().keySet().iterator(); it.hasNext();) {

					String propertyName = (String) it.next();

					if (exportAll || !SKIP_PROPS.contains(propertyName)) {
						Property property = (Property) descr.getProperty(propertyName);

						Method mRead = property.getDescriptor().getReadMethod();
						// Method mWrite = property.getDescriptor().getWriteMethod();

						// if (mWrite != null) { // only export properties that have a write method
						if (mRead != null) {

							Element pValue = elm.addElement(propertyName);
							Object value = mRead.invoke(entity, (Object[]) null);

							xmlBeanSerializer.addValue(pValue, value, false, property.isLazy());
						}
						// }
					}
				}
				serverContext.put(key, entity.getId());
			}
		} catch (Exception e) {
			throw new RuntimeException("Error transforming entity into XML: " + entity.type().getName()
					+ ", #" + entity.getId() + " :" + e, e);
		}

	}

	/**
	 * @param entities
	 * @param descr
	 * @param arr
	 * @param columnNames
	 */
	public void addObjectArray(Element entities, EntityDescriptor descr, Object[] arr,
			List<String> columnNames, boolean lazy) {

		try {
			Element elm = entities.addElement(ELM_ENTITY);

			// first one is always the ID
			Element idValue = elm.addElement("id");
			xmlBeanSerializer.addValue(idValue, arr[0], true, lazy);

			for (int i = 1; i < arr.length; i++) {
				// use the column name as the tag
				Element pValue = elm.addElement(columnNames.get(i - 1));
				Object value = arr[i];

				xmlBeanSerializer.addValue(pValue, value, true, lazy);
			}
		} catch (Exception e) {
			throw new RuntimeException("Error transforming object array into XML: " + arr, e);
		}

	}

	/**
	 * @param doc
	 * @param limit
	 * @return
	 * @throws TransportException
	 */
	public EntityList createList(Document doc, Limit limit, IEntityNodeParser parser, boolean forceLoadCollection)
			throws TransportException {
		Element elmExport = doc.getRootElement();
		if (!ELM_EXPORT.equals(elmExport.getName())) {
			throw new TransportException("Wrong format: Root element '" + ELM_EXPORT + "' expected.");
		}
		Element elmData = elmExport.element(ELM_DATA);
		if (elmData == null) {
			throw new TransportException("Wrong format: Element '" + ELM_DATA + "' not found.");
		}
		Element elmEntities = elmData.element(ELM_ENTITIES);
		if (elmEntities == null) {
			throw new TransportException("Wrong format: Element '" + ELM_ENTITIES + "' not found.");
		}

		String type = elmEntities.attributeValue("type");
		if (type == null || type.length() == 0) {
			throw new TransportException("Type not specified.");
		}

		EntityDescriptor descr;
		Class entityClass;
		try {
			descr = DAOSystem.getEntityDescriptor(type);
			entityClass = DAOSystem.getModelClassLoader().loadClass(descr.getClassname());
		} catch (Exception e) {
			throw new TransportException("Specified type not available/configured: " + type, e);
		}

		String sSize = elmEntities.attributeValue("size");
		String sTotalSize = elmEntities.attributeValue("totalSize");
		int size = sSize != null && sSize.length() != 0 ? Integer.parseInt(sSize) : 0;
		int totalSize = sTotalSize != null && sTotalSize.length() != 0 ? Integer.parseInt(sTotalSize) : size;

		Map<EntityKey, Integer> context = new HashMap<EntityKey, Integer>();

		List data = new ArrayList();
		EntityList list = new EntityList(data, limit, totalSize);

		for (Iterator<Element> it = elmEntities.elementIterator(ELM_ENTITY); it.hasNext();) {
			Element elmEntity = it.next();
			data.add(parser.parseElement(elmEntity, context, entityClass, descr, xmlBeanSerializer, sessionCache, forceLoadCollection));
		}

		return list;
	}

	/**
	 * @return the sessionCache
	 */
	public Map<EntityKey, EntityTransferObject> getSessionCache() {
		return sessionCache;
	}

	// /**
	// * Build a list of ETO objects from a XML document.
	// * @param doc
	// * @param limit
	// * @return
	// */
	// public EntityList createETOList(Document doc, Limit limit) throws TransportException {
	//
	// Element elmExport = doc.getRootElement();
	// if (!ELM_EXPORT.equals(elmExport.getName())) {
	// throw new TransportException("Wrong format: Root element '" + ELM_EXPORT + "' expected.");
	// }
	// Element elmData = elmExport.element(ELM_DATA);
	// if (elmData == null) {
	// throw new TransportException("Wrong format: Element '" + ELM_DATA + "' not found.");
	// }
	// Element elmEntities = elmData.element(ELM_ENTITIES);
	// if (elmEntities== null) {
	// throw new TransportException("Wrong format: Element '" + ELM_ENTITIES + "' not found.");
	// }
	//
	// String type = elmEntities.attributeValue("type");
	// if (type == null || type.length() == 0) {
	// throw new TransportException("Type not specified.");
	// }
	//
	// EntityDescriptor descr;
	// Class entityClass;
	// try {
	// descr = DAOSystem.getEntityDescriptor(type);
	// entityClass = DAOSystem.getModelClassLoader().loadClass(descr.getClassname());
	// } catch (Exception e) {
	// throw new TransportException("Specified type not available/configured: " + type, e);
	// }
	//
	// String sSize = elmEntities.attributeValue("size");
	// String sTotalSize = elmEntities.attributeValue("totalSize");
	// int size = sSize != null && sSize.length() != 0 ? Integer.parseInt(sSize) : 0;
	// int totalSize = sTotalSize != null && sTotalSize.length() != 0 ? Integer.parseInt(sTotalSize)
	// : size;
	//
	//
	// Map<EntityKey, Integer> context = new HashMap<EntityKey, Integer>();
	//
	// List data = new ArrayList();
	// EntityList list = new EntityList(data, limit, totalSize);
	//
	// String lastType = null;
	// Class etoClass = entityClass;
	//
	// for (Iterator<Element> it = elmEntities.elementIterator(ELM_ENTITY); it.hasNext(); ) {
	//
	//
	// Element elmEntity = it.next();
	// String sId = elmEntity.attributeValue("id");
	// String sVersion = elmEntity.attributeValue("version");
	// if (sId == null || sId.length() == 0 || sVersion == null || sVersion.length() == 0) {
	// throw new TransportException("Missing id and version.");
	// }
	// String sType = elmEntity.attributeValue("type");
	// if (sType != null && sType.length() != 0 && !sType.equals(lastType)) {
	// try {
	// etoClass = DAOSystem.getModelClassLoader().loadClass(sType);
	// lastType = stype;
	// } catch (ClassNotFoundException e) {
	// throw new TransportException("Wront type specified" + sType, e);
	// }
	// }
	//
	// EntityTransferObject eto = new EntityTransferObject();
	// eto.setEntityId(Integer.parseInt(sId));
	// eto.setEntityVersion(Integer.parseInt(sVersion));
	// eto.setEntityClass(etoClass);
	// eto.setModified(false);
	// Map<String, PropertyValue> values = new HashMap<String, PropertyValue>();
	// eto.setPropertyValues(values);
	//
	// // add deleted property value
	// PropertyValue pvDeleted = new PropertyValue();
	// pvDeleted.setValue(XmlBeanSerializer.ATTRVALUE_TRUE.equals(elmEntity.attributeValue("deleted")));
	// pvDeleted.setType(boolean.class);
	// values.put("deleted", pvDeleted);
	//
	// for (Iterator<Element> itP = elmEntity.elementIterator(); itP.hasNext(); ) {
	// Element elmProp = itP.next();
	// Property prop = descr.getProperty(elmProp.getName());
	// if (prop != null) {
	//
	//
	// Class<?> propertyType = prop.getDescriptor().getPropertyType();
	// boolean isEntityRef = IEntity.class.isAssignableFrom(propertyType);
	// boolean isPicklistRef = IPicklistEntry.class.isAssignableFrom(propertyType);
	// boolean isNull = elmProp.element(XmlExport.ELM_NULL) != null ||
	// XmlBeanSerializer.ATTRVALUE_TRUE.equals(elmProp.attributeValue("null"));
	//
	// PropertyValue pv = new PropertyValue();
	// pv.setType(propertyType);
	// if (isNull) {
	// pv.setValue(null);
	// pv.setLoaded(true);
	// } else {
	// if ((isEntityRef && !isPicklistRef)) {
	// String id = elmProp.attributeValue("id");
	// if (id != null && !id.isEmpty()) {
	// int refId = Integer.parseInt(id);
	// pv.setEntityId(refId);
	// pv.setLoaded(false);
	// } else {
	// pv.setLoaded(true);
	//
	// }
	// } else if (prop.isCollection() &&
	// XmlBeanSerializer.ATTRVALUE_TRUE.equals(elmProp.attributeValue("lazy"))) {
	// pv.setLoaded(false);
	// } else {
	// pv.setValue(xmlBeanSerializer.readValue(context, elmProp, prop.getDescriptor()));
	// }
	//
	// if (XmlBeanSerializer.ATTRVALUE_TRUE.equals(elmProp.attributeValue("modified"))) {
	// pv.setModified(true);
	// }
	//
	// if (XmlBeanSerializer.ATTRVALUE_TRUE.equals(elmProp.attributeValue("entityType"))) {
	// pv.setEntityType(true);
	// }
	// }
	//
	// values.put(elmProp.getName(), pv);
	// }
	// }
	//
	// data.add(eto);
	// }
	//
	//
	// return list;
	// }

}
