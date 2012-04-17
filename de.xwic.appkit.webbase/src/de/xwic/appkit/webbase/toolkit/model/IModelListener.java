/*
 * $Id: IModelListener.java,v 1.1 2008/10/10 14:22:30 ronnyp Exp $
 *
 * Copyright (c) 2007 Network Appliance.
 * All rights reserved.

 * com.netapp.balanceit.tools.IEditorModelListener.java
 * Created on 19.02.2008
 * 
 * @author Ronny Pfretzschner
 */
package de.xwic.appkit.webbase.toolkit.model;

/**
 * Listener for model changes.
 * 
 * Created on 19.02.2008
 * @author Ronny Pfretzschner
 */
public interface IModelListener {

	
	/**
	 * 
	 * @param event The ModelEvent
	 */
	public void modelContentChanged(ModelEvent event);
}
