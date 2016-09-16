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
import de.xwic.appkit.core.config.editor.EPicklistCheckbox;
import de.xwic.appkit.core.config.model.Property;
import de.xwic.appkit.webbase.editors.FieldChangeListener;
import de.xwic.appkit.webbase.editors.IBuilderContext;
import de.xwic.appkit.webbase.editors.mappers.PicklistEntrySetMapper;
import de.xwic.appkit.webbase.utils.picklist.PicklistEntryCheckboxControl;

/**
 * The Builder for a multi-value PicklistCheckboxControl.
 * 
 * @author lippisch
 */
public class EPicklistCheckboxBuilder extends Builder<EPicklistCheckbox> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.editors.builders.Builder#buildComponents(de.xwic.appkit.core.config.editor.UIElement,
	 *      de.jwic.base.IControlContainer,
	 *      de.xwic.appkit.webbase.editors.IBuilderContext)
	 */
	public IControl buildComponents(EPicklistCheckbox ePl, IControlContainer parent, IBuilderContext context) {

		if (ePl.getProperty() != null) {
			Property finalProperty = ePl.getFinalProperty();
			PicklistEntryCheckboxControl pe = new PicklistEntryCheckboxControl(parent, null, finalProperty.getPicklistId());
			pe.setColumns(ePl.getCols());
			pe.addElementSelectedListener(new FieldChangeListener(context, ePl.getProperty()));
			context.registerField(ePl.getProperty(), pe, ePl, PicklistEntrySetMapper.MAPPER_ID);

			return pe;
			
		} else {

			Label label = null;
			label = new Label(parent);
			label.setText("No Property Specified");
			return label;
			
		}

	}
}
