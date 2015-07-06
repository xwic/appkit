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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.xwic.appkit.core.trace.ITraceCategory;
import de.xwic.appkit.core.trace.ITraceContext;
import de.xwic.appkit.core.trace.ITraceOperation;
import de.xwic.appkit.core.trace.StackTraceSnapShot;

/**
 * The trace context collections informations during a request/activity.
 * @author lippisch
 */
public class TraceContext extends TraceOperation implements ITraceContext {

	private Map<String, ITraceCategory> traceCategories  = new HashMap<String, ITraceCategory>();
	private Map<String, String> attributes = new HashMap<String, String>();
	
	private List<StackTraceSnapShot> stss = new ArrayList<StackTraceSnapShot>();
	private long nextAllowedStackTraceSnapShot = 0;
	
	private Thread myThread = null;
	
	/**
	 * Constructor
	 */
	public TraceContext() {
		super();
		myThread = Thread.currentThread();
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.trace.ITraceContext#takeStackTrace()
	 */
	@Override
	public void doStackTraceSnapShot() {
		
		if (endTime == 0) { // still running
			
			long now = System.currentTimeMillis();
			if (nextAllowedStackTraceSnapShot < now) {
				StackTraceElement[] stackTrace = myThread.getStackTrace();
				if (stackTrace.length > 0) {
					stss.add(new StackTraceSnapShot(stackTrace));
				}
				// dont take another snapshot before ([number of shots taken] * 500).
				// this means that if there are 5 snapshots already taken, 2.5 seconds need to pass before another one is taken.
				nextAllowedStackTraceSnapShot = now + (stss.size() * 500);
			}
		}
		
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.trace.ITraceContext#getTraceCategory(java.lang.String)
	 */
	public ITraceCategory getTraceCategory(String category) {
		return traceCategories.get(category);
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.trace.ITraceContext#getTraceCategories()
	 */
	public Map<String, ITraceCategory> getTraceCategories() {
		return traceCategories;
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.trace.ITraceContext#startOperation()
	 */
	public ITraceOperation startOperation() {
		return startOperation(CATEGORY_DEFAULT, null);
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.trace.ITraceContext#startOperation(java.lang.String)
	 */
	public ITraceOperation startOperation(String category) {
		return startOperation(category, null);
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.trace.ITraceContext#startOperation(java.lang.String, java.lang.String)
	 */
	public ITraceOperation startOperation(String category, String name) {
		ITraceCategory cat = traceCategories.get(category);
		if (cat == null) {
			cat = new TraceCategory(category);
			traceCategories.put(category, cat);
		}
		return cat.startOperation(name);
	}
	
	/**
	 * Set an attribute value. Attributes may be used to store further information
	 * about the trace that help to identify the trace.
	 * @param key
	 * @param value
	 */
	public void setAttribute(String key, String value) {
		attributes.put(key, value);
	}
	
	/**
	 * Returns an attribute value.
	 * @param key
	 * @return
	 */
	public String getAttribute(String key) {
		return attributes.get(key);
	}

	/**
	 * @return the attributes
	 */
	public Map<String, String> getAttributes() {
		return attributes;
	}

	/**
	 * @return the stss
	 */
	public List<StackTraceSnapShot> getStackTraceSnapShots() {
		return stss;
	}
}
