/**
 * 
 */
package de.xwic.appkit.webbase.entityviewer.config;

import java.util.ArrayList;
import java.util.List;

import de.jwic.base.ControlContainer;
import de.jwic.base.Event;
import de.jwic.base.Page;
import de.jwic.controls.ScrollableContainer;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.model.daos.IUserViewConfigurationDAO;
import de.xwic.appkit.core.model.entities.IUserViewConfiguration;
import de.xwic.appkit.webbase.dialog.AbstractPopUpDialogWindow;
import de.xwic.appkit.webbase.dialog.DialogContent;
import de.xwic.appkit.webbase.table.EntityTableModel;
import de.xwic.appkit.webbase.toolkit.app.Site;

/**
 * @author Adrian Ionescu
 */
public class LoadProfileWindow extends AbstractPopUpDialogWindow {

	private EntityTableModel tableModel;
	private IUserViewConfigurationControlListener controlListener;
	private ScrollableContainer configControlsContainer;
	private List<UserViewConfigurationControl> configControls;
	
	/**
	 * @param container
	 */
	public LoadProfileWindow(Site site, final EntityTableModel tableModel) {
		super(site);
		
		this.tableModel = tableModel;
		
		setWidth(460);
		setHeight(270);
		
		configControls = new ArrayList<UserViewConfigurationControl>();
		
		controlListener = new IUserViewConfigurationControlListener() {
			@Override
			public void onConfigApplied(Event event) {
				UserViewConfigurationControl ctrl = (UserViewConfigurationControl) event.getEventSource();
				tableModel.getUserConfigHandler().applyPublicConfig(ctrl.getUserViewConfiguration());
				tableModel.applyColumnReorder();
			}
			
			@Override
			public void onConfigDeleted(Event event) {
			}

			@Override
			public void onConfigUpdated(Event event) {
			}
		};
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.dialog.AbstractDialogWindow#createContent(de.xwic.appkit.webbase.dialog.DialogContent)
	 */
	@Override
	protected void createContent(DialogContent content) {
		btCancel.setVisible(true);
		btCancel.setTitle("Close");
		btOk.setVisible(false);
		
		ControlContainer container = new ControlContainer(content);
		container.setTemplateName(getClass().getName() + "_content");
		
		configControlsContainer = new ScrollableContainer(container, "configControls");
		configControlsContainer.setTemplateName(getClass().getName() + "_controlsContainer");
		configControlsContainer.setHeight(isCurrentUserConfigDirty() ? "127px" : "170px");
		
		List<IUserViewConfiguration> list = ((IUserViewConfigurationDAO) DAOSystem.getDAO(IUserViewConfigurationDAO.class)).getPublicUserConfigurationsForView(tableModel.getCurrentUser(), tableModel.getEntityClass().getName(), tableModel.getViewId());
		for (IUserViewConfiguration config : list) {
			createUserConfigControl(config);
		}
	}
	
	/**
	 * @param userConfig
	 * @return
	 */
	private UserViewConfigurationControl createUserConfigControl(IUserViewConfiguration userConfig) {
		UserViewConfigurationControl ctrl = new UserViewConfigurationControl(configControlsContainer, userConfig, tableModel, false, true);
		ctrl.addListener(controlListener);
		configControls.add(ctrl);
		
		requireRedraw();
		
		return ctrl;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.webbase.dialog.AbstractDialogWindow#show()
	 */
	public void show() {
		if (baseContainer == null) {
			createControls();
		}
		
		int left = Page.findPage(this).getPageSize().width - getWidth() - 340;
		if (left != getLeft()) {
			setLeft(left);
		}
		
		setTop(310);
		
		setVisible(true);
	}

	// ************* USED IN THE VTL *************
	
	/**
	 * @return
	 */
	public List<UserViewConfigurationControl> getConfigControls() {
		return configControls;
	}
	
	/**
	 * @return
	 */
	public boolean isCurrentUserConfigDirty() {
		return tableModel.getUserConfigHandler().isCurrentConfigDirty();
	}
}
