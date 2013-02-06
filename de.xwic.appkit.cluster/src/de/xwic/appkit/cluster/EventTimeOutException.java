/**
 * 
 */
package de.xwic.appkit.cluster;

/**
 * @author lippisch
 *
 */
public class EventTimeOutException extends Exception {

	/**
	 * 
	 */
	public EventTimeOutException() {
	}

	/**
	 * @param message
	 */
	public EventTimeOutException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public EventTimeOutException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public EventTimeOutException(String message, Throwable cause) {
		super(message, cause);
	}

}
