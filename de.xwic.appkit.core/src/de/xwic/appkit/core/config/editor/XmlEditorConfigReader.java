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
package de.xwic.appkit.core.config.editor;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.config.ParseException;
import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.config.model.Model;
import de.xwic.appkit.core.config.model.Property;

/**
 * Reads the editor configuration from an xml document. The data is automaticaly
 * mapped to bean properties via reflection.
 *
 * @author Florian Lippisch
 */
public class XmlEditorConfigReader {

	private final static String NODE_TAB = "tab";
	private final static String NODE_SUBTAB = "subTab";
	private final static String NODE_LISTVIEW = "listView";
	private final static String NODE_AUTOFIELD = "autofield";
	private final static String NODE_COMBOBOX = "combobox";
	private final static String NODE_COMPOSITE = "composite";
	private final static String NODE_SUBTABS = "subTabs";
	private final static String NODE_FIELD = "field";
	private final static String NODE_MAIL = "mail";
	private final static String NODE_WEBADDR = "webaddr";
	private final static String NODE_ENTITY = "entity";
	private final static String NODE_GROUP = "group";
	private final static String NODE_LABEL = "label";
	private final static String NODE_TEXT = "text";
	private final static String NODE_PHONE_TEXT = "phone";
	private final static String NODE_CURRENCY_TEXT = "currency";
	private final static String NODE_PLCOMBO = "plCombo";
	private final static String NODE_PLCHECK = "plCheck";
	private final static String NODE_PLRADIO = "plRadio";
	private final static String NODE_NUMBER = "number";
	private final static String NODE_HTMLEDITOR = "htmleditor";
	private final static String NODE_DATE = "date";
	private final static String NODE_CHECKBOX = "checkbox";
	private final static String NODE_YESNORADIO = "yesNoRadio";
	private final static String NODE_CUSTOM = "custom";
	private final static String NODE_INFO = "info";
	private final static String NODE_IF = "if";
	private final static String NODE_DATE_RANGE = "dateRange";
	private final static String NODE_DATETIMERANGE = "dateTimeRange";

	private final static String NODE_ATTACHMENTS = "attachments";
	private final static String NODE_SINGLE_ATTACHMENT = "singleAttachment";

	private EditorConfiguration config = null;

	private Model model = null;

	private EntityDescriptor enRoot = null;

	private static Map<String, Class<? extends UIElement>> TYPE_MAP = new HashMap<String, Class<? extends UIElement>>();

	/*
         * Map a node to a field implementation. The nodename is used within the xml
         * files to specify such a field.
         */
	static {
		TYPE_MAP.put(NODE_YESNORADIO, EYesNoRadio.class);
		TYPE_MAP.put(NODE_TAB, ETab.class);
		TYPE_MAP.put(NODE_SUBTAB, ESubTab.class);
		TYPE_MAP.put(NODE_LISTVIEW, EListView.class);
		TYPE_MAP.put(NODE_AUTOFIELD, EAutofield.class);
		TYPE_MAP.put(NODE_COMBOBOX, ECombobox.class);
		TYPE_MAP.put(NODE_COMPOSITE, EComposite.class);
		TYPE_MAP.put(NODE_FIELD, EField.class);
		TYPE_MAP.put(NODE_MAIL, EMailField.class);
		TYPE_MAP.put(NODE_WEBADDR, EWebAddressField.class);
		TYPE_MAP.put(NODE_ENTITY, EEntityField.class);
		TYPE_MAP.put(NODE_PLCHECK, EPicklistCheckbox.class);
		TYPE_MAP.put(NODE_PLRADIO, EPicklistRadio.class);
		TYPE_MAP.put(NODE_GROUP, EGroup.class);
		TYPE_MAP.put(NODE_LABEL, ELabel.class);
		TYPE_MAP.put(NODE_TEXT, EText.class);
		TYPE_MAP.put(NODE_PHONE_TEXT, EPhoneText.class);
		TYPE_MAP.put(NODE_CURRENCY_TEXT, ECurrencyText.class);
		TYPE_MAP.put(NODE_PLCOMBO, EPicklistCombo.class);
		TYPE_MAP.put(NODE_NUMBER, ENumberInputField.class);
		TYPE_MAP.put(NODE_HTMLEDITOR, EHtmlEditor.class);
		TYPE_MAP.put(NODE_DATE, EDate.class);
		TYPE_MAP.put(NODE_CHECKBOX, ECheckbox.class);
		TYPE_MAP.put(NODE_CUSTOM, ECustom.class);
		TYPE_MAP.put(NODE_INFO, EInfoField.class);
		TYPE_MAP.put(NODE_IF, EIf.class);
		TYPE_MAP.put(NODE_DATE_RANGE, EDateRange.class);
		TYPE_MAP.put(NODE_DATETIMERANGE, EDateTimeRange.class);
		TYPE_MAP.put(NODE_SINGLE_ATTACHMENT, ESingleAttachmentField.class);
		TYPE_MAP.put(NODE_ATTACHMENTS, EAttachments.class);
	}

