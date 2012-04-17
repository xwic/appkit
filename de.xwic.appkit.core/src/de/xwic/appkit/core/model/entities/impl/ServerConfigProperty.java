/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.model.entities.impl.ServerConfigProperty
 * Created on 24.08.2005
 *
 */
package de.xwic.appkit.core.model.entities.impl;

import de.xwic.appkit.core.dao.Entity;
import de.xwic.appkit.core.model.entities.IServerConfigProperty;

/**
 * Property object in key <-> value design. <p>
 * 
 * Access over DAO only!
 * 
 * @author Ronny Pfretzschner
 */
public class ServerConfigProperty extends Entity implements IServerConfigProperty {
	
	private String key = "";
	private String value = "";

	/**
	 * default constructor. <p>
	 * Just for hibernate and webservice.
	 * Access over DAO only!
	 *  
	 */
	public ServerConfigProperty(){
	}

	/**
	 * Constructor. <p>
	 * Access over DAO only!
	 * 
	 * @param key
	 * @param value
	 */
	public ServerConfigProperty(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.IServerConfigProperty#getKey()
	 */
	public String getKey() {
		return key;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.IServerConfigProperty#setKey(java.lang.String)
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.IServerConfigProperty#getValue()
	 */
	public String getValue() {
		return value;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.IServerConfigProperty#setValue(java.lang.String)
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	/* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Key: ").append(key).append("; ");
        buffer.append("Value: ").append(value).append("; ");
        
        return buffer.toString(); 
    }
    
    /* (non-Javadoc)
     * @see de.xwic.appkit.core.dao.Entity#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        if (obj == null) {
            return false;
        }
        
        if (obj.getClass() != this.getClass()) {
            return false;
        }

        ServerConfigProperty p = (ServerConfigProperty) obj;
        
        if (key.equals(p.getKey()) &&
            value.equals(p.getValue())) {
            return true;
        }
        
        return false;
    }
    
    /* (non-Javadoc)
     * @see de.xwic.appkit.core.dao.Entity#hashCode()
     */
    public int hashCode() {
        int hc = 11;
        int multiplier = 3;
        
        hc = hc * multiplier + key.hashCode();
        hc = hc * multiplier + value.hashCode();
        
       return hc; 
    }
}
