/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.model.queries.ServerConfigPropertyByKeyQuery
 * Created on 24.08.2005
 *
 */
package de.xwic.appkit.core.model.queries;

import de.xwic.appkit.core.dao.EntityQuery;

/**
 * Query for getting a ServerConfigProperty by the given key. <p>
 * 
 * @author Ronny Pfretzschner
 *
 */
public class ServerConfigPropertyByKeyQuery extends EntityQuery {

    private String key = null;
    
    /**
     * Constructor.
     */
    public ServerConfigPropertyByKeyQuery(){
    }
    
    /**
     * Query for getting a ServerConfigProperty by the given key. <p>
     * 
     * @param key of the property
     */
    public ServerConfigPropertyByKeyQuery(String key) {
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
