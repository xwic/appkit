/*
 * Copyright 2005, 2006 pol GmbH
 *
 * de.xwic.appkit.core.config.editor.EditorConfiguration
 * Created on 02.11.2006
 *
 */
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
}
