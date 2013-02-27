/**
 * 
 */
package de.xwic.appkit.core.trace;

/**
 * Stores the data of a taken thread snapshot.
 * @author lippisch
 */
public class StackTraceSnapShot {

	long snapShotTime;
	StackTraceElement[] stackTrace;

	public StackTraceSnapShot (StackTraceElement[] st) {
		stackTrace = st;
		snapShotTime = System.currentTimeMillis();
	}

	/**
	 * @return the snapShotTime
	 */
	public long getSnapShotTime() {
		return snapShotTime;
	}

	/**
	 * @return the stackTrace
	 */
	public StackTraceElement[] getStackTrace() {
		return stackTrace;
	}
	
}
