/*
 * Copyright (c) 2009 Network Appliance, Inc.
 * All rights reserved.
 */

package de.xwic.appkit.webbase.controls.comment;

/**
 * Listener for the EntityShoutBox to listen on comment creation events.
 * @author lippisch
 */
public interface IEntityShoutBoxListener {

	public void commentCreated(EntityShoutBoxEvent event);
	
	public void errorOccured(EntityShoutBoxEvent event, String errorMessage);
	
}
