/**
 * 
 */
package de.xwic.appkit.webbase.async;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.jwic.ecolib.async.IAsyncProcess;
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
