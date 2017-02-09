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
package de.xwic.appkit.core.export;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.config.ConfigurationManager;
import de.xwic.appkit.core.config.Language;
import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.config.model.Property;
import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.daos.IPicklisteDAO;
import de.xwic.appkit.core.model.entities.IPicklistEntry;
import de.xwic.appkit.core.model.entities.IPicklistText;
import de.xwic.appkit.core.model.entities.IPickliste;
import de.xwic.appkit.core.security.daos.IActionDAO;
import de.xwic.appkit.core.security.daos.IActionSetDAO;
import de.xwic.appkit.core.security.daos.IRightDAO;
import de.xwic.appkit.core.security.daos.IRoleDAO;
import de.xwic.appkit.core.security.daos.IScopeDAO;
import de.xwic.appkit.core.security.daos.IUserDAO;

/**
 * @author Florian Lippisch
 *
 */
public class XmlExport {

	/**
	 * 
	 */
	public static final String ELM_NULL = "null";
	/**
	 * 
	 */
	public  static final String ELM_ELEMENT = "element";
	/**
	 * 
	 */
	public  static final String ELM_SET = "set";
	/**
	 * 
	 */
	public  static final String ELM_ENTITY = "entity";
	/**
	 * 
	 */
	public  static final String ELM_ENTITIES = "entities";
	/**
	 * 
	 */
	public  static final String ELM_DATA = "data";
	/**
	 * 
	 */
	public  static final String ELM_EXPORTDDATE = "exportddate";
	/**
	 * 
	 */
	public  static final String ELM_EXPORT = "export";
	
	private static Set<String> SKIP_PROPS = new HashSet<String>(); 
	static {
		SKIP_PROPS.add("deleted");
		SKIP_PROPS.add("id");
		SKIP_PROPS.add("version");
		SKIP_PROPS.add("changed");
		SKIP_PROPS.add("lastModifiedAt");
		SKIP_PROPS.add("lastModifiedFrom");
		SKIP_PROPS.add("createdAt");
		SKIP_PROPS.add("createdFrom");
		SKIP_PROPS.add("serverEntityId");
		SKIP_PROPS.add("downloadVersion");
	}
	
	private boolean exportAll = false;
	
	/**
	 * Constructor.
	 */
	public XmlExport() {
		
	}
	
	/**
	 * Export data with all properties.
	 * @param exportAll
	 */
	public XmlExport(boolean exportAll) {
		this.exportAll = exportAll;
	}
	
	/**
	 * @param secDump
	 */
	public void exportSecurity(File secDump) throws IOException, ConfigurationException {
		
		Document doc = DocumentFactory.getInstance().createDocument();
		Element root = doc.addElement(ELM_EXPORT);
		root.addAttribute("type", "security");
		
		Element info = root.addElement(ELM_EXPORTDDATE);
		info.setText(DateFormat.getDateTimeInstance().format(new Date()));
		
		Element data = root.addElement(ELM_DATA);
		
		addAll(IActionDAO.class, data);
		addAll(IActionSetDAO.class, data);
		addAll(IScopeDAO.class, data);
		addAll(IRoleDAO.class, data);
		addAll(IRightDAO.class, data);
		addAll(IUserDAO.class, data);
		
		OutputFormat prettyFormat = OutputFormat.createPrettyPrint();
		OutputStream out = new FileOutputStream(secDump);
		XMLWriter writer = new XMLWriter(out, prettyFormat);
		writer.write(doc);
		writer.flush();
		out.close();
		
	}

	/**
	 * @param plDump
	 */
	public void exportPicklists(File plDump) throws IOException, ConfigurationException {
		
		Document doc = DocumentFactory.getInstance().createDocument();
		Element root = doc.addElement(ELM_EXPORT);
		root.addAttribute("type", "picklists");
		
		Element info = root.addElement(ELM_EXPORTDDATE);
		info.setText(DateFormat.getDateTimeInstance().format(new Date()));
		
		Element data = root.addElement(ELM_DATA);

		addPicklisten(data);
		
		OutputFormat prettyFormat = OutputFormat.createPrettyPrint();
		OutputStream out = new FileOutputStream(plDump);
		XMLWriter writer = new XMLWriter(out, prettyFormat);
		writer.write(doc);
		writer.flush();
		out.close();
		
	}
	
