package de.xwic.appkit.core.config.model;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import de.xwic.appkit.core.config.ParseException;

/**
 * Class for reading properties from a XML Property type file.
 * 
 * @author Aron Cotrau
 * @author Florian Lippisch
 */
public class XmlEntityDescriptorReader {

	private DocumentBuilder dom = null;
	private Document document = null;
	private Class<?> entityClass = null; // FLI: store the class instead the name -> faster
	private boolean autoLoadProperties = true;
	private EntityDescriptor ed;
	private Property property = null;
	private DefaultPicklistEntry dpentry = null;

	private static ClassLoader classLoader = null;
	
	//public PropertiesXMLParser(EntityDescriptor ed) {
	// FLI -> no descriptor needed.
	
	/**
	 * Constructor.
	 */
	private XmlEntityDescriptorReader() {
		// this.ed = ed;
		// map = new HashMap();
		if (classLoader == null) {
			classLoader = getClass().getClassLoader();
		}
	}
	
	/**
	 * Load an EntityDescriptor from a location.
	 * @param location
	 * @return
	 * @throws ParseException
	 */
	public static EntityDescriptor loadEntityDescriptor(URL location) throws ParseException {
		XmlEntityDescriptorReader reader = new XmlEntityDescriptorReader();
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
			document = (Document) dom.parse(in);
			in.close();
			walk(document);
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

			if (node.getNodeName().equals("entity")) {
				String className = element.getAttribute("class");
				entityClass = classLoader != null ? Class.forName(className, true, classLoader) : Class.forName(className);
				autoLoadProperties = element.getAttribute("autoLoadProperties").equals("true");
				if (ed == null) {
					if (autoLoadProperties) {
						ed = new EntityDescriptor(entityClass);
					} else {
						ed = new EntityDescriptor();
						ed.setClassname(className);
						ed.setProperties(new HashMap<String, Property>());
					}
					ed.setId(className);
					ed.setHistory("true".equals(element.getAttribute("history")));
					ed.setHidden("true".equals(element.getAttribute("hidden")));
					ed.setMonitorable("true".equals(element.getAttribute("monitorable")));
					if (element.hasAttribute("transform")) {
						ed.setTransform(!"false".equals(element.getAttribute("transform")));
					}
					if (element.hasAttribute("replicationOrder")) {
						ed.setReplicationOrder(Integer.parseInt(element.getAttribute("replicationOrder")));
					}
					String s = element.getAttribute("defaultDisplayProperty");
					if (s != null && !s.isEmpty()) {
						ed.setDefaultDisplayProperty(s);
					}
				}
			
			} else if (node.getNodeName().equals("title")) {
				if (ed != null) {
					ed.setTitlePattern(getNodeText(node));
				}
			
			} else if (node.getNodeName().equals("property")) {
				NamedNodeMap map = node.getAttributes();
				String propertyName = element.getAttribute("name");

				property = ed.getProperty(propertyName); // FLI: check if there is a property 
				if (property == null) {
					PropertyDescriptor desc = new PropertyDescriptor(propertyName, entityClass); // use the stored class
					property = new Property(desc);
					property.setEntityDescriptor(ed);
					ed.getProperties().put(propertyName, property); // store the property
				}

				if (null != map.getNamedItem("maxLength")) {
					int maxLength = Integer.parseInt(element.getAttribute("maxLength"));
					property.setMaxLength(maxLength);
				}

				if (null != map.getNamedItem("required")) {
					boolean required = element.getAttribute("required").equals("true");
					property.setRequired(required);
				}

				if (null != map.getNamedItem("lazy")) {
					boolean lazy = element.getAttribute("lazy").equals("true");
					property.setLazy(lazy);
				}

				if (null != map.getNamedItem("hidden")) {
					boolean hidden = element.getAttribute("hidden").equals("true");
					property.setHidden(hidden);
				}

				if (null != map.getNamedItem("embeddedEntity")) {
					boolean embeddedEntity = element.getAttribute("embeddedEntity").equals("true");
					property.setEmbeddedEntity(embeddedEntity);
				}

				if (null != map.getNamedItem("isFileType")) {
					boolean fileType = element.getAttribute("isFileType").equals("true");
					property.setFileType(fileType);
				}
				
				if (null != map.getNamedItem("defaultSearch")) {
					//boolean defaultSearch = element.getAttribute("defaultSearch").equals("true");
					//property.setDefaultSearch(defaultSearch);
				}

				if (null != map.getNamedItem("onRefDelete")) {
					String action = element.getAttribute("onRefDelete");
					if (action.equalsIgnoreCase("deny")) {
						property.setOnRefDeletion(Property.DENY);
					} else if (action.equalsIgnoreCase("delete")) {
						property.setOnRefDeletion(Property.DELETE);
					} else if (action.equalsIgnoreCase("clear")) {
						property.setOnRefDeletion(Property.CLEAR_REFERENCE);
					} else if (action.equalsIgnoreCase("ignore")) {
						property.setOnRefDeletion(Property.IGNORE);
					} else {
						throw new ParseException("Invalid onRefDelete argument: " + action + " for property " + propertyName);
					}
				}

				
				if (null != map.getNamedItem("picklistId")) property.setPicklistId(element.getAttribute("picklistId"));
				
				if (null != map.getNamedItem("datetype")) {
					String sDateType = element.getAttribute("datetype");
					if (sDateType.equals("datetime")) {
						property.setDateType(Property.DATETYPE_DATETIME);
					} else if (sDateType.equals("date")) {
						property.setDateType(Property.DATETYPE_DATE);
					} else if (sDateType.equals("time")) {
						property.setDateType(Property.DATETYPE_TIME);
					}
				}
				
				// this.map.put(propertyName, property);
			} else if (node.getNodeName().equals("picklistentry")) {
				if (property != null) {
					dpentry = new DefaultPicklistEntry();
					property.addDefaultPicklistEntry(dpentry);
					dpentry.setKey(element.hasAttribute("key") ? element.getAttribute("key") : null);
					dpentry.setIndex(element.hasAttribute("index") ? Integer.parseInt(element.getAttribute("index")) : 0);
				}
			} else if (node.getNodeName().equals("name")) {
				if (dpentry != null && element.hasAttribute("lang")) {
					dpentry.addTitle(element.getAttribute("lang"), getNodeText(element));
				}
			}
		}
		for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
			walk(child);
		}
	}
	
	/**
	 * Returns the text value of an XML node element
	 * @return java.lang.String
	 * @param node org.w3c.dom.Node
	 */
	private String getNodeText(org.w3c.dom.Node node) {

		StringBuffer sb = new StringBuffer();
		org.w3c.dom.NodeList children = node.getChildNodes();
		if ( children != null ) {
			int len = children.getLength();
			for ( int i = 0; i < len; i++ ) {
				sb.append(children.item(i).getNodeValue());	
			}
		}

		return sb.toString();
	}

	/**
	 * @return the classLoader
	 */
	public static ClassLoader getClassLoader() {
		return classLoader;
	}

	
}
