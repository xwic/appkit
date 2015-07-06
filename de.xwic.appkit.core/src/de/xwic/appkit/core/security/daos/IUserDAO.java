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
package de.xwic.appkit.core.security.daos;

import java.util.Set;

import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.security.IUser;
import de.xwic.appkit.core.security.ScopeActionKey;

/**
 * @author Florian Lippisch
 */
public interface IUserDAO extends DAO<IUser> {

	/**
	 * Build a list of all rights for the specified user.
	 * @param user
	 * @return
	 */
	public Set<ScopeActionKey> buildAllRights(IUser user);
	
	/**
	 * @param logonName
	 * @return
	 */
	public IUser getUserByLogonName(String logonName);
	
}
