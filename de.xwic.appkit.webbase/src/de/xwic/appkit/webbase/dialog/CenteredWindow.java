package de.xwic.appkit.webbase.dialog;

import de.jwic.base.Page;
import de.jwic.controls.Window;
import de.xwic.appkit.webbase.toolkit.app.Site;

/**
 * @author Adrian Ionescu
 */
public class CenteredWindow extends Window {

	/**
	 * @param container
	 * @param name
	 */
	public CenteredWindow(Site site) {
		super(site.getDialogContainer());
		setTemplateName(Window.class.getName());
		setWidth(400);
		//setHeight(250);
		
		setVisible(false);
		setModal(true);
		
		setDraggable(false);
		setMaximizable(false);
		setMinimizable(false);
		setResizable(false);
		
	}

	/**
	 * Show the window and center it.
	 */
	public void show() {
//		centerWindow(); this is redundant since windows are automatically set to be centered
		setVisible(true);
	}
	
	/**
	 * Close the dialog and destroy it.
	 */
	public void close() {
		destroy();
	}
	
}
