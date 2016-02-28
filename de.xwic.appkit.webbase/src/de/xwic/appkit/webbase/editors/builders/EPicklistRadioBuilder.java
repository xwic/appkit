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
import de.jwic.controls.Label;
import de.xwic.appkit.core.config.editor.EPicklistCombo;
import de.xwic.appkit.core.config.editor.EPicklistRadio;
import de.xwic.appkit.core.config.editor.UIElement;
import de.xwic.appkit.core.config.model.Property;
import de.xwic.appkit.webbase.editors.IBuilderContext;
import de.xwic.appkit.webbase.editors.mappers.PicklistEntryMapper;
import de.xwic.appkit.webbase.utils.picklist.PicklistEntryControl;
import de.xwic.appkit.webbase.utils.picklist.PicklistEntryRadioGroupControl;

/**
 * The Builder for the Label.
 * 
 * @author lippisch
 * @editortag plRadio
 */
public class EPicklistRadioBuilder extends Builder {

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.editors.builders.Builder#buildComponents(de.xwic.appkit.core.config.editor.UIElement,
	 *      de.jwic.base.IControlContainer,
	 *      de.xwic.appkit.webbase.editors.IBuilderContext)
	 */
	public IControl buildComponents(UIElement element, IControlContainer parent, IBuilderContext context) {

		EPicklistRadio ePl = (EPicklistRadio) element;
		if (ePl.getProperty() != null) {
			Property finalProperty = ePl.getFinalProperty();
			PicklistEntryRadioGroupControl pe = new PicklistEntryRadioGroupControl(parent, null, finalProperty.getPicklistId());

			pe.setColumns(ePl.getCols());
			context.registerField(ePl.getProperty(), pe, ePl, PicklistEntryMapper.MAPPER_ID);

			return pe;
			
		} else {

			Label label = null;
			label = new Label(parent);
			label.setText("No Property Specified");
			return label;
			
		}

	}
}