	/**
	 * Constructor.
	 *
	 * @param descriptor
	 */
	private XmlEditorConfigReader(Model model) {
		this.model = model;
	}

	/**
	 * Create/Read an editor configuration.
	 *
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static EditorConfiguration readConfiguration(Model model, URL file) throws ParseException {
		XmlEditorConfigReader instance = new XmlEditorConfigReader(model);

		try {
			InputStream in = file.openStream();
			DocumentBuilder dom = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = dom.parse(in);
			in.close();
			return instance.buildConfig(doc);
		} catch (Exception e) {
			throw new ParseException("Error parsing editor configuration. " + e, e);
		}
	}

	/**
	 * Create/Read an editor configuration.
	 *
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static EditorConfiguration readConfiguration(Model model, InputStream in) throws ParseException {
		XmlEditorConfigReader instance = new XmlEditorConfigReader(model);

		try {
			DocumentBuilder dom = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = dom.parse(in);
			in.close();
			return instance.buildConfig(doc);
		} catch (Exception e) {
			throw new ParseException("Error parsing editor configuration. " + e, e);
		}
	}

	/**
	 * Reads the configuration for the quicksearch panel in a list setup.
	 * @param model2
	 * @param listSetup
	 * @param node
	 * @throws ParseException
	 */
	public static EComposite readSimpleConfig(Model model, EntityDescriptor descriptor, Node node) throws ParseException {

		XmlEditorConfigReader instance = new XmlEditorConfigReader(model);
		return instance.buildSimpleConfig(descriptor, node);

	}

