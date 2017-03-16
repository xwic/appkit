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

import java.lang.reflect.InvocationTargetException;

import de.jwic.base.IControl;
import de.jwic.base.IControlContainer;
import de.jwic.base.Page;
import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.config.editor.EListView;
import de.xwic.appkit.core.config.model.Property;
import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.webbase.actions.IEntityCreator;
import de.xwic.appkit.webbase.editors.IBuilderContext;
import de.xwic.appkit.webbase.editors.mappers.ListViewMapper;
import de.xwic.appkit.webbase.entityviewer.EntityListView;
import de.xwic.appkit.webbase.entityviewer.EntityListViewConfiguration;

/**
 * Creates a ListView, typically in a sub-tab below an editor.
 * 
 */
public class EListViewBuilder extends Builder<EListView> {


	private class ChildEntityCreator implements IEntityCreator {
		private DAO<IEntity> dao;
		private EntityListViewConfiguration listCfg;
		private String targetPropertyName;
		
		/**
		 * @param dao
		 * @param listCfg
		 * @param targetPropertyName
		 */
		public ChildEntityCreator(DAO<IEntity> dao, EntityListViewConfiguration listCfg, String targetPropertyName) {
			super();
			this.dao = dao;
			this.listCfg = listCfg;
			this.targetPropertyName = targetPropertyName;
		}

		/* (non-Javadoc)
		 * @see de.xwic.appkit.webbase.actions.IEntityCreator#createEntity()
		 */
		@Override
		public IEntity createEntity() {
			IEntity newEntity = dao.createEntity();
			IEntity baseEntity = listCfg.getBaseEntity();
			if (baseEntity != null) {
				Property tgProp = dao.getEntityDescriptor().getProperty(targetPropertyName);
				if (tgProp != null) {
					try {
						tgProp.getDescriptor().getWriteMethod().invoke(newEntity, baseEntity);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						log.error("Error writing base entity to target property: " + targetPropertyName);
					}
				} else {
					log.warn("No such property: " + targetPropertyName);
				}
			}
			return newEntity;
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public IControl buildComponents(EListView element, IControlContainer parent, IBuilderContext context) {
		try {
			Class<? extends IEntity> entityClass = (Class<? extends IEntity>) Class.forName(element.getType());
			EntityListViewConfiguration entityListViewConfiguration = new EntityListViewConfiguration(entityClass);
			
			if (element.getListProfile() != null && !element.getListProfile().isEmpty()) {
				entityListViewConfiguration.setListId(element.getListProfile());
			}
			
			// set a base filter here, which will later be manipulated in the mapper
			PropertyQuery query = new PropertyQuery();
			if (element.getFilterOn() != null && !element.getFilterOn().isEmpty()) {
				query.addEquals(element.getFilterOn(), -1);
			}
			
			entityListViewConfiguration.setBaseFilter(query);
			
			EntityListView listView = new EntityListView(parent, entityListViewConfiguration);
			Page page = Page.findPage((IControl)parent);
			listView.setHeightDecrease(page.getPageSize().getHeight() / 2);
			context.registerField(null, listView, element, ListViewMapper.MAPPER_ID);

			if (element.getFilterOn() != null && !element.getFilterOn().isEmpty()) {
				listView.setEntityCreator(new ChildEntityCreator(DAOSystem.findDAOforEntity(element.getType()), entityListViewConfiguration, element.getFilterOn()));
			}
			
			return listView;
			
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(), e);
			return null;
		} catch (ConfigurationException e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}
}
