/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.model.daos.IServerConfigProperty
 * Created on 24.08.2005
 *
 */
package de.xwic.appkit.core.model.daos;

import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.model.entities.IServerConfigProperty;

/**
 * Interface for the Server ConfigProperty DAO. <p>
 * 
 * @author Ronny Pfretzschner
 *
 */
public interface IServerConfigPropertyDAO extends DAO {

	/**
	 * Creates a ServerConfigProperty with a boolean as value. <p>
	 * 
	 * @param key The key as String
	 * @param val The value as Boolean
	 */
	public void setConfigProperty(String key, boolean val);
	
	/**
	 * Creates a ServerConfigProperty with an int as value. <p>
	 * 
	 * @param key The key as String
	 * @param val The value as int
	 */
	public void setConfigProperty(String key, int val);

	/**
	 * Creates a ServerConfigProperty with an double as value. <p>
	 * 
	 * @param key The key as String
	 * @param val The value as double
	 */
	public void setConfigProperty(String key, double val);

	/**
	 * Creates a ServerConfigProperty with a String as value. <p>
	 * 
	 * @param key The key as String
	 * @param val The value as String
	 */
	public void setConfigProperty(String key, String val);
	
	/**
	 * Searches for a ServerConfigProperty with the given key and
	 * return its value. <p>
	 * 
	 * @param key of the ConfigProperty
	 * @return value as boolean
	 */
	public boolean getConfigBoolean(String key);
	
	/**
	 * Searches for a ServerConfigProperty with the given key and
	 * return its value. <p>
	 * 
	 * @param key of the ConfigProperty
	 * @return value as int
	 */
	public int getConfigInteger(String key);
	
	/**
	 * Searches for a ServerConfigProperty with the given key and
	 * return its value. <p>
	 * 
	 * @param key of the ConfigProperty
	 * @return value as double
	 */
	public double getConfigDouble(String key);

	/**
	 * Searches for a ServerConfigProperty with the given key and
	 * return its value. <p>
	 * 
	 * @param key of the ConfigProperty
	 * @return value as String
	 */
	public String getConfigString(String key);
	
	/**
	 * Returns a ServerConfigProperty with the given key. <p>
	 * 
	 * @param key The key looking for
	 * @return IServerConfigProperty
	 */
	public IServerConfigProperty getConfigProperty(String key);
}
