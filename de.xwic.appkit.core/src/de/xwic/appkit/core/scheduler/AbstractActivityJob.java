package de.xwic.appkit.core.scheduler;

import java.nio.channels.ClosedByInterruptException;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.quartz.UnableToInterruptJobException;

import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.impl.hbn.HibernateUtil;
import de.xwic.appkit.core.mail.impl.SendMail;
import de.xwic.appkit.core.model.daos.IActivityDAO;
import de.xwic.appkit.core.model.daos.IPicklisteDAO;
import de.xwic.appkit.core.model.entities.IActivity;
import de.xwic.appkit.core.model.entities.IPicklistEntry;
import de.xwic.appkit.core.util.DateFormatter;

/**
 * Abstract Quartz Job. Sends automatic emergency messages in case of errors. Also manages hibernate session.
 * 
 * @author dotto
 * 
 */
public abstract class AbstractActivityJob implements InterruptableJob, IJob {

	protected static final Log log = LogFactory.getLog(AbstractActivityJob.class);
	public static final String ACTIVITY_KEY = "activity_key";
	private final IActivityDAO activityDao;
	private final IPicklisteDAO plDao;
	private String title;
	private volatile Thread thisThread;
	private volatile boolean isJobInterrupted;

	/**
	 * @param title
	 */
	public AbstractActivityJob() {
		activityDao = (IActivityDAO) DAOSystem.getDAO(IActivityDAO.class);
		plDao = (IPicklisteDAO) DAOSystem.getDAO(IPicklisteDAO.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		thisThread = Thread.currentThread();
		int activityId = (Integer) context.getJobDetail().getJobDataMap().get(ACTIVITY_KEY);
		title = context.getJobDetail().getKey().getName();
		boolean hasError = true;
		String lastMessage = "";
		Date now = new Date();
		log.info(title + " started...");
		try {

			IActivity activity = activityDao.getEntity(activityId);
			if (activity.getStatus().getKey().equals(IActivity.PE_ACTIVITY_STATUS_RUN)
					|| activity.getStatus().getKey().equals(IActivity.PE_ACTIVITY_STATUS_DISABLED)) {
				throw new SchedulerException("Activity " + title + " has incorrect status! Aborting...");
			}
			activity.setStatus(plDao.getPickListEntryByKey(IActivity.PL_ACTIVITY_STATUS, IActivity.PE_ACTIVITY_STATUS_RUN));
			activityDao.update(activity);

			lastMessage = execute();
			hasError = false;
		} catch (ClosedByInterruptException e) {
			log.info("ClosedByInterruptException... exiting job.", e);
		} catch (Exception ex) {
			SendMail.sendEmergencyInfo("Error occured in Job: " + title, ex.getMessage(), ex);
			log.error("Error occured during execution of job #" + activityId, ex);
			lastMessage = ex.getMessage();
		} finally {

			if (isJobInterrupted) {
				log.info("Job " + title + " did not complete");
			}
			Date nextFireTime = context.getNextFireTime();
			try {
				IActivity activity = activityDao.getEntity(activityId);
				IPicklistEntry peStatus = plDao.getPickListEntryByKey(IActivity.PL_ACTIVITY_STATUS,
						hasError ? IActivity.PE_ACTIVITY_STATUS_ERROR : IActivity.PE_ACTIVITY_STATUS_PENDING);
				activity.setStatus(peStatus);
				activity.setNextRun(nextFireTime);
				activity.setLastExecutionMessage(lastMessage);
				activity.setLastRun(context.getFireTime());
				activity.setExecutionTime(System.currentTimeMillis() - now.getTime());
				activityDao.update(activity);
			} catch (Exception ex) {
				log.error("FATAL: Cannot update activity status.", ex);
			}
			HibernateUtil.closeSession();
			if (nextFireTime != null) {
				log.info("Next Fire Time : " + DateFormatter.getDateTimeAmPmSecondsTz(null).format(nextFireTime));
			}
			log.info(title + " finished...");

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.netapp.wwpc.scheduler.IJob#execute()
	 */
	@Override
	public abstract String execute() throws Exception;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.netapp.wwpc.scheduler.IJob#getTitle()
	 */
	@Override
	public String getTitle() {
		return title;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.quartz.InterruptableJob#interrupt()
	 */
	@Override
	public void interrupt() throws UnableToInterruptJobException {
		isJobInterrupted = true;
		if (thisThread != null) {
			thisThread.interrupt();
		}
	}
}
