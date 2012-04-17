/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.client.uitools.editors.ValidationException
 * Created on 10.11.2006 by Florian Lippisch
 *
 */
package de.xwic.appkit.webbase.editors;

import de.xwic.appkit.core.dao.ValidationResult;

/**
 * @author Florian Lippisch
 *
 */
public class ValidationException extends Exception {

	private ValidationResult result = null;

	/**
	 * @param result
	 */
	public ValidationException(ValidationResult result) {
		super();
		this.result = result;
	}

	/**
	 * 
	 */
	public ValidationException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public ValidationException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ValidationException(Throwable cause) {
		super(cause);
	}

	/**
	 * @return the result
	 */
	public ValidationResult getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(ValidationResult result) {
		this.result = result;
	}
}
