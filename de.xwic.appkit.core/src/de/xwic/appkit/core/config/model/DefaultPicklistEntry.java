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
