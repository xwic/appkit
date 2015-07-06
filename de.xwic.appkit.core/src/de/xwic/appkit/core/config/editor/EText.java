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
 * Creates a text control to input plain text. The control can be created as 
 * multiline field.
 * 
 * @editortag text
 * @author Florian Lippisch
 */
public class EText extends ERegExField {

	private boolean multiline = false;
	
	/**
	 * @return the multiline
	 */
	public boolean isMultiline() {
		return multiline;
	}

	/**
	 * @param multiline the multiline to set
	 * @default false
	 */
	public void setMultiline(boolean multiline) {
		this.multiline = multiline;
	}
}
