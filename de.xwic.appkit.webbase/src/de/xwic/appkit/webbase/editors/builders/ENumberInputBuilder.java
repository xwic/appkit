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
import de.jwic.controls.NumericInputBox;
import de.xwic.appkit.core.config.editor.ENumberInputField;
import de.xwic.appkit.core.config.editor.Style;
import de.xwic.appkit.core.config.model.Property;
import de.xwic.appkit.webbase.editors.FieldChangeListener;
import de.xwic.appkit.webbase.editors.IBuilderContext;
import de.xwic.appkit.webbase.editors.mappers.NumericInputboxMapper;
import org.apache.commons.lang.StringUtils;

/**
 * Defines the NumericInputBox builder class.
 * 
 * @author <a href="mailto:vzhovtiuk@gmail.com">Vitaliy Zhovtyuk</a>
 */
public class ENumberInputBuilder extends Builder<ENumberInputField> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.editors.builders.Builder#buildComponents(de.xwic.appkit.core.config.editor.UIElement,
	 *      de.jwic.base.IControlContainer,
	 *      de.xwic.appkit.webbase.editors.IBuilderContext)
	 */
	@Override
	public IControl buildComponents(ENumberInputField text, IControlContainer parent, IBuilderContext context) {

		NumericInputBox numericInputBox = new NumericInputBox(parent);

		numericInputBox.addValueChangedListener(new FieldChangeListener(context, text.getProperty()));
		numericInputBox.setReadonly(text.isReadonly());
		Property prop = text.getFinalProperty();
		if (prop.getMaxLength() > 0) {
			numericInputBox.setMaxLength(prop.getMaxLength());
		}
		if (prop.getRequired()) {
			numericInputBox.setEmptyInfoText("Required Field");
		}

		if ("double".equals(text.getFormat())) {
			numericInputBox.setDecimalPlaces(2);
		} else if("long".equals(text.getFormat())) {
			numericInputBox.setDecimalPlaces(0);
		}
		if(text.getDecimalPoints() != null) {
			numericInputBox.setDecimalPlaces(text.getDecimalPoints());
		}
		if(StringUtils.isNotEmpty(text.getCurrencySymbol())) {
			numericInputBox.setSymbol(text.getCurrencySymbol());
			if(StringUtils.isNotEmpty(text.getCurrencySymbolPlacement())) {
				numericInputBox.setSymbolPlacement(NumericInputBox.SymbolPlacement.valueOf(text.getCurrencySymbolPlacement()));
			}
		}
		Style style = text.getStyle();
		if (style.getStyleInt(Style.WIDTH_HINT) != 0) {
			numericInputBox.setWidth(style.getStyleInt(Style.WIDTH_HINT));
		}

		if (style.getStyleInt(Style.HEIGHT_HINT) != 0) {
			numericInputBox.setHeight(style.getStyleInt(Style.HEIGHT_HINT));
		}

		context.registerField(text.getProperty(), numericInputBox, text, NumericInputboxMapper.MAPPER_ID, text.isReadonly());
		
		return numericInputBox;
	}
}
