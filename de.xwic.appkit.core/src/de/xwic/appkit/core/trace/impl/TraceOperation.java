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
	
	protected String name = null;
	protected String info = null;
	
	/**
	 * Start a new duration based on the current system time.
	 */
	public TraceOperation() {
		startTime = System.currentTimeMillis();
	}
	
	/**
	 * @param name
	 */
	public TraceOperation(String name) {
		super();
		this.name = name;
		startTime = System.currentTimeMillis();
	}

	/**
	 * @param startTime
	 * @param endTime
	 */
	public TraceOperation(long startTime) {
		super();
		this.startTime = startTime;
	}

	/**
	 * @param startTime
	 * @param endTime
	 */
	public TraceOperation(long startTime, long endTime) {
		super();
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.trace.ITraceOperation#stop()
	 */
	public void finished() {
		endTime = System.currentTimeMillis();
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.trace.ITraceOperation#setEndTime(long)
	 */
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.trace.ITraceOperation#getDuration()
	 */
	public long getDuration() {
		if (endTime != 0) {
			return endTime - startTime;
		}
		return System.currentTimeMillis() - startTime;
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.trace.ITraceOperation#restart()
	 */
	public void restart() {
		if (endTime != 0) { // already ended
			
		}
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.trace.ITraceOperation#getName()
	 */
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.trace.ITraceOperation#setName(java.lang.String)
	 */
	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.trace.ITraceOperation#getInfo()
	 */
	public String getInfo() {
		return info;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.trace.ITraceOperation#setInfo(java.lang.String)
	 */
	public void setInfo(String info) {
		this.info = info;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.trace.ITraceOperation#getEndTime()
	 */
	@Override
	public long getEndTime() {
		return endTime;
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.trace.ITraceOperation#getStartTime()
	 */
	@Override
	public long getStartTime() {
		return startTime;
	}
	
}
