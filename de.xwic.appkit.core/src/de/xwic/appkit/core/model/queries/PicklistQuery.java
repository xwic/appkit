/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.model.queries.PicklistQuery
 * Created on 25.07.2005
 *
 */
package de.xwic.appkit.core.model.queries;

import de.xwic.appkit.core.dao.EntityQuery;

/**
 * @author Ronny Pfretzschner
 */
public class PicklistQuery extends EntityQuery {

    private String key = null;
    
    /**
     * Constructor.
     *
     */
    public PicklistQuery(){
    }
    
    /**
     * Constructor - query by key.
     * @param key
     */
    public PicklistQuery(String key) {
        if (key == null || key.length() < 1) {
            throw new IllegalArgumentException("IllegalArgumentException in constructor in class: " + getClass().getName() + "! Arguments not allowed!");
        }
        this.key = key;
    }
    
    /**
     * @return Returns the key.
     */
    public String getKey() {
        return key;
    }
    /**
     * @param key The key to set.
     */
    public void setKey(String key) {
        this.key = key;
    }
}
