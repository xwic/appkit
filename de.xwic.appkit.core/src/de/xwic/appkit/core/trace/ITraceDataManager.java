/*
 * Copyright (c) 2009 Network Appliance, Inc.
 * All rights reserved.
 */

package de.xwic.appkit.core.trace;

import java.io.File;
import java.util.List;


/**
 * @author lippisch
 */
public interface ITraceDataManager {

	public enum TraceLevel {
		BASIC,
		MEDIUM,
		DETAILED
	}
	
	/**
	 * Handle a finished trace result.
	 * @param traceContext
	 */
	public abstract void handleTraceResult(ITraceContext traceContext);

	/**
	 * @return the logTraceAboveThreshold
	 */
	public abstract boolean isLogTraceAboveThreshold();

	/**
	 * @param logTraceAboveThreshold the logTraceAboveThreshold to set
	 */
	public abstract void setLogTraceAboveThreshold(boolean logTraceAboveThreshold);

	/**
	 * @return the logThreshold
	 */
	public abstract long getLogThreshold();

	/**
	 * @param logThreshold the logThreshold to set
	 */
	public abstract void setLogThreshold(long logThreshold);


	/**
	 * @return the traceLogFile
	 */
	public File getTraceLogFile();

	/**
	 * @param traceLogFile the traceLogFile to set
	 */
	public void setTraceLogFile(File traceLogFile);

	/**
	 * @return the traceLevel
	 */
	public TraceLevel getTraceLevel();

	/**
	 * @param traceLevel the traceLevel to set
	 */
	public void setTraceLevel(TraceLevel traceLevel);

	/**
	 * @return
	 */
	boolean isKeepHistory();

	/**
	 * @param keepHistory
	 */
	void setKeepHistory(boolean keepHistory);

	/**
	 * @return
	 */
	int getMaxHistory();

	/**
	 * @param maxHistory
	 */
	void setMaxHistory(int maxHistory);

	/**
	 * Returns the history, with the latest entry at the end of the list. 
	 * @return
	 */
	List<ITraceContext> getTraceHistory();

	/**
	 * Notifies the data manager to save a new SystemTraceStatistic entity with 
	 * the statistic for the given interval.
	 * 
	 * @param interval
	 */
	public abstract void saveSystemTraceStatistic(long interval);

	/**
	 * @return the customCat1Name
	 */
	public String getCustomCat1Name();

	/**
	 * @param customCat1Name the customCat1Name to set
	 */
	public void setCustomCat1Name(String customCat1Name);

	/**
	 * @return the customCat2Name
	 */
	public String getCustomCat2Name();

	/**
	 * @param customCat2Name the customCat2Name to set
	 */
	public void setCustomCat2Name(String customCat2Name);

	/**
	 * @return the customCat3Name
	 */
	public String getCustomCat3Name();

	/**
	 * @param customCat3Name the customCat3Name to set
	 */
	public void setCustomCat3Name(String customCat3Name);

	
}