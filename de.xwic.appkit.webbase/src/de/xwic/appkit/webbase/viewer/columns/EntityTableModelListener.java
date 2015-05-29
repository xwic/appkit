/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.webbase.viewer.columns.EntityTableModelListener
 * Created on Mar 21, 2007 by Aron Cotrau
 *
 */
package de.xwic.appkit.webbase.viewer.columns;

import de.jwic.controls.tableviewer.TableColumn;
import de.jwic.controls.tableviewer.TableModel;
import de.jwic.controls.tableviewer.TableModelAdapter;
import de.jwic.controls.tableviewer.TableModelEvent;
import de.xwic.appkit.webbase.utils.UserProfileWrapper;
import de.xwic.appkit.webbase.viewer.IEnhancedTableViewer;

/**
 * This class handles the event when a column is selected. 
 * @author Aron Cotrau
 */
public class EntityTableModelListener extends TableModelAdapter {

	private IEnhancedTableViewer enhancedTable = null;
	
	/**
	 * c-tor
	 * @param entityTable
	 * @param contentProvider
	 */
	public EntityTableModelListener(IEnhancedTableViewer entityTable) {
		this.enhancedTable = entityTable;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.jwic.ecolib.tableviewer.TableModelAdapter#columnSelected(de.jwic.ecolib.tableviewer.TableModelEvent)
	 */
	public void columnSelected(TableModelEvent event) {
		handleSorting(event.getTableColumn());
	}

	/**
	 * Change the sort icon.
	 * 
	 * @param tableColumn
	 */
	public void handleSorting(TableColumn tableColumn) {
		enhancedTable.handleSorting(tableColumn);
	}
	
	@Override
	public void rangeUpdated(TableModelEvent event) {
		TableModel tableModel = (TableModel)event.getEventSource();
		UserProfileWrapper userListProfile = enhancedTable.getUserListProfile();
		
		if (null != userListProfile) {
			userListProfile.setMaxRows(tableModel.getMaxLines());
		}
	}
}
