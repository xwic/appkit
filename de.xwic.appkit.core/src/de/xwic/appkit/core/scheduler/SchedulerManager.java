package de.xwic.appkit.core.scheduler;

import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.SchedulerException;

import de.xwic.appkit.core.dao.DAOFactory;
import de.xwic.appkit.core.model.daos.IActivityDAO;
import de.xwic.appkit.core.model.daos.impl.ActivityDAO;
import de.xwic.appkit.core.util.ConfigurationUtil;

/**
 * @author dotto
 * 
 */
public class SchedulerManager {

	private static Log log = LogFactory.getLog(SchedulerManager.class);

	private static IScheduler scheduler = null;

	/**
	 * 
	 * @return the scheduler.
	 */
	public static IScheduler getScheduler() {
		if (scheduler == null) {
			try {
				TimeZone timeZone = ConfigurationUtil.getDefaultTimeone();
				scheduler = new QuartzScheduler(timeZone);
			} catch (SchedulerException e) {
				log.error("Cannot create the scheduler", e);
			}
		}
		return scheduler;
	}

}
