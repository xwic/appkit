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
/**
 * 
 */
package de.xwic.appkit.webbase.toolkit.util;

import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.entities.IPicklistEntry;

/**
 * Helper for velocity.
 * 
 * @author Ronny Pfretzschner
 *
 */
public class VtlTool {
	
	/**
	 * Get the text of the picklistentry.
	 * 
	 * @param entry
	 * @param langId
	 * @return
	 */
    public String getText(IPicklistEntry entry, String langId) {
        return entry.getBezeichnung(langId);
    }

    /**
     * Returns the title, specified in the configuration, of the
     * given entity.
     * 
     * @param entity
     * @return
     */
    public String getTitle(IEntity entity) {
        DAO dao = DAOSystem.findDAOforEntity(entity.type());
        if (dao != null) {
            return dao.buildTitle(entity);
        }
        return "[NO DAO for " + entity.type() + "]";
    }

}
