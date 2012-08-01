
package de.xwic.appkit.webbase.table;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.base.RenderContext;
import de.jwic.ecolib.tableviewer.DefaultTableRenderer;
import de.jwic.ecolib.tableviewer.ITableLabelProvider;
import de.jwic.ecolib.tableviewer.TableColumn;
import de.jwic.ecolib.tableviewer.TableModel;
import de.jwic.ecolib.tableviewer.TableModelAdapter;
import de.jwic.ecolib.tableviewer.TableModelEvent;
import de.jwic.ecolib.tableviewer.TableViewer;
import de.jwic.events.ElementSelectedEvent;
import de.jwic.events.ElementSelectedListener;
import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.config.model.Property;
import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.trace.ITraceOperation;
import de.xwic.appkit.core.trace.Trace;
import de.xwic.appkit.webbase.table.columns.ColumnSelectorDialog;
import de.xwic.appkit.webbase.table.filter.ColumnFilterControl;
import de.xwic.appkit.webbase.toolkit.app.ExtendedApplication;
import de.xwic.appkit.webbase.toolkit.util.ImageLibrary;

/**
 * Displays a TableViewer to list entities of a specific type. The user can filter and sort by any columns
 * that are not explicitly excluded.
 * 
 * This table viewer reads the data for each column as single property, not as one fully loaded Entity.
 * 
 * @author lippisch
 */
public class EntityTable extends ControlContainer {

	protected EntityTableModel model;
	protected TableViewer tblViewer;
	
	private ColumnFilterControl colFilter;
	
	private List<ElementSelectedListener> elmSelListeners = new ArrayList<ElementSelectedListener>();
	
	/**
	 * If used without the EntityListView ALWAYS instantiate with initUserConfig=true.
	 * Otherwise the EntityListView will take care of initiating the UserConfigHandler
	 * 
	 * @param container
	 * @param name
	 * @param configuration
	 * @param initUserConfig
	 * @throws ConfigurationException
	 */
	public EntityTable(IControlContainer container, String name, EntityTableConfiguration configuration, boolean initUserConfig) throws ConfigurationException {
		super(container, name);
		
		configuration.setLocale(container.getSessionContext().getLocale());
		model = new EntityTableModel(configuration);
		
		// Register listener to act on updates and refresh properly
		model.addEntityTableListener(new EntityTableAdapter() {
			@Override
			public void columnSorted(EntityTableEvent event) {
				onColumnSortChange(event.getColumn());
			}
			@Override
			public void columnFiltered(EntityTableEvent event) {
				onColumnFilterChange(event.getColumn());
			}
			@Override
			public void columnsReordered(EntityTableEvent event) {
				onColumnsReordered();
			}
			@Override
			public void userConfigurationChanged(EntityTableEvent event) {
				onUserConfigurationChanged();
			}
		});
		
		createTableViewer();
		createColumnFilter();

		if (initUserConfig) {
			getModel().getUserConfigHandler().initUserConfig();
			getTableViewer().getModel().setMaxLines(getModel().getUserConfigHandler().getMaxRows());
		}
	}

	/**
	 * 
	 */
	private void onColumnsReordered() {
		updateTableColumns();
		tblViewer.requireRedraw();
	}
	
	/**
	 * 
	 */
	private void onUserConfigurationChanged() {
		model.getUserConfigHandler().setConfigDirty(false);
		
		updateTableColumns();
		
		TableModel tm = tblViewer.getModel();
		tm.setMaxLines(model.getUserConfigHandler().getMaxRows());
		tm.clearSelection();
		
		tblViewer.requireRedraw();
	}

	/**
	 * Add a listener for selection events. The event will contain the ID of the entity
	 * as Integer or NULL if the selection was lost.
	 * @param listener
	 */
	public void addElementSelectedListener(ElementSelectedListener listener) {
		elmSelListeners.add(listener);
	}
	
