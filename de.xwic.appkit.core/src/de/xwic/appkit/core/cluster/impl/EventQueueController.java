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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.xwic.appkit.core.cluster.ClusterEvent;
import de.xwic.appkit.core.cluster.INode;
import de.xwic.appkit.core.cluster.INode.NodeStatus;
import de.xwic.appkit.core.cluster.Message;

/**
 * Sends events to other nodes. Events are send from this centralized controller
 * to send them in the correct order in an asynchronous fashion. Events support a
 * synchronous mode as well, which is still using this single queue.
 * 
 * @author lippisch
 */
public class EventQueueController implements Runnable {

	private final static Log log = LogFactory.getLog(EventQueueController.class);
	
	private Cluster cluster;
	
	
	/**
	 * @param cluster
	 */
	public EventQueueController(Cluster cluster) {
		super();
		this.cluster = cluster;
	}



	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

		while(true) {
			
			EventWrapper ew = cluster.nextEvent();
			if (ew != null) {
				handleEvent(ew);
			} else {
				
				try {
					Thread.sleep(5000); // wait 5 seconds.
				} catch (InterruptedException e) {
					// expected..
				}
			}
		}
		
	}



	/**
	 * @param ew
	 */
	private void handleEvent(EventWrapper ew) {
		try {
			
			INode[] nodes = cluster.getNodes();	
			
			ClusterEvent event = ew.getEvent();
			Message msg = new Message(ClusterNodeClientProtocol.CMD_EVENT, null, event);
			for (INode node : nodes) {
				try {
					if (node.getStatus() == NodeStatus.CONNECTED) {
						//log.debug("Sending event '" + event.getNamespace() + ":" + event.getName() + "' to " + node);
						node.sendMessage(msg);
					}
				} catch (Exception e) {
					log.warn("Failed to send message to " + node, e);
				}
			}
			
			ew.completed();
			if (!ew.isAsynchronous() && ew.getCallerThread() != null) {
				ew.getCallerThread().interrupt(); // wake it up.
			}
			
		} catch (Throwable t) {
			// make sure not to kill the EventQueueController by some unforseen exception.
			log.error("Error handling event", t);
		}
		
	}

}
