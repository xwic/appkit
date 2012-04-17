/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.webbase.viewer.base.TableViewRender
 * Created on Mar 21, 2007 by Aron Cotrau
 *
 */
package de.xwic.appkit.webbase.viewer;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.base.ImageRef;
import de.jwic.base.Page;
import de.jwic.base.RenderContext;
import de.jwic.base.UserAgentInfo;
import de.jwic.controls.ActionBarControl;
import de.jwic.controls.ButtonControl;
import de.jwic.ecolib.tableviewer.DefaultTableRenderer;
import de.jwic.ecolib.tableviewer.ITableLabelProvider;
import de.jwic.ecolib.tableviewer.TableColumn;
import de.jwic.ecolib.tableviewer.TableModel;
import de.jwic.ecolib.tableviewer.TableModelAdapter;
import de.jwic.ecolib.tableviewer.TableModelEvent;
import de.jwic.ecolib.tableviewer.TableViewer;
import de.jwic.ecolib.tableviewer.export.ExcelExportControl;
import de.jwic.events.SelectionEvent;
import de.jwic.events.SelectionListener;
import de.xwic.appkit.core.config.Bundle;
import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.config.ConfigurationManager;
import de.xwic.appkit.core.config.Setup;
import de.xwic.appkit.core.config.list.ListColumn;
import de.xwic.appkit.core.config.list.ListSetup;
import de.xwic.appkit.core.config.model.Property;
import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.model.daos.IMitarbeiterDAO;
import de.xwic.appkit.core.model.daos.IUserListProfileDAO;
import de.xwic.appkit.core.model.entities.IMitarbeiter;
import de.xwic.appkit.core.model.entities.IUserListProfile;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.core.trace.ITraceOperation;
import de.xwic.appkit.core.trace.Trace;
import de.xwic.appkit.webbase.utils.UserConfigXmlReader;
import de.xwic.appkit.webbase.utils.UserListUtil;
import de.xwic.appkit.webbase.utils.UserProfileWrapper;
import de.xwic.appkit.webbase.viewer.base.DAOContentProvider;
import de.xwic.appkit.webbase.viewer.base.PropertyLabelProvider;
import de.xwic.appkit.webbase.viewer.columns.ColumnSelector;
import de.xwic.appkit.webbase.viewer.columns.EntityTableModelListener;
import de.xwic.appkit.webbase.viewer.columns.TableColumnInfo;
import de.xwic.appkit.webbase.viewer.columns.UserListColumn;
import de.xwic.appkit.webbase.viewer.columns.UserListSetup;
import de.xwic.appkit.webbase.viewer.columns.ColumnSelector.IColumnSelectorListener;
import de.xwic.appkit.webbase.views.BaseBrowserView;

/**
 * Handles table viewer's data and sets default label and content provider.
 * 
 * @author Aron Cotrau
 */
public class EntityTableViewer extends ControlContainer {

	private static Log log = LogFactory.getLog(EntityTableViewer.class);

	private DAO daoClass = null;
	private Bundle bundle = null;
	private ListSetup listSetup = null;

	private UserProfileWrapper userListProfile = null;
	private PropertyLabelProvider labelProvider = null;
	private DAOContentProvider contentProvider = null;

	private TableViewer tableViewer = null;
	private ExcelExportControl excelExport = null;
	private ActionBarControl actionBar = null;

	private int widthDecrease = 26;
	private int heightDecrease = 290;
	private int maxLines = 25;
	private boolean listSetupDirty = false;

	private List<TableColumnInfo> columnsList = null;

	private ColumnSelector columnSelector;
	
	private int ownerId = 0;

	private IColumnSelectorListener listener = new IColumnSelectorListener() {

		@Override
		public void selectionAborted() {
			columnSelector.loadUserListSetup(userListProfile);
		}

		@Override
		public void profileSaved(Object source, UserProfileWrapper userProfile) {
			columnSelector.loadUserListSetup(userProfile);
			refreshInternal(userProfile);
		}

		@Override
		public void profileSavedAs(Object source, UserProfileWrapper newUserProfile) {
			columnSelector.loadUserListSetup(newUserProfile);
			refreshInternal(newUserProfile);
		}
	};

