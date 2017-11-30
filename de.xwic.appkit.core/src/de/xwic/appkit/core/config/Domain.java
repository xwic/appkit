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
	 * Returns true if a bundle for the given language exists.
	 * @param langId
	 * @return
	 */
	public boolean hasBundle(String langId) {
		return bundles.containsKey(langId);
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
				throw new ConfigurationException("Missing fallback bundle for domain " + id);
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
	 * Returns all resources associated with this domain
	 * 
	 * @return
	 */
	public Map<String, Resource> getResources() {
		return resources;
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
						if (!top.equals(defaultBundle)) {
							top.setLinkedBundle(defaultBundle);
						}
					}
				}
			}
		}
		
		
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Domain other = (Domain) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	
}
