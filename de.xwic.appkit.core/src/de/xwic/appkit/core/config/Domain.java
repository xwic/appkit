/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.config.Domain
 * Created on 07.11.2006 by Florian Lippisch
 *
 */
package de.xwic.appkit.core.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import de.xwic.appkit.core.config.model.Model;

/**
 * A domain contains resources and bundles.
 * @author Florian Lippisch
 */
public class Domain {

	private String id = null;
	private Setup setup = null;
	private Map<String, Resource> resources = new HashMap<String, Resource>();
	private Map<String, Bundle> bundles = new HashMap<String, Bundle>();
	private Model model = null;
	
	private Properties defaults = new Properties();
	
	/**
	 * Add a resource.
	 * @param resource
	 */
	public void addResource(Resource resource) {
		if(resources.containsKey(resource.getId())) {
			throw new IllegalArgumentException("A resource with this id is already registered.");
		}
		resources.put(resource.getId(), resource);
	}

	/**
	 * @param langId
	 * @param bundle
	 */
	public void addBundle(String langId, Bundle bundle) {
		this.addBundle(langId, bundle, false);
	}
	
	/**
	 * Add a bundle.
	 * @param basename
	 * @param location
	 */
	public void addBundle(String langId, Bundle bundle, boolean mergeIfExists) {
		Bundle currBundle = bundles.get(langId);
		if (currBundle != null) {			
			// merge is different than link, merge overrides common values while link
			// simply adds to the existing set			
			if (mergeIfExists) {
				currBundle.merge(bundle);
			} else {
				bundle.setLinkedBundle(currBundle);
				bundles.put(langId, bundle);
			}
		} else {		
			bundles.put(langId, bundle);
		}
	}
	
	/**
	 * Returns the bundle for the specified language.
	 * @param langId
	 * @return
	 * @throws ConfigurationException
	 */
	public Bundle getBundle(String langId) throws ConfigurationException {
		Bundle bundle = bundles.get(langId);
		if (bundle == null) {
			bundle = bundles.get(setup.getDefaultLangId());
			if (bundle == null) {
				throw new ConfigurationException("No bundle registered for the specified language");
			}
		}
		return bundle;
	}
	
	/**
	 * Returns a resource.
	 * @param id
	 * @return
	 * @throws ConfigurationException
	 */
	public Resource getResource(String resId) throws ConfigurationException {
		Resource res = resources.get(resId);
		if (res == null) {
			throw new ConfigurationException("A resource with the specified id is not registerd: " + resId);
		}
		return res;
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
	 * @return the model
	 */
	public Model getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(Model model) {
		this.model = model;
	}
	
	/**
	 * Returns the default preference value for the specified key or
	 * null if the key is not specified. 
	 * @param key
	 * @return
	 */
	public String getPrefDefault(String key) {
		return defaults.getProperty(key);
	}

	/**
	 * Returns the default preference value for the specified key.
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public String getPrefDefault(String key, String defaultValue) {
		return defaults.getProperty(key, defaultValue);
	}

	/**
	 * Returns the preference defaults.
	 * @return
	 */
	public Properties getPrefDefaults() {
		return defaults;
	}
	
	/**
	 * Set the default preference value for the specified key.
	 * @param key
	 * @param value
	 */
	public void setPrefDefault(String key, String value) {
		defaults.setProperty(key, value);
	}

	/**
	 * Finialize configuration and link bundles.
	 */
	void finalizeConfig(Setup setup) {
		this.setup = setup;
		String defLang = setup.getDefaultLangId();
		Bundle defaultBundle = bundles.get(defLang);
		if (defaultBundle != null) {
			for (Language lang : setup.getLanguages()) {
				if (!lang.getId().equalsIgnoreCase(defLang)) {
					Bundle bundle = bundles.get(lang.getId());
					if (bundle != null) {
						// find TOP element
						Bundle top = bundle;
						while (top.getLinkedBundle() != null) {
							top = top.getLinkedBundle();
						}
						// link language with default bundle.
						top.setLinkedBundle(defaultBundle);
					}
				}
			}
		}
		
		
	}

	
}
