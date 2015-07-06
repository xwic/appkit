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
package de.xwic.appkit.webbase.toolkit.app;

import de.jwic.base.IControl;
import de.jwic.base.IControlContainer;

/**
 * Helper Class for Controls used via the EditorToolKit.
 *
 * @author Ronny Pfretzschner
 *
 */
public interface IToolkitControlHelper<C extends IControl> {


	/**
	 * Creates the Control.
	 *
	 * @param container parent
	 * @param name control name
	 * @return the created Control
	 */
	public C create(IControlContainer container, String name, Object optionalParam);

	/**
	 * Tries to set the given Object value into the field value.
	 *
	 * @param obj
	 */
	public void loadContent(C control, Object obj);

	/**
	 * @return the entered value of the field value
	 */
	public Object getContent(C control);

	/**
	 * Marks the field as with given class.
	 */
	public void markField(C control, String cssClass);


	public String getFieldMarkedCssClass(C control);
}
