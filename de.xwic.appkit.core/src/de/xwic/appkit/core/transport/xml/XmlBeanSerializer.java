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

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.config.ConfigurationManager;
import de.xwic.appkit.core.config.Language;
import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.EntityKey;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.dao.util.Entities;
import de.xwic.appkit.core.export.XmlExport;
import de.xwic.appkit.core.model.daos.IPicklisteDAO;
import de.xwic.appkit.core.model.entities.IPicklistEntry;
import de.xwic.appkit.core.model.entities.IPickliste;
import de.xwic.appkit.core.remote.client.EntityProxyFactory;
import de.xwic.appkit.core.transfer.EntityTransferObject;
import de.xwic.appkit.core.transfer.PropertyValue;

/**
 * @author Florian Lippisch
 */
public class XmlBeanSerializer {

	/**
	 * 
	 */
	public static final String ATTRVALUE_TRUE = "1";
	/**
	 * 
	 */
	public static final String ELM_NULL = "null";
	/**
	 * 
	 */
	public static final String ELM_ELEMENT = "element";
	/**
	 * 
	 */
	public static final String ELM_SET = "set";
	/**
	 * 
	 */
	public static final String ELM_LIST = "list";
	/**
	 *
	 */
	private static final String ELM_MAP = "map";
	/**
	 *
	 */
	private static final String ELM_MAP_ENTRY = "entry";
	/**
	 *
	 */
	private static final String ELM_MAP_KEY = "key";
	/**
	 *
	 */
	private static final String ELM_MAP_VALUE = "value";
	/**
	 * 
	 */
	public static final String ELM_BEAN = "bean";

	/**
	 *
	 */
	public static final String ELM_ENUM = "enum-name";

	/**
	 * 
	 */
	private static final String NEW_LINE_PLACEHOLDER = "<nl_ph>";

	private final static Object[] NO_ARGS = new Object[0];
	protected static final Log log = LogFactory.getLog(XmlBeanSerializer.class);

	private List<ICustomObjectSerializer> customSerializer = new ArrayList<ICustomObjectSerializer>();
	private Set<String> skipPropertyNames = new HashSet<String>();

	private boolean serializeEntity = false;
	
	private XmlEntityTransport transport;
	
	public XmlBeanSerializer(XmlEntityTransport transport){
		this.transport = transport;
	}
	
	/**
	 * For no caching
	 */
	public XmlBeanSerializer(){
		this(new HashMap<EntityKey, EntityTransferObject>());
	}
	
	public XmlBeanSerializer(Map<EntityKey, EntityTransferObject> sessionCache){
		this(new XmlEntityTransport(sessionCache));
	}

	/**
	 * Add a custom serializer
	 * 
	 * @param serializer
	 */
	public void addCustomObjectSerializer(ICustomObjectSerializer serializer) {
		customSerializer.add(serializer);
	}

	/**
	 * Add a property name to skip.
	 * 
	 * @param name
	 */
	public void addSkipPropertyName(String name) {
		skipPropertyNames.add(name);
	}

	/**
	 * @param rootElement
	 * @param bean
	 * @return
	 * @throws TransportException
	 */
	public static String serializeToXML(String rootElement, Object bean) throws TransportException {
		return serializeToXML(rootElement, bean, false);
	}

	/**
	 * @param rootElement
	 * @param bean
	 * @return
	 * @throws TransportException
	 */
	public static String serializeToXML(String rootElement, Object bean, boolean serializeEntity)
			throws TransportException {
		return serializeCollectionToXML(rootElement, Arrays.asList(bean), serializeEntity);
	}

	/**
	 * @param rootElement
	 * @param beans
	 * @return
	 * @throws TransportException
	 */
	public static String serializeCollectionToXML(String rootElement, Collection<?> beans,
			boolean serializeEntity) throws TransportException {
		ByteArrayOutputStream baOut = new ByteArrayOutputStream();
		OutputStreamWriter writer = new OutputStreamWriter(baOut);

		try {
			Document doc = DocumentFactory.getInstance().createDocument();
			Element root = doc.addElement(rootElement);

			XmlEntityTransport transport = new XmlEntityTransport(new HashMap<EntityKey, EntityTransferObject>());
			XmlBeanSerializer xml = new XmlBeanSerializer(transport);
			xml.setSerializeEntity(serializeEntity);

			for (Object bean : beans) {
				xml.addValue(root, bean, true);
			}

			OutputFormat prettyFormat = OutputFormat.createCompactFormat();// CompactFormat();//PrettyPrint();
			// this "hack" is required to preserve the LBCR's in strings...
			XMLWriter xmlWriter = new XMLWriter(writer, prettyFormat) {
				@Override
				public void write(Document arg0) throws IOException {
					// this.preserve = true;
					super.write(arg0);
				}
			};
			xmlWriter.write(doc);
			xmlWriter.flush();

		} catch (IOException e) {
			throw new TransportException("Unexpected IOException while serializing query.", e);
		}

		return baOut.toString();
	}

