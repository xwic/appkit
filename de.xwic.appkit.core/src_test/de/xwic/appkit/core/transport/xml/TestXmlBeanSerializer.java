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
