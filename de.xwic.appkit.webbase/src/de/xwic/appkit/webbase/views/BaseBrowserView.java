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
package de.xwic.appkit.webbase.views;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.base.ImageRef;
import de.jwic.controls.Button;
import de.jwic.controls.ToolBar;
import de.jwic.controls.ToolBarGroup;
import de.jwic.controls.tableviewer.TableModel;
import de.jwic.controls.tableviewer.TableViewer;
import de.jwic.data.IContentProvider;
import de.jwic.events.ElementSelectedEvent;
import de.jwic.events.ElementSelectedListener;
import de.jwic.events.SelectionEvent;
import de.jwic.events.SelectionListener;
import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.config.ConfigurationManager;
import de.xwic.appkit.core.config.Setup;
import de.xwic.appkit.core.config.list.ListColumn;
import de.xwic.appkit.core.config.list.ListSetup;
import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.webbase.utils.UserListUtil;
import de.xwic.appkit.webbase.utils.UserProfileWrapper;
import de.xwic.appkit.webbase.viewer.EntityTableViewer;
import de.xwic.appkit.webbase.viewer.base.DAOContentProvider;
import de.xwic.appkit.webbase.viewer.columns.UserListColumn;
import de.xwic.appkit.webbase.viewer.columns.UserListSetup;

/**
 * This class is the root class for the views in the web application.
 * 
 * @author Aron Cotrau
 */
public abstract class BaseBrowserView extends ControlContainer {

	/** user id */
	public static final String ID_USER = "user";
	/** the log */
	protected static Log log = LogFactory.getLog(BaseBrowserView.class);

	/** the entity table viewer */
	protected EntityTableViewer entityTableViewer = null;
	/** the search pane for the query items */
	protected IControlContainer searchPane = null;
	/** the actual selected IEntity */
	protected IEntity selectedEntity = null;

	
	
