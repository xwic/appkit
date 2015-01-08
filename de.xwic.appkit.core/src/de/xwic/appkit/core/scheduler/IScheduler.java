package de.xwic.appkit.core.scheduler;

import org.quartz.SchedulerException;

import de.xwic.appkit.core.model.entities.IActivity;

/**
 * @author dotto
 * 
 */
public interface IScheduler {

	/**
	 * Starts the job scheduling.
	 */
	void start();

	/**
	 * Stops the job scheduling.
	 */
	void stop();

	/**
	 * Change Schedule for given activity.
	 * 
	 * @param activity
	 * @throws ClassNotFoundException 
	 */
	void changeCronJob(IActivity activity) throws SchedulerException, ClassNotFoundException;

	/**
	 * Add new cron job to scheduler.
	 * 
	 * @param activity
	 * @throws ClassNotFoundException 
	 */
	void addCronJob(IActivity activity) throws SchedulerException, ClassNotFoundException;

	/**
	 * Runs given job immediately
	 * 
	 * @param activity
	 */
	void runJob(IActivity activity);

	/**
	 * Interrupts a job
	 * 
	 * @param activity
	 * @return
	 */
	boolean interruptJob(IActivity activity);

}
