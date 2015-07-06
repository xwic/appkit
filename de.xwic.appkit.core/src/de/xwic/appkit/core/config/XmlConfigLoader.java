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
package de.xwic.appkit.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.xwic.appkit.core.config.editor.EditorConfiguration;
import de.xwic.appkit.core.config.editor.XmlEditorConfigReader;
import de.xwic.appkit.core.config.list.ListSetup;
import de.xwic.appkit.core.config.list.XmlListSetupReader;
import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.config.model.Model;
import de.xwic.appkit.core.config.model.XmlEntityDescriptorReader;
import de.xwic.appkit.core.config.utils.ListSetupFileIconWrapper;

/**
 * Creates a new Setup instance from a product.xml file.
 * @author Florian Lippisch
 */
public class XmlConfigLoader {

	private static final Log log = LogFactory.getLog(XmlConfigLoader.class);
	
	/** Filename of the product setup. */
	public static final String FILENAME_PRODUCT_SETUP = "product.setup.xml";
	private URL location;
	private Setup setup;
	private Profile profile;
	private List<URL> fileList = new ArrayList<URL>();
	
	/** Internal flag that is true if a user profile or false if the default configuration is loaded. */
	private boolean profileMode;
	private boolean headerOnly;
	
	/**
	 * Constructor.
	 * @param location
	 */
	private XmlConfigLoader(URL location, boolean headerOnly) {
		this.location = location;
		this.setup = new Setup();
		this.profile = setup.getDefaultProfile();
		this.headerOnly = headerOnly;
		this.profileMode = false;
	}
	
	/**
	 * Used to read user profiles.
	 * @param location2
	 * @param headerOnly2
	 * @param setup2
	 * @param newProfile
	 */
	private XmlConfigLoader(URL location, boolean headerOnly, Setup setup, Profile newProfile) {
		this.location = location;
		this.setup = setup;
		this.profile = newProfile;
		this.headerOnly = headerOnly;
		this.profileMode = true;
	}

	/**
	 * Loads a product setup from the specified location. The location can be
	 * a directory or a location on a webserver.
	 * <p>Sample:
	 * <li>http://server/crm/config
	 * 
	 * @param url
	 * @return
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public static Setup loadSetup(URL url) throws IOException, ParseException {
		XmlConfigLoader loader = new XmlConfigLoader(url, false);
		URL pdsLocation = new URL(url, FILENAME_PRODUCT_SETUP);
		loader.fileList.add(pdsLocation);
		return loader.loadSetupInternal(pdsLocation);
	}
	
	/**
	 * Reads the metadata and the filelist from the location. It does not
	 * import the referenced files or descriptors.
	 * @param location
	 * @param fileList An empty array where the list of files is stored into. 
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	public static Setup loadMetaData(URL location, List<URL> fileList) throws IOException, ParseException {
		XmlConfigLoader loader = new XmlConfigLoader(location, true);
		URL pdsLocation = new URL(location, FILENAME_PRODUCT_SETUP);
		fileList.add(pdsLocation);
		Setup setup = loader.loadSetupInternal(pdsLocation);
		fileList.addAll(loader.fileList);
		return setup;
	}
	
	/**
	 * load the configuration
	 * @param in
	 * @return
	 * @throws IOException 
	 * @throws ParseException 
	 */
	private Setup loadSetupInternal(URL pdsLocation) throws IOException, ParseException {
		InputStream in = pdsLocation.openStream();
		try {
			DocumentBuilder dom = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = (Document) dom.parse(in);
			
			Element root = doc.getDocumentElement();
			
			NodeList nl = root.getChildNodes();
			for (int i = 0; i < nl.getLength(); i++) {
				Node node = nl.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element)node;
					String name = element.getNodeName();
					
					if (!profileMode) {
						// the following attributes are "not allowed" in profiles and therefor skipped.
						if (name.equals("id")) {
							setup.setId(getNodeText(element));
						} else if (name.equals("title")) {
							setup.setAppTitle(getNodeText(element));
						} else if (name.equals("version")) {
							setup.setVersion(getNodeText(element));
						} else if (name.equals("languages")) {
							loadLanguages(element);
						} else if (name.equals("properties")) {
							loadProperties(element);
						}
						
					} 
					if (name.equals("domain")) {
						loadDomain(element);
					} else if (name.equals("feature")) {
						String id = element.getAttribute("id");
						if (id == null) {
							throw new ParseException("feature must specify id.");
						}
						profile.addFeature(id, !"false".equals(element.getAttribute("enabled")));
					} else if (name.equals("profiles")) {
						loadProfiles(element);
					}
				}
			}

