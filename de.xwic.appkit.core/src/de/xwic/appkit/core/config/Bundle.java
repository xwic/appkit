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

import java.util.Map;
import java.util.Set;

/**
 * Wrapper for the map that contains the key/value pairs.
 * @author Florian Lippisch
 *
 */
public class Bundle {

	private Map<String, String> map = null;
	private Bundle linkedBundle = null;

	/**
	 * Constructor.
	 * @param stringMap
	 */
	public Bundle(Map<String, String> stringMap) {
		this.map = stringMap;
	}
	
	/**
	 * Returns a string.
	 * @param key
	 * @return
	 */
	public String getString(String key) {
		String value = map.get(key);
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
	
	/**
	 * @param bundle
	 */
	public void merge(Bundle bundle) {
		map.putAll(bundle.map);
	}
}
