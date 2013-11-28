/**
 * 
 */
package de.xwic.appkit.core.remote.client;

import de.xwic.appkit.core.dao.DataAccessException;

/**
 * @author lippisch
 *
 */
public class RemoteDataAccessException extends DataAccessException {

	/**
	 * 
	 */
	public RemoteDataAccessException() {
	}

	/**
	 * @param message
	 */
	public RemoteDataAccessException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public RemoteDataAccessException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public RemoteDataAccessException(Throwable cause) {
		super(cause);
	}

}
