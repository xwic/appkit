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
package de.xwic.appkit.core.security.daos.impl;

import de.xwic.appkit.core.dao.AbstractDAO;
import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.dao.Limit;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.core.security.IRole;
import de.xwic.appkit.core.security.daos.IRoleDAO;
import de.xwic.appkit.core.security.impl.Role;

/**
 * @author Florian Lippisch
 */
public class RoleDAO extends AbstractDAO<IRole, Role> implements IRoleDAO {

	/**
	 *
	 */
	public RoleDAO() {
		super(IRole.class, Role.class);
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.daos.IRoleDAO#getRoleByName(java.lang.String)
	 */
	@Override
	public IRole getRoleByName(String roleName) {
		
		PropertyQuery pq = new PropertyQuery();
		pq.addEquals("name", roleName);
		
		EntityList<IRole> list = getEntities(new Limit(0,1), pq);
		
		if (!list.isEmpty()) {
			return list.get(0);
		}
		
		return null;
	}

}
