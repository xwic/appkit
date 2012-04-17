/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.access.AccessAdapter
 * Created on 04.01.2008 by Florian Lippisch
 *
 */
package de.xwic.appkit.core.access;

/**
 * Adapter for the AccessListener.
 * @author Florian Lippisch
 */
public abstract class AccessAdapter implements AccessListener {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.access.AccessListener#entityDeleted(de.xwic.appkit.core.access.AccessEvent)
	 */
	public void entityDeleted(AccessEvent event) {

	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.access.AccessListener#entitySoftDeleted(de.xwic.appkit.core.access.AccessEvent)
	 */
	public void entitySoftDeleted(AccessEvent event) {

	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.access.AccessListener#entityUpdated(de.xwic.appkit.core.access.AccessEvent)
	 */
	public void entityUpdated(AccessEvent event) {

	}

}
