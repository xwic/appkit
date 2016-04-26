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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.config.model.EntityDescriptor;

/**
 * @author Florian Lippisch
 */
public class EditorConfiguration {

	/** default list and editor id name */
	public final static String DEFAULT = "default";
	
	private String id = DEFAULT;
	private EntityDescriptor entityType = null;
	
	private List<ETab> tabs = new ArrayList<ETab>();

	private List<ESubTab> subTabs = new ArrayList<ESubTab>();
	private Map<String, Style> styleTemplates = new HashMap<String, Style>();
	
	private String globalScript = null;
	
	/**
	 * Constructor.
	 */
	public EditorConfiguration() {
		
	}
	
	/**
	 * Constructor.
	 * @param entityType
	 */
	public EditorConfiguration(EntityDescriptor entityType) {
		this.entityType = entityType;
	}
	
	/**
	 * Add a tab to the editor.
	 * @param tab
	 */
	public void addTab(ETab tab) {
		tabs.add(tab);
	}
	
	/**
	 * Returns the list of tabs.
	 * @return
	 */
	public List<ETab> getTabs() {
		return Collections.unmodifiableList(tabs);
	}

	/**
	 * @return Returns the entityType.
	 */
	public EntityDescriptor getEntityType() {
		return entityType;
	}

	/**
	 * @param entityType The entityType to set.
	 */
	public void setEntityType(EntityDescriptor entityType) {
		this.entityType = entityType;
	}

	/**
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Add a style template.
	 * @param id
	 * @param style
	 */
	public void addStyleTemplate(String tplId, Style style) {
		styleTemplates.put(tplId, style);
	}
	
	/**
	 * Returns the style template with the specified id.
	 * @param id
	 * @return
	 * @throws ConfigurationException
	 */
	public Style getStyleTemplate(String tplId) throws ConfigurationException {
		if (!styleTemplates.containsKey(tplId)) {
			throw new ConfigurationException("No styleTemplate with the id " + tplId);
		}
		return styleTemplates.get(tplId);
	}

	/**
	 * @return the globalScript
	 */
	public String getGlobalScript() {
		return globalScript;
	}

	/**
	 * @param globalScript the globalScript to set
	 */
	public void setGlobalScript(String globalScript) {
		this.globalScript = globalScript;
	}


	/**
	 * Add a sub tab to the editor.
	 * @param tab
	 */
	public void addSubTab(ESubTab tab) {
		subTabs.add(tab);
	}

	public List<ESubTab> getSubTabs() {
		return subTabs;
	}
}
