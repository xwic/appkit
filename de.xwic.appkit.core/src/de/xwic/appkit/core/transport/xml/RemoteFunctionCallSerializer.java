package de.xwic.appkit.core.transport.xml;

import java.io.ByteArrayInputStream;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import de.xwic.appkit.core.dao.UseCase;
import de.xwic.appkit.core.remote.client.IRemoteFunctionCallConditions;

/**
 * @author Daniel Otto
 */
public class RemoteFunctionCallSerializer {


	/**
	 * @param conditions
	 * @return
	 * @throws TransportException
	 */
	public static String serialize(IRemoteFunctionCallConditions conditions) throws TransportException {
		return XmlBeanSerializer.serializeToXML("conditions", conditions);
	}
	
	/**
	 * @param conditions
	 * @return
	 * @throws TransportException
	 */
	public static IRemoteFunctionCallConditions deseralize(String conditions) throws TransportException {
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(conditions.getBytes());
			SAXReader xmlReader = new SAXReader();
			Document doc = xmlReader.read(in);
			Element root = doc.getRootElement();
			Object o =  new XmlBeanSerializer().deserializeBean(root.element("bean"));
			return (IRemoteFunctionCallConditions)o;
		} catch (DocumentException e) {
			throw new TransportException("Unexpected DocumentException while deseiralizing query.", e);
		}
	}
}
