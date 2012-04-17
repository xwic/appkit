/*
 * Copyright (c) 2009 Network Appliance, Inc.
 * All rights reserved.
 */

package de.xwic.appkit.webbase.table;

/**
 * Adapter class for IEntityTableListener.
 * @author lippisch
 */
public abstract class EntityTableAdapter implements IEntityTableListener {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.table.IEntityTableListener#columnSorted(de.xwic.appkit.webbase.table.EntityTableEvent)
	 */
	@Override
	public void columnSorted(EntityTableEvent event) {
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.table.IEntityTableListener#columnFiltered(de.xwic.appkit.webbase.table.EntityTableEvent)
	 */
	@Override
	public void columnFiltered(EntityTableEvent event) {
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.table.IEntityTableListener#columnsReordered(de.xwic.appkit.webbase.table.EntityTableEvent)
	 */
	@Override
	public void columnsReordered(EntityTableEvent event) {
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.table.IEntityTableListener#beforeUserConfigurationChanged(de.xwic.appkit.webbase.table.EntityTableEvent)
	 */
	@Override
	public void beforeUserConfigurationChanged(EntityTableEvent event) {
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.table.IEntityTableListener#userConfigurationChanged(de.xwic.appkit.webbase.table.EntityTableEvent)
	 */
	@Override
	public void userConfigurationChanged(EntityTableEvent event) {
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.table.IEntityTableListener#newUserConfigurationCreated(de.xwic.appkit.webbase.table.EntityTableEvent)
	 */
	@Override
	public void newUserConfigurationCreated(EntityTableEvent event) {
	}
}
