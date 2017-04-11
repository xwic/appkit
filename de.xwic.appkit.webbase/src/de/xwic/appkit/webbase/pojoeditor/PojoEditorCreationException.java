/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 * 
 * de.xwic.appkit.webbase.pojoeditor.EditorCreatorException 
 */

package de.xwic.appkit.webbase.pojoeditor;

/**
 * @author Andrei Pat
 *
 */
public class PojoEditorCreationException extends Exception {

	public PojoEditorCreationException() {
		super();
	}

	public PojoEditorCreationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public PojoEditorCreationException(String message, Throwable cause) {
		super(message, cause);
	}

	public PojoEditorCreationException(String message) {
		super(message);
	}

	public PojoEditorCreationException(Throwable cause) {
		super(cause);
	}

}
