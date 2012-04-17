/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.model.entities.impl.IServerConfigProperty
 * Created on 24.08.2005
 *
 */
package de.xwic.appkit.core.model.entities;

import de.xwic.appkit.core.dao.IEntity;

/**
 * Interface for the Server ConfigProperty. <p>
 * 
 * @author Ronny Pfretzschner
 */
public interface IServerConfigProperty extends IEntity {

	/**
	 * @return Returns the key.
	 */
	public String getKey();

	/**
	 * @param key The key to set.
	 */
	public void setKey(String key);

	/**
	 * @return Returns the value.
	 */
	public String getValue();

	/**
	 * @param value The value to set.
	 */
	public void setValue(String value);

}