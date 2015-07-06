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
package de.xwic.appkit.webbase.history;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


/**
 * Helper class for the History topic. <p>
 * 
 * This object carries the actual Property and its
 * Getter-Methode as identifier. <br>
 * It also contains a Map of historyObjects of different versions.
 * 
 * @author Ronny Pfretzschner
 *
 */
public class PropertyVersionHelper {

	private Map<Integer, HistoryVersion> versions = null;
	private Method propertyMethod = null;
	private String propertyName = null;
	
	/**
	 * Constructor. <p>
	 * 
	 * @param propertyGetter The getter-method of the property
	 * @param propName Name of Property
	 */
	public PropertyVersionHelper(Method propertyGetter, String propName) {
		if (propertyGetter == null || propName == null) {
			throw new IllegalArgumentException("Method or property-name in " + getClass().getName() + " is null!");
		}
		
		versions = new HashMap<Integer, HistoryVersion>();
		this.propertyMethod = propertyGetter;
		this.propertyName = propName;
	}
	
	/**
	 * @return The getter of the property
	 */
	public Method getPropertyMethod() {
		return propertyMethod;
	}
	
	/**
	 * Add a Version object to the helper. <p>
	 * The index will be the later column index. <br>
	 * Caution, start with "1", not "0". "0" is reserved. 
	 *  
	 * @param index The version position as an int
	 * @param ver The version object
	 */
	public void addVersion(int index, HistoryVersion ver) {
		versions.put(new Integer(index), ver);
	}
	
	/**
	 * Returns the HistoryObject-wrapper of the given columnindex...
	 * 
	 * @param index
	 * @return The version object
	 */
	public HistoryVersion getHistoryVersion(int index) {
		return (HistoryVersion)versions.get(new Integer(index));
	}

	/**
	 * @return The property name
	 */
	public String getPropertyName() {
		return propertyName;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((propertyMethod == null) ? 0 : propertyMethod.hashCode());
		result = PRIME * result + ((propertyName == null) ? 0 : propertyName.hashCode());
		result = PRIME * result + ((versions == null) ? 0 : versions.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final PropertyVersionHelper other = (PropertyVersionHelper) obj;
		if (propertyMethod == null) {
			if (other.propertyMethod != null)
				return false;
		} else if (!propertyMethod.equals(other.propertyMethod))
			return false;
		if (propertyName == null) {
			if (other.propertyName != null)
				return false;
		} else if (!propertyName.equals(other.propertyName))
			return false;
		if (versions == null) {
			if (other.versions != null)
				return false;
		} else if (!versions.equals(other.versions))
			return false;
		return true;
	}
	
}
