package de.xwic.appkit.core.scheduler;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.ObjectAlreadyExistsException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.model.daos.IActivityDAO;
import de.xwic.appkit.core.model.daos.IPicklisteDAO;
import de.xwic.appkit.core.model.entities.IActivity;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.core.util.DateFormatter;
import de.xwic.appkit.core.util.StringUtil;

/**
 * Quartz scheduler implementation
 * 
 * @author dotto
 * 
 */
public class QuartzScheduler implements IScheduler {

	private static Log log = LogFactory.getLog(QuartzScheduler.class);
	protected IActivityDAO activityDao;
	protected IPicklisteDAO plDao;
	protected Scheduler scheduler;

	protected static final String CRON_GROUP = "Cron Group";
	protected static final String ONE_TIME_GROUP = "One Time";

	protected TimeZone tz = null;
	/**
	 * @throws SchedulerException
	 */
	public QuartzScheduler(TimeZone tz) throws SchedulerException {
		this.scheduler = new StdSchedulerFactory().getScheduler();
		this.tz = tz;
		activityDao = (IActivityDAO) DAOSystem.getDAO(IActivityDAO.class);
		plDao = (IPicklisteDAO) DAOSystem.getDAO(IPicklisteDAO.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.netapp.wwpc.scheduler.IScheduler#start()
	 */
	@Override
	public void start() {
		try {
			if(scheduler.isShutdown()){
				this.scheduler = new StdSchedulerFactory().getScheduler();
			}
			scheduler.start();
			init();
		} catch (SchedulerException e) {
			log.error("Cannot start the scheduler", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.netapp.wwpc.scheduler.IScheduler#stop()
	 */
	@Override
	public void stop() {
		try {
			scheduler.shutdown();
			log.info("Scheduler has been stopped");
		} catch (SchedulerException e) {
			log.error("Cannot stop the scheduler", e);
		}
	}

	/**
	 * 
	 */
	protected void init() {
		PropertyQuery pq = new PropertyQuery();
		pq.addNotEquals("status.key", IActivity.PE_ACTIVITY_STATUS_DISABLED);

		List<IActivity> activities = activityDao.getEntities(null, pq);
		for (IActivity activity : activities) {

			try {
				addCronJob(activity);
				// reset the status in case it got aborted accidentially
				if (activity.getStatus() == null || activity.getStatus().getKey().equals(IActivity.PE_ACTIVITY_STATUS_RUN)) {
					activity.setStatus(plDao.getPickListEntryByKey(IActivity.PL_ACTIVITY_STATUS, IActivity.PE_ACTIVITY_STATUS_PENDING));
				}
				activityDao.update(activity);
			} catch (SchedulerException e) {
				log.error("Cannot add the cron job", e);
			} catch (ClassNotFoundException e) {
				log.error("Job Class cannot be found or initialized", e);
			}
		}
	}

	/**
	 * @param activity
	 * @throws SchedulerException
	 * @throws ClassNotFoundException
	 */
	public void addCronJob(IActivity activity) throws SchedulerException, ClassNotFoundException {
		try {
			if (!scheduler.isStarted()) {
				return;
			}

			if (activity.getStatus() == null || activity.getStatus().getKey().equals(IActivity.PE_ACTIVITY_STATUS_DISABLED)) {
				return;
			}

			if (StringUtil.isEmpty(activity.getCronExpression())) {
				return;
			}

			@SuppressWarnings("unchecked")
			JobBuilder jb = JobBuilder.newJob((Class<? extends Job>) Class.forName(activity.getJobClass()));
			String jobName = activity.getTitle() + " id: " + activity.getId();
			jb.withIdentity(jobName, CRON_GROUP);
			JobDetail detail = jb.build();

			detail.getJobDataMap().put(AbstractActivityJob.ACTIVITY_KEY, activity.getId());

			CronTrigger trigger = buildCronTrigger(activity, jobName);

			scheduler.scheduleJob(detail, trigger);
			Date nextFireTime = trigger.getNextFireTime();
			activity.setNextRun(nextFireTime);
			log.info("Job " + jobName + " scheduled and will fire at: " + (nextFireTime != null ? DateFormatter.getDateTimeSeconds(tz).format(nextFireTime) : "") + " " + tz.getID());
		} catch (SchedulerException ex) {
			log.error("Cannot add the cron job", ex);
			throw ex;
		} catch (ClassNotFoundException ex) {
			log.error("Job Class cannot be found or initialized", ex);
			throw ex;
		}
	}

	/**
	 * @param activity
	 */
	public void runJob(IActivity activity) {
		JobDetail detail = null;
		Trigger trigger = null;
		String jobName = null;
		try {
			if (!scheduler.isStarted()) {
				return;
			}
			@SuppressWarnings("unchecked")
			JobBuilder jb = JobBuilder.newJob((Class<? extends Job>) Class.forName(activity.getJobClass()));
			jobName = activity.getTitle() + " id: " + activity.getId();
			jb.withIdentity(jobName, ONE_TIME_GROUP);
			detail = jb.build();

			detail.getJobDataMap().put(AbstractActivityJob.ACTIVITY_KEY, activity.getId());

			trigger = TriggerBuilder.newTrigger().withIdentity("One Time Job " + jobName, ONE_TIME_GROUP)
					.withSchedule(SimpleScheduleBuilder.repeatSecondlyForTotalCount(1)).build();
			scheduler.scheduleJob(detail, trigger);
		} catch (ObjectAlreadyExistsException ex) {
			try {
				log.info(ex);
				scheduler.rescheduleJob(TriggerKey.triggerKey(jobName, ONE_TIME_GROUP), trigger);
			} catch (SchedulerException e) {
				log.error("Cannot reschedule the job", e);
			}
		} catch (Exception e) {
			log.error("Cannot add the job", e);
		}
	}

	/**
	 * @param activity
	 * @param jobName
	 * @return
	 */
	private CronTrigger buildCronTrigger(IActivity activity, String jobName) {
		return TriggerBuilder.newTrigger().withIdentity("Cron Job " + jobName, CRON_GROUP)
				.withSchedule(CronScheduleBuilder.cronSchedule(activity.getCronExpression()).inTimeZone(tz)).build();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.netapp.wwpc.scheduler.IScheduler#changeCronJob(com.netapp.wwpc.scheduler.entities.IActivity)
	 */
	public void changeCronJob(IActivity activity) throws SchedulerException, ClassNotFoundException {
		try {
			if (!scheduler.isStarted()) {
				return;
			}

			String jobName = activity.getTitle() + " id: " + activity.getId();
			if (activity.isDeleted() || StringUtil.isEmpty(activity.getCronExpression())
					|| (activity.getStatus() != null && activity.getStatus().getKey().equals(IActivity.PE_ACTIVITY_STATUS_DISABLED))) {
				if (scheduler.deleteJob(JobKey.jobKey(jobName, CRON_GROUP))) {
					log.info("Job " + jobName + " has been removed from scheduler");
				}
			} else {
				CronTrigger trigger = buildCronTrigger(activity, jobName);
				if(scheduler.rescheduleJob(TriggerKey.triggerKey("Cron Job " + jobName, CRON_GROUP), trigger) == null){
					addCronJob(activity);
				}else {
					Date nextFireTime = trigger.getNextFireTime();
					activity.setNextRun(nextFireTime);
					log.info("Next Fire Time: " + (nextFireTime != null ? DateFormatter.getDateTimeSeconds(null).format(nextFireTime) : ""));
				}
				activityDao.update(activity);
				
			}
		} catch (SchedulerException ex) {
			log.error("Error in changing cron job:", ex);
			throw ex;
		}
	}

	/**
	 * @param activity
	 */
	public boolean interruptJob(IActivity activity) {
		try {
			if (!scheduler.isStarted()) {
				return false;
			}
			String jobName = activity.getTitle() + " id: " + activity.getId();
			JobKey jobKey = JobKey.jobKey(jobName, ONE_TIME_GROUP);
			List<JobExecutionContext> jobs = scheduler.getCurrentlyExecutingJobs();
			for (JobExecutionContext context : jobs) {
				if (context.getJobDetail().getKey().equals(jobKey)) {
					scheduler.interrupt(jobKey);
					return true;
				}
			}

			jobKey = JobKey.jobKey(jobName, CRON_GROUP);
			for (JobExecutionContext context : jobs) {
				if (context.getJobDetail().getKey().equals(jobKey)) {
					scheduler.interrupt(jobKey);
					return true;
				}
			}
		} catch (Exception ex) {
			log.error("Error in interrupting job:", ex);
		}
		return false;
	}

}
