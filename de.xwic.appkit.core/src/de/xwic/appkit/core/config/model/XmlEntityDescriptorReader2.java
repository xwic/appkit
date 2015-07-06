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
package de.xwic.appkit.core.config.model;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.DOMReader;

import de.xwic.appkit.core.config.ParseException;

/**
 * Reads the configuration out of an xml file to create an EntityDescriptor. <p>
 * 
 * This class can be used to make a normal creation of an Descriptor via
 * "autoload" the properties. <br>
 * Additionally, the Reader can be used for the generation of a new entity. 
 * Then the properties are read out of the xml file. 
 * 
 * @author Ronny Pfretzschner
 *
 */
public class XmlEntityDescriptorReader2 {
	
	private final static String ELEMENT_ENTITY_CLASS = "class";
	private final static String ELEMENT_ENTITIES = "entities";
	private final static String ELEMENT_ENTITY = "entity";
	private final static String ELEMENT_ENTITY_PROPERTIES = "properties";
	private final static String ELEMENT_ENTITY_REPLICATION_ORDER = "replicationOrder";
	private final static String ELEMENT_ENTITY_HISTORY = "history";
	private final static String ELEMENT_ENTITY_TRANSFORM = "transform";
	private final static String ELEMENT_ENTITY_TITLE = "title";

	private final static String ELEMENT_PROPERTY = "property";
	private final static String ELEMENT_PROPERTY_MAXLENGTH = "maxlength";
	private final static String ELEMENT_PROPERTY_REQUIERED = "requiered";
	private final static String ELEMENT_PROPERTY_PICKLIST_ID = "picklistId";
	private final static String ELEMENT_PROPERTY_ON_DELETION = "onDeletion";
	private final static String ELEMENT_PROPERTY_COLLECTION_ELEMENT_TYPE = "elementType";
	private final static String ELEMENT_PROPERTY_DEFAULT_SEARCH = "defaultSearch";
	
	private final static String NAME = "name";
	private final static String TYPE = "type";
	private final static String COLLECTION = "collection";
	private final static String HIDDEN = "hidden";
	private final static String LAZY = "lazy";
	private final static String AUTOLOAD_FLAG = "autoDetect";
	
	private final static String ACTION_DENY = "deny";
	private final static String ACTION_DELETE = "delete";
	private final static String ACTION_CLEAR = "clear";
	
	private final static String BOOL_TRUE = "true";
	private final static String FILE_TYPE = "file";

	private DocumentBuilder dom = null;
	private Document document = null;
	private Class<?> entityClass = null; // FLI: store the class instead the name -> faster
	private EntityDescriptor ed;

	private static ClassLoader classLoader = null;
	
	private boolean autoLoadProperties = false;
	private boolean generate = false;
	
	/**
	 * Constructor.
	 */
	private XmlEntityDescriptorReader2(boolean generate) {
		this.generate = generate;
		if (classLoader == null) {
			classLoader = getClass().getClassLoader();
		}
	}
	
	/**
	 * Load an EntityDescriptor from a location. <p>
	 * 
	 * If the Loading is used to generate a new entity, set the
	 * generate flag to true. <br>
	 * 
	 * @param location
	 * @param set this flag to false, if started from generator
	 * @return an EntityDescriptor
	 * 
	 * @throws ParseException
	 */
	public static EntityDescriptor loadEntityDescriptor(URL location, boolean generate) throws ParseException {
		XmlEntityDescriptorReader2 reader = new XmlEntityDescriptorReader2(generate);
		return reader.load(location);
	}
	
	/**
	 * Hack.
	 * @param loader
	 */
	public static void setClassLoader(ClassLoader loader) {
		classLoader = loader;
	}

	private EntityDescriptor load(URL location) throws ParseException {
		try {
			dom = DocumentBuilderFactory.newInstance().newDocumentBuilder();			
			InputStream in = location.openStream();
			org.w3c.dom.Document w3cDoc = (org.w3c.dom.Document) dom.parse(in);
			in.close();
			DOMReader reader = new DOMReader();
			document = reader.read(w3cDoc);
			walk(document.getRootElement());
			return ed;
		} catch(Exception e) {
			throw new ParseException("Error parsing configuration: " + e, e);
		}
	}

