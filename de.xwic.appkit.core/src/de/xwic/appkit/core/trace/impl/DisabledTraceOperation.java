/*
 * Copyright (c) 2009 Network Appliance, Inc.
 * All rights reserved.
 */

package de.xwic.appkit.core.trace.impl;

import de.xwic.appkit.core.trace.ITraceOperation;

/**
 * Used when trace is disabled.
 * @author lippisch
 */
public class DisabledTraceOperation implements ITraceOperation {

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
	 * @see de.xwic.appkit.core.trace.ITraceOperation#getEndTime()
	 */
	@Override
	public long getEndTime() {
		return 0;
	}
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.trace.ITraceOperation#getStartTime()
	 */
	@Override
	public long getStartTime() {
		return 0;
	}
	
}
