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
package de.xwic.appkit.core.config.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.config.model.Property;
import de.xwic.appkit.core.dao.IEntity;

/**
 * Exports an EntityDescriptor into an XML file.
 * 
 * @author Florian Lippisch
 */
public class XmlEntityDescriptorWriter {

	/**
	 * @param descr
	 * @param fileName
	 */
	public static void writeEntityDescriptor(EntityDescriptor descr, File targetFile) throws IOException {
		
		Document doc = DocumentFactory.getInstance().createDocument();
		Element entities = doc.addElement("entities");

		Element entity = entities.addElement("entity");
		entity.addElement("class").setText(descr.getClassname());
		entity.addElement("hidden").setText(descr.isHidden() ? "true" : "false");
		entity.addElement("history").setText(descr.isHistory() ? "true" : "false");
		entity.addElement("transform").setText(descr.isTransform() ? "true" : "false");
		if (descr.getTitlePattern() != null) {
			entity.addElement("title").setText(descr.getTitlePattern());
		}
		if (descr.getReplicationOrder() != 0) {
			entity.addElement("replicationOrder").setText(Integer.toString(descr.getReplicationOrder()));
		}
		
		
		Element properties = entity.addElement("properties");
		properties.addAttribute("autoDetect", "true");

		// sort the list.
		List<Property> propList = new ArrayList<Property>();
		propList.addAll(descr.getProperties().values());
		Collections.sort(propList, new Comparator<Property>() {
			public int compare(Property o1, Property o2) {
				Property p1 = (Property)o1;
				Property p2 = (Property)o2;
				if (p1.isHidden() != p2.isHidden()) {
					return p1.isHidden() ? 1 : -1;
				}
				return p1.getName().compareTo(p2.getName());
			}
		});
		
		// export the list
		for (Iterator<Property> it = propList.iterator(); it.hasNext(); ) {
			Property p = it.next();
			
			Element prop = properties.addElement("property");
			//prop.addElement("name").setText(propName);
			prop.addAttribute("name", p.getName());
			
			if (p.isHidden()) {
				prop.addAttribute("hidden", "true");
			}
			if (!p.isLazy()) {
				prop.addAttribute("lazy", "false");
			}
			if (p.getPicklistId() != null && p.getPicklistId().length() != 0) {
				prop.addElement("picklistId").setText(p.getPicklistId());
			}
			if (p.getMaxLength() != 0) {
				prop.addElement("maxlength").setText(Integer.toString(p.getMaxLength()));	
			}
			if (p.getRequired()) {
				prop.addElement("required").setText(p.getRequired() ? "true" : "false");
			}

			Class<?> type = p.getDescriptor().getPropertyType(); 
			
			boolean addRef = false;
			if (IEntity.class.isAssignableFrom(type)) {
				addRef = true;
				prop.addAttribute("type", type.getName());
			} else if (Set.class.isAssignableFrom(type)) {
				addRef = true;
				prop.addAttribute("collection", "true");
				prop.addElement("elementType").setText("");
			} else {
				if (p.isFileType() && type.equals(int.class)) {
					prop.addAttribute("type", "file");
				} else {
					String typeName = type.getName();
					if (typeName.startsWith("java.lang.")) {
						typeName = typeName.substring("java.lang.".length());
					} else if (typeName.equals("java.util.Date")) {
						typeName = "Date";
					}
					prop.addAttribute("type", typeName);
				}
			}
			
			if (addRef) {
				String refDel = null;
				switch (p.getOnRefDeletion()) {
				case Property.DENY:
					refDel = "deny";
					break;
				case Property.DELETE:
					refDel = "delete";
					break;
				case Property.CLEAR_REFERENCE:
					refDel = "clear";
					break;
				}
				if (refDel != null) {
					prop.addElement("onDeletion").setText(refDel);
				}
			}
		
		}
		
		OutputFormat prettyFormat = OutputFormat.createPrettyPrint();
		OutputStream out = new FileOutputStream(targetFile);
		XMLWriter writer = new XMLWriter(out, prettyFormat);
		writer.write(doc);
		writer.flush();
		out.close();
		
	}

	
	
}
