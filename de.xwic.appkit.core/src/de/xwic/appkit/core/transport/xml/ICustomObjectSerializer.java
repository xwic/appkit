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

import org.dom4j.Element;

/**
 * Used to perform custom serialization and de-serialization of objects.
 * 
 * @author lippisch
 */
public interface ICustomObjectSerializer {

	/**
	 * Must return true if the handler can serialize this object.
	 * @param object
	 * @return
	 */
	public boolean handlesObject(Object object);
	
	/**
	 * Must return true if the handler can deserialize objects of this type.
	 * @param clazz
	 * @return
	 */
	public boolean handlesType(Class<?> clazz);
	
	/**
	 * Serialize an object.
	 * @param elm
	 * @param object
	 */
	public void serialize(Element elm, Object object) throws TransportException;
	
	/**
	 * Deserialize an object.
	 * @param elm
	 * @return
	 */
	public Object deserialize(Element elm) throws TransportException;
	
}
