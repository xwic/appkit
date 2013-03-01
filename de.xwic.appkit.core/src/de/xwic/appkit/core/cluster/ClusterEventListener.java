/**
 * 
 */
package de.xwic.appkit.core.cluster;

/**
 * Listener for cluster events.
 * @author lippisch
 */
public interface ClusterEventListener {

	/**
	 * An event was received. 
	 * @param event
	 */
	public void receivedEvent(ClusterEvent event);
	
}