	/**
	 * default constructor.
	 * 
	 * @param container
	 * @param daoClass
	 * @param userLS
	 */
	public EntityTableViewer(IControlContainer container, DAO daoClass, ListSetup lSetup, UserProfileWrapper userLS) {
		super(container);
		this.daoClass = daoClass;
		this.userListProfile = userLS;
		
		if (null != userLS && userLS.getMaxRows() > 0) {
			maxLines = userLS.getMaxRows();
		}
		
		setOwnerId(); 
		this.listSetup = lSetup;

		try {
			bundle = lSetup.getEntityDescriptor().getDomain().getBundle(
					container.getSessionContext().getLocale().getLanguage());
		} catch (ConfigurationException e) {
			log.error("No configuration found", e);
		}

		UserAgentInfo agent = getSessionContext().getUserAgent();
		this.setWidthDecrease(agent.isIE() && agent.getMajorVersion() < 8 ? 26 : 39);
		initTableViewer(container);
	}

	/**
	 * @param container
	 * @param name
	 * @param daoClass
	 * @param lSetup
	 * @param userLS
	 */
	public EntityTableViewer(IControlContainer container, String name, DAO daoClass, ListSetup lSetup,
			UserProfileWrapper userLS) {
		this(container, name, daoClass, lSetup, userLS, -1);
	}

	/**
	 * @param container
	 * @param name
	 * @param daoClass
	 * @param lSetup
	 * @param userLS
	 * @param widthDecrease
	 */
	public EntityTableViewer(IControlContainer container, String name, DAO daoClass, ListSetup lSetup,
			UserProfileWrapper userLS, int widthDecrease) {
		super(container, name);
		this.daoClass = daoClass;
		this.userListProfile = userLS;
		
		if (null != userLS && userLS.getMaxRows() > 0) {
			maxLines = userLS.getMaxRows();
		}
		
		this.listSetup = lSetup;

		setOwnerId();
		
		try {
			bundle = lSetup.getEntityDescriptor().getDomain().getBundle(
					container.getSessionContext().getLocale().getLanguage());
		} catch (ConfigurationException e) {
			log.error("No configuration found", e);
		}

		if (widthDecrease == -1) {
			UserAgentInfo agent = getSessionContext().getUserAgent();
			this.setWidthDecrease(agent.isIE() && agent.getMajorVersion() < 8 ? 26 : 39);
		} else {
			this.setWidthDecrease(widthDecrease);
		}
		initTableViewer(this);
	}
	

	/**
	 * 
	 */
	private void setOwnerId() {
		IMitarbeiterDAO mitDAO = (IMitarbeiterDAO) DAOSystem.getDAO(IMitarbeiterDAO.class);
		IMitarbeiter currentUser = mitDAO.getByCurrentUser();
		
		this.ownerId = null != currentUser ? currentUser.getId() : 0;
	}

	/**
	 * creates and inits the table viewer object
	 * 
	 * @param container
	 */
	private void initTableViewer(IControlContainer container) {
		actionBar = new ActionBarControl(container, "abar");

		ButtonControl buttonColumnSelection = new ButtonControl(actionBar);
		buttonColumnSelection.setTemplateName(ButtonControl.class.getName() + "_Action");
		buttonColumnSelection.setTitle("Select Columns");
		ImageRef selectorIcon = new ImageRef(getClass().getPackage(), "colselect.gif");
		buttonColumnSelection.setIconEnabled(selectorIcon);
		buttonColumnSelection.addSelectionListener(new SelectionListener() {
			public void objectSelected(SelectionEvent event) {
				columnSelector.open();
			}
		});
		
		actionBar.setPosition(buttonColumnSelection, ActionBarControl.Align.RIGHT);

		columnSelector = new ColumnSelector();
		columnSelector.init(container, "columnSelector");
		columnSelector.loadUserListSetup(userListProfile);
		columnSelector.addColumnSelectionListener(listener);

		tableViewer = new TableViewer(container, "tableViewer");
		labelProvider = new PropertyLabelProvider();
		contentProvider = new DAOContentProvider(daoClass);
		contentProvider.setEntityQuery(new PropertyQuery());
		tableViewer.setScrollable(true);
		tableViewer.setResizeableColumns(true);
		tableViewer.setSelectableColumns(true);
		tableViewer.setShowStatusBar(true);

		// Inject a Trace-enabled table renderer to trace table rendering time.
		tableViewer.setTableRenderer(new DefaultTableRenderer() {
			/* (non-Javadoc)
			 * @see de.jwic.ecolib.tableviewer.DefaultTableRenderer#renderTable(de.jwic.base.RenderContext, de.jwic.ecolib.tableviewer.TableViewer, de.jwic.ecolib.tableviewer.TableModel, de.jwic.ecolib.tableviewer.ITableLabelProvider)
			 */
			@Override
			public void renderTable(RenderContext renderContext, TableViewer viewer, TableModel model, ITableLabelProvider labelProvider) {
				ITraceOperation op = null;
				if (Trace.isEnabled()) {
					op = Trace.startOperation("entity-table-viewer");
					op.setInfo("Rendering " + daoClass.getEntityDescriptor().getClassname());
				}
				super.renderTable(renderContext, viewer, model, labelProvider);
				if (op != null) {
					op.finished();
				}
			}
		});
		
		excelExport = new ExcelExportControl(actionBar, "excel", tableViewer);
		excelExport.setTitle("Excel");
		ImageRef imgDef = new ImageRef(BaseBrowserView.class.getPackage(), "excel.gif");
		excelExport.setIconEnabled(imgDef);

		createColumns();

		tableViewer.setContentProvider(contentProvider);
		tableViewer.setTableLabelProvider(labelProvider);

		tableViewer.getModel().addTableModelListener(new TableModelAdapter() { // listen to column changed events
			@Override
			public void columnResized(TableModelEvent event) {
				notifyColumnResized();
			}
			@Override
			public void rangeUpdated(TableModelEvent event) {
				if (userListProfile != null && userListProfile.getMaxRows() != tableViewer.getModel().getMaxLines()) {
					listSetupDirty = true;
				}
			}
		});
		tableViewer.getModel().addTableModelListener(new EntityTableModelListener(this));

		updateTableViewerSize();

		actionBar.setVisible(false);
	}

