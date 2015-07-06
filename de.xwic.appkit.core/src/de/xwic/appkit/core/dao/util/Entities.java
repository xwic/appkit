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
package de.xwic.appkit.core.dao.util;

import de.xwic.appkit.core.dao.IEntity;

/**
 * @author Alexandru Bledea
 * @since Oct 22, 2014
 */
public final class Entities {

	public static final int NEW_ENTITY_ID = 0;
	public static final int LOWEST_POSSIBLE_ID = 1;

	/**
	 *
	 */
	private Entities() {
	}

	/**
	 * @param entity
	 * @return
	 */
	public static int getId(final IEntity entity) {
		if (entity != null) {
			return entity.getId();
		}
		return Entities.NEW_ENTITY_ID;
	}

	/**
	 * @param entity
	 * @return
	 */
	public static Integer getIdOrNull(final IEntity entity) {
		if (entity == null) {
			return null;
		}
		return entity.getId();
	}

}
