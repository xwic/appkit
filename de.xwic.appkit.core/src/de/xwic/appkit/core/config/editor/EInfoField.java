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
 * A special label which is dependant to entity fields, to display infos about
 * the selected entity in the field.
 * 
 * @author Aron Cotrau
 * @editortag info
 */
public class EInfoField extends EField {

	/** if the field depends on any other field, this is the attribute to be set */
	private String depends = null;

	/**
	 * @return the depends
	 */
	public String getDepends() {
		return depends;
	}

	/**
	 * if the field depends on any other field. set the id of the widget as
	 * defined in the editor configuration.
	 * 
	 * @param depends
	 *            the depends to set
	 */
	public void setDepends(String depends) {
		this.depends = depends;
	}
}
