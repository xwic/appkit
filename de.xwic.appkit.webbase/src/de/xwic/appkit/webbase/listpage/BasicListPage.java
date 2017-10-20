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
package de.xwic.appkit.webbase.listpage;

import de.jwic.base.IControlContainer;
import de.xwic.appkit.core.config.Bundle;
import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.config.ConfigurationManager;
import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.webbase.entityviewer.EntityListView;
import de.xwic.appkit.webbase.entityviewer.EntityListViewConfiguration;
import de.xwic.appkit.webbase.toolkit.app.InnerPage;

/**
 * Wrapper for a page displaying a list of entities.
 */
@SuppressWarnings("rawtypes")
public class BasicListPage extends InnerPage {

	protected EntityListView listView;

	/**
	 * @param container
	 * @param name
	 */
	public BasicListPage(IControlContainer container, String name, Class<? extends IEntity> entityType) {
		super(container, name);

		
		try {
			EntityDescriptor ed = ConfigurationManager.getSetup().getEntityDescriptor(entityType.getName());
			Bundle bundle = ed.getDomain().getBundle(getSessionContext().getLocale().getLanguage());
			setTitle(bundle.getString(entityType.getName()));
			setSubtitle("	");
		} catch (ConfigurationException e1) {
			log.error(e1);
			setTitle("Cannot resolve Type Name");
		}
		

		PropertyQuery baseQuery = new PropertyQuery();
		PropertyQuery defaultQuery = new PropertyQuery();
		
		
		EntityListViewConfiguration config = new EntityListViewConfiguration(entityType);
		config.setBaseFilter(baseQuery);
		config.setDefaultFilter(defaultQuery);
		
		try {
			listView = new EntityListView(this,config);
		} catch (ConfigurationException e) {
			throw new RuntimeException("Can not create EntityTable: " + e, e);
		}
	}

	/**
	 * @return the listView
	 */
	public EntityListView getListView() {
		return listView;
	}

}
