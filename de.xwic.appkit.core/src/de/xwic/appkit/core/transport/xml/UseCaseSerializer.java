package de.xwic.appkit.core.transport.xml;

import java.io.ByteArrayInputStream;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import de.xwic.appkit.core.dao.UseCase;

/**
 * @author Adrian Ionescu
 */
public class UseCaseSerializer {

	/**
	 * @param useCase
	 * @return
	 * @throws TransportException
	 */
	public static String serialize(UseCase useCase) throws TransportException {
		return XmlBeanSerializer.serializeToXML("usecase", useCase);
	}
	
	/**
	 * @param strUseCase
	 * @return
	 * @throws TransportException
	 */
	public static UseCase deseralize(String strUseCase) throws TransportException {
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(strUseCase.getBytes());
			SAXReader xmlReader = new SAXReader();
			Document doc = xmlReader.read(in);
			Element root = doc.getRootElement();
			return (UseCase) new XmlBeanSerializer().deserializeBean(root.element("bean"));
		} catch (DocumentException e) {
			throw new TransportException("Unexpected DocumentException while deseiralizing query.", e);
		}
	}
}
