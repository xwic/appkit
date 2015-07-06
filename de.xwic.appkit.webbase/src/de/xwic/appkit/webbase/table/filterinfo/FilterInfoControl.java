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

package de.xwic.appkit.webbase.table.filterinfo;

import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.controls.Label;
import de.xwic.appkit.core.model.queries.QueryElement;
import de.xwic.appkit.webbase.table.Column;
import de.xwic.appkit.webbase.table.EntityTableAdapter;
import de.xwic.appkit.webbase.table.EntityTableEvent;
import de.xwic.appkit.webbase.table.EntityTableModel;
import de.xwic.appkit.webbase.toolkit.util.ImageLibrary;

/**
 * Optional control to be linked with an EntityTable. Displays a summary of the filter settings and 
 * allows to invoke the column selection.
 * @author lippisch
 */
public class FilterInfoControl extends ControlContainer {

	private Label lblFilterInfo;
	private final EntityTableModel model;

	/**
	 * @param container
	 * @param name
	 */
	public FilterInfoControl(IControlContainer container, String name, EntityTableModel model) {
		super(container, name);
		this.model = model;
		
		lblFilterInfo = new Label(this, "lblInfo");
		
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
		
		updateFilterInfo();
		
	}

	/**
	 * Update the filter info.
	 */
	protected void updateFilterInfo() {
		
		// Bundle bundle = model.getBundle();		
		// String entity = bundle.getString(model.getEntityClass().getName());
		
		StringBuilder sb = new StringBuilder();
		StringBuilder sbWhere = new StringBuilder();

		for (Column col : model.getColumns()) {
			if (col.getFilter() != null) {
				QueryElement filter = col.getFilter();
				if (sbWhere.length() != 0) {
					if (filter.getLinkType() == QueryElement.AND) {
						sbWhere.append(" and ");
					} else {
						sbWhere.append(" or ");
					}
				}
				sbWhere.append("<b>").append(col.getTitle()).append("</b> ");
				sbWhere.append(filter.getOperation()).append(" ").append(filter.getValue());
			}
		}
		
		sb.append(ImageLibrary.ICON_TBL_FILTER.toImgTag());
		
		if (sbWhere.length() == 0) {
			sb.append(" All ");
		}
//		sb.append("<b>")
//		  .append(entity)
//		  .append("</b>");
//		if (!entity.endsWith("s")) {
//			sb.append("s");
//		}
		
		if (sbWhere.length() != 0) {
//			sb.append(" filtered by ");
			sb.append(sbWhere);
		}
		
		lblFilterInfo.setText(sb.toString());
	}


}
