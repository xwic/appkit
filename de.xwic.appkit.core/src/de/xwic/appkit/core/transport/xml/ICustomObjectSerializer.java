/*
 * Copyright (c) 2009 Network Appliance, Inc.
 * All rights reserved.
 */

package de.xwic.appkit.core.transport.xml;

import org.dom4j.Element;

/**
 * Used to perform custom serialization and de-serialization of objects.
 * 
 * @author lippisch
 */
public interface ICustomObjectSerializer {

	/**
	 * Must return true if the handler can serialize this object.
	 * @param object
	 * @return
	 */
	public boolean handlesObject(Object object);
	
	/**
	 * Must return true if the handler can deserialize objects of this type.
	 * @param clazz
	 * @return
	 */
	public boolean handlesType(Class<?> clazz);
	
	/**
	 * Serialize an object.
	 * @param elm
	 * @param object
	 */
	public void serialize(Element elm, Object object) throws TransportException;
	
	/**
	 * Deserialize an object.
	 * @param elm
	 * @return
	 */
	public Object deserialize(Element elm) throws TransportException;
	
}
