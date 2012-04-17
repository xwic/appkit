/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.client.uitools.editors.EditorListener
 * Created on 29.11.2006 by Florian Lippisch
 *
 */
package de.xwic.appkit.webbase.editors.events;

/**
 * @author Florian Lippisch
 */
public interface EditorListener {

	/**
	 * Fired after the editor has saved the entity.
	 * @param event
	 */
	public void afterSave(EditorEvent event);
	
	/**
	 * Fired after the entity has been loaded or refreshed.
	 * @param event
	 */
	public void entityLoaded(EditorEvent event);

	/**
	 * Fired before the entity is saved.
	 * @param event
	 */
	public void beforeSave(EditorEvent event);
	
	/**
	 * Fired, after all pages, widgets and so on are created. <p>
	 * 
	 * Could also be considered of some kind of "initialize" event.
	 * This event is called just once, after an editor is created.
	 * 
	 * @param event
	 */
	public void pagesCreated(EditorEvent event);

}
