/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.config.model.DefaultPicklistEntry
 * Created on 28.01.2008 by Florian Lippisch
 *
 */
package de.xwic.appkit.core.config.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Specifies a default picklist entry that is required by the 
 * application. This is used for properties that 
 * @author Florian Lippisch
 */
public class DefaultPicklistEntry {

	private String key = null;
	private int index = 0;
	private Map<String, String> titles = new HashMap<String, String>();
	
	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}
	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}
	
	/**
	 * Add a title.
	 * @param langId
	 * @param title
	 */
	public void addTitle(String langId, String title) {
		titles.put(langId, title);
	}
	
	/**
	 * @return the titles
	 */
	public Map<String, String> getTitles() {
		return titles;
	}
	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}
	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}
	
}
