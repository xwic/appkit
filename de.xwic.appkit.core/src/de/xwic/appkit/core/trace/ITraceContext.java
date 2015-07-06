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
import java.util.Map;


/**
 * @author lippisch
 */
public interface ITraceContext extends ITraceOperation {

	public final static String CATEGORY_DEFAULT = "default";

	/**
	 * Returns the category with the specified name of null if it does not exist.
	 * @param category
	 * @return
	 */
	public abstract ITraceCategory getTraceCategory(String category);

	/**
	 * Returns the map of all categories.
	 * @return
	 */
	public abstract Map<String, ITraceCategory> getTraceCategories();

	/**
	 * Trace a new operation in the default category.
	 * @return
	 */
	public abstract ITraceOperation startOperation();

	/**
	 * Trace a new operation.
	 * @param category
	 * @param name
	 * @return
	 */
	public abstract ITraceOperation startOperation(String category);

	/**
	 * Trace a new operation.
	 * @param category
	 * @param name
	 * @return
	 */
	public abstract ITraceOperation startOperation(String category, String name);
	
	/**
	 * Set an attribute value. Attributes may be used to store further information
	 * about the trace that help to identify the trace.
	 * @param key
	 * @param value
	 */
	public void setAttribute(String key, String value);
	
	/**
	 * Returns an attribute value.
	 * @param key
	 * @return
	 */
	public String getAttribute(String key);
	
	/**
	 * @return the attributes
	 */
	public Map<String, String> getAttributes();

	/**
	 * Take a stack trace snapshot and add it to the list.
	 */
	public abstract void doStackTraceSnapShot();
	
	/**
	 * Returns the list of SnapShots taken.
	 * @return
	 */
	public List<StackTraceSnapShot> getStackTraceSnapShots();
}