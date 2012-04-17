/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.webbase.viewer.columns.IColumnSelectorHandler
 * Created on Mar 21, 2007 by Aron Cotrau
 *
 */
package de.xwic.appkit.webbase.viewer.columns;

/**
 * This interface is used for the ColumnSelector to handle the buttons events.
 * @author Aron Cotrau
 */
public interface IColumnSelectorHandler {
	
	/**
	 * handles "Down" button
	 */
	public void handleDown();

	/**
	 * handles "Up" button
	 */
	public void handleUp();

	/**
	 * handles "Add" button
	 */
	public void handleAddSelection();

	/**
	 * handles "Remove" button
	 */
	public void handleRemoveSelection();
	
	/**
	 * handles "Remove All" button
	 */
	public void handleRemoveAll();

	/**
	 * handles "Add All" button
	 */
	public void handleAddAll();
}
