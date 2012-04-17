/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.webbase.editors.builders.EInputboxBuilder
 * Created on May 14, 2007 by Aron Cotrau
 *
 */
package de.xwic.appkit.webbase.editors.builders;

import de.jwic.base.IControl;
import de.jwic.base.IControlContainer;
import de.jwic.controls.InputBoxControl;
import de.xwic.appkit.core.config.editor.EText;
import de.xwic.appkit.core.config.editor.UIElement;
import de.xwic.appkit.core.config.model.Property;
import de.xwic.appkit.webbase.editors.IBuilderContext;

/**
 * Defines the InputBoxControl builder class.
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
		InputBoxControl inputBox = new InputBoxControl(parent);

		inputBox.setReadonly(text.isReadonly());
		inputBox.setMultiLine(text.isMultiline());
		Property prop = text.getFinalProperty();
		if (prop.getMaxLength() > 0) {
			inputBox.setMaxLength(prop.getMaxLength());
		}

		return inputBox;
	}
}
