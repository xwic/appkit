/*
 * Copyright (c) 2009 Network Appliance, Inc.
 * All rights reserved.
 */

package de.xwic.appkit.core.trace.impl;

import java.util.ArrayList;
import java.util.List;

import de.xwic.appkit.core.trace.ITraceCategory;
import de.xwic.appkit.core.trace.ITraceOperation;

/**
 * Manages a group of durations elements. A category is used for similar traces that 
 * are done within one cycle, i.e. for database access.
 * @author lippisch
 */
public class TraceCategory implements ITraceCategory {

	private List<TraceOperation> operations = new ArrayList<TraceOperation>();
	
	private String name;
	
	/**
	 * Construct a new category. 
	 */
	public TraceCategory() {
		super();
	}
	
	/**
	 * @param name
	 */
	public TraceCategory(String name) {
		super();
		this.name = name;
	}



	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.trace.impl.ITraceCategory#getName()
	 */
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.trace.impl.ITraceCategory#setName(java.lang.String)
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.trace.impl.ITraceCategory#getCount()
	 */
	public int getCount() {
		return operations.size();
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.trace.impl.ITraceCategory#startOperation()
	 */
	public ITraceOperation startOperation() {
		return startOperation(null);
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.trace.impl.ITraceCategory#startOperation(java.lang.String)
	 */
	public ITraceOperation startOperation(String name) {
		if (name == null) {
			name = this.name + "-" + operations.size();
		}
		TraceOperation td = new TraceOperation(name);
		operations.add(td);
		return td;
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.trace.ITraceCategory#getTotalDuration()
	 */
	@Override
	public long getTotalDuration() {
		long d = 0;
		for (ITraceOperation op : operations) {
			d += op.getDuration();
		}
		return d;
	}

	/**
	 * @return the operations
	 */
	public List<TraceOperation> getTraceOperations() {
		return operations;
	}
	
}
