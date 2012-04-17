/*
 * Copyright (c) 2009 Network Appliance, Inc.
 * All rights reserved.
 */

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
