/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.webbase.editors.builders.EContainerBuilder
 * Created on May 14, 2007 by Aron Cotrau
 *
 */
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
