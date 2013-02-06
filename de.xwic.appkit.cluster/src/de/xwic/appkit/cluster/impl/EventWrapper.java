/**
 * 
 */
package de.xwic.appkit.cluster.impl;

import de.xwic.appkit.cluster.ClusterEvent;

/**
 * @author lippisch
 *
 */
public class EventWrapper {

	private ClusterEvent event;
	private Thread callerThread = null;
	private boolean asynchronous = false;
	private boolean completed = false;
	
	/**
	 * @param event
	 */
	public EventWrapper(ClusterEvent event) {
		super();
		this.event = event;
	}
	/**
	 * @param event
	 * @param callerThread
	 * @param synchronous
	 */
	public EventWrapper(ClusterEvent event, Thread callerThread, boolean asynchronous) {
		super();
		this.event = event;
		this.callerThread = callerThread;
		this.asynchronous = asynchronous;
	}
	/**
	 * @return the event
	 */
	public ClusterEvent getEvent() {
		return event;
	}
	/**
	 * @return the callerThread
	 */
	public Thread getCallerThread() {
		return callerThread;
	}
	/**
	 * @return the asynchronous
	 */
	public boolean isAsynchronous() {
		return asynchronous;
	}
	/**
	 * @return the completed
	 */
	public boolean isCompleted() {
		return completed;
	}
	/**
	 * Mark as completed. 
	 */
	public void completed() {
		completed = true;
	}
	
	
}
