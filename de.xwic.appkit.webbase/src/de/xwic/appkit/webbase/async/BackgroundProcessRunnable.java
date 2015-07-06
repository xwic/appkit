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
package de.xwic.appkit.webbase.async;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.jwic.async.IAsyncProcess;
import de.xwic.appkit.core.dao.impl.hbn.HibernateUtil;

/**
 * Enacpsulates an IAsyncBackgroundProcess to properly close any
 * HibernateSession(s).
 * @author lippisch
 */
public class BackgroundProcessRunnable implements Runnable {

	private Log log = LogFactory.getLog(getClass());
	private final IAsyncProcess process;

	/**
	 * 
	 */
	public BackgroundProcessRunnable(IAsyncProcess process) {
		this.process = process;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

		try {
			process.run();
		} catch (Throwable t) {
			log.error("Error executing runnable " + process.getClass().getName() + ": " + t, t);
		} finally {
			HibernateUtil.closeSession();
		}
		

	}

}
