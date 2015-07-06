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

import java.util.Map;

import org.dom4j.Element;

import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.dao.EntityKey;


/**
 * @author Adrian Ionescu
 */
public interface IEntityNodeParser {

	/**
	 * @param elmEntity
	 * @param context
	 * @param descr
	 * @param xmlBeanSerializer
	 * @return
	 * @throws TransportException
	 */
	public Object parseElement(Element elmEntity, Map<EntityKey, Integer> context, Class entityClass, EntityDescriptor descr, XmlBeanSerializer xmlBeanSerializer) throws TransportException; 
	
}
