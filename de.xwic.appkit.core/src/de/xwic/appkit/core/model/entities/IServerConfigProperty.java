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
package de.xwic.appkit.core.model.entities;

import de.xwic.appkit.core.dao.IEntity;

/**
 * Interface for the Server ConfigProperty. <p>
 * 
 * @author Ronny Pfretzschner
 */
public interface IServerConfigProperty extends IEntity {

	/**
	 * @return Returns the key.
	 */
	public String getKey();

	/**
	 * @param key The key to set.
	 */
	public void setKey(String key);

	/**
	 * @return Returns the value.
	 */
	public String getValue();

	/**
	 * @param value The value to set.
	 */
	public void setValue(String value);

}