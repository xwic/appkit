/**
 *
 */
package de.xwic.appkit.core.transport.xml;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.transfer.EntityTransferObject;

/**
 * @author Alexandru Bledea
 * @since Jan 9, 2014
 */
public final class EtoSerializer {

	/**
	 * @param entityType
	 * @param eto
	 * @return
	 * @throws IOException
	 * @throws ConfigurationException
	 */
	public static String serialize(final String entityType, final EntityTransferObject eto) throws IOException, ConfigurationException {
		final EntityDescriptor descr = DAOSystem.getEntityDescriptor(entityType);
		final StringWriter sw = new StringWriter();
		final XmlEntityTransport xet = new XmlEntityTransport();
		xet.write(sw, eto, descr);
		return sw.toString();
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

		XmlEntityTransport xet = new XmlEntityTransport();
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

}
