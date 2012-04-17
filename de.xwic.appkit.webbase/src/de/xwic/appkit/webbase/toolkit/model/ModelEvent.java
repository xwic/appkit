/*
 * $Id: ModelEvent.java,v 1.1 2008/10/10 14:22:30 ronnyp Exp $
 *
 * Copyright (c) 2007 Network Appliance.
 * All rights reserved.

 * com.netapp.balanceit.tools.EditorModelEvent.java
 * Created on 19.02.2008
 * 
 * @author Ronny Pfretzschner
 */
package de.xwic.appkit.webbase.toolkit.model;

/**
 * Event for model / UI information.
 * 
 * Created on 19.02.2008
 * @author Ronny Pfretzschner
 */
public class ModelEvent {

	public final static int CLOSE_REQUEST = 0;
	public final static int AFTER_SAVE = 1;
	public final static int VALIDATION_REQUEST = 2;
	public final static int VALIDATION_REFRESH_REQUEST = 3;
	public final static int MODEL_CONTENT_CHANGED = 4;
	public final static int ABORT_REQUEST = 5;
	
	public final static int RELOAD_REQUEST = 6;
	public final static int SAVE_REQUEST=7;
	
	private int eventType = -1;

	private Object source = null;
	
	/**
	 * Creates the event for model changes.
	 * 
	 * @param eventType
	 * @param source
	 */
	public ModelEvent(int eventType, Object source) {
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
