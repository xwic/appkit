/**
 * 
 */
package de.xwic.appkit.webbase.entityviewer.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.daos.IUserViewConfigurationDAO;
import de.xwic.appkit.core.model.entities.IUserViewConfiguration;
import de.xwic.appkit.webbase.core.Platform;
import de.xwic.appkit.webbase.prefstore.IPreferenceStore;
import de.xwic.appkit.webbase.table.Column;
import de.xwic.appkit.webbase.table.ColumnsConfigurationUtil;
import de.xwic.appkit.webbase.table.EntityTableEvent;
import de.xwic.appkit.webbase.table.EntityTableModel;
import de.xwic.appkit.webbase.table.Column.Sort;
import de.xwic.appkit.webbase.table.ColumnsConfigurationUtil.ColumnConfigurationWrapper;
import de.xwic.appkit.webbase.table.EntityTableModel.EventType;

/**
 * @author Adrian Ionescu
 */
public class UserConfigHandler {
	
	private final static Log log = LogFactory.getLog(UserConfigHandler.class);

	private final static String USER_CUSTOMIZED_NAME = "User Customized";
	private final static String DEFAULT_PROFILE_NAME = "Default Profile";
	
	private IUserViewConfiguration mainConfig;
	private IUserViewConfigurationDAO userConfigDao = (IUserViewConfigurationDAO) DAOSystem.getDAO(IUserViewConfigurationDAO.class);
	private boolean userConfigDirty = false;
	private int newMaxRows;
	
	private EntityTableModel model;
	
	/**
	 * @param model
	 */
	public UserConfigHandler(EntityTableModel model) {
		this.model = model;
	}
	
	/**
	 * 
	 */
	public void initUserConfig() {
		
		mainConfig = userConfigDao.getMainConfigurationForView(model.getCurrentUser(), model.getEntityClass().getName(), model.getViewId());
		
		// the preference store is no longer used for view configurations, but since we can't migrate everything at once,
		// the migration will be done one view at a time, when the user accesses it
		
		if (mainConfig == null) {
			
			// look in the preference store, maybe the user created a config for this view
			
			try {
				IPreferenceStore userConfigPrefStore = Platform.getContextPreferenceProvider().getPreferenceStore("de.xwic.appkit.webbase.UserViewConfiguration");
				int userConfigId = Integer.parseInt(userConfigPrefStore.getString(model.getViewId(), "-1"));
				if (userConfigId > 0) {
					IUserViewConfiguration uvc = (IUserViewConfiguration) userConfigDao.getEntity(userConfigId);
					if (uvc != null) {
						mainConfig = createNewConfig(DEFAULT_PROFILE_NAME);
						mainConfig.setMainConfiguration(true);
						mainConfig.setRelatedConfiguration(uvc);
						
						transferDataToMainConfig(uvc);
					}
				}
			} catch (Exception ex) {
				log.error(ex.getMessage(), ex);
			}	
			
		}
		
		// if no config exists, we create one which will be the main
		if (mainConfig == null) {
			createDefaultMainConfig();
			// no need to apply it again, as it contains the default values
		} else {
			applyConfig(mainConfig);
		}
	}
	
	/**
	 * 
	 */
	private void createDefaultMainConfig() {
		mainConfig = createNewConfig(DEFAULT_PROFILE_NAME);
		mainConfig.setMainConfiguration(true);
		mainConfig.setRelatedConfiguration(null);
		
		storeCurrentData(mainConfig, true);
	}
	
	/**
	 * @param name 
	 * @return
	 */
	private IUserViewConfiguration createNewConfig(String name) {
		IUserViewConfiguration uvc = (IUserViewConfiguration) userConfigDao.createEntity();
		
		uvc.setOwner(model.getCurrentUser());
		uvc.setClassName(model.getEntityClass().getName());
		uvc.setViewId(model.getViewId());			
		uvc.setListSetupId(model.getListSetup().getListId());
		uvc.setName(name);
		
		return uvc;
	}
	
	/**
	 * @param existentUvc
	 */
	private void transferDataToMainConfig(IUserViewConfiguration existentUvc) {
		
		mainConfig = (IUserViewConfiguration) userConfigDao.getEntity(mainConfig.getId());
		
		mainConfig.setName(existentUvc.getName());
		mainConfig.setDescription(existentUvc.getDescription());
		//newUvc.setPublic(currentUvc.isPublic()); not the Public flag!!
		mainConfig.setColumnsConfiguration(existentUvc.getColumnsConfiguration());
		mainConfig.setSortField(existentUvc.getSortField());
		mainConfig.setSortDirection(existentUvc.getSortDirection());
		mainConfig.setMaxRows(existentUvc.getMaxRows());
		
		mainConfig.setRelatedConfiguration(existentUvc);
		
		userConfigDao.update(mainConfig);
	}
	
