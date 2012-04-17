/*
 * Copyright (c) 2009 Network Appliance, Inc.
 * All rights reserved.
 */

package de.xwic.appkit.webbase.table.columns;

import de.jwic.base.Dimension;
import de.jwic.base.Page;
import de.jwic.base.UserAgentInfo;
import de.jwic.ecolib.controls.coledit.ColumnSelector;
import de.jwic.ecolib.controls.coledit.ColumnStub;
import de.xwic.appkit.core.config.Bundle;
import de.xwic.appkit.core.config.model.Property;
import de.xwic.appkit.webbase.dialog.AbstractDialogWindow;
import de.xwic.appkit.webbase.dialog.DialogContent;
import de.xwic.appkit.webbase.table.Column;
import de.xwic.appkit.webbase.table.EntityTableModel;
import de.xwic.appkit.webbase.toolkit.app.Site;

/**
 * Dialog for the Column Selector.
 * @author lippisch
 */
public class ColumnSelectorDialog extends AbstractDialogWindow {

	private final EntityTableModel model;
	private ColumnSelector selector;

	/**
	 * @param site
	 */
	public ColumnSelectorDialog(Site site, EntityTableModel model) {
		super(site);
		this.model = model;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.dialog.AbstractDialogWindow#createContent(de.xwic.appkit.webbase.dialog.DialogContent)
	 */
	@Override
	protected void createContent(DialogContent content) {
		
		Page page = Page.findPage(this);
		Dimension pageSize = page.getPageSize();

		setWidth(600);
		setHeight(pageSize.height - 120);
		
		setTitle("Change Column Configuration");

		btCancel.setVisible(true);
		
		ColumnSelectorContent csContent = new ColumnSelectorContent(content, "csContent");
		selector = new ColumnSelector(csContent, "selector");
		
		selector.setWidth(getWidth() - 22);
		selector.setHeight(content.getHeight() - 70);
		
		Bundle bundle = model.getBundle();
		
		// add all columns.
		for (Column column : model.getColumns()) {
			ColumnStub stub = new ColumnStub(column.isVisible(), column.getTitle());
			stub.setUserObject(column);
			
			// build description
			StringBuilder sbDesc = new StringBuilder();
			if (column.getListColumn().getProperty() != null) {
				for (Property p : column.getListColumn().getProperty()) {
					String name = bundle.getString(p.getEntityDescriptor().getClassname());
					if (!name.startsWith("!")) {
						if (sbDesc.length() != 0) {
							sbDesc.append(" : ");
						}
						sbDesc.append(name);
					}
				}
			} else {
				sbDesc.append("- Custom Column -");
			}
			stub.setDescription(sbDesc.toString());
			
			selector.addColumn(stub);
		}
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.dialog.AbstractDialogWindow#onOk()
	 */
	@Override
	protected void onOk() {
		
		for (ColumnStub stub : selector.getColumns()) {
			Column column = (Column) stub.getUserObject();
			column.setVisible(stub.isVisible());
			column.setColumnOrder(stub.getSortIndex());
		}

		model.applyColumnReorder();
		
		super.onOk(); // close it
	}

}
