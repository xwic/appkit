/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.webbase.editors.builders.ELabelBuilder
 * Created on Jun 11, 2007 by Aron Cotrau
 *
 */
package de.xwic.appkit.webbase.editors.builders;

import de.jwic.base.IControl;
import de.jwic.base.IControlContainer;
import de.jwic.controls.Label;
import de.xwic.appkit.core.config.editor.ELabel;
import de.xwic.appkit.core.config.editor.UIElement;
import de.xwic.appkit.webbase.editors.IBuilderContext;

/**
 * The Builder for the Label.
 * 
 * @author Aron Cotrau
 */
public class ELabelBuilder extends Builder {

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.editors.builders.Builder#buildComponents(de.xwic.appkit.core.config.editor.UIElement,
	 *      de.jwic.base.IControlContainer,
	 *      de.xwic.appkit.webbase.editors.IBuilderContext)
	 */
	public IControl buildComponents(UIElement element, IControlContainer parent, IBuilderContext context) {
		ELabel eLabel = (ELabel) element;
		String text = "unnamed";
		if (eLabel.getTitle() != null) {
			text = context.getResString(eLabel.getTitle());
		} else if (eLabel.getProperty() != null) {
			text = context.getResString(eLabel.getFinalProperty().getName());
		}

		Label label = null;
		label = new Label(parent);
		label.setText(text);

		return label;
	}
}
