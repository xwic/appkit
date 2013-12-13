/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.config.Bundle
 * Created on 07.11.2006 by Florian Lippisch
 *
 */
package de.xwic.appkit.core.config;

import java.util.Map;
import java.util.Set;

/**
 * Wrapper for the map that contains the key/value pairs.
 * @author Florian Lippisch
 *
 */
public class Bundle {

	private Map<?, ?> map = null;
	private Bundle linkedBundle = null;

	/**
	 * Constructor.
	 * @param stringMap
	 */
	public Bundle(Map<?, ?> stringMap) {
		this.map = stringMap;
	}
	
	/**
	 * Returns a string.
	 * @param key
	 * @return
	 */
	public String getString(String key) {
		String value = (String)map.get(key);
		if (value == null) {
			if (linkedBundle != null) {
				value = linkedBundle.getString(key);
			} else {
				value = "!" + key + "!";
			}
		}
		return value;
	}

	/**
	 * @return the linkedBundle
	 */
	public Bundle getLinkedBundle() {
		return linkedBundle;
	}

	/**
	 * @param linkedBundle the linkedBundle to set
	 */
	public void setLinkedBundle(Bundle linkedBundle) {
		this.linkedBundle = linkedBundle;
	}
	
	/**
	 * @return
	 */
	public Set getKeys() {
		return map.keySet();
	}
	
}
