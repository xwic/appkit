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
package de.xwic.appkit.core.trace;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This agent monitors active trace cycles and stores stacktraces if an activity is running
 * longer than the specified treshold. This can help investigate long running operations.
 *  
 * @author lippisch
 */
public class TraceTrapAgent implements Runnable {

	private final static int MIN_DURATION_TRESHHOLD = 7000; // 7 seconds
	private final static int WAIT_TIME_DEFAULT = 2000; // every 2 seconds
	
	private static final Log log = LogFactory.getLog(TraceTrapAgent.class);
	
	private static TraceTrapAgent self = null;
	private static Thread myThread = null;
	
	private boolean exitFlag = false;
	
	private static long interval = WAIT_TIME_DEFAULT; // 5 minutes by default.
	
	/**
	 * Returns true if the agent is running.
	 * @return
	 */
	public static boolean isRunning() {
		return myThread != null && self != null && myThread.isAlive();
	}
	
	
	/**
	 * Start the agent.
	 */
	public synchronized static void start() {
		if (!isRunning()) {
			self = new TraceTrapAgent();
			myThread = new Thread(self);
			myThread.setDaemon(true);
			myThread.setName("TraceTrapAgent");
			myThread.start();
		} 
	}
	
	/**
	 * Stop the running flag.
	 */
	public static void stop() {
		if (isRunning()) {
			self.exitFlag = true;
		}
	}
	
	/**
	 * @param interval the interval to set
	 */
	public static void setInterval(long interval) {
		TraceTrapAgent.interval = interval;
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		
		while (!exitFlag) {
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				// nothing todo 
			}
			
			if (Trace.isEnabled()) {
				
				try {
					long treshhold = System.currentTimeMillis() - MIN_DURATION_TRESHHOLD;
					for (ITraceContext tx : Trace.getActiveTraces()) {
						
						if (tx.getStartTime() < treshhold) {
							// this operation is already running longer than the treshhold
							// take a stacktrace and add it to to the context
							tx.doStackTraceSnapShot();
						}
						
					}
				} catch (Throwable t) {
					log.error("Error during trace trap", t);
				}
				
			}
			
		}
		
	}



}
