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
/**
 * 
 */
package de.xwic.appkit.webbase.actions.editors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.config.ConfigurationManager;
import de.xwic.appkit.core.config.Profile;
import de.xwic.appkit.core.config.editor.EditorConfiguration;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.registry.ExtensionRegistry;
import de.xwic.appkit.core.registry.IExtension;
import de.xwic.appkit.webbase.editors.EntityEditorCreator;
import de.xwic.appkit.webbase.toolkit.app.Site;
import de.xwic.appkit.webbase.toolkit.app.SiteProvider;
import de.xwic.appkit.webbase.toolkit.editor.EditorModel;

/**
 * Utility class to help opening editors through the Extensions system
 * 
 * @author Adrian Ionescu
 */
public class EditorHelper {

	private final static Log log = LogFactory.getLog(EditorHelper.class);
	
	public final static String EXTENSION_POINT_EDITORS = "entityEditors";
	
	// it's safe to cache the editor creators, cause we can't modify the
	// existing ones without a restart of the app
	private Map<String, IEntityEditorCreator> editorCreatorsCache;
	
	private static EditorHelper instance;

	/**
	 * 
	 */
	private EditorHelper() {
		editorCreatorsCache = new HashMap<String, IEntityEditorCreator>();		
	}

	/**
	 * @return
	 */
	public static EditorHelper getInstance() {
		if (instance == null) {
			instance = new EditorHelper();
		}
		
		return instance;
	}
	
	/**
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public static EditorModel openEditor(IEntity entity) throws Exception {
		return openEditor(entity, null);
	}
	
	/**
	 * @param site
	 * @param entity
	 * @param baseEntity
	 * @return
	 * @throws Exception
	 */
	public static EditorModel openEditor(IEntity entity, IEntity baseEntity) throws Exception {
		Site site = SiteProvider.getSite();
		if (site == null) {
			throw new IllegalStateException("The Site is not available.");
		}
		return openEditor(site, entity, baseEntity);
	}
		
	/**
	 * @param site
	 * @param entity
	 * @param baseEntity
	 * @return
	 * @throws Exception
	 */
	public static EditorModel openEditor(Site site, IEntity entity, IEntity baseEntity) throws Exception {
		return getInstance()._openEditor(site, entity, baseEntity, false);		
	}
	
	/**
	 * @param site
	 * @param entity
	 * @param baseEntity
	 * @return
	 * @throws Exception
	 */
	public static EditorModel openEditor(Site site, IEntity entity, IEntity baseEntity, boolean readOnly) throws Exception {
		return getInstance()._openEditor(site, entity, baseEntity, readOnly);		
	}

	/**
	 * INTERNAL method to handle opening the editor.
	 * @param site
	 * @param entity
	 * @param baseEntity
	 * @return
	 * @throws Exception
	 */
	private EditorModel _openEditor(Site site, IEntity entity, IEntity baseEntity, boolean readOnly) throws Exception {
		
		String entityName = entity.type().getName();

		IEntityEditorCreator creator = null;

		if (instance.editorCreatorsCache.containsKey(entityName)) {
			creator = instance.editorCreatorsCache.get(entityName);
		} else {
			List<IExtension> allEditorExtensions = ExtensionRegistry.getInstance().getExtensions(EXTENSION_POINT_EDITORS);

			IExtension editorExt = null;

			for (IExtension ext : allEditorExtensions) {
				if (entityName.equals(ext.getAttribute("editedEntity"))) {
					if (editorExt == null || editorExt.getPriority() < ext.getPriority()) {
						editorExt = ext;
					}
				}
			}

			if (editorExt != null) {
				creator = (IEntityEditorCreator) editorExt.createExtensionObject();
			
				if (creator == null) {
					log.debug("The EntityEditorCreator is null for " + entityName);
				}
			}
			
			if (creator == null) {
				Profile profile = ConfigurationManager.getUserProfile();
				try {
					// just read the configuration. If it does not exist, a ConfigurationException 
					// is thrown. We do not "remember" the configuration, as that way we do not
					// get the "latest" config in case the config was reloaded..
					profile.getEditorConfiguration(entityName);
					creator = new EntityEditorCreator();
				} catch (ConfigurationException  ce) {
					// no configuration available either.
					log.debug("There is no editor configuration for entity " + entityName);
				}
			}
			
			if (creator == null) {
				throw new ConfigurationException("Cannot find an editor or editor configuration for entity " + entityName);
			}
			
			
			synchronized (instance.editorCreatorsCache) {
				instance.editorCreatorsCache.put(entityName, creator);
			}
		}

		EditorModel editModel = creator.createAndOpenEditor(site, entity, baseEntity);
		if (readOnly) {
			editModel.setEditMode(false);
		}
		return editModel;
	}
}