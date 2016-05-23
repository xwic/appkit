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
package de.xwic.appkit.core.model.daos;

import de.xwic.appkit.core.dao.event.DAOWithEvent;
import de.xwic.appkit.core.model.entities.IServerConfigProperty;

/**
 * Interface for the Server ConfigProperty DAO. <p>
 *
 * @author Ronny Pfretzschner
 *
 */
public interface IServerConfigPropertyDAO extends DAOWithEvent<IServerConfigProperty> {

	/**
	 * Creates a ServerConfigProperty with a boolean as value. <p>
	 *
	 * @param key The key as String
	 * @param val The value as Boolean
	 */
	public void setConfigProperty(String key, boolean val);

	/**
	 * Creates a ServerConfigProperty with an int as value. <p>
	 *
	 * @param key The key as String
	 * @param val The value as int
	 */
	public void setConfigProperty(String key, int val);

	/**
	 * Creates a ServerConfigProperty with an double as value. <p>
	 *
	 * @param key The key as String
	 * @param val The value as double
	 */
	public void setConfigProperty(String key, double val);

	/**
	 * Creates a ServerConfigProperty with a String as value. <p>
	 *
	 * @param key The key as String
	 * @param val The value as String
	 */
	public void setConfigProperty(String key, String val);

	/**
	 * Searches for a ServerConfigProperty with the given key and
	 * return its value. <p>
	 *
	 * @param key of the ConfigProperty
	 * @return value as boolean
	 */
	public boolean getConfigBoolean(String key);

	/**
	 * Searches for a ServerConfigProperty with the given key and
	 * return its value. <p>
	 *
	 * @param key of the ConfigProperty
	 * @return value as int
	 */
	public int getConfigInteger(String key);

	/**
	 * Searches for a ServerConfigProperty with the given key and
	 * return its value. <p>
	 *
	 * @param key of the ConfigProperty
	 * @return value as double
	 */
	public double getConfigDouble(String key);

	/**
	 * Searches for a ServerConfigProperty with the given key and
	 * return its value. <p>
	 *
	 * @param key of the ConfigProperty
	 * @return value as String
	 */
	public String getConfigString(String key);

	/**
	 * Returns a ServerConfigProperty with the given key. <p>
	 *
	 * @param key The key looking for
	 * @return IServerConfigProperty
	 */
	public IServerConfigProperty getConfigProperty(String key);
	
	/**
	 * Returns a ServerConfigProperty with the given key. <p>
	 * 
	 * @param key
	 * @param ignoreCache
	 * @return
	 */
	public IServerConfigProperty getConfigProperty(String key, boolean ignoreCache);

	/**
	 * Clear the cache
	 */
	public void dropCache();
}
