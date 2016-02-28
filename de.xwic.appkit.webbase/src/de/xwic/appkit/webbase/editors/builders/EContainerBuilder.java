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

import java.util.Iterator;

import de.jwic.base.Control;
import de.jwic.base.ControlContainer;
import de.jwic.base.IControl;
import de.jwic.base.IControlContainer;
import de.jwic.controls.layout.TableData;
import de.jwic.controls.layout.TableLayoutContainer;
import de.xwic.appkit.core.config.editor.EComposite;
import de.xwic.appkit.core.config.editor.Style;
import de.xwic.appkit.core.config.editor.UIElement;
import de.xwic.appkit.webbase.editors.IBuilderContext;

/**
 * Defines the Container builder.
 * 
 * @author Aron Cotrau
 */
public class EContainerBuilder<T extends EComposite> extends Builder<T> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.editors.builders.Builder#buildComponents(de.xwic.appkit.core.config.editor.UIElement,
	 *      de.jwic.base.IControlContainer,
	 *      de.xwic.appkit.webbase.editors.IBuilderContext)
	 */
	@Override
	public IControl buildComponents(T composite, IControlContainer parent, IBuilderContext context) {
		ControlContainer control = null;
		
		TableLayoutContainer table = null;
		if (composite.getCols() > 1) {
			table = new TableLayoutContainer(parent); 
			control = table;
			
			Style style = composite.getStyle();
			
			table.setColumnCount(composite.getCols());
			table.setCellPadding(style.getStyleInt("padding"));
			table.setCellSpacing(0);
			table.setWidth("100%");
			
			if (composite.getColWidth() != null && !composite.getColWidth().isEmpty()) {
				String[] widthHint = composite.getColWidth().split(";");
				for (int idx = 0; idx < widthHint.length && idx < table.getColumnCount(); idx++) {
					table.setColWidth(idx, Integer.parseInt(widthHint[idx]));
				}
			}
			
		} else {
			control = new ControlContainer(parent);
		}

		context.registerWidget(control, composite);
		buildChilds(composite, control, context, table);

		return control;
	}

	/**
	 * @param composite
	 * @param parent
	 * @param table 
	 * @param widthHint 
	 * @param table 
	 */
	private void buildChilds(EComposite composite, ControlContainer parent, IBuilderContext context, TableLayoutContainer table) {
		for (UIElement element : composite.getChilds()) {
			Builder builder = BuilderRegistry.getBuilder(element);
			if (null != builder) {
				IControl widget = builder.buildComponents(element, parent, context);
				if (table != null) {
					
					if (widget.getContainer() == table) {
						TableData layoutData = new TableData();
						layoutData.setVAlign(TableData.ALIGN_TOP);
						table.setLayoutData((Control)widget, layoutData);
					}
				}
			} else {
				log.warn("No Builder registered for element " + composite.getClass().getName());
			}
		}
	}
}
