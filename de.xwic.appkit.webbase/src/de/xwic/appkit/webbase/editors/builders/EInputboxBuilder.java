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

import de.jwic.base.IControl;
import de.jwic.base.IControlContainer;
import de.jwic.controls.InputBox;
import de.xwic.appkit.core.config.editor.EText;
import de.xwic.appkit.core.config.editor.Style;
import de.xwic.appkit.core.config.editor.UIElement;
import de.xwic.appkit.core.config.model.Property;
import de.xwic.appkit.webbase.editors.IBuilderContext;
import de.xwic.appkit.webbase.editors.mappers.InputboxMapper;

/**
 * Defines the InputBox builder class.
 * 
 * @author Aron Cotrau
 */
public class EInputboxBuilder extends Builder {

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.editors.builders.Builder#buildComponents(de.xwic.appkit.core.config.editor.UIElement,
	 *      de.jwic.base.IControlContainer,
	 *      de.xwic.appkit.webbase.editors.IBuilderContext)
	 */
	public IControl buildComponents(UIElement element, IControlContainer parent, IBuilderContext context) {
		EText text = (EText) element;
		InputBox inputBox = new InputBox(parent);

		inputBox.setReadonly(text.isReadonly());
		inputBox.setMultiLine(text.isMultiline());
		if (text.isMultiline()) {
			inputBox.setHeight(100);
		}
		Property prop = text.getFinalProperty();
		if (prop.getMaxLength() > 0) {
			inputBox.setMaxLength(prop.getMaxLength());
		}
		if (prop.getRequired()) {
			inputBox.setEmptyInfoText("Required Field");
		}
		
		Style style = text.getStyle();
		if (style.getStyleInt(Style.WIDTH_HINT) != 0) {
			inputBox.setWidth(style.getStyleInt(Style.WIDTH_HINT) );
		}

		if (style.getStyleInt(Style.HEIGHT_HINT) != 0) {
			inputBox.setHeight(style.getStyleInt(Style.HEIGHT_HINT) );
		}

		context.registerField(text.getProperty(), inputBox, text, InputboxMapper.MAPPER_ID);
		
		return inputBox;
	}
}
