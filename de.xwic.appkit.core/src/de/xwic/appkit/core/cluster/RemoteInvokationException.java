/**
 * 
 */
package de.xwic.appkit.core.cluster;

/**
 * @author lippisch
 */
public class RemoteInvokationException extends Exception {

	/**
	 * 
	 */
	public RemoteInvokationException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public RemoteInvokationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public RemoteInvokationException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public RemoteInvokationException(Throwable cause) {
		super(cause);
	}

}
