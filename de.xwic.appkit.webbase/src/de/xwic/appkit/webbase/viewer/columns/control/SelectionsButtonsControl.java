/*******************************************************************************
 * Copyright 2015 xWic group (http://www.xwic.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *  
 *******************************************************************************/
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
