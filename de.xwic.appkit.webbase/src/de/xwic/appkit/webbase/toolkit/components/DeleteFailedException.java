/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.pol.isis.web.toolkit.components.DeleteFailedException
 * Created on Jan 13, 2009 by Aron Cotrau
 *
 */
package de.xwic.appkit.webbase.toolkit.components;


/**
 * Exception raised by the {@link ListModel} when delete operation fails.
 * @author Aron Cotrau
 */
public class DeleteFailedException extends RuntimeException {

	/**
	 * 
	 */
	public DeleteFailedException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DeleteFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public DeleteFailedException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public DeleteFailedException(Throwable cause) {
		super(cause);
	}

	
}
