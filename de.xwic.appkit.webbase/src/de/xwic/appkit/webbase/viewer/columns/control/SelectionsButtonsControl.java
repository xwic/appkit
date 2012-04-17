/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.webbase.viewer.columns.control.SelectionsButtonsControl
 * Created on Mar 21, 2007 by Aron Cotrau
 *
 */
package de.xwic.appkit.webbase.viewer.columns.control;

import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.controls.Button;
import de.jwic.events.SelectionEvent;
import de.jwic.events.SelectionListener;
import de.xwic.appkit.webbase.viewer.columns.IColumnSelectorHandler;

/**
 * Defines the control that contains the buttons for the ColumnSelector
 * @author Aron Cotrau
 */
public class SelectionsButtonsControl extends ControlContainer {

	private static final int BUTTON_WIDTH = 60;
	private IColumnSelectorHandler csHandler = null;
	
	/**
	 * c-tor
	 * @param container
	 * @param name
	 */
	public SelectionsButtonsControl(IControlContainer container, String name, IColumnSelectorHandler csHandler) {
		super(container, name);
		this.csHandler = csHandler;
		addContent();
	}
	
	/**
	 * creates content
	 */
	private void addContent() {
		Button buttonAddAll = new Button(this, "buttonAddAll");
		buttonAddAll.setTitle("<<");
		buttonAddAll.setWidth(BUTTON_WIDTH);
		buttonAddAll.addSelectionListener(new SelectionListener() {
			public void objectSelected(SelectionEvent event) {
				csHandler.handleAddAll();
			}
		});
		
		Button buttonRemoveAll = new Button(this, "buttonRemoveAll");
		buttonRemoveAll.setTitle(">>");
		buttonRemoveAll.setWidth(BUTTON_WIDTH);
		buttonRemoveAll.addSelectionListener(new SelectionListener() {
			public void objectSelected(SelectionEvent event) {
				csHandler.handleRemoveAll();
			}
		});

		Button buttonAdd = new Button(this, "buttonAdd");
		buttonAdd.setTitle("<");
		buttonAdd.setWidth(BUTTON_WIDTH);
		buttonAdd.addSelectionListener(new SelectionListener() {
			public void objectSelected(SelectionEvent event) {
				csHandler.handleAddSelection();
			}
		});
		
		Button buttonRemove = new Button(this, "buttonRemove");
		buttonRemove.setTitle(">");
		buttonRemove.setWidth(BUTTON_WIDTH);
		buttonRemove.addSelectionListener(new SelectionListener() {
			public void objectSelected(SelectionEvent event) {
				csHandler.handleRemoveSelection();
			}
		});

		Button buttonUp = new Button(this, "buttonUp");
		buttonUp.setTitle("Up");
		buttonUp.setWidth(BUTTON_WIDTH);
		buttonUp.addSelectionListener(new SelectionListener() {
			public void objectSelected(SelectionEvent event) {
				csHandler.handleUp();
			}
		});
		
		Button buttonDown = new Button(this, "buttonDown");
		buttonDown.setTitle("Down");
		buttonDown.setWidth(BUTTON_WIDTH);
		buttonDown.addSelectionListener(new SelectionListener() {
			public void objectSelected(SelectionEvent event) {
				csHandler.handleDown();
			}
		});
	}
}
