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
