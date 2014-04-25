/**
 *
 */
package de.xwic.appkit.core.remote.server;

import de.xwic.appkit.core.transport.xml.TransportException;

/**
 * @author Alexandru Bledea
 * @since Jan 15, 2014
 */
public class InvalidRequestException extends TransportException {

	/**
	 *
	 */
	InvalidRequestException() {
		this(null);
	}

	/**
	 * @param cause
	 */
	InvalidRequestException(final Throwable cause) {
		this(null, cause);
	}

	/**
	 * @param cause
	 */
	private InvalidRequestException(final String message, final Throwable cause) {
		super("Invalid Request!", cause);
	}
}
