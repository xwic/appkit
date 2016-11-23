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

import java.util.List;

import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.model.entities.IMitarbeiter;
import de.xwic.appkit.core.model.entities.IUserViewConfiguration;

/**
 * @author Adrian Ionescu
 */
public interface IUserViewConfigurationDAO extends DAO<IUserViewConfiguration> {

	/**
	 * @param owner
	 * @param entityClassName
	 * @param viewId
	 * @return
	 */
	public IUserViewConfiguration getMainConfigurationForView(IMitarbeiter owner, String entityClassName, String viewId);

	/**
	 * @param owner
	 * @param entityClassName
	 * @param viewId
	 * @return
	 */
	public List<IUserViewConfiguration> getUserConfigurationsForView(IMitarbeiter owner, String entityClassName, String viewId);

	/**
	 * @param currentUser
	 * @param entityClassName
	 * @param viewId
	 * @return
	 */
	public List<IUserViewConfiguration> getPublicUserConfigurationsForView(IMitarbeiter currentUser, String entityClassName, String viewId);

	/**
	 * @param owner
	 * @param entityClassName
	 * @param viewId
	 * @param name
	 * @param currentId
	 * @return
	 */
	public boolean configNameExists(IMitarbeiter owner, String entityClassName, String viewId, String name, long currentId);
}
