/**
 * 
 */
package de.xwic.appkit.webbase.entityviewer.config;

import java.util.Collections;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.daos.IUserViewConfigurationDAO;
import de.xwic.appkit.core.model.entities.IUserViewConfiguration;
import de.xwic.appkit.core.model.util.EntityUtil;
import de.xwic.appkit.core.util.Equals;
import de.xwic.appkit.webbase.core.Platform;
import de.xwic.appkit.webbase.entityviewer.config.ColumnsConfigurationDeserializer.ColumnConfigurationWrapper;
import de.xwic.appkit.webbase.prefstore.IPreferenceStore;
import de.xwic.appkit.webbase.table.Column;
import de.xwic.appkit.webbase.table.Column.Sort;
import de.xwic.appkit.webbase.table.EntityTableEvent;
import de.xwic.appkit.webbase.table.EntityTableModel;
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
	
	private boolean listenToDirtyChanged = false;
	private boolean listenToConfigUpdateCalls = false;
	
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
						
						transferDataToMainConfig(uvc, true);
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
		
		Collections.sort(model.getColumns()); // sort to apply user sorting
		model.fireEvent(EventType.COLUMN_REORDER, new EntityTableEvent(model));
		
		// the user config handler should only now start to listen to the model, because before default filters might've been set 
		// and we don't want the config to appear dirty even if it's not
		listenToDirtyChanged = true;
		
		// same thing as for above, but for the DB update of the config
		listenToConfigUpdateCalls = true;
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
	 * @param linkConfig
	 */
	private void transferDataToMainConfig(IUserViewConfiguration existentUvc, boolean linkConfig) {
		
		if (mainConfig.getId() > 0) {
			// refresh the entity, unless it's a new one
			mainConfig = (IUserViewConfiguration) userConfigDao.getEntity(mainConfig.getId());
		}
		
		//mainConfig.setPublic(currentUvc.isPublic()); not the Public flag!!
		mainConfig.setColumnsConfiguration(existentUvc.getColumnsConfiguration());
		mainConfig.setFiltersConfiguration(existentUvc.getFiltersConfiguration());
		mainConfig.setSortField(existentUvc.getSortField());
		mainConfig.setSortDirection(existentUvc.getSortDirection());
		mainConfig.setMaxRows(existentUvc.getMaxRows());
		
		if (linkConfig) {
			mainConfig.setName(existentUvc.getName());
			mainConfig.setDescription(existentUvc.getDescription());
			
			mainConfig.setRelatedConfiguration(existentUvc);
		}
		
		userConfigDao.update(mainConfig);
	}
	
	/**
	 * @param uvc
	 * @param updateDb
	 */
	private void storeCurrentData(IUserViewConfiguration uvc, boolean updateDb) {
		
		boolean changed = false;
		
		ColumnsConfigurationSerializer serializer = new ColumnsConfigurationSerializer(model);
		
		if (!Equals.equalNonNullAndTrim(serializer.getColumns(), uvc.getColumnsConfiguration())) {
			uvc.setColumnsConfiguration(serializer.getColumns());
			changed = true;
		}
		
		if (!Equals.equalNonNullAndTrim(serializer.getSortField(), uvc.getSortField())) {
			uvc.setSortField(serializer.getSortField());
			changed = true;
		}
		
		if (!Equals.equalNonNullAndTrim(serializer.getSortDirection(), uvc.getSortDirection())) {
			uvc.setSortDirection(serializer.getSortDirection());
			changed = true;
		}
		
		if (!Equals.equalNonNullAndTrim(serializer.getFilters(), uvc.getFiltersConfiguration())) {
			uvc.setFiltersConfiguration(serializer.getFilters());
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
		
		// refresh first, to get the latest version, as remote deletes also look at the entity version
		mainConfig = DAOSystem.getDAO(IUserViewConfigurationDAO.class).getEntity(mainConfig.getId());
		
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
		
		boolean originalListenToConfigUpdateCalls = listenToConfigUpdateCalls;
		boolean originalListenToDirtyChanged = listenToDirtyChanged;
		
		listenToConfigUpdateCalls = false;
		listenToDirtyChanged = false;
		
		try {	
			model.initModel(true);
			deleteMainConfig();
			
			newMaxRows = 0;
			
			createDefaultMainConfig();
			
			model.fireEvent(EventType.USER_CONFIGURATION_CHANGED, new EntityTableEvent(this));
		} catch (ConfigurationException e) {
			throw new RuntimeException("Can not create EntityTable: " + e, e);
		} finally {
			listenToConfigUpdateCalls = originalListenToConfigUpdateCalls;
			listenToDirtyChanged = originalListenToDirtyChanged;
			setConfigDirty(false); // if we reset the config, it's no longer dirty
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
				
				boolean fireEvent = false;
				if (!name.equals(mainConfig.getName())) {
					fireEvent = true;
				}
				
				mainConfig.setName(name);
				mainConfig.setDescription(description);
				
				userConfigDao.update(mainConfig);
				
				if (fireEvent) {
					// fire the event to trigger the button title change
					model.fireEvent(EventType.USER_CONFIGURATION_DIRTY_CHANGED, new EntityTableEvent(this));
				}
			}
		}
		
		return config;
	}
	
	/**
	 * 
	 */
	public void saveCurrentDataToMainConfig() {
		if (!listenToConfigUpdateCalls) {
			return;
		}
		
		mainConfig = (IUserViewConfiguration) userConfigDao.getEntity(mainConfig.getId());
		storeCurrentData(mainConfig, true);
	}
	
	/**
	 * @param undoChanges
	 */
	public void updateRelatedConfig(boolean undoChanges) {
		
		boolean originalListenToConfigUpdateCalls = listenToConfigUpdateCalls;
		
		listenToConfigUpdateCalls = false;
		
		try {		
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
			
			transferDataToMainConfig(relatedConfig, true);
			
			if (undoChanges) {
				// if we undid the changes, we need to re-apply the config
				applyConfig(mainConfig);
			} else {
				// this is in an else because the same call is executed in applyConfig(mainConfig) of the if block 
				setConfigDirty(false);
			}
		} finally {
			listenToConfigUpdateCalls = originalListenToConfigUpdateCalls;
		}
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
		
		boolean originalListenToConfigUpdateCalls = listenToConfigUpdateCalls;
		boolean originalListenToDirtyChanged = listenToDirtyChanged;
		
		listenToConfigUpdateCalls = false;
		listenToDirtyChanged = false;
		
		try {
			if (!userConfig.isMainConfiguration()) {
				
				// applying a configuration actually means copying its values to the main configuration			
				transferDataToMainConfig(userConfig, true);
				applyConfig(mainConfig);
				
			} else {
				
				// if a user config has no filters config, it means it's an old one, created before we started tracking
				// the filters. In this case we must load the view's default filters
				
				boolean hasFiltersConfig = userConfig.getFiltersConfiguration() != null;
				
				ColumnsConfigurationDeserializer deserializer = new ColumnsConfigurationDeserializer(userConfig);
				
				for (Column col : model.getColumns()) {
					ColumnConfigurationWrapper colConfig = deserializer.getColumnConfiguration(col.getId());
					
					 if (colConfig == null) {
						// AI 18-FEB-2013: no longer add new columns as visible by default
						col.setVisible(false);
						continue;
					 }
					
					col.setWidth(colConfig.size);
					col.setVisible(colConfig.visible);
					col.setColumnOrder(colConfig.index);
					
					if (col.getId().equals(deserializer.getSortField())) {
						col.setSortState(deserializer.getSortDirection());
					} else {
						col.setSortState(Sort.NONE);
					}
					
					if (hasFiltersConfig) {
						col.setFilter(colConfig.filter);
						// fire the event so that any listening quick filter controls updates themselves
						model.fireEvent(EventType.COLUMN_FILTER_CHANGE, new EntityTableEvent(model, col));
					} else {
						col.setFilter(null);
					}
				}
				
				if (!hasFiltersConfig) {
					model.applyDefaultFilter();
				//} else {
				//	model.setCustomQuickFilter(deserializer.getCustomQuickFilter());
				}
				
				newMaxRows = userConfig.getMaxRows();
				
				model.buildQuery();
				
				model.fireEvent(EventType.USER_CONFIGURATION_CHANGED, new EntityTableEvent(this));
			}
		} finally {
			listenToConfigUpdateCalls = originalListenToConfigUpdateCalls;
			listenToDirtyChanged = originalListenToDirtyChanged;
			setConfigDirty(false); // since a new config has been applied, the dirty is false
		}
	}
	
	/**
	 * @param userConfig
	 */
	public void applyPublicConfig(IUserViewConfiguration userConfig) {
		transferDataToMainConfig(userConfig, false);
		applyConfig(mainConfig);
		
		// a public config must always set the dirty flag
		setConfigDirty(true);
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
			
			if (mainConfig.getFiltersConfiguration() != null && !mainConfig.getFiltersConfiguration().equals(relatedConfig.getFiltersConfiguration())) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * @param dirty
	 */
	public void setConfigDirty(boolean dirty) {
		
		if (!listenToDirtyChanged) {
			return;
		}
		
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
		return (mainConfig == null || mainConfig.getMaxRows() < 0) ? 0 : mainConfig.getMaxRows();
	}
	
	/**
	 * @param newMaxRows
	 */
	public void setNewMaxRows(int newMaxRows) {
		this.newMaxRows = newMaxRows;
		setConfigDirty(true);
		saveCurrentDataToMainConfig();
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
