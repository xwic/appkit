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
		setHeight(250);
		
		setVisible(false);
		setModal(true);
		
		setDraggable(false);
		setMaximizable(false);
		setMinimizable(false);
		setResizable(false);
		
	}

	private void centerWindow() {
		//only if we show it...
		int left = Page.findPage(this).getPageSize().width / 2 - (getWidth() / 2);
		if (left != getLeft()) {
			setLeft(left);
		}
		
		//subtract 20 because it looks a bit too high..
		int top = Page.findPage(this).getPageSize().height / 2 - (getHeight() / 2)
				+ Page.findPage(this).getClientTop() - 20;
		if (top != getTop()) {
			setTop(top);
		}

	}
	
	/**
	 * Show the window and center it.
	 */
	public void show() {
		centerWindow();
		setVisible(true);
	}
	
	/**
	 * Close the dialog and destroy it.
	 */
	public void close() {
		destroy();
	}
	
}
