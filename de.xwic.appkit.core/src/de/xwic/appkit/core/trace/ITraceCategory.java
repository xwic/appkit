/*
 * Copyright (c) 2009 Network Appliance, Inc.
 * All rights reserved.
 */

package de.xwic.appkit.core.trace;

import java.util.List;

import de.xwic.appkit.core.trace.impl.TraceOperation;


/**
 * @author lippisch
 */
public interface ITraceCategory {

	/**
	 * @return the name
	 */
	public abstract String getName();

	/**
	 * @param name the name to set
	 */
	public abstract void setName(String name);

	/**
	 * Returns the number of operations recorded.
	 * @return
	 */
	public abstract int getCount();

	/**
	 * Create a new TraceDuration.
	 * @return
	 */
	public abstract ITraceOperation startOperation();

	/**
	 * Create a new named TraceDuration.
	 * @return
	 */
	public abstract ITraceOperation startOperation(String name);

	/**
	 * @return
	 */
	public abstract long getTotalDuration();

	/**
	 * Returns the list of TraceOperations.
	 * @return
	 */
	public List<TraceOperation> getTraceOperations();
	
}