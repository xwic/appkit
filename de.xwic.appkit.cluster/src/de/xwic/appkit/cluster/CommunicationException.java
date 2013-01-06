/**
 * 
 */
package de.xwic.appkit.cluster;

/**
 * 
 * @author lippisch
 */
public class CommunicationException extends Exception {

	/**
	 * 
	 */
	public CommunicationException() {
	}

	/**
	 * @param message
	 */
	public CommunicationException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public CommunicationException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public CommunicationException(String message, Throwable cause) {
		super(message, cause);
	}

}
