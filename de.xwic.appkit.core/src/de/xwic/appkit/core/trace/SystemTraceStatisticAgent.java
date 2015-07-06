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

package de.xwic.appkit.core.trace;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.xwic.appkit.core.dao.impl.hbn.HibernateUtil;

/**
 * This thread can be started to store statistic information into the database.
 * 
 * @author lippisch
 */
public class SystemTraceStatisticAgent implements Runnable {

	private static final Log log = LogFactory.getLog(SystemTraceStatisticAgent.class);
	
	private static SystemTraceStatisticAgent self = null;
	private static Thread myThread = null;
	private static long interval = 1000 * 60 * 5; // 5 minutes by default.
	
	
	private boolean exitFlag = false;
	
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
			self = new SystemTraceStatisticAgent();
			myThread = new Thread(self);
			myThread.setDaemon(true);
			myThread.setName("SystemTraceStatisticAgent");
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
		SystemTraceStatisticAgent.interval = interval;
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
					ITraceDataManager dataManager = Trace.getDataManager();
					if (dataManager != null) {
						dataManager.saveSystemTraceStatistic(interval);
						
						// since it is running in it's own thread, need to close Hibernate Session afterwards
						HibernateUtil.closeSession();
					}
				} catch (Throwable t) {
					log.error("Error during trace log", t);
				}
				
			}
			
		}
		
	}


}
