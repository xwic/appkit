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

import static org.junit.Assert.assertEquals;

import java.io.StringReader;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;

import de.xwic.appkit.core.transport.xml.SimpleClassWithEnum.SimpleClassWithEnumEnum;

/**
 * @author Alexandru Bledea
 * @since Oct 8, 2014
 */
@SuppressWarnings ("static-method")
public final class TestXmlBeanSerializer {

	/**
	 * @throws Exception
	 */
	@Test
	public void testSerializeEnum() throws Exception {
		serializeAndDeserialize(SimpleClassWithEnum.of(SimpleClassWithEnumEnum.MEDIUM));
		serializeAndDeserialize(SimpleClassWithEnum.of(SimpleClassWithEnumEnum.SMALL));
		serializeAndDeserialize(SimpleClassWithEnum.of(null));
	}

	/**
	 * @throws DocumentException
	 * @throws TransportException
	 */
	@Test
	public void testSerializeMap() throws TransportException, DocumentException {
		serializeAndDeserialize(SimpleClassWithMap.of("shoe", 12));
		serializeAndDeserialize(SimpleClassWithMap.of(null, null));
		serializeAndDeserialize(SimpleClassWithMap.of(null));
	}

	/**
	 * @param enumClass
	 * @throws TransportException
	 * @throws DocumentException
	 */
	private static void serializeAndDeserialize(final Object what) throws TransportException, DocumentException {
		final String serialized = XmlBeanSerializer.serializeToXML("main", what);
		final Document doc = new SAXReader().read(new StringReader(serialized));
		final Element root = doc.getRootElement().element("bean");

		final Object deserialized = new XmlBeanSerializer().deserializeBean(root);
		assertEquals(what, deserialized);
	}



}
