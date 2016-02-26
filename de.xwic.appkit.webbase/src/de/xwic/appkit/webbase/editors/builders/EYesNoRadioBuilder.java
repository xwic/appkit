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
import de.jwic.controls.Label;
import de.jwic.controls.RadioGroup;
import de.jwic.events.ElementSelectedEvent;
import de.jwic.events.ElementSelectedListener;
import de.xwic.appkit.core.config.editor.EText;
import de.xwic.appkit.core.config.editor.EYesNoRadio;
import de.xwic.appkit.core.config.editor.Style;
import de.xwic.appkit.core.config.editor.UIElement;
import de.xwic.appkit.core.config.model.Property;
import de.xwic.appkit.webbase.editors.IBuilderContext;
import org.apache.poi.util.StringUtil;

/**
 * Defines the InputBox builder class.
 * 
 * @author Aron Cotrau
 */
public class EYesNoRadioBuilder extends Builder<EYesNoRadio> {

	public static final String KEY_YES = "1";
    public static final String KEY_NO = "0";


	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.editors.builders.Builder#buildComponents(de.xwic.appkit.core.config.editor.UIElement,
	 *      de.jwic.base.IControlContainer,
	 *      de.xwic.appkit.webbase.editors.IBuilderContext)
	 */
	public IControl buildComponents(EYesNoRadio radio, IControlContainer parent, IBuilderContext context) {
		if(radio.getProperty() != null) {
			final RadioGroup rg = new RadioGroup(parent, null);
			rg.addElement("Yes", KEY_YES);
			rg.addElement("No", KEY_NO);
			context.registerField(radio.getProperty(), rg, radio.getId());
			return rg;
		} else {
			Label label = new Label(parent);
			label.setText("No Property Specified");
			return label;
		}

	}
}
