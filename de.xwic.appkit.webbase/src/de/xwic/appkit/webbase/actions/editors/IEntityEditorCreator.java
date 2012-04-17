/**
 * 
 */
package de.xwic.appkit.webbase.actions.editors;

import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.webbase.toolkit.app.Site;
import de.xwic.appkit.webbase.toolkit.editor.EditorModel;

/**
 * @author Adrian Ionescu
 */
public interface IEntityEditorCreator {

	/**
	 * @param site
	 * @param entity
	 * @param baseEntity
	 * @return
	 */
	public EditorModel createAndOpenEditor(Site site, IEntity entity, IEntity baseEntity);
}