	/**
	 * 
	 */
	private void updateTableViewerSize() {

		if (tableViewer != null) {
			Page page = (Page) getSessionContext().getTopControl();
			if (null != page) {
				int pageHeight = page.getPageSize().height;
				int pageWidth = page.getPageSize().width;

				int height = pageHeight - getHeightDecrease();
				if (height < 200) {
					height = 200;
				}
				int width = pageWidth - getWidthDecrease();
				if (width < 100) {
					width = 100;
				}
				tableViewer.setWidth(width);
				tableViewer.setHeight(height);
			} else {
				tableViewer.setWidth(600);
				tableViewer.setHeight(300);
			}
		}

	}

	/**
	 * creates the columns for the table, depending on the user list setup
	 * 
	 * @return a list with <code>TableColumnInfo</code> objects, to be added to
	 *         the <code>PropertyLabelProvider</code>
	 */
	public List<TableColumnInfo> createColumns() {
		List<?> list = null;

		UserListSetup userLS = null;

		try {

			if (null != userListProfile) {
				userLS = UserConfigXmlReader.getUserColumnList(this.userListProfile.getXmlContent());
				list = userLS.getColumns();
			} else {
				list = listSetup.getColumns();
			}

			columnsList = new ArrayList<TableColumnInfo>();
			TableModel tableModel = tableViewer.getModel();
			int idx = 1;

			tableModel.setSelectionMode(TableModel.SELECTION_SINGLE);
			tableModel.setMaxLines(maxLines > 0 ? maxLines : 25); // do not allow to set the maxLines to -ALL- from the setup

			for (Iterator<?> it = list.iterator(); it.hasNext();) {
				ListColumn lColumn = null;
				UserListColumn userLC = null;

				if (userLS != null) {
					userLC = (UserListColumn) it.next();
					lColumn = UserListUtil.getListColumn(listSetup, userLC);
					if (lColumn == null) {
						it.remove(); // remove the entry from the user list
						continue; 
					}
				} else {
					lColumn = (ListColumn) it.next();
				}

				boolean isVisible = true;
				Property[] properties = lColumn.getProperty();

				// see if all the properties are "readable". if not, don't show
				// the column.
				for (int i = 0; i < properties.length && isVisible; i++) {
					Property property = properties[i];
					isVisible = property.hasReadAccess() && !property.isHidden();
				}

				if (isVisible) {
					String title = null;
					if (null != bundle) {
						String key = lColumn.getTitleId();
						title = bundle.getString(key);
					}

					if (null == title || "!!".equals(title)) {
						title = getResString(lColumn.getFinalProperty());
					}

					TableColumn tc = new TableColumn(title);
					int width = userLC != null ? userLC.getWidth() : lColumn.getDefaultWidth();
					tc.setWidth(width <= 0 ? 100 : width);
					tableModel.addColumn(tc);

					TableColumnInfo property = new TableColumnInfo(getSessionContext().getLocale());
					property.setColumn(lColumn);
					property.setDataIdx(idx++);

					tc.setUserObject(property);
					columnsList.add(property);
				}
			}

			labelProvider.setPropertiesList(columnsList);
			
			if (null != userListProfile && null != userListProfile.getSortField() && propertyExists(userListProfile.getSortField())) {
				// sort
				boolean up = userListProfile.getSortDirection() == EntityQuery.SORT_DIRECTION_UP;
				sort(up, userListProfile.getSortField());
				TableColumn tableColumn = getTableColumn(userListProfile.getSortField());
				if (tableColumn != null) {
					if (tableColumn.getSortIcon() == TableColumn.SORT_ICON_NONE) {
						// clear all columns
						for (Iterator<TableColumn> it = tableViewer.getModel().getColumnIterator(); it.hasNext();) {
							TableColumn col = it.next();
							col.setSortIcon(TableColumn.SORT_ICON_NONE);
						}
					}
					tableColumn.setSortIcon(up ? TableColumn.SORT_ICON_UP : TableColumn.SORT_ICON_DOWN);
				}
				
				tableViewer.setRequireRedraw(true);
			}
			listSetupDirty = false;
		} catch (Exception e) {
			log.error("Exception occured while creating columns !", e);
		}
		return columnsList;
	}

