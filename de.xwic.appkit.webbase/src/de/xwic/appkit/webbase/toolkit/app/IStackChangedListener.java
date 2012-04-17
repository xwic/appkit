/**
 * 
 */
package de.xwic.appkit.webbase.toolkit.app;

/**
 * Listener, when the control stack changed.
 * 
 * @author Ronny Pfretzschner
 *
 */
public interface IStackChangedListener {

	/**
	 * The stack changed. Inform all listeners.
	 */
	public void stackChanged();
}
