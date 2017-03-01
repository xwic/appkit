/**
 * 
 */
package de.xwic.appkit.dev.engine.model;

import java.io.File;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import de.xwic.appkit.dev.engine.ConfigurationException;
import de.xwic.appkit.dev.engine.model.impl.XMLDataModel;

/**
 * @author lippisch
 *
 */
public class DataModelFactory {

	/**
	 * Create a DataModel representation from an XML file.
	 * @param file
	 * @return
	 * @throws ConfigurationException 
	 */
	public static DataModel createModel(File file) throws ConfigurationException {
	
		SAXReader reader = new SAXReader();
		try {
			Document document = reader.read(file);
			return new XMLDataModel(document);
		} catch (DocumentException e) {
			throw new ConfigurationException("Error reading model", e);
		}
		
	}
	
}
