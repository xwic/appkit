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
package de.xwic.appkit.core.security.queries;

import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.security.IRole;

/**
 * @author Florian Lippisch
 */
public class UniqueRoleQuery extends EntityQuery {

	private IRole role = null;
	
	/**
	 * Default Constructor.
	 */
	public UniqueRoleQuery() {
		
	}

	/**
	 * 
	 */
	public UniqueRoleQuery(IRole role ) {
		this.role = role;
	}

	/**
	 * @return Returns the action.
	 */
	public IRole getRole() {
		return role;
	}

	/**
	 * @param action The action to set.
	 */
	public void setRole(IRole role) {
		this.role = role;
	}

}
