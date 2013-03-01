/**
 * 
 */
package de.xwic.appkit.webbase.async;

import de.jwic.base.Control;
import de.jwic.base.IControlContainer;
import de.jwic.base.JavaScriptSupport;

/**
 * This class is used to properly close the dialog when the backend process
 * was completed.
 * 
 * @author lippisch
 */
@JavaScriptSupport
public class AsyncProcessControlHelper extends Control {

	private Control toBeDestroyed;
	private boolean destroyNow = false;

	/**
	 * @param container
	 * @param name
	 */
	public AsyncProcessControlHelper(IControlContainer container, String name, Control toBeDestroyed) {
		super(container, name);
		this.toBeDestroyed = toBeDestroyed;
	}

	/**
	 * @return the destroyNow
	 */
	public boolean isDestroyNow() {
		return destroyNow;
	}
	

	/**
	 * Invoked by the JavaScript if the dialog is ready to be closed.
	 */
	public void actionCloseDialog() {
		if (toBeDestroyed != null) {
			toBeDestroyed.destroy();
		}
	}

	/**
	 * @param destroyNow the destroyNow to set
	 */
	public void setDestroyNow(boolean destroyNow) {
		this.destroyNow = destroyNow;
		requireRedraw();
	}
	
}
