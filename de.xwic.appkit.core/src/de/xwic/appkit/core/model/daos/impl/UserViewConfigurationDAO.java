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

package de.xwic.appkit.core.model.daos.impl;

import java.util.List;

import de.xwic.appkit.core.dao.AbstractDAO;
import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.model.daos.IUserViewConfigurationDAO;
import de.xwic.appkit.core.model.entities.IMitarbeiter;
import de.xwic.appkit.core.model.entities.IUserViewConfiguration;
import de.xwic.appkit.core.model.entities.impl.UserViewConfiguration;
import de.xwic.appkit.core.model.queries.PropertyQuery;

/**
 * @author Adrian Ionescu
 */
public class UserViewConfigurationDAO extends AbstractDAO<IUserViewConfiguration, UserViewConfiguration> implements IUserViewConfigurationDAO {

	/**
	 *
	 */
	public UserViewConfigurationDAO() {
		super(IUserViewConfiguration.class, UserViewConfiguration.class);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.daos.IUserViewConfigurationDAO#getMainConfigurationForView(de.xwic.appkit.core.model.entities.IMitarbeiter, java.lang.String, java.lang.String)
	 */
	@Override
	public IUserViewConfiguration getMainConfigurationForView(IMitarbeiter owner, String entityClassName, String viewId) {
		PropertyQuery pq = new PropertyQuery();

		pq.addEquals("owner", owner);
		pq.addEquals("className", entityClassName);
		pq.addEquals("viewId", viewId);
		pq.addEquals("mainConfiguration", true);

		EntityList list = getEntities(null, pq);

		return list.size() > 0 ? (IUserViewConfiguration) list.get(0) : null;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.daos.IUserViewConfigurationDAO#getUserConfigurationsForView(de.xwic.appkit.core.model.entities.IMitarbeiter, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<IUserViewConfiguration> getUserConfigurationsForView(IMitarbeiter owner, String entityClassName, String viewId) {
		PropertyQuery pq = new PropertyQuery();

		pq.addEquals("owner", owner);
		pq.addEquals("className", entityClassName);
		pq.addEquals("viewId", viewId);

		pq.setSortField("name");
		pq.setSortDirection(PropertyQuery.SORT_DIRECTION_UP);

		return getEntities(null, pq);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.daos.IUserViewConfigurationDAO#getPublicUserConfigurationsForView(de.xwic.appkit.core.model.entities.IMitarbeiter, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<IUserViewConfiguration> getPublicUserConfigurationsForView(IMitarbeiter currentUser, String entityClassName, String viewId) {
		PropertyQuery pq = new PropertyQuery();

		pq.addNotEquals("owner", currentUser);
		pq.addEquals("className", entityClassName);
		pq.addEquals("viewId", viewId);
		pq.addEquals("public", true);
		pq.addEquals("mainConfiguration", false); // the main config should never be public, but just in case

		pq.setSortField("name");
		pq.setSortDirection(PropertyQuery.SORT_DIRECTION_UP);

		return getEntities(null, pq);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.daos.IUserViewConfigurationDAO#configNameExists(de.xwic.appkit.core.model.entities.IMitarbeiter, java.lang.String, java.lang.String, java.lang.String, int)
	 */
	@Override
	public boolean configNameExists(IMitarbeiter owner, String entityClassName, String viewId, String name, long currentId) {
		PropertyQuery pq = new PropertyQuery();

		pq.addEquals("owner", owner);
		pq.addEquals("className", entityClassName);
		pq.addEquals("viewId", viewId);
		pq.addEquals("name", name);
		pq.addNotEquals("id", currentId);
		pq.addNotEquals("mainConfiguration", true);

		return !getEntities(null, pq).isEmpty();
	}
}
