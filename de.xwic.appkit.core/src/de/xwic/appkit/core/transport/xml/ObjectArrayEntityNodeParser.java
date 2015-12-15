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

import java.util.Iterator;
import java.util.Map;

import org.dom4j.Element;

import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.dao.EntityKey;
import de.xwic.appkit.core.transfer.EntityTransferObject;


/**
 * @author Adrian Ionescu
 */
public class ObjectArrayEntityNodeParser implements IEntityNodeParser {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.transport.xml.IEntityNodeParser#parseElement(org.dom4j.Element, java.util.Map, de.xwic.appkit.core.config.model.EntityDescriptor, de.xwic.appkit.core.transport.xml.XmlBeanSerializer)
	 */
	@Override
	public Object parseElement(Element elmEntity, Map<EntityKey, Integer> context, Class entityClass, EntityDescriptor descr, XmlBeanSerializer xmlBeanSerializer, Map<EntityKey, EntityTransferObject> sessionCache, boolean forceLoadCollection) throws TransportException {
		
		Object[] result = new Object[elmEntity.elements().size()];
		int i = 0;
		
		for (Iterator<Element> it = elmEntity.elementIterator() ; it.hasNext() ; ) {
			Element elm = it.next();
			result[i++] = xmlBeanSerializer.readValue(null, elm, null, forceLoadCollection);
		}
		
		return result;
	}

}
