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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.xwic.appkit.core.trace.impl.DisabledTraceContext;
import de.xwic.appkit.core.trace.impl.TraceContext;
import de.xwic.appkit.core.trace.impl.TraceDataManager;


/**
 * Helps to trace certain events during one request/action. Binds activity information to the current thread
 * to allow an analysis after processing an event or request.
 * 
 * @author lippisch
 */
public class Trace {

	private static boolean enabled = false;
	
	private static ThreadLocal<ITraceContext> tlCtx = new ThreadLocal<ITraceContext>();
	private static DisabledTraceContext disabledContext = new DisabledTraceContext();
	private static ITraceDataManager dataManager = new TraceDataManager();
	
	private static Map<Long, ITraceContext> activeTraces = new HashMap<Long, ITraceContext>();
	
	/**
	 * Initialize the current thread with a new TraceContext. Any trace information made
	 * prior to this call are lost.
	 * @return 
	 */
	public static ITraceContext startTrace() {
		
		if (enabled) {
			ITraceContext tx = new TraceContext();
			tlCtx.set(tx);
			synchronized (activeTraces) {
				activeTraces.put(Thread.currentThread().getId(), tx);
			}
			return tx;
		} else {
			return disabledContext;
		}
		
	}
	
	/**
	 * Returns the TraceContext associated with the current thread. If the context is not created
	 * yet, it will be started.
	 * @return
	 */
	public static ITraceContext getTraceContext() {
		// do not create a traceContext through getTraceContext...
		if (!enabled || tlCtx.get() == null) {
			// return a trace context in case it is used even thought that it is not enabled
			// to avoid NPEs.
			return disabledContext;
		}
		return tlCtx.get();
	}
	
	/**
	 * Remove the trace context from the current thread.
	 */
	public static ITraceContext endTrace() {
		ITraceContext tx = tlCtx.get();
		if (tx != null) {
			tx.finished();
			synchronized (activeTraces) {
				activeTraces.remove(Thread.currentThread().getId());
			}
		}
		tlCtx.set(null); 
		return tx;
	}

	/**
	 * @return the enabled
	 */
	public static boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public static void setEnabled(boolean enabled) {
		Trace.enabled = enabled;
	}
	
	/**
	 * Trace a new operation. The duration of the operation is tracked
	 * until the ITraceOperation.finish() method is invoked.
	 * @param category
	 * @return
	 */
	public static ITraceOperation startOperation(String category) {
		return getTraceContext().startOperation(category);
	}

	/**
	 * @return the dataManager
	 */
	public static ITraceDataManager getDataManager() {
		return dataManager;
	}

	/**
	 * @param dataManager the dataManager to set
	 */
	public static void setDataManager(ITraceDataManager dataManager) {
		Trace.dataManager = dataManager;
	}

	/**
	 * Returns a snapshot of the active ITraceContext objects at this time. If
	 * ITraceContext objects are added/removed later on it will not be reflected
	 * in the list.
	 * @return the activeTraces
	 */
	public static Collection<ITraceContext> getActiveTraces() {
		List<ITraceContext> list = new ArrayList<ITraceContext>();
		synchronized (activeTraces) {
			for (ITraceContext tx : activeTraces.values()) {
				list.add(tx);
			}
		}
		return list;
	}
	
}
