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
		EditorModel editorModel = createEditorModel(entity);
		if (baseEntity != null) {
			editorModel.setBaseEntity(baseEntity);
		}
		
		return editorModel;
	}

	/**
	 * @param entity
	 * @return
	 */
	protected EditorModel createEditorModel(IEntity entity) {
		return new EditorModel(entity);
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
