/*
 * Copyright (c) 2009 Network Appliance, Inc.
 * All rights reserved.
 */

package de.xwic.appkit.core.trace.impl;

import java.util.HashMap;
import java.util.Map;

import de.xwic.appkit.core.trace.ITraceCategory;
import de.xwic.appkit.core.trace.ITraceContext;
import de.xwic.appkit.core.trace.ITraceOperation;

/**
 * This ITraceContext implementation is used when the Trace is disabled. It does not 
 * store anything but avoids creation of objects on locations that start tracing
 * operations without checking if Trace is enabled.
 * @author lippisch
 */
public class DisabledTraceContext extends DisabledTraceOperation implements ITraceContext {

	private DisabledTraceOperation disTraceOp = new DisabledTraceOperation();
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.trace.ITraceContext#getTraceCategories()
	 */
	@Override
	public Map<String, ITraceCategory> getTraceCategories() {
		return new HashMap<String, ITraceCategory>();
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.trace.ITraceContext#getTraceCategory(java.lang.String)
	 */
	@Override
	public ITraceCategory getTraceCategory(String category) {
		return null;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.trace.ITraceContext#startOperation()
	 */
	@Override
	public ITraceOperation startOperation() {
		return disTraceOp;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.trace.ITraceContext#startOperation(java.lang.String)
	 */
	@Override
	public ITraceOperation startOperation(String category) {
		return disTraceOp;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.trace.ITraceContext#startOperation(java.lang.String, java.lang.String)
	 */
	@Override
	public ITraceOperation startOperation(String category, String name) {
		return disTraceOp;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.trace.ITraceOperation#finished()
	 */
	@Override
	public void finished() {
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.trace.ITraceOperation#getDuration()
	 */
	@Override
	public long getDuration() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.trace.ITraceOperation#getInfo()
	 */
	@Override
	public String getInfo() {
		return null;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.trace.ITraceOperation#getName()
	 */
	@Override
	public String getName() {
		return "trace-disabled";
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.trace.ITraceOperation#restart()
	 */
	@Override
	public void restart() {
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.trace.ITraceOperation#setEndTime(long)
	 */
	@Override
	public void setEndTime(long endTime) {
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.trace.ITraceOperation#setInfo(java.lang.String)
	 */
	@Override
	public void setInfo(String info) {
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.trace.ITraceOperation#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.trace.ITraceContext#getAttribute(java.lang.String)
	 */
	@Override
	public String getAttribute(String key) {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.trace.ITraceContext#setAttribute(java.lang.String, java.lang.String)
	 */
	@Override
	public void setAttribute(String key, String value) {
		
	}
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.trace.ITraceContext#getAttributes()
	 */
	@Override
	public Map<String, String> getAttributes() {
		return new HashMap<String, String>();
	}
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.trace.ITraceOperation#getEndTime()
	 */

	
}
