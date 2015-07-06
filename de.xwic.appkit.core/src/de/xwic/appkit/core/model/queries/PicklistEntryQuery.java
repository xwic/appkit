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
package de.xwic.appkit.core.model.queries;

import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.model.entities.IPickliste;

/**
 * @author Ronny Pfretzschner
 */
public class PicklistEntryQuery extends EntityQuery {

    private int pickListID = -1;
    
    private int entryID;
    
    /**
     * Constructor.
     *
     */
    public PicklistEntryQuery(){
    }
    
    /**
     * Constructor with IPickliste parameter. <p>
     * 
     * @param list the IPickliste of which all entries will be searched
     */
    public PicklistEntryQuery(IPickliste list) {
        if (list == null) {
            throw new IllegalArgumentException("Error in constructor in class: " + getClass().getName() + "! Arguments not allowed!");
        }
        this.pickListID = list.getId();
    }
    
    /**
     * 
     * Constructor with IPicklistEntry ID to search for just PicklistEntries
     * with the given id. <p>
     * 
     * @param id of entry which should be searched. 
     */
    public PicklistEntryQuery(int id) {
        if (id < 1) {
            throw new IllegalArgumentException("Error in constructor in class: " + getClass().getName() + "! Arguments not allowed!");
        }
        this.entryID = id;
    }

    
    
    /**
	 * @return Returns the pickListID.
	 */
	public int getPickListID() {
		return pickListID;
	}

	/**
	 * @param pickListID The pickListID to set.
	 */
	public void setPickListID(int pickListID) {
		this.pickListID = pickListID;
	}

	/**
     * @return Returns the entryID.
     */
    public int getEntryID() {
        return entryID;
    }
    /**
     * @param entryID The entryID to set.
     */
    public void setEntryID(int entryID) {
        this.entryID = entryID;
    }
}
