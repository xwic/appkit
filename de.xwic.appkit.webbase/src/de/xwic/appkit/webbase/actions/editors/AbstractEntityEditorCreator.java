/**
 * 
 */
package de.xwic.appkit.webbase.actions.editors;

import de.jwic.base.IControl;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.webbase.toolkit.app.Site;
import de.xwic.appkit.webbase.toolkit.editor.EditorModel;

/**
 * @author Adrian Ionescu
 */
public abstract class AbstractEntityEditorCreator implements IEntityEditorCreator {

	/**
	 * @return
	 */
	protected abstract IControl getEditorPage(Site site, EditorModel editorModel);

	/**
	 * To be overidden for entities which need custom editor models
	 * @param entity
	 * @param baseEntity
	 * @return
	 */
	protected EditorModel getEditorModel(IEntity entity, IEntity baseEntity) {
		EditorModel editorModel = new EditorModel(entity);
		if (baseEntity != null) {
			editorModel.setBaseEntity(baseEntity);
		}
		
		return editorModel;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.netapp.pulse.basegui.editors.IEntityEditorCreator#createAndOpenEditor(de.xwic.appkit.webbase.toolkit.app.Site, de.xwic.appkit.core.dao.IEntity, de.xwic.appkit.core.dao.IEntity)
	 */
	@Override
	public EditorModel createAndOpenEditor(Site site, IEntity entity, IEntity baseEntity) {
		EditorModel editorModel = getEditorModel(entity, baseEntity);
		
		IControl page = getEditorPage(site, editorModel);
		
		site.pushPage(page);
		
		return editorModel;
	}
}
