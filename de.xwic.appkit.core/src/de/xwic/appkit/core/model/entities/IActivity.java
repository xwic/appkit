package de.xwic.appkit.core.model.entities;

import java.util.Date;

import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.entities.IPicklistEntry;

public interface IActivity extends IEntity {

	public static final String PL_ACTIVITY_STATUS = "activity.status";
	public static final String PE_ACTIVITY_STATUS_DISABLED = "activity.status.disabled";
	public static final String PE_ACTIVITY_STATUS_RUN = "activity.status.run";
	public static final String PE_ACTIVITY_STATUS_PENDING = "activity.status.pending";
	public static final String PE_ACTIVITY_STATUS_ERROR = "activity.status.error";

	/**
	 * @return the title
	 */
	public abstract String getTitle();

	/**
	 * @param title
	 *            the title to set
	 */
	public abstract void setTitle(String title);

	/**
	 * @return the jobClass
	 */
	public abstract String getJobClass();

	/**
	 * @param jobClass
	 *            the jobClass to set
	 */
	public abstract void setJobClass(String jobClass);

	/**
	 * @return the cronExpression
	 */
	public abstract String getCronExpression();

	/**
	 * @param cronExpression
	 *            the cronExpression to set
	 */
	public abstract void setCronExpression(String cronExpression);

	/**
	 * @return the executionTime
	 */
	public abstract long getExecutionTime();

	/**
	 * @param executionTime
	 *            the executionTime to set
	 */
	public abstract void setExecutionTime(long executionTime);

	/**
	 * @return the lastRun
	 */
	public abstract Date getLastRun();

	/**
	 * @param lastRun
	 *            the lastRun to set
	 */
	public abstract void setLastRun(Date lastRun);

	/**
	 * @return the nextRun
	 */
	public abstract Date getNextRun();

	/**
	 * @param nextRun
	 *            the nextRun to set
	 */
	public abstract void setNextRun(Date nextRun);

	/**
	 * @return the lastExecutionMessage
	 */
	public abstract String getLastExecutionMessage();

	/**
	 * @param lastExecutionMessage
	 *            the lastExecutionMessage to set
	 */
	public abstract void setLastExecutionMessage(String lastExecutionMessage);

	/**
	 * @return the status
	 */
	public abstract IPicklistEntry getStatus();

	/**
	 * @param status
	 *            the status to set
	 */
	public abstract void setStatus(IPicklistEntry status);

}
