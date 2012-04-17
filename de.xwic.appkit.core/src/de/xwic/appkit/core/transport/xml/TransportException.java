/**
 * 
 */
package de.xwic.appkit.core.transport.xml;

/**
 * @author Florian Lippisch
 *
 */
public class TransportException extends Exception {

	private static final long serialVersionUID = -133790806810705478L;

	/**
	 * 
	 */
	public TransportException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public TransportException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public TransportException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public TransportException(Throwable cause) {
		super(cause);
	}

	
	
}
