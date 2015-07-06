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
package de.xwic.appkit.webbase.toolkit.components;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.jwic.base.IControlContainer;
import de.jwic.controls.dialogs.DialogEvent;
import de.jwic.controls.dialogs.DialogListener;
import de.xwic.appkit.webbase.dialog.AbstractDialogWindow;
import de.xwic.appkit.webbase.dialog.DialogContent;
import de.xwic.appkit.webbase.toolkit.app.Site;

/**
 * A Dialog is displayed on top of all other controls/dialogs.
 * 
 * @author Florian Lippisch
 * @version $Revision: 1.2 $
 */
public abstract class Dialog extends AbstractDialogWindow {

	protected Site site;
	private List<DialogListener> listeners = null;
	
	/**
	 * Constructor.
	 * @param site
	 */
	public Dialog(Site site) {
		super(site);
		this.site = site;
//		this.createControls(this.baseContainer);
	}
	
	/**
	 * Add a DialogListener.
	 * @param listener
	 */
	public synchronized void addDialogListener(DialogListener listener) {
		if (listeners == null) {
			listeners = new ArrayList<DialogListener>();
		}
		listeners.add(listener);
	}
	
	/**
	 * Remove a DialogListener instance.
	 * @param listener
	 */
	public synchronized void removeDialogListener(DialogListener listener) {
		if (listeners != null) {
			listeners.remove(listener);
		}
	}
	
	/**
	 * Opens the dialog.
	 *
	 */
	public final void open() {
       this.show();
	}
	
	/**
	 * Closes the dialog without notification to the DialogListeners.
	 */
	public final void close() {
//		site.popPage(contentContainer);
		this.setVisible(false);
		destroy();
	}
	
	
	public abstract void destroy();
	
	/**
	 * Notifies the listeners that the dialog was 'finished' 
	 * and closes the dialog.
	 */
	public void finish() {
		if (listeners != null) {
			DialogEvent event = new DialogEvent(this);
			for (Iterator<DialogListener> it = listeners.iterator(); it.hasNext(); ) {
				DialogListener listener = it.next();
				listener.dialogFinished(event);
			}
		}
		close();
	}

	/**
	 * Notifies the listeners that the dialog was 'aborted' 
	 * and closes the dialog.
	 */
	public void abort() {
		if (listeners != null) {
			DialogEvent event = new DialogEvent(this);
			for (Iterator<DialogListener> it = listeners.iterator(); it.hasNext(); ) {
				DialogListener listener = it.next();
				listener.dialogAborted(event);
			}
		}
		close();
	}

	/**
	 * Create the dialogs content.
	 * @param container
	 */
	public abstract void createControls(IControlContainer container);

	@Override
	protected void createContent(DialogContent content) {
		this.createControls(content);
	}
	
}
