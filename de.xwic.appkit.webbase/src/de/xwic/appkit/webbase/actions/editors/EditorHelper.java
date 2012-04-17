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