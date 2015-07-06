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
import de.xwic.appkit.core.security.IRight;
import de.xwic.appkit.core.security.daos.impl.RightDAO;
import de.xwic.appkit.core.security.queries.UniqueRightQuery;

/**
 * Server DAO for the Right. <p>
 * 
 * @author Florian Lippisch
 */
public class LocalRightDAO extends RightDAO {
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.AbstractDAO#update(de.xwic.appkit.core.dao.IEntity)
	 */
	public void update(IEntity entity) throws DataAccessException {
		
		IRight newRight = (IRight) entity;
		EntityList erg = getEntities(null, new UniqueRightQuery(newRight));
		
		if (erg.size() == 0) {
			super.update(entity);
		} else {
			throw new DataAccessException("Right bereits vorhanden!");
		}
	}

}