	/**
	 * Remove an element selected listener.
	 * @param listener
	 */
	public void removeElementSelectedListener(ElementSelectedListener listener) {
		elmSelListeners.remove(listener);
	}
	
	/**
	 * @param column
	 */
	private void onColumnFilterChange(Column column) {
		
		for (Iterator<TableColumn> it = tblViewer.getModel().getColumnIterator(); it.hasNext(); ) {
			TableColumn tc = it.next();
			Column col = (Column)tc.getUserObject();
			setColumnFilterIcon(tc, col);
		}
		tblViewer.getModel().clearSelection();
		tblViewer.requireRedraw();
		
	}

	/**
	 * @param column
	 */
	private void onColumnSortChange(Column column) {
		
		for (Iterator<TableColumn> it = tblViewer.getModel().getColumnIterator(); it.hasNext(); ) {
			TableColumn tc = it.next();
			Column col = (Column)tc.getUserObject();
			setColumnSortIcon(tc, col);
		}
		tblViewer.requireRedraw();
	}
	
	/**
	 * 
	 */
	private void createColumnFilter() {
		colFilter = new ColumnFilterControl(this, "balloon", model);
		colFilter.setTableViewer(tblViewer);
	}

	/**
	 * 
	 */
	private void createTableViewer() {
		
		DAO dao = DAOSystem.findDAOforEntity(model.getEntityClass());
		
		tblViewer = new TableViewer(this, "tbl");
		tblViewer.setHeight(400);
		tblViewer.setWidth(1200);
		tblViewer.setShowStatusBar(true);
		tblViewer.setContentProvider(new EntityContentProvider(dao, model));
		tblViewer.setTableLabelProvider(new EntityLabelProvider());
		
		tblViewer.setScrollable(true);
		tblViewer.setResizeableColumns(true);
		tblViewer.setSelectableColumns(true);
		tblViewer.setShowStatusBar(true);
		tblViewer.setRowHeightHint(19);

		// Inject a Trace-enabled table renderer to trace table rendering time.
		tblViewer.setTableRenderer(new DefaultTableRenderer() {
			@Override
			public void renderTable(RenderContext renderContext, TableViewer viewer, TableModel tm, ITableLabelProvider labelProvider) {
				ITraceOperation op = null;
				if (Trace.isEnabled()) {
					op = Trace.startOperation("entity-table");
					op.setInfo("Rendering " + model.getEntityClass().getName());
				}
				super.renderTable(renderContext, viewer, tm, labelProvider);
				if (op != null) {
					op.finished();
				}
			}
		});

		TableModel tblModel = tblViewer.getModel();
		tblModel.setSelectionMode(TableModel.SELECTION_SINGLE);
		tblModel.addTableModelListener(new TableModelAdapter() {
			@Override
			public void columnSelected(TableModelEvent event) {
				colFilter.open(event.getTableColumn());
			}
			
			@Override
			public void columnResized(TableModelEvent event) {
				Column col = (Column) event.getTableColumn().getUserObject();
				col.setWidth(event.getTableColumn().getWidth());
				
				model.getUserConfigHandler().setConfigDirty(true);
			}
			
			@Override
			public void rangeUpdated(TableModelEvent event) {
				// range updated is also fired when we switch through pages, that's why
				// this check is needed
				if (model.getUserConfigHandler().getMaxRows() != tblViewer.getModel().getMaxLines()) {
					model.getUserConfigHandler().setNewMaxRows(tblViewer.getModel().getMaxLines());
				}
			}
		});
		
		tblModel.addElementSelectedListener(new ElementSelectedListener() {
			@Override
			public void elementSelected(ElementSelectedEvent event) {
				if (event.getElement() instanceof String) {
					String s = (String)event.getElement();
					fireSelectionEvent(s.isEmpty() ? null : Integer.parseInt(s), event.isDblClick());
				} else {
					fireSelectionEvent(null, event.isDblClick());
				}
			}
		});
		
		
		updateTableColumns();
		
	}

