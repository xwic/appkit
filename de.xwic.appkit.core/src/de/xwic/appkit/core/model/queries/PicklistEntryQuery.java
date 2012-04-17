/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.model.queries.AllEntriesForPicklistQuery
 * Created on 11.07.2005
 *
 */
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
