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
package de.xwic.appkit.core.config.editor;

/**
 * Used to handle conditional elements. This version handles
 * hasAccess and hasNoAccess on a specified scope. The condition is
 * true, if the user has the action ACCESS on the specified scope
 * or not, if hasNoAccess is used.
 * 
 * @author Florian Lippisch
 * @editortag if
 */
public class EIf extends EComposite {

	private String hasAccess = null;
	private String hasNoAccess = null;
	/**
	 * @return the hasAccess
	 */
	public String getHasAccess() {
		return hasAccess;
	}
	/**
	 * @param hasAccess the hasAccess to set
	 */
	public void setHasAccess(String hasAccess) {
		this.hasAccess = hasAccess;
	}
	/**
	 * @return the hasNoAccess
	 */
	public String getHasNoAccess() {
		return hasNoAccess;
	}
	/**
	 * @param hasNoAccess the hasNoAccess to set
	 */
	public void setHasNoAccess(String hasNoAccess) {
		this.hasNoAccess = hasNoAccess;
	}
	
}
