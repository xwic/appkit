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

import de.xwic.appkit.core.ApplicationData;
import de.xwic.appkit.core.dao.DAOCallback;
import de.xwic.appkit.core.dao.DAOProviderAPI;
import de.xwic.appkit.core.dao.DataAccessException;
import de.xwic.appkit.core.dao.HistoryTool;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.dao.IHistory;
import de.xwic.appkit.core.model.daos.impl.MitarbeiterDAO;
import de.xwic.appkit.core.model.entities.impl.history.MitarbeiterHistory;

/**
 * @author Florian Lippisch
 */
public class LocalMitarbeiterDAO extends MitarbeiterDAO {
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.AbstractDAO#update(de.xwic.appkit.core.dao.IEntity)
	 */
	public void update(final IEntity entity) throws DataAccessException {

    	checkRights(ApplicationData.SECURITY_ACTION_UPDATE);

		
        provider.execute(new DAOCallback() {
       		public Object run(DAOProviderAPI api) {

       			int reason = entity.getId() == 0 ? IHistory.REASON_CREATED : IHistory.REASON_UPDATED;
       			api.update(entity);
       			
       			if (getEntityDescriptor().isHistory()) {
	       			MitarbeiterHistory his = new MitarbeiterHistory();
	
	       			HistoryTool.createHistoryEntity(entity, his);
	       			his.setEntityID(entity.getId());
	       			his.setEntityVersion(entity.getVersion() + (reason == IHistory.REASON_CREATED ? 0 : 1));
	       			his.setHistoryReason(reason);
	       			api.update(his);
      			}
       			return null;
       		       			
       		}
       	});
	}

}
