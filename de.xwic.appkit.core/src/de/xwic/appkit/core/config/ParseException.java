/*
 * Copyright 2005, 2006 pol GmbH
 *
 * de.xwic.appkit.core.config.ParseException
 * Created on 02.11.2006
 *
 */
package de.xwic.appkit.core.config;

/**
 * Thrown when the configuration is invalid.
 * @author Florian Lippisch
 */
public class ParseException extends Exception {

	/**
	 * 
	 */
	public ParseException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ParseException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public ParseException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ParseException(Throwable cause) {
		super(cause);
	}

}
