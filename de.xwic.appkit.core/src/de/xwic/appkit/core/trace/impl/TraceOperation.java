/*******************************************************************************
 * Copyright 2015 xWic group (http://www.xwic.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *  
 *******************************************************************************/

package de.xwic.appkit.core.trace.impl;

import de.xwic.appkit.core.trace.ITraceOperation;

/**
 * @author lippisch
 */
public class TraceOperation implements ITraceOperation {

	protected long startTime;
	protected long endTime = 0;

	private long startDuration;
	private long endDuration;

	protected String name = null;
	protected String info = null;

	/**
	 * Start a new duration based on the current system time.
	 */
	public TraceOperation() {
		startTime = System.currentTimeMillis();
		startDuration = System.nanoTime();
	}

	/**
	 * @param name
	 */
	public TraceOperation(String name) {
		this();
		this.name = name;
	}

	/**
	 * @param name
	 * @param startTime
	 * @param endTime
	 */
	public TraceOperation(String name, long startTime, long endTime) {
		this(name);
		this.startTime = startTime;
		this.endTime = endTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.trace.ITraceOperation#stop()
	 */
	@Override
	public void finished() {
		endTime = System.currentTimeMillis();
		endDuration = System.nanoTime();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.trace.ITraceOperation#getDuration()
	 */
	@Override
	public long getDuration() {
		if (startDuration != 0) {
			//we are measuring the operation, it's in nanos
			long stop = endDuration != 0 ? endDuration : System.nanoTime();
			return (stop - startDuration) / 1000000;
		} else {
			//we received the start and end time, they were measured externally
			return endTime - startTime;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.trace.ITraceOperation#restart()
	 */
	@Override
	public void restart() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.trace.ITraceOperation#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.trace.ITraceOperation#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.trace.ITraceOperation#getInfo()
	 */
	@Override
	public String getInfo() {
		return info;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.trace.ITraceOperation#setInfo(java.lang.String)
	 */
	@Override
	public void setInfo(String info) {
		this.info = info;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.trace.ITraceOperation#getEndTime()
	 */
	@Override
	public long getEndTime() {
		return endTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.trace.ITraceOperation#getStartTime()
	 */
	@Override
	public long getStartTime() {
		return startTime;
	}

}
