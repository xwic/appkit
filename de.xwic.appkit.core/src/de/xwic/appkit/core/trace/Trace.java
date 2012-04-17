package de.xwic.appkit.core.trace;

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
	
	/**
	 * Initialize the current thread with a new TraceContext. Any trace information made
	 * prior to this call are lost.
	 * @return 
	 */
	public static ITraceContext startTrace() {
		
		if (enabled) {
			ITraceContext tx = new TraceContext();
			tlCtx.set(tx);
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
		if (!enabled) {
			// return a trace context in case it is used even thought that it is not enabled
			// to avoid NPEs.
			return disabledContext;
		}
		if (tlCtx.get() == null) {
			startTrace();
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
	
}
