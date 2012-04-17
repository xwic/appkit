/*
 * Copyright (c) 2009 Network Appliance, Inc.
 * All rights reserved.
 */

package de.xwic.appkit.core.trace;

/**
 * @author lippisch
 */
public interface ITraceOperation {

	/**
	 * Set the end time to now.
	 */
	public abstract void finished();

	/**
	 * Set the end time.
	 * @param endTime
	 */
	public abstract void setEndTime(long endTime);

	/**
	 * Returns the duration from start to end time. If the end time
	 * is not yet set (because its not ended yet), it returns the time since the start.
	 * @return
	 */
	public abstract long getDuration();

	/**
	 * Start counting the duration. If the current duration is already stopped,
	 * a new duration is started and the previous one is linked so that it is added
	 * to the total time.
	 */
	public abstract void restart();

	/**
	 * @return the name
	 */
	public abstract String getName();

	/**
	 * @param name the name to set
	 */
	public abstract void setName(String name);

	/**
	 * @return the info
	 */
	public abstract String getInfo();

	/**
	 * @param info the info to set
	 */
	public abstract void setInfo(String info);
	
	/**
	 * Returns the time the operation was finished.
	 * @return
	 */
	public abstract long getEndTime();
	
	/**
	 * Returns the time the operation did start.
	 * @return
	 */
	public abstract long getStartTime();

}