/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.webbase.viewer.columns.ColumnSelectorDialog
 * Created on Mar 21, 2007 by Aron Cotrau
 *
 */
package de.xwic.appkit.webbase.viewer.columns;

import java.util.ArrayList;
import java.util.List;

import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.controls.Button;
import de.jwic.controls.Window;
import de.jwic.events.SelectionEvent;
import de.jwic.events.SelectionListener;
import de.xwic.appkit.webbase.utils.UserProfileWrapper;
import de.xwic.appkit.webbase.viewer.columns.control.ColumnSelectorControl;

/**
 * Defines the column selector dialog.
 * 
 * @author Aron Cotrau
 */
public class ColumnSelector {

	private ColumnSelectorControl selectorControl = null;
	private Window win;

	private List<IColumnSelectorListener> listeners = new ArrayList<IColumnSelectorListener>();

	/**
	 * Column Selector Listener
	 */
	public interface IColumnSelectorListener {
		
		/**
		 * Profile was saved
		 * @param source
		 * @param userProfile
		 */
		public void profileSaved(Object source, UserProfileWrapper userProfile);

		/**
		 * Profile was saved as
		 * @param source
		 * @param newUserProfile
		 */
		public void profileSavedAs(Object source, UserProfileWrapper newUserProfile);
		
		/**
		 * Selection was aborted. CANCEL was pressed
		 */
		public void selectionAborted();
	}

	/**
	 * Loads the user list setup
	 * @param userLS
	 */
	public void loadUserListSetup(UserProfileWrapper userLS) {
		if (null == win) {
			throw new IllegalStateException(
					"Window is not initialized. You must call the init method before opening/closing the selector.");
		}
		
		selectorControl.loadUserListSetup(userLS);
	}

	/**
	 * Inits the Column Selector
	 * @param container
	 * @param name
	 */
	public void init(IControlContainer container, String name) {

		win = new Window(container, name);
		win.setWidth(530);
		win.setHeight(450);
		win.setTitle("Column Selection");
		win.setModal(true);

		ControlContainer windowContent = new ControlContainer(win, "winContainer");
		windowContent.setTemplateName(getClass().getName());

		selectorControl = new ColumnSelectorControl(windowContent, "selectorControl");

		Button buttonOk = new Button(windowContent, "buttonOk");
		buttonOk.setTitle("Save");
		buttonOk.setWidth(80);
		buttonOk.addSelectionListener(new SelectionListener() {
			public void objectSelected(SelectionEvent event) {
				save();
			}

		});

		Button buttonSaveNew = new Button(windowContent, "buttonSaveNew");
		buttonSaveNew.setTitle("Save As New");
		//buttonSaveNew.setWidth(150);
		buttonSaveNew.addSelectionListener(new SelectionListener() {
			public void objectSelected(SelectionEvent event) {
				saveNew();
			}

		});
		
		Button buttonClose = new Button(windowContent, "buttonClose");
		buttonClose.setTitle("Close");
		buttonClose.setWidth(80);
		buttonClose.addSelectionListener(new SelectionListener() {
			public void objectSelected(SelectionEvent event) {
				close();
			}
		});

		// let the user open this !
		close();
	}

	protected void saveNew() {
		// notify the listeners that the selection has changed
		for (IColumnSelectorListener listener : listeners) {
			listener.profileSavedAs(this, selectorControl.saveNewListProfile());
		}

		close();
	}

	/**
	 * opens the selector
	 */
	public void open() {
		if (null == win) {
			throw new IllegalStateException(
					"Window is not initialized. You must call the init method before opening/closing the selector.");
		}

		win.setVisible(true);
	}

	/**
	 * closes the selector
	 */
	public void close() {
		if (null == win) {
			throw new IllegalStateException(
					"Window is not initialized. You must call the init method before opening/closing the selector.");
		}

		win.setVisible(false);
	}

	protected void save() {
		// notify the listeners that the selection has changed
		for (IColumnSelectorListener listener : listeners) {
			listener.profileSaved(this, selectorControl.saveListProfile());
		}

		close();
	}

	public void addColumnSelectionListener(IColumnSelectorListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}
	
	public void removeColumnSelectionListener(IColumnSelectorListener listener) {
		listeners.remove(listener);
	}
}