	/**
	 * @param doc
	 * @param string
	 * @param query
	 * @throws IntrospectionException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public void serializeBean(Element elm, String elementName, Object bean) throws TransportException {

		Element root = elm.addElement(elementName);

		root.addAttribute("type", bean.getClass().getName());

		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
			for (PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {

				if (!"class".equals(pd.getName())) {
					Element pElm = root.addElement(pd.getName());
					Method mRead = pd.getReadMethod();
					if (mRead == null) {
						log.debug(String.format("No '%s' property on '%s'.", pd.getName(), bean.getClass()));
						continue;
					}
					Object value = mRead.invoke(bean, NO_ARGS);

					boolean addType = true;
					addType = value != null && !value.getClass().equals(pd.getPropertyType())
							&& !pd.getPropertyType().isPrimitive();
					addValue(pElm, value, addType);
				}

			}
		} catch (Exception e) {
			throw new TransportException("Error serializing bean: " + e, e);
		}

	}

	public void addValue(Element elm, Object value, boolean addTypeInfo) throws TransportException {
		addValue(elm, value, addTypeInfo, true);
	}
	/**
	 * @param value
	 * @param value2
	 * @throws TransportException
	 */
	public void addValue(Element elm, Object value, boolean addTypeInfo, boolean lazy) throws TransportException {

		// check custom object serializers first.
		for (ICustomObjectSerializer cos : customSerializer) {
			if (cos.handlesObject(value)) {
				cos.serialize(elm, value);
				return; // early exit
			}
		}

		String typeInfo = null;

		if (value instanceof PropertyValue) {
			PropertyValue pv = (PropertyValue) value;
			if (pv.isEntityType()) {
				if (pv.isLoaded() && pv.getValue() == null) {
					value = null;
				} else {
					elm.addAttribute("id", Integer.toString(pv.getEntityId()));
					elm.addAttribute("entityType", ATTRVALUE_TRUE);
					if (pv.isLoaded()) {
						value = pv.getValue();
					} else {
						value = "";
						typeInfo = pv.getType().getName();
					}
				}
			} else if (pv.isLoaded()) {
				value = pv.getValue();
			} else {
				// non loaded value
				value = null;
				elm.addAttribute("lazy", ATTRVALUE_TRUE);
				return; // BREAK
			}

			if (pv.isModified()) {
				elm.addAttribute("modified", ATTRVALUE_TRUE);
			}
		}

		if (typeInfo == null) {
			typeInfo = value != null ? value.getClass().getName() : null;
		}

		if (value instanceof Enum<?>) {
			serializeEnum(elm, (Enum<?>) value);
			return;
		}

		if (value == null) {
			elm.addAttribute(ELM_NULL, ATTRVALUE_TRUE);
		} else if (value instanceof String) {
			String newValue = ((String) value).replaceAll("(\n\r)|\n", NEW_LINE_PLACEHOLDER);
			elm.setText(newValue);
		} else if (value instanceof Integer) {
			elm.setText(value.toString());
		} else if (value instanceof Boolean) {
			Boolean bo = (Boolean) value;
			elm.setText(bo.booleanValue() ? "true" : "false");
		} else if (value instanceof Long) {
			elm.setText(value.toString());
		} else if (value instanceof Double) {
			elm.setText(value.toString());
		} else if (value instanceof Date) {
			Date date = (Date) value;
			elm.setText(Long.toString(date.getTime()));

		} else if (value instanceof IPicklistEntry) {
			IPicklistEntry entry = (IPicklistEntry) value;
			typeInfo = entry.type().getName();
			if (entry.getKey() != null) {
				elm.addAttribute("key", entry.getKey());
			}
			elm.addAttribute("picklistid", entry.getPickliste().getKey());
			elm.addAttribute("entryid", Integer.toString(entry.getId()));
			elm.addAttribute("langid", "de");
			elm.setText(entry.getBezeichnung("de")); // use german language in export, as it is most
														// common

		} else if (value instanceof IEntity) {
			IEntity entity = (IEntity) value;
			typeInfo = entity.type().getName();
			if (entity.getId() == Entities.NEW_ENTITY_ID || serializeEntity || !lazy) {
				EntityDescriptor descr;
				try {
					descr = DAOSystem.getEntityDescriptor(typeInfo);
					transport.addEntity(elm, descr, entity);
				} catch (ConfigurationException e) {
					throw new TransportException(e);
				}

			}
			elm.addAttribute("id", Integer.toString(entity.getId()));
			
		} else if (value instanceof EntityTransferObject) {
			EntityTransferObject eto = (EntityTransferObject) value;
			DAO<?> dao = DAOSystem.findDAOforEntity(eto.getEntityClass());
			typeInfo = dao.getEntityClass().getName();
			EntityDescriptor descr;
			try {
				descr = DAOSystem.getEntityDescriptor(typeInfo);
				transport.addEntity(elm, descr, eto);
			} catch (ConfigurationException e) {
				throw new TransportException(e);
			}
			elm.addAttribute("id", Integer.toString(eto.getEntityId()));

		} else if (value instanceof Set) {
			Set<?> set = (Set<?>) value;
			Element elmSet = elm.addElement(ELM_SET);
			for (Object o : set) {
				Element entry = elmSet.addElement(ELM_ELEMENT);
				addValue(entry, o, true);
			}
		} else if (value instanceof List) {
			List<?> list = (List<?>) value;
			Element elmSet = elm.addElement(ELM_LIST);
			for (Object o : list) {
				Element entry = elmSet.addElement(ELM_ELEMENT);
				addValue(entry, o, true);
			}
		} else if (value instanceof Map<?, ?>) {
			serializeMap(elm, (Map<?, ?>) value);
		} else {
			serializeBean(elm, ELM_BEAN, value);
			addTypeInfo = false;
		}

		if (addTypeInfo && typeInfo != null) {
			elm.addAttribute("type", typeInfo);
		}

	}

