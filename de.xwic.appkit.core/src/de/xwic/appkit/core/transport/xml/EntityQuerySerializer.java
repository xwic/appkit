package de.xwic.appkit.core.transport.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import de.xwic.appkit.core.dao.EntityQuery;

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
		
		ByteArrayOutputStream baOut = new ByteArrayOutputStream();
		OutputStreamWriter writer = new OutputStreamWriter(baOut);
		
		try {
			Document doc = DocumentFactory.getInstance().createDocument();
			Element root = doc.addElement("query");
			new XmlBeanSerializer().serializeBean(root, "bean", query);
			
			OutputFormat prettyFormat = OutputFormat.createCompactFormat();//CompactFormat();//PrettyPrint();
			// this "hack" is required to preserve the LBCR's in strings... 
			XMLWriter xmlWriter = new XMLWriter(writer, prettyFormat) {
				public void write(Document arg0) throws IOException {
					//this.preserve = true;
					super.write(arg0);
				}
			};
			xmlWriter.write(doc);
			xmlWriter.flush();
			
		} catch (IOException e) {
			throw new TransportException("Unexpected IOException while serializing query.", e);
		}

		return baOut.toString();
	}
	
	


	/**
	 * Returns the EntityQuery serialized in the parameter string.
	 * @param queryString
	 * @return
	 */
	public static EntityQuery stringToQuery(String queryString) throws TransportException {
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(queryString.getBytes());
			SAXReader xmlReader = new SAXReader();
			Document doc = xmlReader.read(in);
			Element root = doc.getRootElement();
			return (EntityQuery)new XmlBeanSerializer().deserializeBean(root.element("bean"));
		} catch (DocumentException e) {
			throw new TransportException("Unexpected DocumentException while deseiralizing query.", e);
		}
	}
	
}
