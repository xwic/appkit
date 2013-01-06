/**
 * 
 */
package de.xwic.appkit.cluster;

/**
 * @author lippisch
 *
 */
public class NodeUnavailableException extends Exception {

	/**
	 * 
	 */
	public NodeUnavailableException() {
		super();
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public NodeUnavailableException(String message, Throwable t) {
		super(message, t);
	}

	/**
	 * @param arg0
	 */
	public NodeUnavailableException(String message) {
		super(message);
	}

	/**
	 * @param arg0
	 */
	public NodeUnavailableException(Throwable t) {
		super(t);
	}

}
