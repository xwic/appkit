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
package de.xwic.appkit.webbase.toolkit.components;

import de.xwic.appkit.core.dao.IEntity;

/**
 * Listboxcontrol of entities.
 *
 * Created on 29.02.2008
 * @author Ronny Pfretzschner
 */
public interface IEntityListBoxControl<E extends IEntity> {


	/**
	 * select the item corresponding to the given entity
	 *
	 * @param entity
	 */
	public void selectEntry(E entity);

	/**
	 * @return the Entity of the selected entry, can be null if nothing is selected
	 */
	public E getSelectedEntry();

}
