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

import de.xwic.appkit.core.transport.xml.TestXmlBeanSerializer.Coffee.CoffeeSize;

/**
 * @author Alexandru Bledea
 * @since Oct 8, 2014
 */
@SuppressWarnings ("static-method")
public class TestXmlBeanSerializer {

	/**
	 * @throws Exception
	 */
	@Test
	public void testSerializeEnum() throws Exception {
		serializeAndDeserialize(Coffee.of(CoffeeSize.MEDIUM));
		serializeAndDeserialize(Coffee.of(CoffeeSize.SMALL));
		serializeAndDeserialize(Coffee.of(null));
	}

	/**
	 * @param coffee
	 * @throws TransportException
	 * @throws DocumentException
	 */
	private static void serializeAndDeserialize(final Coffee coffee) throws TransportException, DocumentException {
		final String serialized = XmlBeanSerializer.serializeToXML("main", coffee);
		final Document doc = new SAXReader().read(new StringReader(serialized));
		final Element root = doc.getRootElement().element("bean");

		final Object deserialized = new XmlBeanSerializer().deserializeBean(root);
		assertEquals(coffee.size, ((Coffee) deserialized).size);
	}

	/**
	 * @author Alexandru Bledea
	 * @since Oct 8, 2014
	 */
	static class Coffee {

		private CoffeeSize size;

		/**
		 * @param size the size to set
		 */
		public void setSize(final CoffeeSize size) {
			this.size = size;
		}

		/**
		 * @return the size
		 */
		public CoffeeSize getSize() {
			return size;
		}

		/**
		 * @param size
		 * @return
		 */
		static Coffee of(final CoffeeSize size) {
			final Coffee coffee = new Coffee();
			coffee.setSize(size);
			return coffee;
		}

		/**
		 * @author Alexandru Bledea
		 * @since Oct 8, 2014
		 */
		enum CoffeeSize {
			SMALL, MEDIUM, LARGE;
		}

	}

}
