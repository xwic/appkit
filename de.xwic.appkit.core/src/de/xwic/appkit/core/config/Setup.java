/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.config.Dictionary
 * Created on 02.11.2006
 *
 */
package de.xwic.appkit.core.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.config.model.Model;

/**
 * The setup is the central dictionary for the systems configuration. The model
 * is registered here as well as userprofiles, that contain the UI informations 
 * to build editors and lists.
 * 
 * The dictionary is itself no singleton, but it is recommended to centralize access
 * to the dictionary via a singleton.
 * 
 * @author Florian Lippisch
 */
public class Setup {

	/** The default ID for list setup and editors */
	public final static String ID_DEFAULT = "default";
	
	private String id = null;
	private String appTitle = null;
	private String version = null;
	private String defaultLangId = null;
	private List<Language> languages = new ArrayList<Language>();
	private List<Model> models = new ArrayList<Model>();
	private Map<String, Model> entityModelMap = new HashMap<String, Model>();
	private Map<String, Domain> domains = new HashMap<String, Domain>();
	private Profile defaultProfile = new Profile();
	private Map<String, Profile> profiles = new HashMap<String, Profile>();
	
	private Properties properties = new Properties();
	
	/**
	 * Adds a model to the dictionary. 
	 * @param model
	 */
	public void addModel(Model model) {
		models.add(model);
		// now add all entities from the model to the entityModelMap
		for (Iterator<String> it = model.getManagedEntities().iterator(); it.hasNext(); ) {
			entityModelMap.put(it.next(), model);
		}
	}
	
	/**
	 * @param lang
	 */
	public void addLanguage(Language lang) {
		languages.add(lang);
	}
	
	/**
	 * Returns the default user profile.
	 * @return
	 */
	public Profile getDefaultProfile() {
		return defaultProfile;
	}
	
	/**
	 * Returns the available profiles. Use only for information purpose.
	 * @return
	 */
	public Map<String, Profile> getProfiles() {
		return Collections.unmodifiableMap(profiles);
	}
	
	/**
	 * Returns the profile with the specified name.
	 * @param name
	 * @return
	 */
	public Profile getProfile(String name) {
		if (name == null) {
			return defaultProfile;
		} else {
			if (profiles.containsKey(name)) {
				return profiles.get(name);
			}
		}
		throw new IllegalArgumentException("A profile with that name ('" + name + "') does not exist.");
	}
	
	/**
	 * Add a user profile with the specified name.
	 * @param name
	 * @param profile
	 * @throws ConfigurationException 
	 */
	public void addProfile(String name, Profile profile) throws ConfigurationException {
		if (profiles.containsKey(name)) {
			throw new ConfigurationException("A profile with the name '" + name + "' already exists.");
		}
		profiles.put(name, profile);
	}
	
	/**
	 * Returns the EntityDescriptor for the specified entity type. Throws
	 * an IllegalArgumentException when no EntityDescriptor exists for the 
	 * specified classname.
	 * @param classname
	 * @return
	 * @throws ConfigurationException 
	 */
	public EntityDescriptor getEntityDescriptor(String classname) throws ConfigurationException {
		// find model
		Model model = entityModelMap.get(classname);
		if (model == null) {
			throw new ConfigurationException("No model is configured to handle the specified entity (" + classname +")");
		}
		return model.getEntityDescriptor(classname);
	}
	

	/**
	 * @return the appTitle
	 */
	public String getAppTitle() {
		return appTitle;
	}

	/**
	 * @param appTitle the appTitle to set
	 */
	public void setAppTitle(String appTitle) {
		this.appTitle = appTitle;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the languages
	 */
	public List<Language> getLanguages() {
		return languages;
	}

	/**
	 * @param languages the languages to set
	 */
	public void setLanguages(List<Language> languages) {
		this.languages = languages;
	}

	/**
	 * @param domain
	 */
	public void addDomain(Domain domain) {
		domains.put(domain.getId(), domain);
		
		// "initialize" the domain after it has been loaded. Used to link bundles to default language
		domain.finalizeConfig(this);
	}

	/**
	 * Returns the IDs of all domains.
	 * @return
	 */
	public Set<String> getDomains() {
		return domains.keySet();
	}
	
	/**
	 * @return the defaultLangId
	 */
	public String getDefaultLangId() {
		return defaultLangId;
	}

	/**
	 * @param defaultLangId the defaultLangId to set
	 */
	public void setDefaultLangId(String defaultLangId) {
		this.defaultLangId = defaultLangId;
	}

	/**
	 * @param string
	 * @return
	 * @throws ConfigurationException 
	 */
	public Domain getDomain(String domainId) throws ConfigurationException {
		Domain domain = domains.get(domainId);
		if (domain == null) {
			throw new ConfigurationException("The specified domain is not configured: " + domainId);
		}
		return domain;
	}

	/**
	 * Returns the first registered model (for testing).
	 * @return
	 */
	public Model getModel() {
		return models.get(0);
	}

	/**
	 * Returns the available models.
	 * @return
	 */
	public List<Model> getModels() {
		return models;
	}
	
	/**
	 * Returns the value for the specified key. If the key is not specified,
	 * null is returned.
	 * @param key
	 * @return
	 */
	public String getProperty(String key) {
		return properties.getProperty(key);
	}

	/**
	 * Returns the value for the specified key. If the key is not specified,
	 * the default value is returned.
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public String getProperty(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}
	
	/**
	 * Set the value of the specified property.
	 * @param key
	 * @param value
	 */
	public void setProperty(String key, String value) {
		properties.setProperty(key, value);
	}

	/**
	 * Returns the properties.
	 * @return
	 */
	public Properties getProperties() {
		return properties;
	}

	/**
	 * This method is invoked by the XmlConfigLoader when the configuration has been 
	 * loaded completly. This gives the setup/profiles the possibility to initialize
	 * elements that depend on each other.
	 */
	public void initializeConfig() throws ConfigurationException {
		
		// initialize all profiles
		defaultProfile.initializeConfig();
		for (Profile profile : profiles.values()) {
			profile.initializeConfig();
		}
	}
}