	/**
	 * @param node
	 * @return
	 * @throws ParseException
	 */
	private EComposite buildSimpleConfig(EntityDescriptor descriptor, Node root) throws ParseException {

		config = new EditorConfiguration();
		enRoot = descriptor;
		EComposite result = null;

		NodeList nl = root.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node node = nl.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				String name = element.getNodeName();
				if (name.equals("templates")) {
					readTemplates(element);
				} else if (name.equals("script")) {
					config.setGlobalScript(getNodeText(element));
				} else if (name.equals("layout")) {
					NodeList layoutNodes = node.getChildNodes();
					for (int a = 0; a < layoutNodes.getLength(); a++) {
						Node layoutNode = layoutNodes.item(a);
						if (layoutNode.getNodeType() == Node.ELEMENT_NODE) {
							Element layoutElement = (Element) layoutNode;
							if (layoutElement.getNodeName().equals(NODE_COMPOSITE)) {
								result = (EComposite)createUINode(layoutElement);
								break;
							}
						}
					}
				}
			}
		}

		return result;
	}

	/**
	 * @param document
	 * @return
	 * @throws ParseException
	 * @throws DOMException
	 */
	private EditorConfiguration buildConfig(Document document) throws DOMException, ParseException {

		config = new EditorConfiguration();
		Element root = document.getDocumentElement();

		NodeList nl = root.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node node = nl.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				String name = element.getNodeName();
				if (name.equals("id")) {
					config.setId(getNodeText(element));
				} else if (name.equals("entity")) {
					testType(getNodeText(element));
				} else if (name.equals("script")) {
					config.setGlobalScript(getNodeText(element));
				} else if (name.equals("templates")) {
					readTemplates(element);
				} else if (name.equals("layout")) {
					buildLayout(element);
				}
			}
		}

		return config;
	}

	private void buildSubTabs(Element subTabs) throws ParseException {

	}

	/**
	 * @param element
	 * @throws ParseException
	 */
	private void readTemplates(Element tplElement) throws ParseException {

		NodeList nl = tplElement.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node node = nl.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				if (element.getNodeName().equals("style")) {
					Style style = new Style();
					String id = element.getAttribute("id");
					if (id == null || id.length() == 0) {
						throw new ParseException("A style must have an id attribute.");
					}
					style.readStyles(getNodeText(element));
					config.addStyleTemplate(id, style);
				}
			}
		}

	}

	/**
	 * @param element
	 * @throws ParseException
	 */
	private void buildLayout(Element layout) throws ParseException {

		NodeList nl = layout.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node node = nl.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				if (element.getNodeName().equals(NODE_TAB)) {
					ETab tab = (ETab) createUINode(element);
					config.addTab(tab);
				}
                if (element.getNodeName().equals(NODE_SUBTABS)) {
                    Element subTabs = (Element) node;
                    NodeList subTabElement = subTabs.getChildNodes();
                    for (int subTabIndex = 0; subTabIndex < subTabElement.getLength(); subTabIndex++) {
                        Node subTabItem = subTabElement.item(subTabIndex);
                        if (subTabItem.getNodeType() == Node.ELEMENT_NODE) {
                            Element subTabItem1 = (Element) subTabItem;
                            if (subTabItem1.getNodeName().equals(NODE_SUBTAB)) {
                                ESubTab tab = (ESubTab) createUINode(subTabItem1);
                                config.addSubTab(tab);
                            }
                        }
                    }
                }
            }
		}
	}

	/**
	 * @param element
	 * @return
	 * @throws ParseException
	 */
	private UIElement createUINode(Element element) throws ParseException {

		Class<?> clazz = TYPE_MAP.get(element.getNodeName());
		if (clazz == null) {
			throw new ParseException("Unknown UIElement " + element.getNodeName());
		}

		// instanciate
		UIElement uiEl;
		try {
			uiEl = (UIElement) clazz.newInstance();
		} catch (Exception e) {
			throw new ParseException("Can not create UIElement instance: " + e);
		}

		// automap attributes
		NamedNodeMap map = element.getAttributes();
		for (int i = 0; i < map.getLength(); i++) {
			Node aNode = map.item(i);
			String attrName = aNode.getNodeName();
			String attrValue = aNode.getNodeValue();

			try {
				PropertyDescriptor pDesc = new PropertyDescriptor(attrName, clazz);
				Class<?> propType = pDesc.getPropertyType();
				Method mWrite = pDesc.getWriteMethod();
				Object value = attrValue;

				if (propType.isArray() && propType.getComponentType().equals(Property.class)) {
					StringTokenizer stk = new StringTokenizer(attrValue, ".");
					Property[] props = new Property[stk.countTokens()];
					EntityDescriptor enDescr = enRoot;
					for (int nr = 0; stk.hasMoreTokens(); nr++) {
						props[nr] = enDescr.getProperty(stk.nextToken());
						if (props[nr] == null) {
							throw new ParseException("The specified property '" + attrValue + "' does not exist" + (config.getEntityType() != null ? " (" + config.getEntityType().getClassname() + ")." : "."));
						}
						if (stk.hasMoreTokens()) {
							enDescr = model.getEntityDescriptor(props[nr].getDescriptor().getPropertyType().getName());
							if (enDescr == null) {
								throw new ParseException("The property '" + attrValue
										+ "' has a type in the path that is not a known entity ("
										+ props[nr].getDescriptor().getPropertyType().getName() + ")");
							}
						}
					}
					value = props;

				} else if (propType.equals(Style.class)) {
					// special property: Style
					if (attrName.equals("template")) {
						Style tplStyle = config.getStyleTemplate(attrValue);
						value = tplStyle;
					} else {
						Style style = new Style();
						style.readStyles(attrValue);
						value = style;
					}
				} else {
					PropertyEditor editor = PropertyEditorManager.findEditor(propType);
					if (editor != null) {
						editor.setAsText(attrValue);
						value = editor.getValue();
					}
				}

				mWrite.invoke(uiEl, value);

			} catch (ParseException pe) {
				throw pe;
			} catch (ConfigurationException ce) {
				throw new ParseException("Configuration/Setup error: " + ce.getMessage(), ce);
			} catch (IntrospectionException e) {
				throw new ParseException("The attribute '" + attrName + "' can not be mapped to class " + clazz.getName());
			} catch (Exception e2) {
				throw new ParseException("Error mapping the attribute '" + attrName + "' to class " + clazz.getName(), e2);
			}
		}

		// read childs, if there are any
		if (element.hasChildNodes()) {
			if (!(uiEl instanceof EComposite)) {
				throw new ParseException("Only composite-type elements can have child nodes. (" + element.getNodeName() + ")");
			}
			EComposite composite = (EComposite) uiEl;
			NodeList nl = element.getChildNodes();
			for (int i = 0; i < nl.getLength(); i++) {
				Node node = nl.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					UIElement childElement = createUINode((Element) node);
					composite.addElement(childElement);
				}
			}
		}

		return uiEl;
	}

	/**
	 * @param nodeValue
	 * @throws ParseException
	 */
	private void testType(String type) throws ParseException {

		enRoot = model.getEntityDescriptor(type);
		if (enRoot == null && type.indexOf('.') == -1) {
			// find matching type
			String search = "." + type;
			for (String enType : model.getManagedEntities()) {
				if (enType.endsWith(search)) {
					enRoot = model.getEntityDescriptor(enType);
					break;
				}
			}
		}
		if (enRoot == null) {
			throw new ParseException("Unknown EntityType.");
		}
		config.setEntityType(enRoot);

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
