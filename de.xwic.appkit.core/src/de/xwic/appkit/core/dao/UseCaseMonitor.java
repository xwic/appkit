/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.dao.UseCaseMonitor
 * Created on 04.02.2007 by Florian Lippisch
 *
 */
package de.xwic.appkit.core.dao;

/**
 * Acts as a monitor for asynchronous UseCase execution. The monitor is used by
 * the user of the UseCase to monitor the status of the UseCase processing.
 * 
 * @author Florian Lippisch
 */
public class UseCaseMonitor implements Cloneable {

	private long ticketNr = 0;
	
	private long startTime = 0;
	private long finishTime = 0;
	
	/** Counter for progress bar */
	private int counter = 0;
	private int max = 0;
	private int min = 0;
	
	private boolean abortable = false;
	private boolean aborted = false;
	private boolean started = false;
	private boolean failed = false;
	private boolean finished = false;
	
	private String info = null;

	private Object result = null;
	
	/**
	 * @return the abortable
	 */
	public boolean isAbortable() {
		return abortable;
	}

	/**
	 * @param abortable the abortable to set
	 */
	public void setAbortable(boolean abortable) {
		this.abortable = abortable;
	}

	/**
	 * @return the aborted
	 */
	public boolean isAborted() {
		return aborted;
	}

	/**
	 * @param aborted the aborted to set
	 */
	public void setAborted(boolean aborted) {
		this.aborted = aborted;
	}

	/**
	 * @return the counter
	 */
	public int getCounter() {
		return counter;
	}

	/**
	 * @param counter the counter to set
	 */
	public void setCounter(int counter) {
		this.counter = counter;
	}

	/**
	 * @return the finishTime
	 */
	public long getFinishTime() {
		return finishTime;
	}

	/**
	 * @param finishTime the finishTime to set
	 */
	public void setFinishTime(long finishTime) {
		this.finishTime = finishTime;
	}

	/**
	 * @return the info
	 */
	public String getInfo() {
		return info;
	}

	/**
	 * @param info the info to set
	 */
	public void setInfo(String info) {
		this.info = info;
	}

	/**
	 * @return the max
	 */
	public int getMax() {
		return max;
	}

	/**
	 * @param max the max to set
	 */
	public void setMax(int max) {
		this.max = max;
	}

	/**
	 * @return the min
	 */
	public int getMin() {
		return min;
	}

	/**
	 * @param min the min to set
	 */
	public void setMin(int min) {
		this.min = min;
	}

	/**
	 * @return the started
	 */
	public boolean isStarted() {
		return started;
	}

	/**
	 * @param started the started to set
	 */
	public void setStarted(boolean started) {
		this.started = started;
	}

	/**
	 * @return the startTime
	 */
	public long getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the ticketNr
	 */
	public long getTicketNr() {
		return ticketNr;
	}

	/**
	 * @param ticketNr the ticketNr to set
	 */
	public void setTicketNr(long ticketNr) {
		this.ticketNr = ticketNr;
	}

	/**
	 * @return the result
	 */
	public Object getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(Object result) {
		this.result = result;
	}

	/**
	 * @return the failed
	 */
	public boolean isFailed() {
		return failed;
	}

	/**
	 * @param failed the failed to set
	 */
	public void setFailed(boolean failed) {
		this.failed = failed;
	}

	/**
	 * @return the finished
	 */
	public boolean isFinished() {
		return finished;
	}

	/**
	 * @param finished the finished to set
	 */
	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone() throws CloneNotSupportedException {
		UseCaseMonitor clone = (UseCaseMonitor)super.clone();
		clone.ticketNr = ticketNr;
		clone.startTime = startTime;
		clone.finishTime = finishTime;
		clone.counter = counter;
		clone.max = max;
		clone.min = min;
		clone.abortable = abortable;
		clone.aborted = aborted;
		clone.started = started;
		clone.failed = failed;
		clone.finished = finished;
		clone.info = info;
		clone.result = result;
		return clone;
	}

	/**
	 * Increase the counter by 1.
	 */
	public void count() {
		counter++;
	}
	
}
