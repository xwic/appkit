/*
 * Copyright 2015 xWic group (http://www.xwic.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
**/
package de.xwic.appkit.webbase.editors.builders;

import de.jwic.base.IControl;
import de.jwic.base.IControlContainer;
import de.jwic.controls.GroupControl;
import de.xwic.appkit.core.config.editor.EGroup;
import de.xwic.appkit.core.config.editor.UIElement;
import de.xwic.appkit.webbase.editors.IBuilderContext;

/**
 * Generates a group as per the EGroup settings.
 * @author Lippisch
 */
public class EGroupBuilder extends EContainerBuilder {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.editors.builders.EContainerBuilder#buildComponents(de.xwic.appkit.core.config.editor.UIElement, de.jwic.base.IControlContainer, de.xwic.appkit.webbase.editors.IBuilderContext)
	 */
	@Override
	public IControl buildComponents(UIElement element, IControlContainer parent, IBuilderContext context) {
		
		EGroup group = (EGroup)element;
		GroupControl gc = new GroupControl(parent);
		gc.setTitle(group.getTitle());
		
		super.buildComponents(element, gc, context);
		
		return gc;
	}
	
}
