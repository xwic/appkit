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
package de.pol.platform.transport.xml;

import junit.framework.TestCase;
import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.core.transport.xml.EntityQuerySerializer;
import de.xwic.appkit.core.transport.xml.TransportException;

/**
 * @author Florian Lippisch
 *
 */
public class EntityQuerySerializerTest extends TestCase {

	public void testSerialize() throws TransportException {
		PropertyQuery query = new PropertyQuery();
		query.addEquals("name", "Abc");
		query.addLeftOuterJoinProperty("kontakt");
		PropertyQuery sub = new PropertyQuery();
		sub.addOrEquals("ho", new Integer(1));
		query.addSubQuery(sub);
		
		String data = EntityQuerySerializer.queryToString(query);
		
		System.out.println(data);
		System.out.println("size: " + data.length());
		
		EntityQuery q2 = EntityQuerySerializer.stringToQuery(data);
		System.out.println(query);
		System.out.println(q2);
		
		assertEquals(query, q2);
		
	}
	
}
