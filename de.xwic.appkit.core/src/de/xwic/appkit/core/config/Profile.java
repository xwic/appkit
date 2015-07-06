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
import java.util.List;
import java.util.Map;

import de.xwic.appkit.core.config.editor.EditorConfiguration;
import de.xwic.appkit.core.config.list.ListColumn;
import de.xwic.appkit.core.config.list.ListSetup;

/**
 * The profile contains the user specific configuration elements
 * like editors or list setups.
 * 
 * @author Florian Lippisch
 */
public class Profile {
	
	private Profile baseProfile = null;
	
	private Map<String, Map<String, ListSetup>> listMap = new HashMap<String, Map<String, ListSetup>>();
	private Map<String, Map<String, EditorConfiguration>> editorMap = new HashMap<String, Map<String, EditorConfiguration>>();
	private Map<String, Boolean> features = new HashMap<String, Boolean>();

	/**
	 * Create a new root profile.
	 */
	Profile() {
		
	}
	
	/**
	 * Constructs a new profile based upon the specified profile.
	 * @param baseProfile
	 */
	public Profile(Profile baseProfile) {
		this.baseProfile = baseProfile;
		if (baseProfile == null) {
			throw new NullPointerException("baseProfile must not be null.");
		}
	}
	
	/**
	 * @param ls
	 */
	public void addListSetup(ListSetup ls) {
		Map<String, ListSetup> map = listMap.get(ls.getTypeClass());
		if (map == null) {
			map = new HashMap<String, ListSetup>();
			listMap.put(ls.getTypeClass(), map);
		}
		map.put(ls.getListId(), ls);
	}

	/**
	 * @param conf
	 */
	public void addEditorConfiguration(EditorConfiguration conf) {
		String classname = conf.getEntityType().getClassname();
		Map<String, EditorConfiguration> map = editorMap.get(classname);
		if (map == null) {
			map = new HashMap<String, EditorConfiguration>();
			editorMap.put(classname, map);
		}
		map.put(conf.getId(), conf);
	}

	/**
	 * Enables a feature within the product setup.
	 * @param featureId
	 */
	public void addFeature(String featureId, boolean enabled) {
		features.put(featureId, new Boolean(enabled));
	}
	
	/**
	 * Returns true if the specified feature is enabled.
	 * @param featureId
	 * @return
	 */
	public boolean isFeatureEnabled(String featureId) {
		if (features.containsKey(featureId)) {
			Boolean b = features.get(featureId);
			return b.booleanValue();
		}
		if (baseProfile != null) {
			return baseProfile.isFeatureEnabled(featureId);
		}
		return false;
	}
	
	/**
	 * Returns a map of features and the state of the feature.
	 * @return Map of String and Boolean values.
	 */
	public Map<String, Boolean> getFeatures() {
		return features;
	}

	/**
	 * Returns the default ListSetup for the specified entity type.
	 * @param classname
	 * @return
	 * @throws ConfigurationException 
	 */
	public ListSetup getListSetup(String classname) throws ConfigurationException {
		return getListSetup(classname, Setup.ID_DEFAULT);
	}
	
	/**
	 * Returns the ListSetup for the specified listId for the entity type.
	 * @param classname
	 * @param listId
	 * @return
	 * @throws ConfigurationException 
	 */
	public ListSetup getListSetup(String classname, String listId) throws ConfigurationException {
		ListSetup ls = getSingleListSetup(classname, listId);

		if (null == ls) {
			throw new ConfigurationException("The specified listsetup is not configured: " + classname + " (" + listId + ")");			
		}
		
		return ls;
	}

	/**
	 * @param classname
	 * @param listId
	 * @return
	 * @throws ConfigurationException
	 */
	private ListSetup getSingleListSetup(String classname, String listId) throws ConfigurationException {
		Map<?, ?> map = listMap.get(classname);
		ListSetup ls = null;
		
		if (map != null) {
			ls = (ListSetup)map.get(listId);
		}
		
		if (ls == null) {
			if (baseProfile != null) {
				ls = baseProfile.getListSetup(classname, listId);
			} 
		}
		
		return ls;
	}
	
	/**
	 * Returns the default editor configuration for the specified entitytype.
	 * @param classname
	 * @return
	 * @throws ConfigurationException 
	 */
	public EditorConfiguration getEditorConfiguration(String classname) throws ConfigurationException {
		return getEditorConfiguration(classname, Setup.ID_DEFAULT);
	}

	/**
	 * Returns the EditorConfiguration for the specified id for the entity type.
	 * @param classname
	 * @param editorId
	 * @return
	 * @throws ConfigurationException 
	 */
	public EditorConfiguration getEditorConfiguration(String classname, String editorId) throws ConfigurationException {
		Map<?, ?> map = editorMap.get(classname);
		EditorConfiguration conf = null;
		if (map != null) {
			conf = (EditorConfiguration)map.get(editorId);
		}
		if (conf == null) {
			if (baseProfile != null) {
				return baseProfile.getEditorConfiguration(classname, editorId);
			}
			throw new ConfigurationException("The specified editor-configuration is not configured: " + classname + " (" + editorId + ")");
		}
		return conf;
	}

	/**
	 * Initialize a profile and link configurations together. (i.e. ListSetup)
	 * @throws ConfigurationException 
	 */
	public void initializeConfig() throws ConfigurationException {
		
		// check for unlinked ListSetups.
		// here we concat the list setup with the "extends" one

		for (String classname : listMap.keySet()) {
			Map<String, ListSetup> lsMap = listMap.get(classname);
			for (ListSetup ls : lsMap.values()) {
				extendListSetup(classname, ls);
			}
		}
	}
	
	private void extendListSetup(String classname, ListSetup ls) throws ConfigurationException {

		if (ls.getExtendsID() == null || ls.getExtendsID().length() == 0 || ls.isMerged()) {
			// the ListSetup does not extend another setup or is already merged with the super-list
			return;
		}

		ls.setMerged(true);	// setting it to true here already prevents endless-loops
			
		ListSetup extendedLS = getSingleListSetup(classname, ls.getExtendsID());
		
		if (null == extendedLS) {
			throw new ConfigurationException("The listsetup '" + ls.getListId() + "' for class '" + classname + "' extends a list that does not exist: " + ls.getExtendsID());			
		}
		
		extendListSetup(classname, extendedLS); // make sure it is extended as well
		
		// now add the columns from the extended
		List<ListColumn> extColumns = extendedLS.getColumns();
		List<ListColumn> columns = ls.getColumns();
		
		for (ListColumn extLc : extColumns) {
			// columns are identified by propertyId, which does not need to be unique
			boolean found = false;
			for (ListColumn lc : columns) {
				if (lc.getPropertyId().equals(extLc.getPropertyId())) {
					// update entry
					found = true; 
					if (lc.getDefaultWidth() == 0) {
						lc.setDefaultWidth(extLc.getDefaultWidth());
					}
					if (lc.getTitleId() == null) {
						lc.setTitleId(extLc.getTitleId());
					}
					if (lc.getDescriptionId() == null) {
						lc.setDescriptionId(extLc.getDescriptionId());
					}
					if (lc.getCustomLabelProviderClass() == null) {
						lc.setCustomLabelProviderClass(extLc.getCustomLabelProviderClass());
					}
				}
			}
			if (!found) {
				// clone the list column, and make it default invisible 
				ListColumn cloneLC = extLc.cloneListColumn();
				cloneLC.setDefaultVisible(false);
				columns.add(cloneLC);
			}
		}
		
		
	}

}
