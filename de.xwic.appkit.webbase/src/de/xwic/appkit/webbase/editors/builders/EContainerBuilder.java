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
package de.xwic.appkit.webbase.editors.builders;

import java.util.Iterator;

import de.jwic.base.ControlContainer;
import de.jwic.base.IControl;
import de.jwic.base.IControlContainer;
import de.jwic.controls.layout.TableLayoutContainer;
import de.xwic.appkit.core.config.editor.EComposite;
import de.xwic.appkit.core.config.editor.UIElement;
import de.xwic.appkit.webbase.editors.IBuilderContext;

/**
 * Defines the Container builder.
 * 
 * @author Aron Cotrau
 */
public class EContainerBuilder extends Builder {

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.editors.builders.Builder#buildComponents(de.xwic.appkit.core.config.editor.UIElement,
	 *      de.jwic.base.IControlContainer,
	 *      de.xwic.appkit.webbase.editors.IBuilderContext)
	 */
	public IControl buildComponents(UIElement element, IControlContainer parent, IBuilderContext context) {
		EComposite composite = (EComposite) element;
		ControlContainer control = null;

		if (composite.getCols() > 1) {
			control = new TableLayoutContainer(parent);
			((TableLayoutContainer) control).setColumnCount(composite.getCols());
		} else {
			control = new ControlContainer(parent);
		}
		buildChilds(composite, control, context);

		return control;
	}

	/**
	 * @param composite
	 * @param parent
	 */
	private void buildChilds(EComposite composite, ControlContainer parent, IBuilderContext context) {
		for (Iterator it = composite.getChilds().iterator(); it.hasNext();) {
			UIElement element = (UIElement) it.next();
			Builder builder = BuilderRegistry.getBuilder(element);
			if (null != builder) {
				builder.buildComponents(element, parent, context);
			}
		}
	}
}
