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
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.util.EntityUtil;
import de.xwic.appkit.core.remote.client.ETOSessionCache;
import de.xwic.appkit.core.remote.client.EntityProxyFactory;
import de.xwic.appkit.core.transfer.EntityTransferObject;
import de.xwic.appkit.core.util.Function;
import de.xwic.appkit.core.util.MapUtil;

/**
 * @author Alexandru Bledea
 * @since Jan 9, 2014
 */
public final class EtoSerializer {

	public static final String ETO_PROPERTY = "eto";

	private static final List<String> IGNORE_PROPERTIES = Collections.unmodifiableList(Arrays.asList("id", "class"));

	/**
	 * @param entityType
	 * @param eto
	 * @return
	 * @throws TransportException
	 */
	public static String serialize(final String entityType, final EntityTransferObject eto) throws TransportException {
		try {

			final EntityDescriptor descr = DAOSystem.getEntityDescriptor(entityType);
			final StringWriter sw = new StringWriter();
			final XmlEntityTransport xet = new XmlEntityTransport(ETOSessionCache.getInstance().getSessionCache());

			xet.write(sw, eto, descr);

			return sw.toString();

		} catch (IOException e) {
			throw new TransportException("Failed to serialize entity", e);
		} catch (ConfigurationException e) {
			throw new TransportException("Failed to serialize entity", e);
		}
	}

	/**
	 * @param serializedETO
	 * @return
	 * @throws DocumentException
	 * @throws TransportException
	 */
	public static EntityTransferObject deserialize(final String serializedETO) throws DocumentException, TransportException {
		if (serializedETO == null || serializedETO.isEmpty()) {
			throw new IllegalArgumentException("ETO details not specified");
		}

		SAXReader xmlReader = new SAXReader();
		Document doc = xmlReader.read(new StringReader(serializedETO));

		XmlEntityTransport xet = new XmlEntityTransport(ETOSessionCache.getInstance().getSessionCache());
		EntityList list = xet.createList(doc, null, new EtoEntityNodeParser());

		int size = list.size();
		if (size > 0) {
			if (size > 1) {
				LogFactory.getLog(EtoSerializer.class).warn("More than one ETO in list: " + serializedETO);
			}
			return (EntityTransferObject) list.get(0);
		}

		throw new IllegalStateException("ETO could not be parsed: " + serializedETO);
	}

	/**
	 * @param serializedETO
	 * @return
	 * @throws DocumentException
	 * @throws TransportException
	 */
	public static IEntity newEntity(final String serializedETO) {
		try {
			EntityTransferObject deserialize = deserialize(serializedETO);
			IEntity createEntityProxy = EntityProxyFactory.createEntityProxy(deserialize);
			Class<? extends IEntity> type = EntityUtil.type(deserialize.getEntityClass());
			return transferData(createEntityProxy, type);
		} catch (Exception e) {
			throw new IllegalStateException("Failed to transfer data", e);
		}
	}

	/**
	 * @param proxy
	 * @param to
	 * @param type
	 * @return
	 * @throws IntrospectionException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	private static IEntity transferData(final IEntity proxy, final Class<? extends IEntity> type) throws IntrospectionException,
			IllegalArgumentException, IllegalAccessException, InvocationTargetException {

		final IEntity result = EntityUtil.createEntity(type);
		Map<String, PropertyDescriptor> objectProp = getMap(result);
		Map<String, PropertyDescriptor> proxyProp = getMap(proxy);
		Set<String> allSet = new HashSet<String>(objectProp.keySet());
		allSet.addAll(proxyProp.keySet());
		allSet.removeAll(IGNORE_PROPERTIES);
		for (String prop : allSet) {
			if (proxyProp.containsKey(prop) && objectProp.containsKey(prop)) {
				Method readMethod = proxyProp.get(prop).getReadMethod();
				Method writeMethod = objectProp.get(prop).getWriteMethod();
				if (readMethod == null || writeMethod == null) {
					System.out.println("Illegal " + prop);
					continue;
				}
				writeMethod.invoke(result, readMethod.invoke(proxy));
			}
		}

		return result;
	}

	/**
	 * @param o
	 * @return
	 * @throws IntrospectionException
	 */
	private static Map<String, PropertyDescriptor> getMap(final Object o) throws IntrospectionException {
		final BeanInfo beanInfo = Introspector.getBeanInfo(o.getClass(), Introspector.USE_ALL_BEANINFO);
		final List<PropertyDescriptor> asList = Arrays.asList(beanInfo.getPropertyDescriptors());
		final Map<String, PropertyDescriptor> map = MapUtil.generateMap(asList, new Function<PropertyDescriptor, String>() {

			@Override
			public String evaluate(final PropertyDescriptor obj) {
				return obj.getName();
			}
		});
		return map;
	}

}
