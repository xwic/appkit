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
package de.xwic.appkit.core.model.daos.impl.local;

import de.xwic.appkit.core.dao.DataAccessException;
import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.daos.impl.SalesTeamDAO;
import de.xwic.appkit.core.model.entities.ISalesTeam;
import de.xwic.appkit.core.model.queries.UniqueSTQuery;

/**
 * Server DAO for the SalesTeam entity. <p>
 *  
 * @author Ronny Pfretzschner
 */
public class LocalSalesTeamDAO extends SalesTeamDAO {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.AbstractDAO#update(de.xwic.appkit.core.dao.IEntity)
	 */
	public void update(IEntity entity) throws DataAccessException {
		ISalesTeam newST = (ISalesTeam) entity;
		EntityList erg = getEntities(null, new UniqueSTQuery(newST));
		
		if (erg.size() == 0) {
			super.update(entity);
		} else {
			StringBuffer errMessage = new StringBuffer();
			errMessage.append("SalesTeam mit Bezeichnung: ").append(newST.getBezeichnung());
			errMessage.append(" existiert bereits.");
			
			throw new DataAccessException(errMessage.toString());
		}	
	}
}
