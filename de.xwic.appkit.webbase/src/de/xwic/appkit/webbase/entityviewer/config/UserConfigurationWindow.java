/**
 * 
 */
package de.xwic.appkit.webbase.entityviewer.config;

import java.util.ArrayList;
import java.util.List;

import de.jwic.base.ControlContainer;
import de.jwic.base.Event;
import de.jwic.base.Page;
import de.xwic.appkit.core.model.entities.IUserViewConfiguration;
import de.xwic.appkit.webbase.dialog.AbstractDialogWindow;
import de.xwic.appkit.webbase.dialog.DialogContent;
import de.xwic.appkit.webbase.table.EntityTableModel;
import de.xwic.appkit.webbase.toolkit.app.Site;
import de.xwic.appkit.webbase.toolkit.components.TabStripControlFix;

/**
 * @author Adrian Ionescu
 */
public class UserConfigurationWindow extends AbstractDialogWindow {

	private EntityTableModel tableModel;
	private TabStripControlFix tabPanel;
	private UserConfigurationsTab tabOwn;
	private UserConfigurationsTab tabShared;

	private List<IUserConfigurationWindowListener> listeners;
	private UserViewConfigurationControlAdapter controlListener;	
	
	/**
	 * @param container
	 */
	public UserConfigurationWindow(Site site, final EntityTableModel tableModel) {
		super(site);
		
		listeners = new ArrayList<IUserConfigurationWindowListener>();
		this.tableModel = tableModel;
		
		setTemplateName(getClass().getName());
		setWidth(520);
		setHeight(350);
		setCssClass("j-combo-content");
		setModal(false);
		
		controlListener = new UserViewConfigurationControlAdapter() {
			@Override
			public void onConfigApplied(Event event) {
				UserViewConfigurationControl ctrl = (UserViewConfigurationControl) event.getEventSource();
				tableModel.applyUserViewConfiguration(ctrl.getUserViewConfiguration().getId());
			}
			
			@Override
			public void onPublicConfigCopied(Event event) {
				UserViewConfigurationControl ctrl = (UserViewConfigurationControl) event.getEventSource();
				copySharedConfig(ctrl.getUserViewConfiguration().getId());
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
		
		tabPanel = new TabStripControlFix(container, "tabPanel");

		tabOwn = new UserConfigurationsTab(tabPanel, "tabOwn", tableModel, false);
		tabOwn.setTitle("&nbsp;&nbsp;&nbsp;Own Profiles&nbsp;&nbsp;&nbsp;");
		for (UserViewConfigurationControl ctrl : tabOwn.getConfigControls()) {
			ctrl.addListener(controlListener);			
		}
		
		tabShared = new UserConfigurationsTab(tabPanel, "tabShared", tableModel, true);
		tabShared.setTitle("&nbsp;&nbsp;&nbsp;Public Profiles&nbsp;&nbsp;&nbsp;");
		for (UserViewConfigurationControl ctrl : tabShared.getConfigControls()) {
			ctrl.addListener(controlListener);			
		}
		
		tabPanel.setActiveTabName(tableModel.isCurrentConfigurationMine() ? tabOwn.getName() : tabShared.getName());
	}
	
	/**
	 * 
	 */
	public void actionDuplicate() {
		fireUserConfigurationWindowEvent();
		addNewConfigControl(tableModel.cloneUserViewConfiguration(tableModel.getCurrentUserConfigurationId()));
	}
	
	/**
	 * 
	 */
	public void actionCreate() {
		addNewConfigControl(tableModel.createNewUserViewConfiguration(true));
		requireRedraw();
	}
	
	/**
	 * 
	 */
	private void copySharedConfig(int id) {
		UserViewConfigurationControl ctrl = addNewConfigControl(tableModel.cloneUserViewConfiguration(id));
		ctrl.actionUpdate();
		ctrl.actionApply();
	}
	
	/**
	 * 
	 */
	private UserViewConfigurationControl addNewConfigControl(IUserViewConfiguration config) {
		UserViewConfigurationControl ctrl = tabOwn.createUserConfigControl(config);
		ctrl.addListener(controlListener);
		
		if (!tabOwn.getName().equals(tabPanel.getActiveTabName())) {
			tabPanel.setActiveTabName(tabOwn.getName());
		}
		
		return ctrl;
	}
	
	/**
	 * @param listener
	 */
	public void addDuplicateProfileListener(IUserConfigurationWindowListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}
	
	/**
	 * @param listener
	 */
	public void removeDuplicateProfileListener(IUserConfigurationWindowListener listener) {
		if (!listeners.contains(listener)) {
			listeners.remove(listener);
		}
	}
	
	/**
	 * 
	 */
	private void fireUserConfigurationWindowEvent() {
		for (IUserConfigurationWindowListener l : listeners) {
			l.beforeDuplicateProfile(new Event(this));
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
	 * Used in the VTL
	 * @return
	 */
	public boolean hasCurrentConfig() {
		return tableModel.getCurrentUserConfigurationId() > 0;
	}
}
