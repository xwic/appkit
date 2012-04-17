/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.webbase.editors.builders.Builder
 * Created on May 14, 2007 by Aron Cotrau
 *
 */
package de.xwic.appkit.webbase.editors.builders;

import de.jwic.base.IControl;
import de.jwic.base.IControlContainer;
import de.xwic.appkit.core.config.editor.UIElement;
import de.xwic.appkit.webbase.editors.IBuilderContext;

/**
 * Used to create ui components (widgets) based on ui descriptor elements.
 * 
 * @author Florian Lippisch
 */
public abstract class Builder {

	/**
	 * Create the components.
	 * 
	 * @param element
	 * @param parent
 	 * @param context
	 */
	public abstract IControl buildComponents(UIElement element, IControlContainer parent, IBuilderContext context);
}