	/**
	 * @param plDump
	 */
	public void exportPicklists(Writer writer) throws IOException, ConfigurationException {
		
		Document doc = DocumentFactory.getInstance().createDocument();
		Element root = doc.addElement(ELM_EXPORT);
		root.addAttribute("type", "picklists");
		
		Element info = root.addElement(ELM_EXPORTDDATE);
		info.setText(DateFormat.getDateTimeInstance().format(new Date()));
		
		Element data = root.addElement(ELM_DATA);

		addPicklisten(data);
		
		OutputFormat prettyFormat = OutputFormat.createCompactFormat();//PrettyPrint();
		XMLWriter xmlWriter = new XMLWriter(writer, prettyFormat);
		xmlWriter.write(doc);
		xmlWriter.flush();
		
	}
	
	/**
	 * Export a list of entities to a xml file.
	 * @param file
	 * @param entities
	 * @throws IOException
	 * @throws ConfigurationException
	 */
	public void exportEntity(File file, IEntity entity) throws IOException, ConfigurationException {
		List<IEntity> l = new ArrayList<IEntity>();
		l.add(entity);
		exportEntities(file, l);
	}
	/**
	 * Export a list of entities to a xml file.
	 * @param file
	 * @param entities
	 * @throws IOException
	 * @throws ConfigurationException
	 */
	public void exportEntities(File file, List<IEntity> entities) throws IOException, ConfigurationException {
		
		if (entities.size() == 0) {
			throw new IllegalArgumentException("Entity list must not be null");
		}
		String type = entities.get(0).type().getName();
		
		Document doc = DocumentFactory.getInstance().createDocument();
		Element root = doc.addElement(ELM_EXPORT);
		root.addAttribute("type", "entities");
		
		Element info = root.addElement(ELM_EXPORTDDATE);
		info.setText(DateFormat.getDateTimeInstance().format(new Date()));
		
		Element data = root.addElement(ELM_DATA);

		Element elmEntities = data.addElement(ELM_ENTITIES);
		elmEntities.addAttribute("type", type);
		
		EntityDescriptor descr = DAOSystem.getEntityDescriptor(type);

		for (Iterator<IEntity> it = entities.iterator(); it.hasNext(); ) {
			IEntity entity = it.next();
			if (!entity.type().getName().equals(type)) {
				throw new IllegalArgumentException("All entities in the list must be of the same type!");
			}
			addEntity(elmEntities, descr, entity);
		}
		
		
		OutputFormat prettyFormat = OutputFormat.createPrettyPrint();
		OutputStream out = new FileOutputStream(file);
		// this "hack" is required to preserve the LBCR's in strings... 
		XMLWriter writer = new XMLWriter(out, prettyFormat) {
			@Override
			public void write(Document arg0) throws IOException {
				//this.preserve = true;
				super.write(arg0);
			}
		};
		writer.write(doc);
		writer.flush();
		out.close();

	}

	/**
	 * @param data
	 */
	private void addPicklisten(Element data) {
		
		IPicklisteDAO dao = DAOSystem.getDAO(IPicklisteDAO.class);
		List<Language> langs = ConfigurationManager.getSetup().getLanguages();
		
		List<?> list = dao.getEntities(null); // get all Picklisten
		for (Iterator<?> it = list.iterator(); it.hasNext(); ) {
			
			IPickliste pl = (IPickliste)it.next();
			Element elPl = data.addElement("pickliste");
			elPl.addAttribute("key", pl.getKey());
			elPl.addAttribute("id", Long.toString(pl.getId()));
			elPl.addAttribute("version", Long.toString(pl.getVersion()));
			if (pl.isDeleted()) {
				elPl.addAttribute("deleted", "1");
			}
			
			elPl.addElement("title").setText(pl.getTitle());
			elPl.addElement("beschreibung").setText(pl.getBeschreibung());
			
			Element elEntries = elPl.addElement("entries");
			// add entries
			List<?> entries = dao.getAllEntriesToList(pl);
			for (Iterator<?> itE = entries.iterator(); itE.hasNext(); ) {
				IPicklistEntry entry = (IPicklistEntry)itE.next();
				if (!entry.isDeleted()) {
					Element elEntry = elEntries.addElement("entry");
					elEntry.addAttribute("id", Long.toString(entry.getId()));
					elEntry.addAttribute("version", Long.toString(entry.getVersion()));
					if (entry.isDeleted()) {
						elEntry.addAttribute("deleted", "1");
					}
					if (entry.isVeraltet()) {
						elEntry.addAttribute("veraltet", "1");
					}
					elEntry.addAttribute("sortindex", Integer.toString(entry.getSortIndex()));
					if (entry.getKey() != null) {
						elEntry.addAttribute("key", entry.getKey());
					}
					for (Iterator<Language> itT = langs.iterator(); itT.hasNext(); ) {
						Language language = itT.next();
						IPicklistText pt = dao.getPicklistText(entry, language.getId());
						if (pt != null) {
							Element elText = elEntry.addElement("text");
							elText.addAttribute("id", Long.toString(pt.getId()));
							elText.addAttribute("version", Long.toString(pt.getVersion()));
							elText.addAttribute("lang", pt.getLanguageID());
							if (pt.getBeschreibung() != null && pt.getBeschreibung().length() != 0) {
								elText.addAttribute("description", pt.getBeschreibung());
							}
							elText.setText(pt.getBezeichnung());
						}
					}
				}
				
			}
			
		}
		
	}

