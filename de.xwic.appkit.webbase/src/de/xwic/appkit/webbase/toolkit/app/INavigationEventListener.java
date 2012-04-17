/**
 * 
 */
package de.xwic.appkit.webbase.toolkit.app;

/**
 * Listener for changes on the navigation.
 * 
 * @author Ronny Pfretzschner
 *
 */
public interface INavigationEventListener {

	/**
	 * The user navigated through the application. Inform all listeners.
	 */
	public void navigationChanged();
}
