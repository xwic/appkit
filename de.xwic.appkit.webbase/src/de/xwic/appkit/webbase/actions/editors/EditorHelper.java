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

import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.registry.ExtensionRegistry;
import de.xwic.appkit.core.registry.IExtension;
import de.xwic.appkit.webbase.toolkit.app.Site;
import de.xwic.appkit.webbase.toolkit.editor.EditorModel;

/**
 * Utility class to help opening editors through the Extensions system
 * 
 * @author Adrian Ionescu
 */
public class EditorHelper {

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
	 * @param site
	 * @param entity
	 * @param baseEntity
	 * @return
	 * @throws Exception
	 */
	public EditorModel openEditor(Site site, IEntity entity, IEntity baseEntity) throws Exception {
		String entityName = entity.type().getName();

		IEntityEditorCreator creator = null;

		if (editorCreatorsCache.containsKey(entityName)) {
			creator = editorCreatorsCache.get(entityName);
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

			if (editorExt == null) {
				throw new Exception("Could not find an editor extension for " + entityName);
			}
			
			creator = (IEntityEditorCreator) editorExt.createExtensionObject();
			
			if (creator == null) {
				throw new Exception("The EntityEditorCreator is null for " + entityName);
			}
			
			editorCreatorsCache.put(entityName, creator);
		}

		EditorModel editModel = creator.createAndOpenEditor(site, entity, baseEntity);
		return editModel;
	}
}