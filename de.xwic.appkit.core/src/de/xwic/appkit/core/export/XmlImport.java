/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.dev.dump.XmlImport
 * Created on 02.03.2007 by Florian Lippisch
 *
 */
package de.xwic.appkit.core.export;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.config.ConfigurationManager;
import de.xwic.appkit.core.config.Language;
import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.config.model.Property;
import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.DataAccessException;
import de.xwic.appkit.core.dao.EntityKey;
import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.daos.IPicklisteDAO;
import de.xwic.appkit.core.model.daos.impl.PicklistCache;
import de.xwic.appkit.core.model.entities.IPicklistEntry;
import de.xwic.appkit.core.model.entities.IPicklistText;
import de.xwic.appkit.core.model.entities.IPickliste;
import de.xwic.appkit.core.model.entities.impl.PicklistEntry;
import de.xwic.appkit.core.model.entities.impl.PicklistText;
import de.xwic.appkit.core.model.entities.impl.Pickliste;
import de.xwic.appkit.core.model.queries.PicklistTextQuery;
import de.xwic.appkit.core.model.util.ServerConfig;

/**
 * @author Florian Lippisch
 *
 */
public class XmlImport {

	private Map<EntityKey, Integer> importedEntities = new HashMap<EntityKey, Integer>();
	
	private Element elmData = null;
	private Iterator<?> itRead = null;
	private Iterator<?> itMain = null;
	private String currType = null;
	
	/**
	 * @param file
	 * @throws ConfigurationException 
	 */
	public void importFile(File file) throws IOException, DocumentException, ConfigurationException {
		
		if (!file.exists()) {
			throw new FileNotFoundException("File not found: " + file.getName());
		}
		System.out.println("Importing " + file.getName());
		
		Document doc = new SAXReader().read(file);
		Element root = doc.getRootElement();
		
		String type = root.attributeValue("type");
		if (type != null && type.equals("security")) {
			importAll(root.element(XmlExport.ELM_DATA));
		} else if (type != null && type.equals("entities")) {
			importAll(root.element(XmlExport.ELM_DATA));
		} else if (type != null && type.equals("picklists")) {
			importPicklists(root.element(XmlExport.ELM_DATA));
		} else if (root.getName().equals("config")) {
			importConfig(root);
		}
		
	}

	/**
	 * Creates a PicklistCache from a document. 
	 * @param doc
	 * @return
	 */
	public PicklistCache createPicklistCache(Document doc) {
		
		PicklistCache cache = new PicklistCache();
		Element root = doc.getRootElement();
		@SuppressWarnings("hiding")
		Element elmData = root.element(XmlExport.ELM_DATA);
		
		for (Iterator<?> it = elmData.elementIterator(); it.hasNext(); ) {
			Element elPl = (Element)it.next();
			if (elPl.getName().equals("pickliste")) {
				
				String key = elPl.attributeValue("key");
				String id = elPl.attributeValue("id");
				String version = elPl.attributeValue("version");
				Pickliste pickliste = new Pickliste();
				
				pickliste.setKey(key);
				pickliste.setId(Integer.parseInt(id));
				pickliste.setVersion(Long.parseLong(version));
				
				Element elBeschr = elPl.element("beschreibung");
				if (elBeschr != null) {
					pickliste.setBeschreibung(elBeschr.getTextTrim());
				}
				Element elTitle = elPl.element("title");
				if (elTitle != null) {
					pickliste.setTitle(elTitle.getTextTrim());
				}
				
				cache.putPickliste(pickliste);
				List<Object> entries = new ArrayList<Object>();
				
				Element elEntries = elPl.element("entries");
				if (elEntries != null) {
					
					for (Iterator<?> itEn = elEntries.elementIterator(); itEn.hasNext(); ) {
						Element elEntry = (Element)itEn.next();
						if (elEntry.getName().equals("entry")) {
							PicklistEntry entry = new PicklistEntry();
							String peId = elEntry.attributeValue("id");
							String peVersion = elEntry.attributeValue("version");
							entry.setId(Integer.parseInt(peId));
							entry.setVersion(Long.parseLong(peVersion));
							entry.setPickliste(pickliste);
							entry.setPickTextValues(new HashSet<IPicklistText>());
							if (elEntry.attributeValue("key") != null) {
								entry.setKey(elEntry.attributeValue("key"));
							}
							if (elEntry.attributeValue("sortindex") != null) {
								entry.setSortIndex(Integer.parseInt(elEntry.attributeValue("sortindex")));
							}
							if (null != elEntry.attributeValue("veraltet") && elEntry.attributeValue("veraltet").equals("1")) {
								entry.setVeraltet(true);
							}
							cache.putPicklistEntry(entry);
							entries.add(entry);
							
							for (Iterator<?> itT = elEntry.elementIterator(); itT.hasNext(); ) {
								Element elText = (Element)itT.next();
								String ptId = elText.attributeValue("id");
								String ptVersion = elText.attributeValue("version");
								String langId = elText.attributeValue("lang");
								String description = elText.attributeValue("description");
								
								PicklistText pt = new PicklistText();
								pt.setId(Integer.parseInt(ptId));
								pt.setVersion(Long.parseLong(ptVersion));
								pt.setLanguageID(langId);
								pt.setBezeichnung(elText.getTextTrim());
								pt.setPicklistEntry(entry);
								
								if (description != null) {
									pt.setBeschreibung(description);
								}
								entry.getPickTextValues().add(pt);
								cache.putPicklistText(pt);
								
							}
						}
					}
					
				}
				EntityList eEntryList = new EntityList(entries, null, entries.size());
				cache.putEntryList(pickliste.getKey(), eEntryList);
			}
		}
		
		return cache;
		
	}
	
