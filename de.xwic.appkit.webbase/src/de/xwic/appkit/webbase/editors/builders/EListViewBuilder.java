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
package de.xwic.appkit.webbase.editors.builders;

import de.jwic.base.IControl;
import de.jwic.base.IControlContainer;
import de.jwic.controls.InputBox;
import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.config.editor.EListView;
import de.xwic.appkit.core.config.editor.EText;
import de.xwic.appkit.core.config.editor.Style;
import de.xwic.appkit.core.config.model.Property;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.webbase.editors.FieldChangeListener;
import de.xwic.appkit.webbase.editors.IBuilderContext;
import de.xwic.appkit.webbase.editors.mappers.InputboxMapper;
import de.xwic.appkit.webbase.entityviewer.EntityListView;
import de.xwic.appkit.webbase.entityviewer.EntityListViewConfiguration;

/**
 * Defines the InputBox builder class.
 * 
 * @author Aron Cotrau
 */
public class EListViewBuilder extends Builder<EListView> {


	@Override
	public IControl buildComponents(EListView element, IControlContainer parent, IBuilderContext context) {
		try {
			Class<? extends IEntity> entityClass = (Class<? extends IEntity>) Class.forName(element.getType());
			EntityListViewConfiguration entityListViewConfiguration = new EntityListViewConfiguration(entityClass);
			return new EntityListView(parent, entityListViewConfiguration);
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(), e);
			return null;
		} catch (ConfigurationException e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}
}
