/**
 * 
 */
package de.xwic.appkit.webbase.entityviewer.config;

import java.util.ArrayList;
import java.util.List;

import de.jwic.base.Event;
import de.jwic.base.IControlContainer;
import de.jwic.controls.ScrollableContainer;
import de.jwic.controls.TabControl;
import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.model.daos.IMitarbeiterDAO;
import de.xwic.appkit.core.model.daos.IUserViewConfigurationDAO;
import de.xwic.appkit.core.model.entities.IUserViewConfiguration;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.webbase.table.EntityTableModel;

/**
 * @author Adrian Ionescu
 */
public class UserConfigurationsTab extends TabControl {

	private EntityTableModel tableModel;
	private boolean sharedConfigsView;
	
	private List<UserViewConfigurationControl> configControls;
	private ScrollableContainer configControlsContainer;
	
	private UserViewConfigurationControlAdapter listener;

	/**
	 * @param container
	 * @param name
	 * @param sharedConfigsView
	 */
	@SuppressWarnings("unchecked")
	public UserConfigurationsTab(IControlContainer container, String name, final EntityTableModel tableModel, boolean sharedConfigsView) {
		super(container, name);
		
		configControls = new ArrayList<UserViewConfigurationControl>();
		
		this.tableModel = tableModel;
		this.sharedConfigsView = sharedConfigsView;
		
		listener = new UserViewConfigurationControlAdapter(){
			@Override
			public void onConfigDeleted(Event event) {
				UserViewConfigurationControl ctrl = (UserViewConfigurationControl) event.getEventSource();
				tableModel.deleteUserViewConfiguration(ctrl.getUserViewConfiguration());
				configControls.remove(ctrl);
				requireRedraw();

				// if the current config was deleted, apply the next one.. if no other exist, reset to the default list
				if (ctrl.isCurrentConfig()) {
					if (configControls.size() > 0) {
						configControls.get(0).actionApply();
					} else {
						try {
							tableModel.initModel(true);
						} catch (ConfigurationException e) {
							throw new RuntimeException("Can not create EntityTable: " + e, e);
						}
					}
				}
			}
		};
		
		configControlsContainer = new ScrollableContainer(this, "configControls");
		configControlsContainer.setTemplateName(getClass().getName() + "_config_controls");
		configControlsContainer.setHeight("217px");
		
		PropertyQuery pq = new PropertyQuery();
		pq.addEquals("className", tableModel.getEntityClass().getName());
		pq.addEquals("viewId", tableModel.getViewId());
		pq.setSortField("name");
		pq.setSortDirection(PropertyQuery.SORT_DIRECTION_UP);
		if (sharedConfigsView) {
			pq.addNotEquals("owner", ((IMitarbeiterDAO) DAOSystem.getDAO(IMitarbeiterDAO.class)).getByCurrentUser());
			pq.addEquals("public", true);
		} else {
			pq.addEquals("owner", ((IMitarbeiterDAO) DAOSystem.getDAO(IMitarbeiterDAO.class)).getByCurrentUser());			
		}
		
		List<IUserViewConfiguration> list = DAOSystem.getDAO(IUserViewConfigurationDAO.class).getEntities(null, pq);
		for (IUserViewConfiguration config : list) {
			createUserConfigControl(config);
		}
	}

	/**
	 * @param userConfig
	 * @return
	 */
	public UserViewConfigurationControl createUserConfigControl(IUserViewConfiguration userConfig) {
		UserViewConfigurationControl ctrl = new UserViewConfigurationControl(configControlsContainer, userConfig, tableModel, userConfig.getId() == 0, sharedConfigsView);
		ctrl.addListener(listener);
		configControls.add(0, ctrl);
		
		requireRedraw();
		
		return ctrl;
	}
	
	/**
	 * @return
	 */
	public List<UserViewConfigurationControl> getConfigControls() {
		return configControls;
	}
	
	/**
	 * Used in the VTL
	 * @return
	 */
	public boolean isSharedConfigsView() {
		return sharedConfigsView;
	}
}
