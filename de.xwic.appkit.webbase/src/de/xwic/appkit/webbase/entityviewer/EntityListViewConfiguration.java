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
package de.xwic.appkit.webbase.entityviewer;

import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.webbase.entityviewer.quickfilter.IQuickFilterPanelCreator;
import de.xwic.appkit.webbase.table.EntityTableConfiguration;

/**
 * @author Adrian Ionescu
 */
public class EntityListViewConfiguration extends EntityTableConfiguration {

	private IEntity baseEntity = null;
	private IQuickFilterPanelCreator quickFilterPanelCreator; 
	
	/**
	 * 
	 */
	public EntityListViewConfiguration(Class<? extends IEntity> entityClass) {
		super(entityClass);
	}
	
	/**
	 * @return the baseEntity
	 */
	public IEntity getBaseEntity() {
		return baseEntity;
	}
	/**
	 * @param baseEntity the baseEntity to set
	 */
	public void setBaseEntity(IEntity baseEntity) {
		this.baseEntity = baseEntity;
	}

	/**
	 * @return the quickFilterPanelCreator
	 */
	public IQuickFilterPanelCreator getQuickFilterPanelCreator() {
		return quickFilterPanelCreator;
	}

	/**
	 * @param quickFilterPanelCreator the quickFilterPanelCreator to set
	 */
	public void setQuickFilterPanelCreator(IQuickFilterPanelCreator quickFilterPanelCreator) {
		this.quickFilterPanelCreator = quickFilterPanelCreator;
	}
}