	/**
	 * Dispatch selection event to listeners.
	 * @param integer
	 */
	private void fireSelectionEvent(Integer entityId, boolean isDblClick) {
		ElementSelectedEvent event = new ElementSelectedEvent(this, entityId, isDblClick);
		
		ElementSelectedListener l[] = new ElementSelectedListener[elmSelListeners.size()];
		l = elmSelListeners.toArray(l);
		for (ElementSelectedListener listener : l) {
			listener.elementSelected(event);
		}
	}

	/**
	 * 
	 */
	private void updateTableColumns() {
		TableModel tm = tblViewer.getModel();
		tm.removeAllColumns();
		
		for (Column column : model.getColumns()) {
			boolean isVisible = column.isVisible();
			Property[] properties = column.getListColumn().getProperty();

			// can be null if it's a custom column which does not relate to a field on the entity
			if (properties != null) {
				// see if all the properties are "readable". if not, don't show
				// the column.
				for (int i = 0; i < properties.length && isVisible; i++) {
					Property property = properties[i];
					isVisible = property.hasReadAccess() && !property.isHidden();
				}
			}

			if (isVisible) {
				TableColumn tc = new TableColumn(column.getTitle(), column.getWidth(), column);
				tm.addColumn(tc);
				setColumnFilterIcon(tc, column);
				setColumnSortIcon(tc, column);
			}
		}
	}


	/**
	 * @param tableColumn
	 * @param col
	 */
	private void setColumnFilterIcon(TableColumn tableColumn, Column col){
		if (col.getFilter() != null) {
			tableColumn.setImage(ImageLibrary.ICON_TBL_FILTER);
		} else {
			tableColumn.setImage(null);
		}
	}
	
	/**
	 * @param tableColumn
	 * @param col
	 */
	private void setColumnSortIcon(TableColumn tableColumn, Column col){
		switch (col.getSortState()) {
		case DOWN:
			tableColumn.setSortIcon(TableColumn.SORT_ICON_DOWN);
			break;
		case UP:
			tableColumn.setSortIcon(TableColumn.SORT_ICON_UP);
			break;
		case NONE:
			tableColumn.setSortIcon(TableColumn.SORT_ICON_NONE);
			break;
		}
	}
	
	/**
	 * @return the model
	 */
	public EntityTableModel getModel() {
		return model;
	}

	/**
	 * @return
	 * @see de.jwic.ecolib.tableviewer.TableViewer#getHeight()
	 */
	public int getHeight() {
		return tblViewer.getHeight();
	}

	/**
	 * @return
	 * @see de.jwic.ecolib.tableviewer.TableViewer#getWidth()
	 */
	public int getWidth() {
		return tblViewer.getWidth();
	}

	/**
	 * @param height
	 * @see de.jwic.ecolib.tableviewer.TableViewer#setHeight(int)
	 */
	public void setHeight(int height) {
		tblViewer.setHeight(height);
	}

	/**
	 * @param width
	 * @see de.jwic.ecolib.tableviewer.TableViewer#setWidth(int)
	 */
	public void setWidth(int width) {
		tblViewer.setWidth(width);
	}

	/**
	 * Open the column selector dialog.
	 */
	public void openColumnSelector() {
		ColumnSelectorDialog dialog = new ColumnSelectorDialog(ExtendedApplication.getInstance(this).getSite(), model);
		dialog.show();
	}

	/**
	 * @return
	 */
	public TableViewer getTableViewer()	{
		return tblViewer;
	}
	
	/* (non-Javadoc)
	 * @see de.jwic.base.ControlContainer#destroy()
	 */
	@Override
	public void destroy() {
		
		if (model.getUserConfigHandler().isCurrentConfigDirty()) {
			model.getUserConfigHandler().saveCurrentDataToMainConfig();
		}		
		
		super.destroy();
	}
}
