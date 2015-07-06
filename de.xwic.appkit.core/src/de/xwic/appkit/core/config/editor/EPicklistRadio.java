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
 * Defines a control used for displaying the picklist entries in radio buttons format. 
 * 
 * @author Andra Iacovici
 * @editortag plRadio
 */
public class EPicklistRadio extends ENoBackgroundField {
	private int cols = 1;

	/**
	 * @return Returns the cols.
	 */
	public int getCols() {
		return cols;
	}

	/**
	 * Sets the number of columns
	 * @param cols
	 *            The cols to set.
	 */
	public void setCols(int cols) {
		this.cols = cols;
	}
}
