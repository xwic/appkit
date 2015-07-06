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