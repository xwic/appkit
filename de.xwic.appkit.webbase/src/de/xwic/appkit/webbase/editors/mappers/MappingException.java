/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.client.uitools.editors.mapper.MappingException
 * Created on 10.11.2006 by Florian Lippisch
 *
 */
package de.xwic.appkit.webbase.editors.mappers;

/**
 * @author Florian Lippisch
 *
 */
public class MappingException extends Exception {

	/**
	 * 
	 */
	public MappingException() {
	}

	/**
	 * @param message
	 */
	public MappingException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public MappingException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public MappingException(String message, Throwable cause) {
		super(message, cause);
	}

}
