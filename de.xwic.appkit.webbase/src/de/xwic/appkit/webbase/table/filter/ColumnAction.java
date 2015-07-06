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

package de.xwic.appkit.webbase.table.filter;

import de.jwic.base.IControlContainer;
import de.jwic.base.ImageRef;
import de.jwic.controls.SelectableControl;

/**
 * Used as an action on a column.
 * @author lippisch
 */
public class ColumnAction extends SelectableControl {

	private String title = null;
	private ImageRef image = null;

	/**
	 * @param container
	 * @param name
	 */
	public ColumnAction(IControlContainer container, String name) {
		super(container, name);
	}

	/**
	 * Click action.
	 */
	public void actionClick() {
		click();
	}
	
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the image
	 */
	public ImageRef getImage() {
		return image;
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(ImageRef image) {
		this.image = image;
	}
	
	

}