			setup.initializeConfig();

		} catch (ConfigurationException ce) {
			throw new ParseException("Error parsing setup", ce);
		} catch (ParserConfigurationException pce) {
			throw new ParseException("Error parsing setup", pce);
		} catch (SAXException e) {
			throw new ParseException("Error parsing setup", e);
		} finally {
			in.close();
		}
		
		return setup;
	}
	
	/**
	 * @param element
	 * @throws ConfigurationException 
	 * @throws IOException 
	 * @throws ParseException 
	 */
	private void loadProfiles(Element elProfiles) throws ConfigurationException, IOException, ParseException {
		
		NodeList nl = elProfiles.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node node = nl.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element)node;
				if (element.getNodeName().equals("profile")) {
					String name = element.getAttribute("name");
					String file = element.getAttribute("file");
					
					URL profileURL = new URL(location, file);
					Profile newProfile = new Profile(profile);
					setup.addProfile(name, newProfile);
					fileList.add(profileURL);
					
					XmlConfigLoader profileLoader = new XmlConfigLoader(location, headerOnly, setup, newProfile);
					profileLoader.loadSetupInternal(profileURL);
					fileList.addAll(profileLoader.fileList);
				}
			}
		}
	}

	/**
	 * @param element
	 * @throws IOException 
	 * @throws ParseException 
	 */
	private void loadDefaults(Domain domain, Element elProp) throws IOException, ParseException {
		
		NodeList nl = elProp.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node node = nl.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element)node;
				if (element.getNodeName().equals("preference")) {
					if (!element.hasAttribute("key")) {
						throw new ParseException("key must be specified for a preference");
					}
					String key = element.getAttribute("key");
					String value = getNodeText(element);
					domain.setPrefDefault(key, value);
				}
			}
		}
		
	}

	/**
	 * @param element
	 * @throws IOException 
	 * @throws ParseException 
	 */
	private void loadProperties(Element elProp) throws IOException, ParseException {
		
		NodeList nl = elProp.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node node = nl.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element)node;
				if (element.getNodeName().equals("property")) {
					if (!element.hasAttribute("key")) {
						throw new ParseException("key must be specified for a property");
					}
					String key = element.getAttribute("key");
					String value = getNodeText(element);
					setup.setProperty(key, value);
				}
			}
		}
		
	}

	
	/**
	 * @param element
	 * @throws IOException 
	 * @throws ParseException 
	 * @throws ConfigurationException 
	 */
	private void loadDomain(Element elDomain) throws IOException, ParseException, ConfigurationException {
		
		String id = elDomain.getAttribute("id");
		Domain domain;
		if (profileMode) {
			// the domain has already been loaded/specified. 
			domain = setup.getDomain(id);
		} else {
			domain = new Domain();
			domain.setId(id);
		}
		
		NodeList nl = elDomain.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node node = nl.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element)node;
				
				if (element.getNodeName().equals("bundle") && !profileMode) {
					String basename = element.getAttribute("basename");
					// load each configured language
					for (Iterator<Language> it = setup.getLanguages().iterator(); it.hasNext(); ) {
						Language lang = it.next();
						String filename = basename + "_" + lang.getId() + ".properties";
						URL bundleUrl = new URL(location, filename);
						fileList.add(bundleUrl);
						if (!headerOnly) {
							Properties prop = new Properties();
							try {
								InputStream in = bundleUrl.openStream(); 
								prop.load(in);
								in.close();
								// use unsynchronized hashMap
								HashMap<String, String> map = new HashMap<String, String>();
								for (Object key : prop.keySet()) {
									String strKey = (String) key;
									map.put(strKey, prop.getProperty(strKey));
								}
								domain.addBundle(lang.getId(), new Bundle(map));
							} catch (Exception e) {
								// file might not exist. 
								if (setup.getDefaultLangId().equals(lang.getId())) {
									throw new ConfigurationException("The default language file for the bundle '" +  basename + "' does not exist.");
								}
							}
						}
					}
				} else if (element.getNodeName().equals("resource") && !profileMode) {
					Resource res = new Resource();
					res.setId(element.getAttribute("id"));
					URL resUrl = new URL(location, element.getAttribute("file"));
					res.setLocation(resUrl);
					res.setFilePath(element.getAttribute("file"));
					domain.addResource(res);
					fileList.add(resUrl);
					
				} else if (element.getNodeName().equals("entities")) {
					loadEntities(domain, element);
				} else if (element.getNodeName().equals("defaults")) {
					loadDefaults(domain, element);
				}
			}
		}
		if (!profileMode) {
			setup.addDomain(domain);
		}
		
	}

	/**
	 * @param element
	 * @throws IOException 
	 * @throws ParseException 
	 * @throws ConfigurationException 
	 */
	private void loadEntities(Domain domain, Element entityElem) throws IOException, ParseException {
		
		List<URL> editorFiles = new ArrayList<URL>();
		List<ListSetupFileIconWrapper> listFiles = new ArrayList<ListSetupFileIconWrapper>();
		
		Model model = null;
		if (!profileMode) {
			String depends = entityElem.getAttribute("depends");
			if (depends != null && depends.length() != 0) {
				try {
					Domain dm = setup.getDomain(depends);
					if (dm.getModel() == null) {
						throw new ParseException("Dependend model not found: " + depends);
					}
					model = new Model(dm.getModel());
				} catch (ConfigurationException e) {
					throw new ParseException("Invalid depends reference: " + depends);
				}
			} else {
				model = new Model();
			}
			
			domain.setModel(model);
		} else {
			model = domain.getModel();
		}
		
		NodeList nl = entityElem.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node node = nl.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element)node;
				if (element.getNodeName().equals("entity")) {
					// load specific attributes for the entity descriptor
					String fileName = element.getAttribute("file");
					String className = element.getAttribute("class");
					String iconKey = element.getAttribute("icon");
					
					try {
						EntityDescriptor descriptor = null;
						
						if (fileName != null && fileName.length() > 0) {
							URL entityFile = new URL(location, fileName);
							fileList.add(entityFile);
							if (!headerOnly && !profileMode) {
								descriptor = XmlEntityDescriptorReader.loadEntityDescriptor(entityFile);
								descriptor.setDomain(domain);
								model.addEntityDescriptor(descriptor);
							}
						} else if (!profileMode) {
							try {
								descriptor = new EntityDescriptor(XmlEntityDescriptorReader.getClassLoader().loadClass(className));
								descriptor.setDomain(domain);
								model.addEntityDescriptor(descriptor);
								
							} catch (Exception e) {
								throw new ParseException("Illegal entity class specified: " + e);
							}
						}
						
						if (null != descriptor) {
							descriptor.setIconKey(iconKey);
						}
						
						// register list and editor files to be loaded after the model
						// is completed
						NodeList nlEntity = element.getChildNodes();
						for (int a = 0; a < nlEntity.getLength(); a++) {
							if (nlEntity.item(a).getNodeType() == Node.ELEMENT_NODE) {
								Element subElm = (Element)nlEntity.item(a);
								if (subElm.getNodeName().equals("list")) {
									URL listFile = new URL(location, subElm.getAttribute("file"));
									fileList.add(listFile);
									
									ListSetupFileIconWrapper wrapper = new ListSetupFileIconWrapper();
									wrapper.setUrl(listFile);
									wrapper.setIconKey(subElm.getAttribute("icon"));
									
									listFiles.add(wrapper);
								} else if (subElm.getNodeName().equals("editor")) {
									URL editorFile = new URL(location, subElm.getAttribute("file"));
									fileList.add(editorFile);
									editorFiles.add(editorFile);
									
								} else if (subElm.getNodeName().equals("mailtemplate") && !profileMode) {
									String templateFile = subElm.getAttribute("file");
									if (templateFile != null && templateFile.length() > 0) {
										URL emailTemplate = new URL(location, templateFile);
										fileList.add(emailTemplate);
										if (!headerOnly) {
											descriptor.setEmailTemplate(emailTemplate);
										}
									}
								} else if (subElm.getNodeName().equals("report") && !profileMode) {
									String templateFile = subElm.getAttribute("file");
									if (templateFile != null && templateFile.length() > 0) {
										URL reportTemplate = new URL(location, templateFile);
										fileList.add(reportTemplate);
										if (!headerOnly) {
											ReportTemplate tpl = new ReportTemplate();
											tpl.setLocation(reportTemplate);
											tpl.setFilePath(templateFile);
											if (subElm.hasAttribute("title")) {
												tpl.setTitle(subElm.getAttribute("title"));
											}
											if (subElm.hasAttribute("right")) {
												tpl.setRight(subElm.getAttribute("right"));
											}
											descriptor.addReportTemplate(tpl);
										}
									}
								} 
							}
						}
					} catch (Exception e) {
						log.warn("Error loading EntityDescriptor for entity " + className + ". Skipping Descriptor! :" + e.toString());
					}
				}
			}
		}
		if (!profileMode) {
			setup.addModel(model);
		}
		
		if (!headerOnly) {
			// load lists first
			for (Iterator<ListSetupFileIconWrapper> it = listFiles.iterator(); it.hasNext(); ) {
				ListSetupFileIconWrapper lFile = it.next();
				ListSetup ls = XmlListSetupReader.loadListSetup(model, lFile.getUrl());
				
				ls.setIconKey(lFile.getIconKey());
				profile.addListSetup(ls);
			}
			// load editors
			for (Iterator<URL> it = editorFiles.iterator(); it.hasNext(); ) {
				URL eFile = it.next();
				EditorConfiguration conf = XmlEditorConfigReader.readConfiguration(model, eFile);
				profile.addEditorConfiguration(conf);
			}
		}
		
	}
	
	/**
	 * @param element
	 * @throws ParseException 
	 */
	private void loadLanguages(Element langElem) throws ParseException {
		
		String defaultLang = langElem.getAttribute("default");
		if (defaultLang == null) {
			throw new ParseException("no default language specified.");
		}
		setup.setDefaultLangId(defaultLang);
		
		NodeList nl = langElem.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node node = nl.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element)node;
				if (element.getNodeName().equals("lang")) {
					Language lang = new Language();
					lang.setId(element.getAttribute("id"));
					lang.setTitle(element.getAttribute("title"));
					setup.addLanguage(lang);
				}
			}
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
	
}
