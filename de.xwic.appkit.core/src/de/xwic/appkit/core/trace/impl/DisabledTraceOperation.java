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