	private boolean propertyExists(String propertyId) {
		StringTokenizer stk = new StringTokenizer(propertyId, ".");
		Class<?> clazz = daoClass.getEntityClass();
		
		for (int nr = 0; stk.hasMoreTokens(); nr++) {
			PropertyDescriptor desc;
			try {
				desc = new PropertyDescriptor(stk.nextToken(), clazz);
				clazz = desc.getPropertyType();
			} catch (IntrospectionException e) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * refreshes columns
	 */
	public void refreshColumns() {
		TableModel model = tableViewer.getModel();
		List<TableColumn> temp = new ArrayList<TableColumn>();
		Iterator<TableColumn> columnIterator = model.getColumnIterator();

		while (columnIterator.hasNext()) {
			temp.add(columnIterator.next());
		}
		for (Iterator<TableColumn> it = temp.iterator(); it.hasNext();) {
			TableColumn column = (TableColumn) it.next();
			model.removeColumn(column);
		}

		createColumns();
	}

	/**
	 * @param key
	 * @return the resource String from the bundle
	 */
	public String getResString(Property property) {
		String key = "";
		if (null != property) {
			key = property.getEntityDescriptor().getClassname() + "." + property.getName();
		}
		if (null != bundle) {
			if (!"".equals(key)) {
				return bundle.getString(key);
			}
		} else {
			return "!" + key + "!";
		}

		return "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jwic.base.ControlContainer#destroy()
	 */
	public void destroy() {
		super.destroy();
		try {
			if (null != userListProfile) {
				userListProfile.setMaxRows(tableViewer.getModel().getMaxLines());
			}
			
			saveColumnSetup();
			columnSelector.removeColumnSelectionListener(listener);
		} catch (Exception e) {
			log.warn("Error saving user list profile: " + e, e);
		}
	}

	/**
	 * saves the current columns setup
	 */
	public void saveColumnSetup() {
		// avoid NPE, when the view was not initialized correctly
		if (null != getTableViewer() && listSetupDirty) {
			List<?> lcUser = null;
			UserListSetup userLS = null;

			try {

				if (null != userListProfile) {
					userLS = UserConfigXmlReader.getUserColumnList(this.userListProfile.getXmlContent());
					lcUser = userLS.getColumns();
				} else {
					lcUser = listSetup.getColumns();
				}

				Iterator<TableColumn> tableColumnsIterator = tableViewer.getModel().getColumnIterator();
				UserListSetup userListSetup = new UserListSetup();
				String lsId = userLS != null ? userLS.getListId() : listSetup.getListId();
				userListSetup.setListId(null == lsId || "".equals(lsId) ? Setup.ID_DEFAULT : lsId);
				userListSetup.setTypeClass(userLS != null ? userLS.getTypeClass() : listSetup.getTypeClass());

				// sets the column widths in the ListSetup
				while (tableColumnsIterator.hasNext()) {
					TableColumn tc = (TableColumn) tableColumnsIterator.next();

					for (Iterator<?> iter = lcUser.iterator(); iter.hasNext();) {
						ListColumn column = null;
						UserListColumn lc = null;

						String colText = "";

						if (userLS != null) {
							lc = (UserListColumn) iter.next();
							column = UserListUtil.getListColumn(listSetup, lc);
							if (column == null) {
								continue; // skip columns that do not exist anymore
							}
						} else {
							column = (ListColumn) iter.next();
						}

						colText = getResString(column.getFinalProperty());
						if (column != null) {
							if (null != column.getTitleId() && !"".equals(column.getTitleId())) {
								colText = bundle.getString(column.getTitleId());
							}
						} 

						if (tc.getTitle().equals(colText)) {
							if (lc != null) {
								lc.setWidth(tc.getWidth());
								userListSetup.addColumn(lc);
							}
						}
					}
				}

				//UserConfigXmlReader.setUserColumnList(userListSetup);
				if (null != userListProfile) {
					IUserListProfile userProfile = UserListUtil.toUserProfile(userListSetup, userListProfile.getDescription(), ownerId);

					DAO dao = DAOSystem.getDAO(IUserListProfileDAO.class);

					userProfile.setSortField(null != userListProfile && propertyExists(userListProfile.getSortField()) ? userListProfile.getSortField() : null);
					userProfile.setSortDirection(null != userListProfile ? userListProfile.getSortDirection() : 0);
					userProfile.setMaxRows(null != userListProfile ? userListProfile.getMaxRows() : 0);

					dao.update(userProfile);
				}
				listSetupDirty = false;
			} catch (Throwable e) {
				log.error("Exception occured while saving preferences !", e);
			}
		}
	}

	private void refreshInternal(UserProfileWrapper userLS) {
		setUserListProfile(userLS);
	}

	/**
	 * @return the tableViewer
	 */
	public TableViewer getTableViewer() {
		return tableViewer;
	}

	/**
	 * @param tableViewer
	 *            the tableViewer to set
	 */
	public void setTableViewer(TableViewer tableViewer) {
		this.tableViewer = tableViewer;
	}

	/**
	 * @return the userLS
	 */
	public UserProfileWrapper getUserListProfile() {
		return userListProfile;
	}

	/**
	 * @param userProfile
	 *            the userProfile to set
	 */
	public void setUserListProfile(UserProfileWrapper userProfile) {
		this.userListProfile = userProfile;
		// reset the List setup too
		String lsId = userProfile.getBaseProfileName();
		
		try {
			ListSetup ls = ConfigurationManager.getUserProfile().getListSetup(userListProfile.getClassName(), lsId);
			setListSetup(ls);
			
			columnSelector.loadUserListSetup(userProfile);
		} catch (Throwable e) {
			log.error("Exception saving list setup !", e);
		}

		// set the default lines
		maxLines = 25; // limit to 25 lines max by default.
		if (userProfile.getMaxRows() > 0) {
			maxLines = userProfile.getMaxRows();
		}
		
		// reload all
		refreshColumns();
		getTableViewer().setRequireRedraw(true);
		listSetupDirty = false;
	}

	/**
	 * @return the actionBar
	 */
	public ActionBarControl getActionBar() {
		return actionBar;
	}

	/**
	 * @param actionBar
	 *            the actionBar to set
	 */
	public void setActionBar(ActionBarControl actionBar) {
		this.actionBar = actionBar;
	}

	/**
	 * Change the sort icon.
	 * 
	 * @param tableColumn
	 */
	public void handleSorting(TableColumn tableColumn) {

		TableViewer viewer = getTableViewer();
		if (tableColumn.getSortIcon() == TableColumn.SORT_ICON_NONE) {
			// clear all columns
			for (Iterator<TableColumn> it = viewer.getModel().getColumnIterator(); it.hasNext();) {
				TableColumn col = it.next();
				col.setSortIcon(TableColumn.SORT_ICON_NONE);
			}
		}

		boolean up = true;
		switch (tableColumn.getSortIcon()) {
		case TableColumn.SORT_ICON_NONE:
			tableColumn.setSortIcon(TableColumn.SORT_ICON_UP);
			break;
		case TableColumn.SORT_ICON_UP:
			tableColumn.setSortIcon(TableColumn.SORT_ICON_DOWN);
			up = false;
			break;
		case TableColumn.SORT_ICON_DOWN:
			// once sorted, the list can not be displayed in the
			// original order as we sort the original table,
			// therefor loosing the original order.
			tableColumn.setSortIcon(TableColumn.SORT_ICON_UP);
			break;
		}

		// do the sort
		TableColumnInfo ci = (TableColumnInfo) tableColumn.getUserObject();
		Property[] props = ci.getColumn().getProperty();
		String propName = "";
		for (int i = 0; i < props.length; i++) {
			propName += props[i].getName() + ".";
		}
		propName = propName.substring(0, propName.length() - 1);
		sort(up, propName);

		if (null != userListProfile) {
			userListProfile.setSortField(propName);
			userListProfile.setSortDirection(up ? EntityQuery.SORT_DIRECTION_UP : EntityQuery.SORT_DIRECTION_DOWN);
		}
		
		viewer.setRequireRedraw(true);
	}

	private void sort(boolean up, String propName) {
		EntityQuery entityQuery = contentProvider.getEntityQuery();
		if (null == entityQuery) {
			entityQuery = new PropertyQuery();
		}
		
		entityQuery.setSortField(propName);
		int direction = up ? EntityQuery.SORT_DIRECTION_UP : EntityQuery.SORT_DIRECTION_DOWN;
		entityQuery.setSortDirection(direction);
		contentProvider.setEntityQuery(entityQuery);
		listSetupDirty = true; // sorting is a stored element
	}
	
	private TableColumn getTableColumn(String propName) {
		for (Iterator<TableColumn> it = tableViewer.getModel().getColumnIterator(); it.hasNext();) {
			TableColumn col = it.next();
			TableColumnInfo ci = (TableColumnInfo) col.getUserObject();
			if (ci.getColumn().getPropertyId().equals(propName)) {
				return col;
			}
 		}
		
		return null;
	}

	/**
	 * @return the contentProvider
	 */
	public DAOContentProvider getContentProvider() {
		return contentProvider;
	}

	/**
	 * @return the labelProvider
	 */
	public PropertyLabelProvider getLabelProvider() {
		return labelProvider;
	}

	/**
	 * @param labelProvider
	 *            the labelProvider to set
	 */
	public void setLabelProvider(PropertyLabelProvider labelProvider) {
		this.labelProvider = labelProvider;
		this.tableViewer.setTableLabelProvider(labelProvider);
		this.labelProvider.setPropertiesList(columnsList);
	}

	/**
	 * @param contentProvider
	 *            the contentProvider to set
	 */
	public void setContentProvider(DAOContentProvider contentProvider) {
		this.contentProvider = contentProvider;
		this.tableViewer.setContentProvider(contentProvider);
	}

	/**
	 * @param heightDecrease
	 *            the heightDecrease to set
	 */
	public void setHeightDecrease(int heightDecrease) {
		this.heightDecrease = heightDecrease;
		updateTableViewerSize();
	}

	/**
	 * @return the heightDecrease
	 */
	public int getHeightDecrease() {
		return heightDecrease;
	}

	/**
	 * @param widthDecrease
	 *            the widthDecrease to set
	 */
	public void setWidthDecrease(int widthDecrease) {
		this.widthDecrease = widthDecrease;
		updateTableViewerSize();
	}

	/**
	 * @return the widthDecrease
	 */
	public int getWidthDecrease() {
		return widthDecrease;
	}

	/**
	 * @return the lSetup
	 */
	public ListSetup getLSetup() {
		return listSetup;
	}

	/**
	 * @param newSetup
	 *            the lSetup to set
	 */
	public void setListSetup(ListSetup newSetup) {
		listSetup = newSetup;
	}

	/**
	 * opens the column selector window
	 */
	public void openColumnSelector() {
		columnSelector.open();
	}

	/**
	 * adds a column selector listener
	 * 
	 * @param listener
	 */
	public void addColumnSelectorListener(IColumnSelectorListener listener) {
		columnSelector.addColumnSelectionListener(listener);
	}

	/**
	 * removes a column selector listener
	 * 
	 * @param listener
	 */
	public void removeColumnSelectorListener(IColumnSelectorListener listener) {
		columnSelector.removeColumnSelectionListener(listener);
	}

	/**
	 * Notification that the columns got resized
	 */
	void notifyColumnResized() {
		listSetupDirty = true;
	}
}