	/**
	 * Walks the DOM tree, getting the properties and adding them to a 
	 * EntityDescriptor
	 * 
	 * @param node
	 * @throws IntrospectionException
	 * @throws ClassNotFoundException
	 */
	private void walk(Node node) throws IntrospectionException, ClassNotFoundException, ParseException {
		
		int type = node.getNodeType();

		if (type == Node.ELEMENT_NODE) {
			Element element = (Element) node;

			//root element, get in deeper, 
			if (ELEMENT_ENTITIES.equals(element.getName())) {
				List<?> childs = element.elements();
				for (Iterator<?> iter = childs.iterator(); iter.hasNext();) {
					Element child = (Element) iter.next();
					walk(child);
				}
			}
			
			//entity entry is coming -> read data
			else if (ELEMENT_ENTITY.equals(node.getName())) {
				//the className of the entity
				String className = element.element(ELEMENT_ENTITY_CLASS).getTextTrim();
				
				//added properties of the entity
				Element properties = element.element(ELEMENT_ENTITY_PROPERTIES);
				
				//check autoload flag set in file
				if (properties != null) {
					Attribute auto = properties.attribute(AUTOLOAD_FLAG);
					//autoload means, read the properties via reflection,
					//this does not work if generate is true
					if (auto != null) {
						autoLoadProperties = BOOL_TRUE.equals(auto.getText()); 
					}
				}
				
				//generate, no class is existing -> create empty EntityDescriptor and set properties
				if (generate) {
					ed = new EntityDescriptor();
					ed.setClassname(className);
					ed.setProperties(new HashMap<String, Property>());
				}
				//no generate -> get the class, maybe autoload properties is true ;)
				else if (!generate){
					entityClass = classLoader != null ? Class.forName(className, true, classLoader) : Class.forName(className);
					ed = new EntityDescriptor(entityClass);
				}
				
				//set properties
				ed.setId(className);
				
				if (element.element(ELEMENT_ENTITY_HISTORY) != null) {
					ed.setHistory(BOOL_TRUE.equals(element.element(ELEMENT_ENTITY_HISTORY).getTextTrim()));
				}
				
				if (element.element(HIDDEN) != null) {
					ed.setHidden(BOOL_TRUE.equals(element.element(HIDDEN).getTextTrim()));
				}
				
				if (element.element(ELEMENT_ENTITY_TITLE) != null) {
					ed.setTitlePattern(element.element(ELEMENT_ENTITY_TITLE).getTextTrim());
				}
				
				if (element.element(ELEMENT_ENTITY_TRANSFORM) != null) {
					ed.setTransform(BOOL_TRUE.equals(element.element(ELEMENT_ENTITY_TRANSFORM).getTextTrim()));
				}

				if (element.element(ELEMENT_ENTITY_REPLICATION_ORDER) != null) {
					String repOrderEl = element.element(ELEMENT_ENTITY_REPLICATION_ORDER).getTextTrim();
					if (repOrderEl != null && repOrderEl.length() > 0) {
						ed.setReplicationOrder(Integer.parseInt(repOrderEl));
					}
				}
				
				//generate and not autoload means read the properties defined in the xml file
				//start method again and stop on next if :(
				if (generate || !autoLoadProperties) {
					List<?> childs = properties.elements();
					//check childs
					for (Iterator<?> iter = childs.iterator(); iter.hasNext();) {
						Element child = (Element) iter.next();
						walk(child);
					}
				}
			} 
			
			//load property
			else if (ELEMENT_PROPERTY.equals(element.getName())) {
				loadProperty(element, ed);
			}
		}
	}
	

