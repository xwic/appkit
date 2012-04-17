/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.client.uitools.editors.EditorEvent
 * Created on 29.11.2006 by Florian Lippisch
 *
 */
package de.xwic.appkit.webbase.editors.events;

/**
 * Event for the EditorListener.
 * @author Florian Lippisch
 */
public class EditorEvent {

	private Object source = null;
	private boolean newEntity = false;
	
	/**
	 * Constructor.
	 * @param source
	 */
	public EditorEvent(Object source) {
		this.source = source;
	}

	/**
	 * Constructor.
	 * @param source
	 */
	public EditorEvent(Object source, boolean isNew) {
		this.source = source;
		this.newEntity = isNew;
	}
	
	
	/**
	 * @return the source
	 */
	public Object getSource() {
		return source;
	}

	/**
	 * @return the newEntity
	 */
	public boolean isNewEntity() {
		return newEntity;
	}
	
	
}