	/**
	 * Open a file for reading. Use "readNextEntry()" to read
	 * the next entity from the file.
	 * @param file
	 * @throws IOException
	 * @throws DocumentException
	 * @throws ConfigurationException
	 */
	public void openFile(File file) throws IOException, DocumentException, ConfigurationException {
		
		if (!file.exists()) {
			throw new FileNotFoundException("File not found: " + file.getName());
		}
		System.out.println("Importing " + file.getName());
		
		Document doc = new SAXReader().read(file);
		Element root = doc.getRootElement();
		
		String type = root.attributeValue("type");
		if (type != null && type.equals("security")) {
			elmData = root.element(XmlExport.ELM_DATA);
		} else if (type != null && type.equals("entities")) {
			elmData = root.element(XmlExport.ELM_DATA);
		} else if (type != null && type.equals("picklists")) {
			elmData = root.element(XmlExport.ELM_DATA);
		} else if (root.getName().equals("config")) {
			elmData = root;
		}
		itMain = elmData.elementIterator(XmlExport.ELM_ENTITIES);
		if (itMain.hasNext()) {
			Element elEntities = (Element)itMain.next();
			currType = elEntities.attributeValue("type");
			itRead = elEntities.elementIterator(XmlExport.ELM_ENTITY);
		} else {
			itRead = null;
		}
		
		

	}
	
	/**
	 * 
	 * @return
	 */
	public boolean hasNextEntity() {
		while (itRead != null && !itRead.hasNext()) {
			if (itMain.hasNext()) {
				Element elEntities = (Element)itMain.next();
				currType = elEntities.attributeValue("type");
				itRead = elEntities.elementIterator(XmlExport.ELM_ENTITY);
			} else {
				itRead = null;
			}
		}
		return itRead != null && itRead.hasNext();
	}
	
	/**
	 * @return
	 * @throws ConfigurationException
	 */
	public IEntity readNextEntity() throws ConfigurationException {
		
		while (itRead != null && !itRead.hasNext()) {
			if (itMain.hasNext()) {
				Element elEntities = (Element)itMain.next();
				currType = elEntities.attributeValue("type");
				itRead = elEntities.elementIterator(XmlExport.ELM_ENTITY);
			} else {
				itRead = null;
			}
		}

		if (itRead == null) {
			throw new IllegalStateException("No more entities");
		}
			
		DAO dao = DAOSystem.findDAOforEntity(currType);
		EntityDescriptor descr = DAOSystem.getEntityDescriptor(currType);
		
		Element elEntity = (Element)itRead.next();
		
		IEntity entity = dao.createEntity();
		readEntityProperties(descr, elEntity, entity);

		return entity;
	}
	

	/**
	 * @param root
	 */
	private void importConfig(Element root) {

		IPicklisteDAO dao = (IPicklisteDAO)DAOSystem.getDAO(IPicklisteDAO.class);
		
		for (Iterator<?> it = root.elementIterator("key"); it.hasNext(); ) {
			Element element = (Element)it.next();
			String id = element.attributeValue("id");
			
			Element elValue = element.element("value");
			Element elPl = element.element("picklistentry");
			if (elValue != null) {
				ServerConfig.set(id, elValue.getTextTrim());
			} else if (elPl != null) {
				String list = elPl.attributeValue("list");
				String lang = elPl.attributeValue("lang");
				String text = elPl.getTextTrim();
				if (list != null && lang != null && text != null) {
					IPickliste pListe = dao.getPicklisteByKey(list);
					if (pListe != null) {
						PicklistTextQuery tquery = new PicklistTextQuery();
						tquery.setLanguageId(lang);
						tquery.setBezeichnung(text);
						tquery.setPicklisteID(pListe.getId());
						
						List<?> result = dao.getEntities(null, tquery);
						if (result.size() == 0) {
							System.out.println("WARNING: Picklistentry not found specified for " + id);
							System.out.println("Creating entry '" + text + "'");
							
							IPicklistEntry entry = dao.createPickListEntry(pListe);
							for (Iterator<Language> itLang = ConfigurationManager.getSetup().getLanguages().iterator(); itLang.hasNext();) {
								Language language = itLang.next();
								dao.createBezeichnung(entry, language.getId(), text);
							}
							
							ServerConfig.set(id, entry.getId());
						
						} else {
							IPicklistText pText = (IPicklistText)result.get(0);
							ServerConfig.set(id, pText.getPicklistEntry().getId());
							if (result.size() > 1) {
								System.out.println("WARNING: Found more then one picklistentries that match for ID " + id);
							}
						}
					}
				}
			}
			
		}
		
	}