	/**
	 * @param class1
	 * @param data
	 * @throws ConfigurationException 
	 */
	private void addAll(Class<? extends DAO> daoClass, Element data) throws ConfigurationException {
		
		DAO dao = DAOSystem.getDAO(daoClass); 
			
		Element entities = data.addElement(ELM_ENTITIES);
		entities.addAttribute("type", dao.getEntityClass().getName());
		
		EntityDescriptor descr = DAOSystem.getEntityDescriptor(dao.getEntityClass().getName());
		
		List<?> all = dao.getEntities(null);
		
		System.out.println("Adding " + all.size() + " " + dao.getEntityClass().getName());
		
		for (Iterator<?> it = all.iterator(); it.hasNext(); ) {
			
			IEntity entity = (IEntity)it.next();
			if (!entity.isDeleted()) {
				addEntity(entities, descr, entity);
			}
			
		}
		
	}

	/**
	 * @param entities
	 * @param descr 
	 * @param entity
	 */
	public void addEntity(Element entities, EntityDescriptor descr, IEntity entity) {
		
		try {
			Element elm = entities.addElement(ELM_ENTITY);
			elm.addAttribute("id", Long.toString(entity.getId()));
			
			for (Iterator<String> it = descr.getProperties().keySet().iterator(); it.hasNext(); ) {
				
				String propertyName = it.next();

				if (exportAll || !SKIP_PROPS.contains(propertyName)) {
					Property property = descr.getProperty(propertyName);
					
					
					Method mRead = property.getDescriptor().getReadMethod();
					Method mWrite = property.getDescriptor().getWriteMethod();
					
					if (mWrite != null) { // only export properties that have a write method

						Element pValue = elm.addElement(propertyName);
						Object value = mRead.invoke(entity, (Object[]) null);
						
						addValue(pValue, value, false);
						
					}
				}
				
			}
		} catch (Exception e) {
			throw new RuntimeException("Error transforming entity into XML: " + entity.type().getName() + ", #" + entity.getId() + " :" + e, e);
		}
		
	}

	/**
	 * @param value
	 * @param value2
	 */
	private void addValue(Element elm, Object value, boolean addTypeInfo) {

		String typeInfo = value != null ? value.getClass().getName() : null;
		if (value == null) {
			elm.addElement(ELM_NULL);
		} else if (value instanceof String) {
			elm.setText((String)value);
		} else if (value instanceof Integer) {
			elm.setText(value.toString());
		} else if (value instanceof Boolean) {
			Boolean bo = (Boolean)value;
			elm.setText(bo.booleanValue() ? "true" : "false");
		} else if (value instanceof Long) {
			elm.setText(value.toString());
		} else if (value instanceof Double) {
			elm.setText(value.toString());
		} else if (value instanceof Date) {
			Date date = (Date)value;
			elm.setText(Long.toString(date.getTime()));
			
		} else if (value instanceof IPicklistEntry) {
			IPicklistEntry entry = (IPicklistEntry)value;
			typeInfo = entry.type().getName();
			if (entry.getKey() != null) {
				elm.addAttribute("key", entry.getKey());
			}
			elm.addAttribute("picklistid", entry.getPickliste().getKey());
			elm.addAttribute("entryid", Long.toString(entry.getId()));
			elm.addAttribute("langid", "de");
			elm.setText(entry.getBezeichnung("de")); // use german language in export, as it is most common

		} else if (value instanceof IEntity) {
			IEntity entity = (IEntity)value;
			typeInfo = entity.type().getName();
			elm.addAttribute("id", Long.toString(entity.getId()));
			DAO dao = DAOSystem.findDAOforEntity(entity.type());
			elm.setText(dao.buildTitle(entity));
			
		} else if (value instanceof Set) {
			Set<?> set = (Set<?>)value;
			Element elmSet = elm.addElement(ELM_SET);
			for (Iterator<?> it = set.iterator(); it.hasNext(); ) {
				Object o = it.next();
				Element entry = elmSet.addElement(ELM_ELEMENT);
				addValue(entry, o, true);
			}
		}
		
		if (addTypeInfo && typeInfo != null) {
			elm.addAttribute("type", typeInfo);
		}
		
	}


}
