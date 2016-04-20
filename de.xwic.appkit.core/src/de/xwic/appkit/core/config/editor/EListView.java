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
 * A listview element within an editor.
 *
 * @author Vitaliy Zhovtyuk
 * @editortag listview
 */
public class EListView extends EComposite {

	private String type = null;
	private String filterOn = null;
	private String listProfile = null;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFilterOn() {
		return filterOn;
	}

	public void setFilterOn(String filterOn) {
		this.filterOn = filterOn;
	}

	public String getListProfile() {
		return listProfile;
	}

	public void setListProfile(String listProfile) {
		this.listProfile = listProfile;
	}
}
