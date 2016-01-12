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

import java.io.ByteArrayInputStream;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.dao.Limit;

/**
 * Serializes and deserializes an EntityQuery object.
 * @author Florian Lippisch
 */
public class EntityQuerySerializer {

	/**
	 * Returns the query as serialized string.
	 * @param query
	 * @return
	 * @throws TransportException 
	 */
	public static String queryToString(EntityQuery query) throws TransportException {
		return XmlBeanSerializer.serializeToXML("query", query);		
	}
	
	/**
	 * Returns the EntityQuery serialized in the parameter string.
	 * @param queryString
	 * @return
	 */
	public static EntityQuery stringToQuery(String queryString, boolean forceLoadCollection) throws TransportException {
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(queryString.getBytes());
			SAXReader xmlReader = new SAXReader();
			Document doc = xmlReader.read(in);
			Element root = doc.getRootElement();
			return (EntityQuery)new XmlBeanSerializer().deserializeBean(root.element("bean"), forceLoadCollection);
		} catch (DocumentException e) {
			throw new TransportException("Unexpected DocumentException while deseiralizing query.", e);
		}
	}
	
	/**
	 * @param limit
	 * @return
	 */
	public static String limitToString(Limit limit) {
		if (limit == null) {
			return "";
		}
		
		return limit.startNo + ";" + limit.maxResults;
	}
	
	/**
	 * @param strLimit
	 * @return
	 */
	public static Limit stringToLimit(String strLimit) {
		
		if (strLimit == null || strLimit.isEmpty()) {
			return null;
		}
		
		int separatorIndex = strLimit.indexOf(";");
		
		if (separatorIndex < 0) {
			throw new IllegalArgumentException("Cannot find ';' separator: " + strLimit);
		}
		
		String strStart = strLimit.substring(0, separatorIndex);
		String strMax = strLimit.substring(separatorIndex + 1);
		
		Limit limit = new Limit();
		try {
			limit.startNo = Integer.parseInt(strStart);
			limit.maxResults = Integer.parseInt(strMax);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Badly formatted string: " + strLimit);
		}
		
		return limit;
		
	}
}
