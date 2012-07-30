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
import de.xwic.appkit.webbase.dialog.DialogEvent;
import de.xwic.appkit.webbase.dialog.DialogWindowAdapter;
import de.xwic.appkit.webbase.table.EntityTableModel;
import de.xwic.appkit.webbase.toolkit.app.ExtendedApplication;
import de.xwic.appkit.webbase.toolkit.app.Site;

/**
 * @author Adrian Ionescu
 */
public class UserConfigurationWindow extends AbstractPopUpDialogWindow {

	private EntityTableModel tableModel;
	private IUserViewConfigurationControlListener controlListener;
	private ScrollableContainer configControlsContainer;
	private List<UserViewConfigurationControl> configControls;
	
	private LoadProfileWindow loadProfileWindow;
	
	/**
	 * @param container
	 */
	public UserConfigurationWindow(Site site, final EntityTableModel tableModel) {
		super(site);
		
		this.tableModel = tableModel;
		
		setWidth(520);
		setHeight(365);
		
		configControls = new ArrayList<UserViewConfigurationControl>();
		
		controlListener = new IUserViewConfigurationControlListener() {
			@Override
			public void onConfigApplied(Event event) {
				UserViewConfigurationControl ctrl = (UserViewConfigurationControl) event.getEventSource();
				tableModel.getUserConfigHandler().applyConfig(ctrl.getUserViewConfiguration());
				
				closeProfileWindow();
			}
			
			@Override
			public void onConfigDeleted(Event event) {
				UserViewConfigurationControl ctrl = (UserViewConfigurationControl) event.getEventSource();

				configControls.remove(ctrl);
				
				// if the current config was deleted, apply the next one.. if no other exist, reset to the default list
				if (ctrl.isCurrentConfig()) {
					
					if (configControls.size() > 0) {
						tableModel.getUserConfigHandler().applyConfig(configControls.get(0).getUserViewConfiguration());
						tableModel.getUserConfigHandler().deleteConfig(ctrl.getUserViewConfiguration().getId());
					} else {
						tableModel.getUserConfigHandler().resetConfig();
					}
					
				} else {
					tableModel.getUserConfigHandler().deleteConfig(ctrl.getUserViewConfiguration().getId());
				}
				
				requireRedraw();
				
				ctrl.destroy();
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
		configControlsContainer.setHeight(isCurrentUserConfigDirty() ? "183px" : "265px");
		
		List<IUserViewConfiguration> list = ((IUserViewConfigurationDAO) DAOSystem.getDAO(IUserViewConfigurationDAO.class)).getUserConfigurationsForView(tableModel.getCurrentUser(), tableModel.getEntityClass().getName(), tableModel.getViewId());
		for (IUserViewConfiguration config : list) {
			if (config.isMainConfiguration()) {
				// don't add a control for the main config
				continue;
			}
			createUserConfigControl(config, false);
		}
		
		// custom template to add the Load Profile link
		buttonsContainer.setTemplateName(getClass().getName() + "_buttonsContainer");
	}
	
	/**
	 * @param userConfig
	 * @return
	 */
	private UserViewConfigurationControl createUserConfigControl(IUserViewConfiguration userConfig, boolean isNew) {
		UserViewConfigurationControl ctrl = new UserViewConfigurationControl(configControlsContainer, userConfig, tableModel, userConfig.getId() == 0, false);
		ctrl.addListener(controlListener);
		if (isNew) {
			configControls.add(0, ctrl);
		} else {
			configControls.add(ctrl);
		}
		
		requireRedraw();
		
		return ctrl;
	}
	
	/* (non-Javadoc)
	 * @see de.jwic.base.Control#actionPerformed(java.lang.String, java.lang.String)
	 */
	@Override
	public void actionPerformed(String actionId, String parameter) {
		if ("undoChanges".equalsIgnoreCase(actionId)) {
			
			tableModel.getUserConfigHandler().updateRelatedConfig(true);
			onCancel(); // to close the window
			
		} else if ("saveCurrent".equalsIgnoreCase(actionId)) {
			
			tableModel.getUserConfigHandler().updateRelatedConfig(false);
			onCancel(); // to close the window
			
		} else if ("saveNew".equalsIgnoreCase(actionId)) {
			
			final IUserViewConfiguration uvc = tableModel.getUserConfigHandler().createConfigWithCurrentSettings();
			UserViewConfigurationControl ctrl = createUserConfigControl(uvc, true);
			ctrl.actionEdit();
			
			ctrl.addListener(new IUserViewConfigurationControlListener() {
				@Override
				public void onConfigDeleted(Event event) {
				}
				
				@Override
				public void onConfigApplied(Event event) {
				}

				@Override
				public void onConfigUpdated(Event event) {
					// if it's a new config created from current settings, we need to apply it after it's saved, to make it the main one
					tableModel.getUserConfigHandler().applyConfig(uvc);
				}
			});
		} else if ("loadProfile".equalsIgnoreCase(actionId)) {
			// to prevent opening the dialog multiple times
			if (loadProfileWindow != null) {
				return;
			}
			
			loadProfileWindow = new LoadProfileWindow(ExtendedApplication.getInstance(this).getSite(), tableModel);
			loadProfileWindow.addDialogWindowListener(new DialogWindowAdapter() {
				@Override
				public void onDialogAborted(DialogEvent event) {
					loadProfileWindow = null;
				}
			});
			loadProfileWindow.show();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.webbase.dialog.AbstractDialogWindow#show()
	 */
	public void show() {
		if (baseContainer == null) {
			createControls();
		}
		
		// -30, in case of scrollbars.. just to be safe
		int left = Page.findPage(this).getPageSize().width - getWidth() - 30;
		if (left != getLeft()) {
			setLeft(left);
		}
		
		setTop(153);
		
		setVisible(true);
	}
	
	/**
	 * 
	 */
	public void closeProfileWindow() {
		if (loadProfileWindow != null) {
			loadProfileWindow.close();
			loadProfileWindow = null;
		}
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.dialog.CenteredWindow#close()
	 */
	@Override
	public void close() {
		closeProfileWindow();
		super.close();
	}
	
	/* (non-Javadoc)
	 * @see de.jwic.base.ControlContainer#destroy()
	 */
	@Override
	public void destroy() {
		closeProfileWindow();
		super.destroy();
	}
	
	// ************* USED IN THE VTL *************
	
	/**
	 * @return
	 */
	public boolean isDefaultConfig() {
		return tableModel.getUserConfigHandler().isDefaultConfig();
	}
	
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
