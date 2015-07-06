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
/**
 * 
 */
package de.xwic.appkit.webbase.dialog;

import de.jwic.controls.Window;
import de.xwic.appkit.webbase.toolkit.app.Site;

/**
 * @author Adrian Ionescu
 */
public abstract class AbstractPopUpDialogWindow extends AbstractDialogWindow {

	/**
	 * @param site
	 */
	public AbstractPopUpDialogWindow(Site site) {
		super(site);
		
		setTemplateName(Window.class.getName());
		setCssClass("j-combo-content");
		setModal(false);
	}

}
