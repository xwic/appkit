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
package de.xwic.appkit.webbase.prefstore;

import java.beans.PropertyChangeListener;
import java.io.IOException;

/**
 * Represents a storage for basic datatypes as a key-value pair. 
 *  
 * @author Florian Lippisch
 * @version $Revision: 1.1 $
 */
public interface IPreferenceStore {

	/**
	 * Returns true if any change of the values are ignored. This is usually the
	 * case if this storage is the default storage, temporary bound to an anonymous
	 * user.
	 * @return
	 */
	public boolean isReadonly(); 
	
	/**
	 * Add a new PropertyChangeListener.
	 * 
	 * @param listener
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener);
	
	/**
	 * Removes a propertyChangeListener.
	 * 
	 * @param listener
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener);
	
	/**
	 * Forces to store all changes to the storage.
	 */
	public void flush() throws IOException;
	
	/**
	 * Returns the value for the given key.
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public String getString(String key, String defaultValue);
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setValue(String key, String value);
	
	/**
	 * Set to true if changes should be ignored.
	 * @param readonly
	 */
	public void setReadonly(boolean readonly);
	
}
