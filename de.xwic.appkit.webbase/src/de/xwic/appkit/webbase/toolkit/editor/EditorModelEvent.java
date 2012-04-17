/*
 * $Id: EditorModelEvent.java,v 1.3 2008/05/27 07:31:12 rpfretzschner Exp $
 *
 * Copyright (c) 2007 Network Appliance.
 * All rights reserved.

 * com.netapp.balanceit.tools.EditorModelEvent.java
 * Created on 19.02.2008
 * 
 * @author Ronny Pfretzschner
 */
package de.xwic.appkit.webbase.toolkit.editor;

/**
 * Editor event.
 * 
 * Created on 19.02.2008
 * @author Ronny Pfretzschner
 */
public class EditorModelEvent {

	public final static int CLOSE_REQUEST = 0;
	public final static int AFTER_SAVE = 1;
	public final static int VALIDATION_REQUEST = 2;
	public final static int VALIDATION_REFRESH_REQUEST = 3;
	public final static int ENTITY_CHANGED = 4;
	public final static int EDIT_MODE_CHANGED = 5;
	public final static int ABORT = 6;
	public final static int ATTACHMENTS_CHANGED = 7;
	
	private int eventType = -1;

	private Object source = null;
	
	/**
	 * Creates the event for model changes.
	 * 
	 * @param eventType
	 * @param source
	 */
	public EditorModelEvent(int eventType, Object source) {
		this.eventType = eventType;
		this.source = source;
	}

	/**
	 * @return Returns the eventType.
	 */
	public int getEventType() {
		return eventType;
	}

	/**
	 * @return Returns the source.
	 */
	public Object getSource() {
		return source;
	}
}