	/**
	 * Loads the property of the given property element. <p>
	 * 
	 * @param propElem The node from the xml file
	 * @param desc the EntityDescriptor to store the data
	 * 
	 * @throws IntrospectionException
	 * @throws ParseException
	 */
	private void loadProperty(Element propElem, EntityDescriptor desc) throws IntrospectionException, ParseException{
		
		//something went wrong -> nothing is coming -> get out to make
		//continue possible
		if (propElem == null || desc == null) {
			return;
		}
		
		String propertyName = propElem.attributeValue(NAME);
		String javaType = propElem.attributeValue(TYPE);
		
		boolean isCollection = false;
		Attribute collAttr = propElem.attribute(COLLECTION);
		
		if (collAttr != null) {
			isCollection = BOOL_TRUE.equals(collAttr.getText());
		}
		
		Property property = ed.getProperty(propertyName); // FLI: check if there is a property 
		if (property == null) {
			//-> no generate, the entityClass is set properly
			if (!generate) {
				PropertyDescriptor pDesc = new PropertyDescriptor(propertyName, entityClass); // use the stored class
				property = new Property(pDesc);
			} 
			//generate -> create property without the propertydescriptor
			else {
				property = new Property();
				property.setName(propertyName);
				property.setEntityType(javaType);
			}
			property.setEntityDescriptor(ed);
			ed.getProperties().put(propertyName, property); // store the property
		}

		//"file" is set in type -> that means the value is a file ID -> set flag
		if (FILE_TYPE.equals(javaType)) {
			property.setFileType(true);
		}
		
		//collection is coming...
		if (isCollection) {
			Element collTypElem = propElem.element(ELEMENT_PROPERTY_COLLECTION_ELEMENT_TYPE);
			
			//get the type of the entity within the collection
			if (collTypElem != null) {
				String collType = collTypElem.getTextTrim();
				
				//set flags and type only, if type is not null
				//isCollection cannot be set without the proper type
				property.setCollection(true);
				property.setCollectionEntityType(collType);
			}
			else {
				throw new ParseException("Invalid collection argument for property " + propertyName + ". The collection type is missing!");
			}
		}
		
		//maxlength 
		if (null != propElem.element(ELEMENT_PROPERTY_MAXLENGTH)) {
			String maxLengthStr = propElem.element(ELEMENT_PROPERTY_MAXLENGTH).getTextTrim();
			if (maxLengthStr != null && maxLengthStr.length() > 0) {
				int maxLength = Integer.parseInt(maxLengthStr);
				property.setMaxLength(maxLength);
			}
		}

		if (null != propElem.element(ELEMENT_PROPERTY_REQUIERED)) {
			String temp = propElem.element(ELEMENT_PROPERTY_REQUIERED).getTextTrim();
			boolean required = BOOL_TRUE.equals(temp);
			property.setRequired(required);
		}

		if (null != propElem.attribute(HIDDEN)) {
			String temp = propElem.attributeValue(HIDDEN);
			boolean hidden = BOOL_TRUE.equals(temp);
			property.setHidden(hidden);
		}

		if (null != propElem.attribute(LAZY)) {
			String temp = propElem.attributeValue(LAZY);
			boolean lazy = BOOL_TRUE.equals(temp);
			property.setLazy(lazy);
		}

		//get the deletion type
		if (null != propElem.element(ELEMENT_PROPERTY_ON_DELETION)) {
			String action = propElem.element(ELEMENT_PROPERTY_ON_DELETION).getTextTrim();
			if (ACTION_DENY.equalsIgnoreCase(action)) {
				property.setOnRefDeletion(Property.DENY);
			} else if (ACTION_DELETE.equalsIgnoreCase(action)) {
				property.setOnRefDeletion(Property.DELETE);
			} else if (ACTION_CLEAR.equalsIgnoreCase(action)) {
				property.setOnRefDeletion(Property.CLEAR_REFERENCE);
			} else {
				throw new ParseException("Invalid onRefDelete argument: " + action + " for property " + propertyName);
			}
		}

		if (null != propElem.element(ELEMENT_PROPERTY_PICKLIST_ID)) { 
			String picklistID = propElem.element(ELEMENT_PROPERTY_PICKLIST_ID).getTextTrim();
			if (picklistID != null && picklistID.length() > 0) {
				property.setPicklistId(picklistID);
			}
		}
		
		if (null != propElem.element(ELEMENT_PROPERTY_DEFAULT_SEARCH)) { 
			String bool = propElem.element(ELEMENT_PROPERTY_DEFAULT_SEARCH).getTextTrim();
			if (bool != null && bool.length() > 0) {
				//property.setDefaultSearch("true".equals(bool));
			}
		}
	}

	/**
	 * @return the classLoader
	 */
	public static ClassLoader getClassLoader() {
		return classLoader;
	}
}