	/**
	 * @param uvc
	 * @param updateDb
	 */
	private void storeCurrentData(IUserViewConfiguration uvc, boolean updateDb) {
		
		boolean changed = false;
		
		String sortField = "";
		String sortDirection = "";		
		StringBuilder sbColumnsConfiguration = new StringBuilder();
		
		for (Column col : model.getColumns()) {
			if (sbColumnsConfiguration.length() > 0) {
				sbColumnsConfiguration.append(";");
			}
			
			sbColumnsConfiguration.append(col.getId()).append(",");
			sbColumnsConfiguration.append(col.getWidth()).append(",");
			sbColumnsConfiguration.append(col.isVisible()).append(",");
			sbColumnsConfiguration.append(col.getColumnOrder());
			
			if (col.getSortState() != Sort.NONE) {
				sortField = col.getId();
				sortDirection = col.getSortState().name();
			}
		}
		
		String columnsConfiguration = sbColumnsConfiguration.toString();
		if (!columnsConfiguration.equals(uvc.getColumnsConfiguration())) {
			uvc.setColumnsConfiguration(columnsConfiguration);
			changed = true;
		}
		
		if (!sortField.equals(uvc.getSortField())) {
			uvc.setSortField(sortField);
			changed = true;
		}
		
		if (!sortDirection.equals(uvc.getSortDirection())) {
			uvc.setSortDirection(sortDirection);
			changed = true;
		}
		
		if (newMaxRows != uvc.getMaxRows()) {
			uvc.setMaxRows(newMaxRows);
			changed = true;
		}
		
		if (updateDb && changed) {
			userConfigDao.update(uvc);
		}
	}
	
	/**
	 * 
	 */
	private void deleteMainConfig() {
		
		if (mainConfig == null) {
			return;
		}
		
		IUserViewConfiguration related = mainConfig.getRelatedConfiguration();
		
		userConfigDao.delete(mainConfig);
		
		if (related != null) {
			userConfigDao.delete(related);
		}
	}
	
	/**
	 * 
	 */
	public void resetConfig() {
		try {							
			model.initModel(true);
			deleteMainConfig();
			
			newMaxRows = 0;
			
			createDefaultMainConfig();
			
			model.fireEvent(EventType.USER_CONFIGURATION_CHANGED, new EntityTableEvent(this));
		} catch (ConfigurationException e) {
			throw new RuntimeException("Can not create EntityTable: " + e, e);
		}
	}
	
	/**
	 * @return
	 */
	public IUserViewConfiguration createConfigWithCurrentSettings() {
		IUserViewConfiguration uvc = createNewConfig(mainConfig.getName());
		storeCurrentData(uvc, false);
		
		return uvc;		
	}
	
	/**
	 * @param userConfig 
	 * 
	 */
	public IUserViewConfiguration updateConfig(IUserViewConfiguration config, String name, String description, boolean isPublic) {
		
		if (config.getId() > 0) {
			// refresh the entity, unless it's a new one
			config = (IUserViewConfiguration) userConfigDao.getEntity(config.getId());
		}
		
		config.setName(name);
		config.setDescription(description);
		config.setPublic(isPublic);
		
		userConfigDao.update(config);
		
		if (isCurrentConfig(config)) {
			
			mainConfig = (IUserViewConfiguration) userConfigDao.getEntity(mainConfig.getId());

			// check again, maybe it was updated in the meantime (two browser windows?)
			if (mainConfig.getRelatedConfiguration() != null && mainConfig.getRelatedConfiguration().getId() == config.getId()) {
				mainConfig.setName(name);
				mainConfig.setDescription(description);
				
				userConfigDao.update(mainConfig);
				
				// fire the event to trigger the button title change
				model.fireEvent(EventType.USER_CONFIGURATION_DIRTY_CHANGED, new EntityTableEvent(this));
			}
		}
		
		return config;
	}
	
	/**
	 * 
	 */
	public void saveCurrentDataToMainConfig() {
		mainConfig = (IUserViewConfiguration) userConfigDao.getEntity(mainConfig.getId());
		storeCurrentData(mainConfig, true);
	}
	
	/**
	 * 
	 */
	public void updateRelatedConfig(boolean undoChanges) {
		
		if (mainConfig.getRelatedConfiguration() == null) {
			
			// if we're still on the default config, reset it all			
			if (undoChanges) {
				resetConfig();
			}
			
			return;
		}
			
		IUserViewConfiguration relatedConfig = (IUserViewConfiguration) userConfigDao.getEntity(mainConfig.getRelatedConfiguration().getId());
		
		if (!undoChanges) {
			storeCurrentData(relatedConfig, true);
		}
		
		transferDataToMainConfig(relatedConfig);
		
		if (undoChanges) {
			// if we undid the changes, we need to re-apply the config
			applyConfig(mainConfig);
		}
		
		setConfigDirty(false);
	}
	
