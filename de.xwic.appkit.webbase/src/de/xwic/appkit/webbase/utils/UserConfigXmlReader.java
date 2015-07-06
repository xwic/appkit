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
package de.xwic.appkit.webbase.utils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.config.ParseException;
import de.xwic.appkit.core.config.Setup;
import de.xwic.appkit.webbase.core.Platform;
import de.xwic.appkit.webbase.prefstore.IPreferenceStore;
import de.xwic.appkit.webbase.viewer.columns.UserListColumn;
import de.xwic.appkit.webbase.viewer.columns.UserListSetup;
import de.xwic.appkit.webbase.views.BaseBrowserView;

/**
 * class that reads/writes data for the user configurations in/from XML files
 * 
 * @author Aron Cotrau
 */
public class UserConfigXmlReader {

	private DocumentBuilder dom;
	private Document doc;

	private UserListSetup listSetup = null;

	/**
	 * default constructor
	 */
	private UserConfigXmlReader() {
		listSetup = new UserListSetup();
	}

	/**
	 * reads the columns from the given XML file
	 * 
	 * @param pXmlContent
	 * @param pClassName
	 * @param pListSetupId
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	static public UserListSetup getUserColumnList(String pXmlContent) throws ParseException {
		if (null != pXmlContent && !"".equals(pXmlContent)) {
			UserConfigXmlReader reader = new UserConfigXmlReader();
			reader.readUserColumnListInternal(pXmlContent);

			return reader.listSetup;
		}

		return null;
	}

	/**
	 * @param pXmlFile
	 */
	private void readUserColumnListInternal(String pXmlFile) throws ParseException {
		StringReader stringReader = null;

		try {
			dom = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			stringReader = new StringReader(pXmlFile);
			InputSource inputSource = new InputSource(stringReader);
			doc = (Document) dom.parse(inputSource);
			walk(doc);
		} catch (Exception e) {
			throw new ParseException("Error parsing listsetup: " + e, e);
		} finally {
			if (null != stringReader) {
				stringReader.close();
			}
		}
	}

	/**
	 * @param node
	 * @throws ConfigurationException
	 */
	private void walk(Node node) throws ParseException, ConfigurationException {
		int type = node.getNodeType();

		if (type == Node.ELEMENT_NODE) {
			Element element = (Element) node;
			if ("column".equals(node.getNodeName())) {
				String id = element.getAttribute("id");
				// check to see if we have this property in the object first. if
				// not, ignore the column, don't add it
				String typeClass = listSetup.getTypeClass();
				if (null != typeClass) {
					Class<?> clazz;
					try {
						clazz = Class.forName(typeClass);
						if (!propertyExists(id, clazz)) {
							// return, let it flow
							return;
						}
					} catch (ClassNotFoundException e) {
						return;
					}					
				}
				
				String width = element.getAttribute("width");
				UserListColumn uList = new UserListColumn();
				uList.setPropertyId(id);
				try {
					uList.setWidth(Integer.parseInt(width));
				} catch (NumberFormatException e) {
					// Do nothing, skip this
				}

				listSetup.addColumn(uList);
			} else {
				if ("list".equals(node.getNodeName())) {
					String typeClass = element.getAttribute("type");
					if (typeClass == null) {
						throw new ParseException("Must specify the entity type!");
					}

					listSetup.setTypeClass(typeClass);
					String id = element.getAttribute("id");
					listSetup.setListId(id == null || "".equals(id) ? Setup.ID_DEFAULT : id);
				}
			}
		}

		for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
			walk(child);
		}
	}
	
	private boolean propertyExists(String propertyId, Class<?> baseClass) {
		StringTokenizer stk = new StringTokenizer(propertyId, ".");
		Class<?> clazz = baseClass;
		
		for (int nr = 0; stk.hasMoreTokens(); nr++) {
			PropertyDescriptor desc;
			try {
				desc = new PropertyDescriptor(stk.nextToken(), clazz);
				clazz = desc.getPropertyType();
			} catch (IntrospectionException e) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * writes the current column configuration to an xml file
	 * 
	 * @param lSetup
	 * @throws IOException
	 */
	static public void setUserColumnList(UserListSetup lSetup) throws IOException {
		UserConfigXmlReader reader = new UserConfigXmlReader();
		reader.writeUserColumnListInternal(lSetup);
	}

	/**
	 * @param lSetup
	 * @param pListSetupId
	 * @throws IOException
	 */
	private void writeUserColumnListInternal(UserListSetup lSetup) throws IOException {

		IPreferenceStore prefStore = Platform.getContextPreferenceProvider().getPreferenceStore(
				"de.xwic.appkit.webbase.UserColumnList");
		String listId = lSetup.getListId();
		String fullFileName = lSetup.getTypeClass() + "_" + listId + "_" + BaseBrowserView.ID_USER + ".xml";

		String xmlContent = generateXmlContent(lSetup);

		prefStore.setValue(fullFileName, xmlContent);
		prefStore.flush();
	}

	/**
	 * @param lSetup
	 * @return generated XML content from the {@link UserListSetup}
	 */
	public static String generateXmlContent(UserListSetup lSetup) {

		String listId = lSetup.getListId();

		List<UserListColumn> columns = lSetup.getColumns();
		UserListColumn[] lc = new UserListColumn[columns.size()];
		for (int i = 0; i < columns.size(); i++) {
			lc[i] = (UserListColumn) columns.get(i);
		}

		StringBuffer buffer = new StringBuffer();
		buffer.append("<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>");
		buffer.append("<list type=\"" + lSetup.getTypeClass() + "\" id=\"" + listId + "\">");
		buffer.append("<columns>");

		for (int i = 0; i < lc.length; i++) {
			UserListColumn column = lc[i];
			buffer.append("<column id=\"" + column.getPropertyId() + "\" width=\"" + column.getWidth() + "\"/>");
		}
		buffer.append("</columns>");
		buffer.append("</list>");

		String xmlContent = buffer.toString();
		return xmlContent;
	}
}