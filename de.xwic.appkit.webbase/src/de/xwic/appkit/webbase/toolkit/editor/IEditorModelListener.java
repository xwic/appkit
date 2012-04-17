/*
 * $Id: IEditorModelListener.java,v 1.1 2008/05/21 12:40:39 rpfretzschner Exp $
 *
 * Copyright (c) 2007 Network Appliance.
 * All rights reserved.

 * com.netapp.balanceit.tools.IEditorModelListener.java
 * Created on 19.02.2008
 * 
 * @author Ronny Pfretzschner
 */
package de.xwic.appkit.webbase.toolkit.editor;

/**
 * Listener for entity editor model changes.
 * 
 * Created on 19.02.2008
 * @author Ronny Pfretzschner
 */
public interface IEditorModelListener {

	
	/**
	 * 
	 * @param event The EditorModelEvent
	 */
	public void modelContentChanged(EditorModelEvent event);
}