	/**
	 * @param name
	 * @param currentId
	 * @return
	 */
	public boolean configNameExists(String name, int currentId) {
		return userConfigDao.configNameExists(model.getCurrentUser(), model.getEntityClass().getName(), model.getViewId(), name, currentId);
	}
	
	/**
	 * @param userConfigId
	 */
	public void deleteConfig(int id) {
		if (id > 0) {
			IEntity uvc = userConfigDao.getEntity(id);
			if (uvc != null) {
				userConfigDao.delete(uvc);
			}
		}
	}

	/**
	 * @param userConfig
	 */
	public void applyConfig(IUserViewConfiguration userConfig) {
		
		if (!userConfig.isMainConfiguration()) {
			// applying a configuration actually means copying its values to the main configuration 
			
			transferDataToMainConfig(userConfig);
			applyConfig(mainConfig);
			
		} else {
			
			ColumnsConfigurationUtil util = new ColumnsConfigurationUtil(userConfig);
			
			for (Column col : model.getColumns()) {
				ColumnConfigurationWrapper colConfig = util.getColumnConfiguration(col.getId());
				// if it's null, it means it's a new column, which did not exist
				// when the user configuration was created. We display it by
				// default, so the users see it exists, they can take it out after
				if (colConfig == null) {
					col.setVisible(true);
					continue;
				}
				
				col.setWidth(colConfig.size);
				col.setVisible(colConfig.visible);
				col.setColumnOrder(colConfig.index);
				
				if (col.getId().equals(util.getSortField())) {
					col.setSortState(util.getSortDirection());
				} else {
					col.setSortState(Sort.NONE);
				}
			}
			
			newMaxRows = userConfig.getMaxRows();
			
			model.buildQuery();
			
			model.fireEvent(EventType.USER_CONFIGURATION_CHANGED, new EntityTableEvent(this));
		}
	}
	
	/**
	 * @return
	 */
	public boolean isCurrentConfigDirty() {
		
		// if the user has changed something or this is a modified default config
		
		if (userConfigDirty || (mainConfig.getRelatedConfiguration() == null && mainConfig.getName().equals(USER_CUSTOMIZED_NAME)) ) {
			return true;
		}
		
		IUserViewConfiguration relatedConfig = mainConfig.getRelatedConfiguration();
		
		if (relatedConfig != null) {
			if (mainConfig.getMaxRows() != relatedConfig.getMaxRows()) {
				return true;
			}
			
			if (mainConfig.getSortField() != null && !mainConfig.getSortField().equals(relatedConfig.getSortField())) {
				return true;
			}
			
			if (mainConfig.getSortDirection() != null && !mainConfig.getSortDirection().equals(relatedConfig.getSortDirection())) {
				return true;
			}
			
			if (mainConfig.getColumnsConfiguration() != null && !mainConfig.getColumnsConfiguration().equals(relatedConfig.getColumnsConfiguration())) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * @param dirty
	 */
	public void setConfigDirty(boolean dirty) {
		
		userConfigDirty = dirty;
		
		if (dirty && mainConfig.getName().equals(DEFAULT_PROFILE_NAME)) {
			mainConfig.setName(USER_CUSTOMIZED_NAME);
			userConfigDao.update(mainConfig);
		}
		
		model.fireEvent(EventType.USER_CONFIGURATION_DIRTY_CHANGED, new EntityTableEvent(this));
	}

	/**
	 * @return the maxRows
	 */
	public int getMaxRows() {
		return mainConfig.getMaxRows() < 0 ? 0 : mainConfig.getMaxRows();
	}
	
	/**
	 * @param newMaxRows
	 */
	public void setNewMaxRows(int newMaxRows) {
		this.newMaxRows = newMaxRows;
		setConfigDirty(true);
	}
	
	/**
	 * @return
	 */
	public String getCurrentConfigName() {
		return mainConfig.getName();
	}
	
	/**
	 * @return
	 */
	public boolean isCurrentConfig(IUserViewConfiguration uvc) {
		int id = mainConfig.getRelatedConfiguration() != null ? mainConfig.getRelatedConfiguration().getId() : mainConfig.getId();		
		return id == uvc.getId();
	}

	/**
	 * @return
	 */
	public boolean isDefaultConfig() {
		return mainConfig.getName().equals(USER_CUSTOMIZED_NAME);
	}
}
