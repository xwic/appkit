/*
 * $Id: IEntityListBoxControl.java,v 1.1 2008/10/10 14:22:30 ronnyp Exp $
 *
 * Copyright (c) 2007 Network Appliance.
 * All rights reserved.

 * com.netapp.balanceit.tools.list.IEntityListBoxControl.java
 * Created on 29.02.2008
 * 
 * @author Ronny Pfretzschner
 */
package de.xwic.appkit.webbase.toolkit.components;

import de.xwic.appkit.core.dao.IEntity;

/**
 * Listboxcontrol of entities.
 * 
 * Created on 29.02.2008
 * @author Ronny Pfretzschner
 */
public interface IEntityListBoxControl {

	
	/** 
	 * select the item corresponding to the given entity
	 * 
	 * @param entity
	 */
	public void selectEntry(IEntity entity);
	
	/**
	 * @return the Entity of the selected entry, can be null if nothing is selected
	 */
	public IEntity getSelectedEntry();

}
