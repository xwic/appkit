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

import java.util.Iterator;

import de.xwic.appkit.core.ApplicationData;
import de.xwic.appkit.core.dao.DAOCallback;
import de.xwic.appkit.core.dao.DAOProviderAPI;
import de.xwic.appkit.core.dao.DataAccessException;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.daos.impl.PicklisteDAO;
import de.xwic.appkit.core.model.entities.IPicklistEntry;
import de.xwic.appkit.core.model.entities.IPicklistText;

/**
 * @author Florian Lippisch
 */
public class LocalPicklisteDAO extends PicklisteDAO {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.AbstractDAO#delete(de.xwic.appkit.core.dao.IEntity)
	 */
	public void delete(final IEntity entity) throws DataAccessException {
		
		if (entity instanceof IPicklistEntry) {
	    	checkRights(ApplicationData.SECURITY_ACTION_DELETE);
	    	
	    	//remove entry from cache!
	    	//if an error occurs -> the entry will be reloaded correctly
	    	cache.removePicklistEntry((IPicklistEntry)entity);
	    	
	    	provider.execute(new DAOCallback() {
	       		public Object run(DAOProviderAPI api) {
	       			
	       			IPicklistEntry entry =(IPicklistEntry)entity;
	       			// remove texte first
	       			for (Iterator<IPicklistText> it = entry.getPickTextValues().iterator(); it.hasNext(); ) {
	       				IPicklistText text = it.next();
	       				api.delete(text);
	       				//it.remove();
	       			}
	       			//api.update(entry);
	       			entry.setPickTextValues(null);
	       			api.delete(entry);
	       			return null;
	       		}
	       	});
	
		} else {
			super.delete(entity);
		}
	}
	
}