	/**
	 * @param root
	 * @param string
	 * @return
	 */
	public Object deserializeBean(Element elm) throws TransportException {
		Map<EntityKey, Integer> context = new HashMap<EntityKey, Integer>();
		return deserializeBean(elm, context);
	}

	/**
	 * Deserializes a bean.
	 * 
	 * @param elm
	 * @param context
	 * @return
	 * @throws TransportException
	 */
	public Object deserializeBean(Element elm, Map<EntityKey, Integer> context) throws TransportException {
		Element firstElem = (Element) elm.elementIterator().next();
		if (firstElem != null
				&& (ELM_LIST.equals(firstElem.getName()) || ELM_SET.equals(firstElem.getName()))) {
			return readValue(context, elm, null);
		}

		String strTypeClass = elm.attributeValue("type");
		if (strTypeClass == null || strTypeClass.length() == 0) {
			throw new TransportException("Missing type attribute in bean element (" + elm.getName() + ")");
		}

		// instanciate bean
		Class<?> beanType;
		Object bean;
		try {
			beanType = Class.forName(strTypeClass);
			bean = beanType.newInstance();
		} catch (InstantiationException e) {
			throw new TransportException("Can not instantiate bean class " + strTypeClass, e);
		} catch (IllegalAccessException e) {
			throw new TransportException("IllegalAccessException instantiating bean class " + strTypeClass);
		} catch (ClassNotFoundException e) {
			throw new TransportException("The bean '" + strTypeClass
					+ "' can not be deserialized because the class can not be found.", e);
		}

		deserializeBean(bean, elm, context);

		return bean;
	}

	/**
	 * Deserializes an element into a bean.
	 * 
	 * @param elm
	 * @param context
	 * @return
	 * @throws TransportException
	 */
	public void deserializeBean(Object bean, Element elm, Map<EntityKey, Integer> context)
			throws TransportException {
		// now read the properties
		try {
			BeanInfo bi = Introspector.getBeanInfo(bean.getClass());
			for (PropertyDescriptor pd : bi.getPropertyDescriptors()) {
				Method mWrite = pd.getWriteMethod();
				if (!"class".equals(pd.getName()) && mWrite != null
						&& !skipPropertyNames.contains(pd.getName())) {
					Element elmProp = elm.element(pd.getName());
					if (elmProp != null) {
						Object value = readValue(context, elmProp, pd);
						mWrite.invoke(bean, new Object[] { value });
					} else {
						log.warn("The property "
								+ pd.getName()
								+ " is not specified in the xml document. The bean may not be restored completly");
					}
				}
			}
		} catch (Exception e) {
			throw new TransportException("Error deserializing bean: " + e, e);
		}
	}

