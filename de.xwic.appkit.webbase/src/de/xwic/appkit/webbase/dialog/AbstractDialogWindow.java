/*
 * Copyright (c) 2009 Network Appliance, Inc.
 * All rights reserved.
 */

package de.xwic.appkit.webbase.dialog;

import java.util.ArrayList;
import java.util.List;

import de.jwic.base.ControlContainer;
import de.jwic.controls.Button;
import de.jwic.controls.Window;
import de.jwic.events.SelectionEvent;
import de.jwic.events.SelectionListener;
import de.xwic.appkit.webbase.toolkit.app.Site;

/**
 * @author lippisch
 */
public abstract class AbstractDialogWindow  extends CenteredWindow {

	protected enum EventType {
		OK,
		ABORTED
	}
	
	private List<IDialogWindowListener> listeners = new ArrayList<IDialogWindowListener>();
	
	protected ControlContainer baseContainer = null;
	protected DialogContent content = null;
	protected ControlContainer buttonsContainer = null;
	
	protected Button btOk;
	protected Button btCancel;
	
	/**
	 * @param container
	 * @param name
	 */
	public AbstractDialogWindow(Site site) {
		super(site);
		setTemplateName(Window.class.getName());
		setVisible(false);
		setClosable(false);
		setResizable(false);
		setMinimizable(false);
		setMaximizable(false);
		
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.dialog.CenteredWindow#show()
	 */
	@Override
	public void show() {
		if (baseContainer == null) {
			createControls();
		}
		super.show();
	}
	
	/**
	 * 
	 */
	protected void createControls(){
		
		baseContainer = new ControlContainer(this, "baseContainer");
		baseContainer.setTemplateName(AbstractDialogWindow.class.getPackage().getName() + ".AbstractDialogLayout");
		buttonsContainer = new ControlContainer(baseContainer, "buttonsContainer");
		buttonsContainer.setTemplateName(AbstractDialogWindow.class.getPackage().getName() + ".ButtonsContainer");

		content = new DialogContent(baseContainer, "content");
		
		btOk = new Button(buttonsContainer, "btOk");
		btOk.setTitle("OK");
		btOk.addSelectionListener(new SelectionListener() {
			@Override
			public void objectSelected(SelectionEvent event) {
				onOk();
			}
		});
		btOk.setWidth(80);
		
		btCancel = new Button(buttonsContainer, "btCancel");
		btCancel.setTitle("Close");
		btCancel.addSelectionListener(new SelectionListener() {
			@Override
			public void objectSelected(SelectionEvent event) {
				onCancel();
			}
		});
		btCancel.setVisible(false);
		btCancel.setWidth(80);
		
		content.setHeight(getHeight() - 60);
		content.setWidth(getWidth() - 10);
		
		createContent(content);
		
	}
	
	/**
	 * @param content2
	 */
	protected abstract void createContent(DialogContent content);
	

	/* (non-Javadoc)
	 * @see de.jwic.controls.Window#setHeight(int)
	 */
	@Override
	public void setHeight(int height) {
		super.setHeight(height);
		if (content != null) {
			content.setHeight(height - 60);
		}
	}
	
	/* (non-Javadoc)
	 * @see de.jwic.controls.Window#setWidth(int)
	 */
	@Override
	public void setWidth(int width) {
		super.setWidth(width);
		if (content != null) {
			content.setWidth(width - 10);
		}

	}
	
	/**
	 * 
	 */
	protected void onCancel() {
		fireEvent(EventType.ABORTED, new DialogEvent(this));
		close();
	}

	/**
	 * 
	 */
	protected void onOk() {
		fireEvent(EventType.OK, new DialogEvent(this));
		close();
	}
	
	/**
	 * Add a DialogWindowListener.
	 * @param listener
	 */
	public void addDialogWindowListener(IDialogWindowListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * Remove a previously added DialogWindowListener.
	 * @param listener
	 */
	public void removeDialogWindowListener(IDialogWindowListener listener) {
		listeners.remove(listener);
	}
	
	/**
	 * Fire an event.
	 * @param et
	 * @param event
	 */
	protected void fireEvent(EventType et, DialogEvent event) {
		
		IDialogWindowListener[] lst = new IDialogWindowListener[listeners.size()];
		lst = listeners.toArray(lst);
		
		for (IDialogWindowListener listener : lst) {
			switch (et) {
			case ABORTED:
				listener.onDialogAborted(event);
				break;
			case OK:
				listener.onDialogOk(event);
				break;
			}
		}
		
	}
	
}
