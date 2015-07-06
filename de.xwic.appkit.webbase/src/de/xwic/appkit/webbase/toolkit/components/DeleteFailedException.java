/*******************************************************************************
 * Copyright 2015 xWic group (http://www.xwic.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *  
 *******************************************************************************/
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
