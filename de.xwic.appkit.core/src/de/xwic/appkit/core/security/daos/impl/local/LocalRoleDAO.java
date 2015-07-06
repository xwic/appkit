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

import java.util.Iterator;
import java.util.List;

import de.xwic.appkit.core.ApplicationData;
import de.xwic.appkit.core.dao.DAOCallback;
import de.xwic.appkit.core.dao.DAOProviderAPI;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.DataAccessException;
import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.security.IRight;
import de.xwic.appkit.core.security.IRole;
import de.xwic.appkit.core.security.daos.IRightDAO;
import de.xwic.appkit.core.security.daos.impl.RoleDAO;
import de.xwic.appkit.core.security.queries.RightQuery;
import de.xwic.appkit.core.security.queries.UniqueRoleQuery;

/**
 * Server DAO for the Role. <p>
 * 
 * @author Florian Lippisch
 */
public class LocalRoleDAO extends RoleDAO {
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.AbstractDAO#update(de.xwic.appkit.core.dao.IEntity)
	 */
	public void update(IEntity entity) throws DataAccessException {
		
		IRole newRole = (IRole) entity;
		EntityList erg = getEntities(null, new UniqueRoleQuery(newRole));
		
		if (erg.size() == 0) {
			super.update(entity);
		} else {
			throw new DataAccessException("Role mit dem Namen: \"" + newRole.getName() + "\" bereits vorhanden!");
		}
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.AbstractDAO#delete(de.xwic.appkit.core.dao.IEntity)
	 */
	public void delete(final IEntity entity) throws DataAccessException {
		
    	checkRights(ApplicationData.SECURITY_ACTION_DELETE);
    	
    	provider.execute(new DAOCallback() {
       		public Object run(DAOProviderAPI api) {
       			IRole role = (IRole)entity;

       			// delete all rights
       			IRightDAO rightDao = (IRightDAO)DAOSystem.getDAO(IRightDAO.class);
       			List<?> list = rightDao.getEntities(null, new RightQuery(role));
       			for (Iterator<?> it = list.iterator(); it.hasNext(); ) {
       				api.delete((IRight)it.next());
       			}
       			// now we can delete the role
       			api.delete(entity);
       			
       			return null;
       		}
       	});
				
	}
	
}