	/**
	 * @param context
	 * @param elmProp
	 * @param pd
	 * @return
	 * @throws TransportException
	 */
	@SuppressWarnings("unchecked")
	public Object readValue(Map<EntityKey, Integer> context, Element elProp, PropertyDescriptor pd)
			throws TransportException {

		// check if value is null
		if (elProp.element(XmlExport.ELM_NULL) != null
				|| ATTRVALUE_TRUE.equals(elProp.attributeValue("null"))) {
			return null;
		}

		Class<?> type = getType(elProp.attributeValue("type"));

		if (type == null) {
			if (pd != null) {
				type = pd.getPropertyType();
			} else {
				// is it a bean?
				Element elBean = elProp.element(ELM_BEAN);
				if (elBean != null) {
					return deserializeBean(elBean, context);
				}
				throw new TransportException("Can't deserialize element '" + elProp.getName()
						+ "' - no type informations available.");
			}
		}

		// check custom object serializers first.
		for (ICustomObjectSerializer cos : customSerializer) {
			if (cos.handlesType(type)) {
				// found a custom serializer, directly return the value, even if null
				return cos.deserialize(elProp);
			}
		}

		Object value = null;

		if (Enum.class.isAssignableFrom(type)) {
			return deserializeEnum(type, elProp);
		}

		if (Set.class.isAssignableFrom(type)) {
			// a set.
			Element elSet = elProp.element(ELM_SET);
			if (elSet != null) {
				Set<Object> set = new HashSet<Object>();
				for (Iterator<?> itSet = elSet.elementIterator(ELM_ELEMENT); itSet.hasNext();) {
					Element elSetElement = (Element) itSet.next();
					set.add(readValue(context, elSetElement, null));
				}
				value = set;
			}
		} else if (List.class.isAssignableFrom(type)) {
			Element elSet = elProp.element(ELM_LIST);
			if (elSet != null) {
				List<Object> list = new ArrayList<Object>();
				for (Iterator<?> itSet = elSet.elementIterator(ELM_ELEMENT); itSet.hasNext();) {
					Element elSetElement = (Element) itSet.next();
					list.add(readValue(context, elSetElement, null));
				}
				value = list;
			}
		} else if (Map.class.isAssignableFrom(type)) {
			value = deserializeMap(context, elProp);
		} else if (IPicklistEntry.class.isAssignableFrom(type)) {

			IPicklisteDAO plDAO = DAOSystem.getDAO(IPicklisteDAO.class);
			IPicklistEntry entry = null;

			String picklistId = elProp.attributeValue("picklistid");
			String key = elProp.attributeValue("key");
			String text = elProp.getText();

			if ((picklistId == null || picklistId.trim().isEmpty()) || (key == null || key.trim().isEmpty())) {
				// maybe it was sent with the ID?

				String strId = elProp.attributeValue("id");

				if (strId != null && !strId.isEmpty()) {
					int id = Integer.parseInt(strId);
					entry = plDAO.getPickListEntryByID(id);
				}

			} else {

				String langid = elProp.attributeValue("langid");
				if (langid == null || langid.length() == 0) {
					langid = "en";
				}

				// try to find by key
				if (key != null && key.length() != 0 && picklistId != null && picklistId.length() != 0) {
					entry = plDAO.getPickListEntryByKey(picklistId, key); // try to find by key
				}

				// not found, try by title
				if (entry == null && picklistId != null && picklistId.length() != 0 && text != null
						&& text.length() != 0) {
					List<IPicklistEntry> lst = plDAO.getAllEntriesToList(picklistId);
					for (IPicklistEntry pe : lst) {
						if (text.equals(pe.getBezeichnung(langid))) {
							entry = pe;
							break;
						}
					}
				}
			}

			if (entry == null && picklistId != null && text != null) {
				// create??
				IPickliste plist = plDAO.getPicklisteByKey(picklistId);
				if (plist != null) {
					entry = plDAO.createPicklistEntry();
					entry.setKey(key);
					entry.setPickliste(plist);
					plDAO.update(entry);

					for (Language lang : ConfigurationManager.getSetup().getLanguages()) {
						plDAO.createBezeichnung(entry, lang.getId(), text);
					}
				}
			}
			value = entry;

		} else if (IEntity.class.isAssignableFrom(type)) {
			// entity type
			int refId = Integer.parseInt(elProp.attributeValue("id"));
			if (context != null) {
				Integer newId = context.get(new EntityKey(type.getName(), refId));
				if (newId != null) {
					// its an imported object
					refId = newId.intValue();
				}
			}
			DAO<?> refDAO = DAOSystem.findDAOforEntity((Class<? extends IEntity>) type);
			IEntity refEntity;

			if (refId == Entities.NEW_ENTITY_ID) {
				refEntity = EtoSerializer.newEntity(elProp.attributeValue(EtoSerializer.ETO_PROPERTY));
			} else if (elProp.element(XmlEntityTransport.ELM_ENTITY) != null) {

				EtoEntityNodeParser parser = new EtoEntityNodeParser();
				EntityTransferObject refEto= (EntityTransferObject) parser.parseElement(elProp.element(XmlEntityTransport.ELM_ENTITY),
						context, type, refDAO.getEntityDescriptor(), this, transport.getSessionCache());
				refEntity = EntityProxyFactory.createEntityProxy(refEto);
			} else {
				refEntity = refDAO.getEntity(refId);
			}

			if (refEntity == null) {
				throw new TransportException(String.format("No entity of type %s with id %s.", type, refId));
			}
			value = refEntity;

		} else {

			Element elBean = elProp.element(ELM_BEAN);
			if (elBean != null) {
				value = deserializeBean(elBean, context);
			} else {
				// basic type
				String text = elProp.getText();
				if (String.class.equals(type)) {
					if (text != null) {
						value = text.replaceAll(NEW_LINE_PLACEHOLDER, "\n");
					}
				} else if (int.class.equals(type) || Integer.class.equals(type)) {
					value = new Integer(text);
				} else if (long.class.equals(type) || Long.class.equals(type)) {
					value = new Long(text);
				} else if (boolean.class.equals(type) || Boolean.class.equals(type)) {
					value = new Boolean(text.equals("true"));
				} else if (Date.class.equals(type) || java.sql.Timestamp.class.equals(type)) {
					// entities coming from the DB have the Date field as java.sql.Timestamp
					// the serialized value is the ms timestamp, so we can instantiate a Date from
					// it
					value = new Date(Long.parseLong(text));
				} else if (double.class.equals(type) || Double.class.equals(type)) {
					value = new Double(text);
				}
			}
		}

		return value;
	}

