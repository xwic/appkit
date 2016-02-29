/*******************************************************************************
 * Copyright 2015 xWic group (http://www.xwic.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *******************************************************************************/
package de.xwic.appkit.webbase.editors;

import de.jwic.base.IControl;
import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.config.ConfigurationManager;
import de.xwic.appkit.core.config.Profile;
import de.xwic.appkit.core.config.editor.EditorConfiguration;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.webbase.actions.editors.AbstractEntityEditorCreator;
import de.xwic.appkit.webbase.toolkit.app.Site;
import de.xwic.appkit.webbase.toolkit.editor.EditorModel;

/**
 * Adapter to the old way of opening an editor via an EditorCreator.
 * @author lippisch
 */
public class EntityEditorCreator extends AbstractEntityEditorCreator {


	/**
	 * @param editorConfig
	 */
	public EntityEditorCreator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.actions.editors.AbstractEntityEditorCreator#getEditorPage(de.xwic.appkit.webbase.toolkit.app.Site,
	 * de.xwic.appkit.webbase.toolkit.editor.EditorModel)
	 */
	@Override
	protected IControl getEditorPage(Site site, EditorModel editorModel) {
		Profile profile = ConfigurationManager.getUserProfile();
		EditorConfiguration editorConfig;
		try {
			editorConfig = profile.getEditorConfiguration(editorModel.getEntity().type().getName());
			return new EntityEditorPage(site.getContentContainer(), null, editorModel.getEntity(), editorConfig);
		} catch (ConfigurationException e) {
			throw new IllegalStateException("The editor configuration somehow disappeared??", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.actions.editors.AbstractEntityEditorCreator#getEditorModel(de.xwic.appkit.core.dao.IEntity,
	 * de.xwic.appkit.core.dao.IEntity)
	 */
	@Override
	protected EditorModel getEditorModel(IEntity entity, IEntity baseEntity) {
		return new EntityEditorModel(entity);
	}

}
