/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.config.ConfigurationException
 * Created on 07.11.2006 by Florian Lippisch
 *
 */
package de.xwic.appkit.core.config;

/**
 * @author Florian Lippisch
 *
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
