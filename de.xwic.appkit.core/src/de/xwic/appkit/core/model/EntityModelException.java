/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.model.EntityModelException
 * Created on 10.08.2005
 *
 */
package de.xwic.appkit.core.model;

/**
 * This exception is thrown during the creation or invocation of an EntityModel. 
 * @author Florian Lippisch
 */
public class EntityModelException extends Exception {

	/**
	 * 
	 */
	public EntityModelException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public EntityModelException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public EntityModelException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public EntityModelException(Throwable cause) {
		super(cause);
	}

}