	/**
	 * @param root
	 */
	private void importPicklists(Element data) {
		
		IPicklisteDAO dao = (IPicklisteDAO)DAOSystem.getDAO(IPicklisteDAO.class);
		
		for (Iterator<?> it = data.elementIterator(); it.hasNext(); ) {
			Element elPl = (Element)it.next();
			if (elPl.getName().equals("pickliste")) {
				
				String key = elPl.attributeValue("key");
				IPickliste pickliste = (IPickliste)dao.createEntity();
				pickliste.setKey(key);
				
				Element elBeschr = elPl.element("beschreibung");
				if (elBeschr != null) {
					pickliste.setBeschreibung(elBeschr.getTextTrim());
				}
				Element elTitle = elPl.element("title");
				if (elTitle != null) {
					pickliste.setTitle(elTitle.getTextTrim());
				}
				
				dao.update(pickliste);
				
				Element elEntries = elPl.element("entries");
				if (elEntries != null) {
					
					for (Iterator<?> itEn = elEntries.elementIterator(); itEn.hasNext(); ) {
						Element elEntry = (Element)itEn.next();
						if (elEntry.getName().equals("entry")) {
							IPicklistEntry entry = dao.createPickListEntry(pickliste);
							String oldId = elEntry.attributeValue("id");
							if (oldId != null) {
								importedEntities.put(new EntityKey(IPicklistEntry.class.getName(), Integer.parseInt(oldId)), new Integer(entry.getId()));
							}
							boolean modified = false;
							if (elEntry.attributeValue("key") != null) {
								entry.setKey(elEntry.attributeValue("key"));
								modified = true;
							}
							if (elEntry.attributeValue("sortindex") != null) {
								entry.setSortIndex(Integer.parseInt(elEntry.attributeValue("sortindex")));
								modified = true;
							}
							if (modified) {
								dao.update(entry);
							}
							
							for (Iterator<?> itT = elEntry.elementIterator(); itT.hasNext(); ) {
								Element elText = (Element)itT.next();
								String langId = elText.attributeValue("lang");
								dao.createBezeichnung(entry, langId, elText.getTextTrim());
							}
						}
					}
					
				}
			}
		}
		
	}

	/**
	 * @param root
	 * @throws ConfigurationException 
	 */
	private void importAll(Element data) throws ConfigurationException {

		for (Iterator<?> it = data.elementIterator(XmlExport.ELM_ENTITIES); it.hasNext(); ) {
			
			Element elEntities = (Element)it.next();
			importEntities(elEntities);
			
		}
		
	}

	/**
	 * @param elEntities
	 * @throws ConfigurationException 
	 */
	private void importEntities(Element elEntities) throws ConfigurationException {
		
		String type = elEntities.attributeValue("type");
		DAO dao = DAOSystem.findDAOforEntity(type);
		EntityDescriptor descr = DAOSystem.getEntityDescriptor(type);
		
		int count = 0;
		for (Iterator<?> it = elEntities.elementIterator(XmlExport.ELM_ENTITY); it.hasNext(); ) {
			
			Element elEntity = (Element)it.next();
			int id = Integer.parseInt(elEntity.attributeValue("id"));
			
			IEntity entity = dao.createEntity();
			readEntityProperties(descr, elEntity, entity);
			
			dao.update(entity);
			importedEntities.put(new EntityKey(type, id), new Integer(entity.getId()));
			//System.out.println((count++) + " imp " + type + " id: "+ id + " as " + entity.getId());
			count++;
		}
		System.out.println("imported " + count + " entities.");
		
	}

