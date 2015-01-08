package de.xwic.appkit.core.model.entities.impl;

import java.util.Date;

import de.xwic.appkit.core.dao.Entity;
import de.xwic.appkit.core.model.entities.IActivity;
import de.xwic.appkit.core.model.entities.IPicklistEntry;

/**
 * @author dotto
 * 
 */
public class Activity extends Entity implements IActivity {

	private String title;
	private String jobClass;
	private String cronExpression;
	private long executionTime;
	private Date lastRun;
	private Date nextRun;
	private String lastExecutionMessage;
	private IPicklistEntry status;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.netapp.wwpc.scheduler.entities.impl.IActivity#getTitle()
	 */
	@Override
	public String getTitle() {
		return title;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.netapp.wwpc.scheduler.entities.impl.IActivity#setTitle(java.lang.String)
	 */
	@Override
	public void setTitle(String title) {
		this.title = title;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.netapp.wwpc.scheduler.entities.impl.IActivity#getJobClass()
	 */
	@Override
	public String getJobClass() {
		return jobClass;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.netapp.wwpc.scheduler.entities.impl.IActivity#setJobClass(java.lang.String)
	 */
	@Override
	public void setJobClass(String jobClass) {
		this.jobClass = jobClass;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.netapp.wwpc.scheduler.entities.impl.IActivity#getCronExpression()
	 */
	@Override
	public String getCronExpression() {
		return cronExpression;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.netapp.wwpc.scheduler.entities.impl.IActivity#setCronExpression(java.lang.String)
	 */
	@Override
	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.netapp.wwpc.scheduler.entities.impl.IActivity#getExecutionTime()
	 */
	@Override
	public long getExecutionTime() {
		return executionTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.netapp.wwpc.scheduler.entities.impl.IActivity#setExecutionTime(java.util.Date)
	 */
	@Override
	public void setExecutionTime(long executionTime) {
		this.executionTime = executionTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.netapp.wwpc.scheduler.entities.impl.IActivity#getLastRun()
	 */
	@Override
	public Date getLastRun() {
		return lastRun;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.netapp.wwpc.scheduler.entities.impl.IActivity#setLastRun(java.util.Date)
	 */
	@Override
	public void setLastRun(Date lastRun) {
		this.lastRun = lastRun;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.netapp.wwpc.scheduler.entities.impl.IActivity#getNextRun()
	 */
	@Override
	public Date getNextRun() {
		return nextRun;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.netapp.wwpc.scheduler.entities.impl.IActivity#setNextRun(java.util.Date)
	 */
	@Override
	public void setNextRun(Date nextRun) {
		this.nextRun = nextRun;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.netapp.wwpc.scheduler.entities.impl.IActivity#getLastExecutionMessage()
	 */
	@Override
	public String getLastExecutionMessage() {
		return lastExecutionMessage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.netapp.wwpc.scheduler.entities.impl.IActivity#setLastExecutionMessage(java.lang.String)
	 */
	@Override
	public void setLastExecutionMessage(String lastExecutionMessage) {
		this.lastExecutionMessage = lastExecutionMessage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.netapp.wwpc.scheduler.entities.impl.IActivity#getStatus()
	 */
	@Override
	public IPicklistEntry getStatus() {
		return status;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.netapp.wwpc.scheduler.entities.impl.IActivity#setStatus(de.xwic.appkit.core.model.entities.IPicklistEntry)
	 */
	@Override
	public void setStatus(IPicklistEntry status) {
		this.status = status;
	}
}
