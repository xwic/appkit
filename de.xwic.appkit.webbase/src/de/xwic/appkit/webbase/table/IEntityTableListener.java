/*
 * Copyright (c) 2009 Network Appliance, Inc.
 * All rights reserved.
 */

package de.xwic.appkit.webbase.table;

/**
 * Listener to the entity table model.
 * @author lippisch
 */
public interface IEntityTableListener {
	
	/**
	 * A column sort setting was changed.
	 * @param event
	 */
	public void columnSorted(EntityTableEvent event);

	/**
	 * A columns filter options have been changed.
	 * @param event
	 */
	public void columnFiltered(EntityTableEvent event);

	/**
	 * The columns have been reordered or the visibility changed.
	 * @param event
	 */
	public void columnsReordered(EntityTableEvent event);
	
	/**
	 * The user configuration has been changed
	 * @param event
	 */
	public void userConfigurationChanged(EntityTableEvent event);
	
	/**
	 * Called when a new User Configuration has been created by the user
	 * @param event
	 */
	public void newUserConfigurationCreated(EntityTableEvent event);
	
	/**
	 * Called when the current User Configuration has been modified
	 * @param event
	 */
	public void userConfigurationDirtyChanged(EntityTableEvent event);
}