	/**
	 * @param descr 
	 * @param elEntity
	 * @param entity
	 */
	private void readEntityProperties(EntityDescriptor descr, Element elEntity, IEntity entity) {
		
		for (Iterator<?> it = elEntity.elementIterator(); it.hasNext(); ) {

			Element elProp = (Element)it.next();
			String propertyName = elProp.getName();
			
			Property property = descr.getProperty(propertyName);
			if (property != null) {
				
				try {
					loadPropertyValue(entity, property, elProp);
				} catch (Exception e) {
					throw new DataAccessException("Error loading properties: " + e, e);
				}
			}
		}
	}

	/**
	 * @param entity
	 * @param property
	 * @param elProp
	 */
	@SuppressWarnings("unchecked")
	private void loadPropertyValue(IEntity entity, Property property, Element elProp) throws Exception {
		
		PropertyDescriptor pd = property.getDescriptor();
		Class<?> type = pd.getPropertyType();
		Method mWrite = pd.getWriteMethod();
		// check if value is null
		boolean isNull = elProp.element(XmlExport.ELM_NULL) != null;
		
		Object value = null;
		if (!isNull) {
			
			if (Set.class.isAssignableFrom(type)) {
				// a set.
				Set<IEntity> set = new HashSet<IEntity>();
				Element elSet = elProp.element(XmlExport.ELM_SET);
				for (Iterator<?> itSet = elSet.elementIterator(XmlExport.ELM_ELEMENT); itSet.hasNext(); ) {
					Element elSetElement = (Element)itSet.next();
					String typeElement = elSetElement.attributeValue("type");
					int refId = Integer.parseInt(elSetElement.getText());

					Integer newId = importedEntities.get(new EntityKey(typeElement, refId));
					if (newId != null) {
						// its an imported object
						refId = newId.intValue();
					}
					DAO refDAO = DAOSystem.findDAOforEntity(typeElement);
					IEntity refEntity = refDAO.getEntity(refId);
					set.add(refEntity);
				}
				value = set;
				
			} else if (IPicklistEntry.class.isAssignableFrom(type)){

				String picklistId = elProp.attributeValue("picklistid");
				String key = elProp.attributeValue("key");
				String langid = elProp.attributeValue("langid");
				String text = elProp.getText();
				
				if (langid == null || langid.length() == 0) {
					langid = "de";
				}
				
				// try to find by key
				IPicklisteDAO plDAO = (IPicklisteDAO)DAOSystem.getDAO(IPicklisteDAO.class);
				IPicklistEntry entry = null;
				if (key != null && key.length() != 0 && picklistId != null && picklistId.length() != 0) {
					entry = plDAO.getPickListEntryByKey(picklistId, key); // try to find by key
				}
				
				// not found, try by title
				if (entry == null && picklistId != null && picklistId.length() != 0 && text != null && text.length() != 0) {
					List<IPicklistEntry> lst = plDAO.getAllEntriesToList(picklistId);
					for (Iterator<IPicklistEntry> it = lst.iterator(); it.hasNext(); ) {
						IPicklistEntry pe = it.next();
						if (text.equals(pe.getBezeichnung(langid))) {
							entry = pe;
							break;
						}
					}
				}

				if (entry == null && picklistId != null && text != null) {
					// create?? 
					IPickliste plist = plDAO.getPicklisteByKey(picklistId);
					if (plist != null) {
						entry = plDAO.createPicklistEntry();
						entry.setKey(key);
						entry.setPickliste(plist);
						plDAO.update(entry);
						
						for(Iterator<Language> it = ConfigurationManager.getSetup().getLanguages().iterator(); it.hasNext(); ) {
							Language lang = it.next();
							plDAO.createBezeichnung(entry, lang.getId(), text);
						}
					}
				}
				value = entry;
				
			} else if (IEntity.class.isAssignableFrom(type)){
				// entity type
				int refId = Integer.parseInt(elProp.attributeValue("id"));
				Integer newId = importedEntities.get(new EntityKey(type.getName(), refId));
				if (newId != null) {
					// its an imported object
					refId = newId.intValue();
				}
				DAO refDAO = DAOSystem.findDAOforEntity((Class<? extends IEntity>) type);
				IEntity refEntity = refDAO.getEntity(refId);
				value = refEntity;
				
			} else {
				// basic type
				String text = elProp.getText();
				if (String.class.equals(type)) {
					value = text;
				} else if (int.class.equals(type) || Integer.class.equals(type)) {
					value = new Integer(text);
				} else if (long.class.equals(type) || Long.class.equals(type)) {
					value = new Long(text);
				} else if (boolean.class.equals(type) || Boolean.class.equals(type)) {
					value = new Boolean(text.equals("true"));
				} else if (Date.class.equals(type)) {
					value = new Date(Long.parseLong(text));
				} else if (double.class.equals(type) || Double.class.equals(type)) {
					value = new Double(text);
				}
			}
			
		}
		mWrite.invoke(entity, new Object[] { value });
		
	}

}
