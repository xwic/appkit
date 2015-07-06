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
/**
 * 
 */
package de.xwic.appkit.webbase.entityviewer;

import de.jwic.base.IControlContainer;
import de.jwic.base.JavaScriptSupport;
import de.jwic.controls.Button;
import de.jwic.events.SelectionEvent;
import de.jwic.events.SelectionListener;
import de.xwic.appkit.webbase.table.Column;
import de.xwic.appkit.webbase.table.EntityTableAdapter;
import de.xwic.appkit.webbase.table.EntityTableEvent;
import de.xwic.appkit.webbase.table.EntityTableModel;
import de.xwic.appkit.webbase.toolkit.util.ImageLibrary;

/**
 * @author Adrian Ionescu
 */
@JavaScriptSupport
public class ClearFiltersButton extends Button {

	private final EntityTableModel model;

	/**
	 * @param container
	 * @param name
	 */
	public ClearFiltersButton(IControlContainer container, String name, EntityTableModel model) {
		super(container, name);
		
		this.model = model;
		
		setTemplateName(Button.class.getName());
		
		setTitle("");
		setIconEnabled(ImageLibrary.ICON_TBL_FILTER_CLEAR);
		
		model.addEntityTableListener(new EntityTableAdapter() {
			@Override
			public void columnFiltered(EntityTableEvent event) {
				updateFilterInfo();
			}
			@Override
			public void columnSorted(EntityTableEvent event) {
				updateFilterInfo();
			}
		});
		
		addSelectionListener(new SelectionListener() {			
			@Override
			public void objectSelected(SelectionEvent event) {
				ClearFiltersButton.this.model.clearFilters();
			}
		});
		
		updateFilterInfo();
	}

	/**
	 * Update the filter info.
	 */
	private void updateFilterInfo() {
		StringBuilder sb = new StringBuilder();

		for (Column col : model.getColumns()) {
			if (col.getFilter() != null) {
				if (sb.length() > 0) {
					sb.append(" ; ");
				}
				sb.append("'").append(col.getTitle()).append("'");
			}
		}
		
		setTooltip(sb.length() == 0 ? "There are no filters" : "Clear current filters: " + sb.toString());
		requireRedraw();
	}
	
}
