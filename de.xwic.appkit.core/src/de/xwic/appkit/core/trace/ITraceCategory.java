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
	 * @param name
	 *            the name to set
	 */
	public abstract void setName(String name);

	/**
	 * Returns the number of operations recorded.
	 * 
	 * @return
	 */
	public abstract int getCount();

	/**
	 * Create a new TraceDuration.
	 * 
	 * @return
	 */
	public abstract ITraceOperation startOperation();

	/**
	 * Create a new named TraceDuration.
	 * 
	 * @return
	 */
	public abstract ITraceOperation startOperation(String name);

	/**
	 * @return
	 */
	public abstract long getTotalDuration();

	/**
	 * Returns the list of TraceOperations.
	 * 
	 * @return
	 */
	public List<TraceOperation> getTraceOperations();

	/**
	 * Add an external TraceOperation
	 * 
	 * @param name
	 * @param duration
	 * @param info
	 */
	public void addOperation(String name, long duration, String info);

}
