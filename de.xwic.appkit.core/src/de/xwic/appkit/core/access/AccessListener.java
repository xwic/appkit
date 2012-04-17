/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.access.AccessListener
 * Created on 04.01.2008 by Florian Lippisch
 *
 */
package de.xwic.appkit.core.access;

/**
 * Listener for AccessHandler event.
 * @author Florian Lippisch
 */
public interface AccessListener {

	/**
	 * Notification that an entity has been deleted.
	 * @param event
	 */
	public void entityDeleted(AccessEvent event);
	
	/**
	 * Notification that an entity has been updated.
	 * @param event
	 */
	public void entityUpdated(AccessEvent event);
	
	/**
	 * Notification that an entity has been deleted using softDelete.
	 * @param event
	 */
	public void entitySoftDeleted(AccessEvent event);
	
}
