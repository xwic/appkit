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
import de.jwic.controls.ckeditor.CKEditor;
import de.xwic.appkit.core.config.editor.EHtmlEditor;
import de.xwic.appkit.core.config.editor.EText;
import de.xwic.appkit.core.config.editor.Style;
import de.xwic.appkit.core.config.model.Property;
import de.xwic.appkit.webbase.editors.FieldChangeListener;
import de.xwic.appkit.webbase.editors.IBuilderContext;
import de.xwic.appkit.webbase.editors.mappers.HtmlEditorMapper;
import de.xwic.appkit.webbase.editors.mappers.InputboxMapper;

/**
 * Defines the CKEditor builder class.
 * 
 * @author Aron Cotrau
 */
public class EHtmlEditorBuilder extends Builder<EHtmlEditor> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.editors.builders.Builder#buildComponents(de.xwic.appkit.core.config.editor.UIElement,
	 *      de.jwic.base.IControlContainer,
	 *      de.xwic.appkit.webbase.editors.IBuilderContext)
	 */
	public IControl buildComponents(EHtmlEditor text, IControlContainer parent, IBuilderContext context) {

		CKEditor ckEditor = new CKEditor(parent, null);
		ckEditor.addValueChangedListener(new FieldChangeListener(context, text.getProperty()));
		Style style = text.getStyle();
		if (style.getStyleInt(Style.WIDTH_HINT) != 0) {
			ckEditor.setWidth(style.getStyleInt(Style.WIDTH_HINT));
		}

		if (style.getStyleInt(Style.HEIGHT_HINT) != 0) {
			ckEditor.setHeight(style.getStyleInt(Style.HEIGHT_HINT));
		}

		context.registerField(text.getProperty(), ckEditor, text, HtmlEditorMapper.MAPPER_ID);
		
		return ckEditor;
	}
}
