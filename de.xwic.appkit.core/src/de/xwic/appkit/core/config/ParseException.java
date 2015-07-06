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
