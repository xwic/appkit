/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.webbase.viewer.columns.EntityTableModelListener
 * Created on Mar 21, 2007 by Aron Cotrau
 *
 */
package de.xwic.appkit.webbase.viewer.columns;

import de.jwic.ecolib.tableviewer.TableColumn;
import de.jwic.ecolib.tableviewer.TableModel;
import de.jwic.ecolib.tableviewer.TableModelAdapter;
import de.jwic.ecolib.tableviewer.TableModelEvent;
import de.xwic.appkit.webbase.utils.UserProfileWrapper;
import de.xwic.appkit.webbase.viewer.EntityTableViewer;

/**
 * This class handles the event when a column is selected. 
 * @author Aron Cotrau
 */
public class EntityTableModelListener extends TableModelAdapter {

	private EntityTableViewer entityTable = null;
	
	/**
	 * c-tor
	 * @param entityTable
	 * @param contentProvider
	 */
	public EntityTableModelListener(EntityTableViewer entityTable) {
		this.entityTable = entityTable;
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
		entityTable.handleSorting(tableColumn);
	}
	
	@Override
	public void rangeUpdated(TableModelEvent event) {
		TableModel tableModel = (TableModel)event.getEventSource();
		UserProfileWrapper userListProfile = entityTable.getUserListProfile();
		
		if (null != userListProfile) {
			userListProfile.setMaxRows(tableModel.getMaxLines());
		}
	}
}