	/**
	 * @param container
	 * @param name
	 */
	public BaseBrowserView(IControlContainer container, String name) {
		super(container, name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jwic.wap.core.Part#createControls(de.jwic.base.IControlContainer)
	 */
	public void createControls(IControlContainer container) {
		entityTableViewer = new EntityTableViewer(container, getDaoClass(), getListSetup(), getUserListSetup());

		TableModel model = entityTableViewer.getTableViewer().getModel();
		model.addElementSelectedListener(new ElementSelectedListener() {
			public void elementSelected(ElementSelectedEvent event) {
				// the key is the id of the object.
				String key = (String) event.getElement();
				selectedEntity = getDaoClass().getEntity(Long.parseLong(key));
				//site.getSelectionService().fireElementSelected(BaseBrowserView.this, selectedEntity);
			}
		});

		ToolBar aBar = entityTableViewer.getToolBar();
		ToolBarGroup tbg = aBar.addGroup();
		if (getListSetup().isActionEnabled(ListSetup.ACTION_EDIT)) {
			Button buttonEdit = tbg.addButton();
			buttonEdit.setTitle("Edit");
			ImageRef imgDef = new ImageRef(BaseBrowserView.class.getPackage(), "editor.gif");
			buttonEdit.setIconEnabled(imgDef);
			buttonEdit.addSelectionListener(new SelectionListener() {
				public void objectSelected(SelectionEvent event) {
					editSelection();
				}
			});
		}

		if (getListSetup().isActionEnabled(ListSetup.ACTION_CREATE)) {
			Button buttonAdd = tbg.addButton();
			buttonAdd.setTitle("New");
			ImageRef imgDef = new ImageRef(BaseBrowserView.class.getPackage(), "newfile_wiz.gif");
			buttonAdd.setIconEnabled(imgDef);
			buttonAdd.addSelectionListener(new SelectionListener() {
				public void objectSelected(SelectionEvent event) {
					createNewEntity();
				}
			});
		}

		if (getListSetup().isActionEnabled(ListSetup.ACTION_DELETE)) {
			Button buttonDelete = tbg.addButton();
			buttonDelete.setTitle("Delete");
			ImageRef imgDef = new ImageRef(BaseBrowserView.class.getPackage(), "remove.gif");
			buttonDelete.setIconEnabled(imgDef);
			buttonDelete.addSelectionListener(new SelectionListener() {
				public void objectSelected(SelectionEvent event) {
					deleteSelection();
				}
			});
		}
	}

	private void showNotImplementedDialog() {
//		MessageDialog dialog = new MessageDialog(site);
//		dialog.setText("This method is not implemented at the moment. But will be very soon !");
//		dialog.setTitle("Information");
//
//		dialog.open();
	}

	protected void createNewEntity() {
		showNotImplementedDialog();
	}

	/**
	 * deletes selected entity
	 */
	protected void deleteSelection() {
		if (null != selectedEntity) {
			DAO dao = getDaoClass();
			dao.softDelete(selectedEntity);
			TableViewer tableViewer = entityTableViewer.getTableViewer();
			IContentProvider contentProvider = tableViewer.getModel().getContentProvider();
			if (contentProvider instanceof DAOContentProvider) {
				((DAOContentProvider) contentProvider).removeElement(selectedEntity);
				tableViewer.setRequireRedraw(true);
			}
		}
	}

	protected void editSelection() {
		if (null != selectedEntity) {
//			OpenEntityCommand.openEntityEditor(selectedEntity, site);
		} else {
//			JWicErrorDialog.openError("Please select an entity first", site);
		}
	}

	/**
	 * @return the column setup for the setted list setup id
	 */
	protected ListSetup getListSetup() {
		return getListSetup(getListSetupId());
	}

	/**
	 * @param pListId
	 * @return the column setup of the given listSetupId
	 */
	protected ListSetup getListSetup(String pListId) {
		ListSetup listSetup;

		try {
			if (null == pListId || "".equals(pListId)) {
				pListId = Setup.ID_DEFAULT;
			}

			listSetup = ConfigurationManager.getUserProfile().getListSetup(getEntityDescriptor().getClassname(), pListId);
			return listSetup;
		} catch (ConfigurationException e) {
			log.error("No configuration found");
		}

		return null;
	}

	/**
	 * @return the UserListSetup for the given user
	 */
	protected UserProfileWrapper getUserListSetup() {
		return this.getUserListSetup(getListSetupId());
	}

	/**
	 * @return the UserListSetup for the given user and for the given
	 *         ListSetupId
	 */
	protected UserProfileWrapper getUserListSetup(String pListId) {
		UserListSetup setup = null;

		try {
//			IPreferenceStore prefStore = WebToolsPlugin.getDefault().getPreferenceStore();
//			String location = getUserFileLocation();
//
//			if (null == pListId || "".equals(pListId)) {
//				pListId = Setup.ID_DEFAULT;
//			}
//
//			setup = UserConfigXmlReader.getUserColumnList(prefStore.getString(location, ""));
//			if (null == setup) {
//				// the directory and the file was now created
//				// add the default visible columns
//				setup = getDefaultVisibleColumns();
//			}
		} catch (Exception e) {
			log.error("Error while loading user list setup", e);
		}

		
		//return setup;
		return null;
	}

	/**
	 * @return the EntityDescriptor for the current entity type
	 */
	public EntityDescriptor getEntityDescriptor() {
		EntityDescriptor ed = null;

		try {
			ed = ConfigurationManager.getSetup().getEntityDescriptor(getEntityClassName());
		} catch (ConfigurationException e) {
			log.error("No configuration found");
		}

		return ed;
	}

	/**
	 * @return a user list setup with the default visible columns
	 */
	private UserListSetup getDefaultVisibleColumns() {
		UserListSetup userList = new UserListSetup();
		ListSetup setup = getListSetup();

		userList.setEntityDescriptor(getEntityDescriptor());
		userList.setTypeClass(getEntityDescriptor().getClassname());

		List columns = setup.getColumns();
		for (Iterator iter = columns.iterator(); iter.hasNext();) {
			ListColumn element = (ListColumn) iter.next();
			if (element.isDefaultVisible()) {
				UserListColumn column = UserListUtil.createUserListColumn(element);
				userList.addColumn(column);
			}
		}
		userList.setListId(setup.getListId());
		try {
			//UserConfigXmlReader.setUserColumnList(userList);
		} catch (Throwable e) {
			log.error("Exception occured while saving preferences !", e);
		}

		return userList;
	}

	/**
	 * @param entity
	 * @return if the entity is relevant
	 */
	public boolean isEntityRelevant(IEntity entity) {
		try {
			Class baseClass = Class.forName(getEntityClassName());
			return baseClass.isAssignableFrom(entity.getClass());
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

	/**
	 * @return the DAO class for this view.
	 */
	public abstract DAO getDaoClass();

	/**
	 * @return the classname for the entity shown in this view
	 */
	public abstract String getEntityClassName();

	/**
	 * @return the id for list setup and user list setup.<br>
	 *         if <code>null</code> or <code>""</code> is returned, the
	 *         "default" list setup id will be used
	 */
	public abstract String getListSetupId();
}
