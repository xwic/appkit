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
 * Defines a tab in the bottom section of an editor. SubTab's cannot contain
 * regular editor elements, but things like list-views and others. SubTabs are 
 * hidden if the entity is new and not saved yet.
 * 
 * @author Florian Lippisch
 * @editortag subTab
 */
public class ESubTab extends EComposite {

	private String title = null;

	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * the title of the tab
	 * @param title The title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
}
