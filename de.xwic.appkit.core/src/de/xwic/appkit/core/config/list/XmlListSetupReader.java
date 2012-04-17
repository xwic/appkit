package de.xwic.appkit.core.config.list;

import java.io.InputStream;
import java.net.URL;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import de.xwic.appkit.core.config.ParseException;
import de.xwic.appkit.core.config.Setup;
import de.xwic.appkit.core.config.editor.XmlEditorConfigReader;
import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.config.model.Model;
import de.xwic.appkit.core.config.model.Property;

/**
 * Class for reading ListSetup configuration from an XML file.
 * @author Aron
 */
public class XmlListSetupReader  {

	private DocumentBuilder dom;
	private Document doc;
	private ListSetup listSetup = new ListSetup();
	private Model model = null;
	private EntityDescriptor rootEd = null;
	private int defaultSort = -1;

	/**
	 * Constructor.
	 */
	private XmlListSetupReader(Model model) {
		this.model = model;
	}
	
	/**
	 * Load a ListSetup configuration from an url
	 * @param location
	 * @return
	 * @throws ParseException 
	 */
	public static ListSetup loadListSetup(Model model, URL location) throws ParseException {
		XmlListSetupReader reader = new XmlListSetupReader(model);
		reader.loadListSetupInternal(location);
		return reader.listSetup;
	}

	/**
	 * Load the setup.
	 * @param location
	 * @throws ParseException 
	 */
	private void loadListSetupInternal(URL location) throws ParseException {
		try {
			InputStream in = location.openStream();
			dom = DocumentBuilderFactory.newInstance().newDocumentBuilder();			
			doc = (Document) dom.parse(in);
			in.close();
			walk(doc);
		} catch (Exception e) {
			throw new ParseException("Error parsing listsetup (" + location.toExternalForm() + "): " + e, e);
		}
	}

	/**
	 * walk the document.
	 * @param node
	 * @throws ParseException 
	 */
	private void walk(Node node) throws ParseException {
		int type = node.getNodeType();
		
		if (type == Node.ELEMENT_NODE) {
			if (node.getNodeName().equals("column")) {
				Element element = (Element)node;
				String title = element.getAttribute("title");
				String id = element.getAttribute("id");
				ListColumn currColumn = new ListColumn(title, id);
				// resolve id into property object
				if (!id.startsWith("#")) {
					StringTokenizer stk = new StringTokenizer(id, ".");
					Property[] props = new Property[stk.countTokens()];
					EntityDescriptor enDescr = rootEd;
					for (int nr = 0; stk.hasMoreTokens(); nr++) {
						props[nr] = enDescr.getProperty(stk.nextToken());
						if (props[nr] == null) {
							// let it flow
							return;
							//throw new ParseException("The specified property '" + id + "' does not exist.");
						}
						if (stk.hasMoreTokens()) {
							enDescr = model.getEntityDescriptor(props[nr].getDescriptor().getPropertyType().getName());
							if (enDescr == null) {
								// let it flow
								return;
								//throw new ParseException("The property '" + id + "' has a type in the path that is not a known entity (" + props[nr].getDescriptor().getPropertyType().getName() + ")");
							}
						}
					}
					currColumn.setProperty(props);
				}
				
				String width = element.getAttribute("width");
				int sort = getSortMethod(element.getAttribute("sort"));
				String labelProvider = element.getAttribute("labelProvider");
				currColumn.setDefaultVisible("1".equals(element.getAttribute("default")));
				if (width != null && width.length() != 0) {
					currColumn.setDefaultWidth(Integer.parseInt(width));
				}
				if (labelProvider != null && labelProvider.length() != 0) {
					currColumn.setCustomLabelProviderClass(labelProvider);
				}
				if (sort != -1) {
					currColumn.setSortMode(sort);
				} else {
					if (defaultSort != -1) {
						currColumn.setSortMode(defaultSort);
					}
				}
				listSetup.addColumn(currColumn);
				
			} else if (node.getNodeName().equals("table")) {
				Element element = (Element)node;
				int sort = getSortMethod(element.getAttribute("default-sort"));
				if (sort != -1) {
					defaultSort = sort;
				} else {
					defaultSort = ListColumn.SORT_CLIENT;
				}
				String typeClass = element.getAttribute("type");
				if (typeClass == null) {
					throw new ParseException("Must specify the entity type!");
				}
				rootEd = model.getEntityDescriptor(typeClass);
				if (rootEd == null) {
					throw new ParseException("The specified type is not known to the model: " + typeClass);
				}
				listSetup.setEntityDescriptor(rootEd);
				listSetup.setQueryEntities(!"false".equals(element.getAttribute("queryEntities")));
				listSetup.setTypeClass(typeClass);
				String id = element.getAttribute("id");
				listSetup.setListId(id == null ? Setup.ID_DEFAULT : id);
			
				listSetup.setExtendsID(element.getAttribute("extends"));
			} else if (node.getNodeName().equals("quicksearch")) {
				// build the quickSearch composite
				listSetup.setQuickSearchComposite(
						XmlEditorConfigReader.readSimpleConfig(model, listSetup.getEntityDescriptor(), node)
				);
				
				//look for the related base entity attribute name
				NamedNodeMap attrMap = node.getAttributes();
				if (attrMap != null) {
					Node attr = attrMap.getNamedItem("baseEntityAttribute");
					if (attr != null) {
						String attrStr = attr.getNodeValue();
						//could be null, but is okay
						listSetup.setBaseTypeAttribute(attrStr);
					}
				}
			} else if (node.getNodeName().equals("action")) {
				listSetup.addAction(getNodeText(node).trim());
			} else if (node.getNodeName().equals("defaultSearch")) {
				Element element = (Element) node;
				String props = element.getAttribute("properties");
				listSetup.setDefaultSearchProperties(props);
			}
		}
		for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
			walk(child);
		}
	}
	
	/**
	 * Translate sort parameter into int.
	 * @param string
	 * @return
	 */
	private int getSortMethod(String string) {
		if ("none".equalsIgnoreCase(string)) {
			return ListColumn.SORT_NONE;
		} else
		if ("server".equalsIgnoreCase(string)) {
			return ListColumn.SORT_SERVER;
		} else
		if ("client".equalsIgnoreCase(string)) {
			return ListColumn.SORT_CLIENT;
		} 
		return -1;
	}
	
	/**
	 * Returns the text value of an XML node element
	 * 
	 * @return java.lang.String
	 * @param node
	 *            org.w3c.dom.Node
	 */
	private String getNodeText(org.w3c.dom.Node node) {

		StringBuffer sb = new StringBuffer();
		org.w3c.dom.NodeList children = node.getChildNodes();
		if (children != null) {
			int len = children.getLength();
			for (int i = 0; i < len; i++) {
				sb.append(children.item(i).getNodeValue());
			}
		}

		return sb.toString();
	}

}