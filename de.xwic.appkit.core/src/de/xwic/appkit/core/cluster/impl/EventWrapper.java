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
/**
 * 
 */
package de.xwic.appkit.core.cluster.impl;

import de.xwic.appkit.core.cluster.ClusterEvent;

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
