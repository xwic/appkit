/**
 * 
 */
package de.xwic.appkit.dev.engine;

/**
 * Thrown when the configuration is missing or erroneous.
 * @author lippisch
 */
public class ConfigurationException extends Exception {

	/**
	 * 
	 */
	public ConfigurationException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public ConfigurationException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ConfigurationException(Throwable cause) {
		super(cause);
	}

}