	/**
	 * @param elm
	 * @param value
	 * @param addTypeInfo
	 * @throws TransportException
	 */
	private void serializeMap(final Element elm, final Map<?, ?> map) throws TransportException {
		final Element elmMap = elm.addElement(ELM_MAP);
		for (final Entry<?, ?> entry : map.entrySet()) {
			final Element element = elmMap.addElement(ELM_MAP_ENTRY);
			final Element key = element.addElement(ELM_MAP_KEY);
			addValue(key, entry.getKey(), true);
			final Element mapValue = element.addElement(ELM_MAP_VALUE);
			addValue(mapValue, entry.getValue(), true);
		}
	}

	/**
	 * @param context
	 * @param elProp
	 * @param value
	 * @return
	 * @throws TransportException
	 */
	private Map<?, ?> deserializeMap(final Map<EntityKey, Integer> context, final Element elProp)
			throws TransportException {
		final Element element = elProp.element(ELM_MAP);
		if (element == null) {
			return null;
		}
		final Map<Object, Object> map = new HashMap<Object, Object>();
		final Iterator<Element> it = element.elementIterator(ELM_MAP_ENTRY);
		while (it.hasNext()) {
			final Element entry = it.next();

			final Object key = readValue(context, entry.element(ELM_MAP_KEY), null);
			final Object value = readValue(context, entry.element(ELM_MAP_VALUE), null);

			map.put(key, value);
		}
		return map;
	}

	/**
	 * @param typeName
	 * @return
	 * @throws TransportException if the class is not found
	 */
	private static Class<?> getType(final String typeName) throws TransportException {
		if (typeName != null) {
			try {
				return Class.forName(typeName);
			} catch (ClassNotFoundException e) {
				throw new TransportException("Can not create class: " + e, e);
			}
		}
		return null;
	}

	/**
	 * If true, entity object would be serialized as well
	 * 
	 * @return the serializeEntity
	 */
	public boolean isSerializeEntity() {
		return serializeEntity;
	}

	/**
	 * @param serializeEntity the serializeEntity to set
	 */
	public void setSerializeEntity(boolean serializeEntity) {
		this.serializeEntity = serializeEntity;
	}

	/**
	 * @param element
	 * @param value
	 */
	private static <T extends Enum<T>> void serializeEnum(final Element element, final Enum<T> value) {
		element.addElement(ELM_ENUM).setText(value.name());
	}

	/**
	 * @param beanType
	 * @param element
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Object deserializeEnum(final Class beanType, final Element element) {
		return Enum.valueOf(beanType, element.elementText(ELM_ENUM));
	}

}
