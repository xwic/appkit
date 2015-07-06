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
package de.xwic.appkit.core.security.daos.impl.local;

import de.xwic.appkit.core.dao.DataAccessException;
import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.security.IUser;
import de.xwic.appkit.core.security.daos.impl.UserDAO;
import de.xwic.appkit.core.security.queries.UniqueUserQuery;

/**
 * Server DAO for the User. <p>
 * 
 * @author Florian Lippisch
 */
public class LocalUserDAO extends UserDAO {
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.AbstractDAO#update(de.xwic.appkit.core.dao.IEntity)
	 */
	public void update(IEntity entity) throws DataAccessException {
		
		IUser newUser = (IUser) entity;
		EntityList erg = getEntities(null, new UniqueUserQuery(newUser));
		
		if (erg.size() == 0) {
			super.update(entity);
		} else {
			throw new DataAccessException("User mit dem LogonNamen: \"" + newUser.getName() + "\" bereits vorhanden!");
		}
	}

}
