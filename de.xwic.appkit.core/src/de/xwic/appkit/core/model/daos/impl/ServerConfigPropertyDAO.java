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

import java.util.HashMap;
import java.util.Map;

import de.xwic.appkit.core.dao.AbstractDAO;
import de.xwic.appkit.core.dao.DAOCallback;
import de.xwic.appkit.core.dao.DAOProviderAPI;
import de.xwic.appkit.core.dao.DataAccessException;
import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.dao.event.AbstractDAOWithEvent;
import de.xwic.appkit.core.dao.event.DaoEntityEvent;
import de.xwic.appkit.core.model.daos.IServerConfigPropertyDAO;
import de.xwic.appkit.core.model.entities.IServerConfigProperty;
import de.xwic.appkit.core.model.entities.impl.ServerConfigProperty;
import de.xwic.appkit.core.model.queries.ServerConfigPropertyByKeyQuery;

/**
 * DAO implementation of the Server ConfigProperty entity.
 * <p>
 *
 * @author Ronny Pfretzschner
 *
 */
public class ServerConfigPropertyDAO extends AbstractDAOWithEvent<IServerConfigProperty, ServerConfigProperty> implements IServerConfigPropertyDAO {

	private Map<String, IServerConfigProperty> allProperties = new HashMap<String, IServerConfigProperty>();

	/**
	 *
	 */
	public ServerConfigPropertyDAO() {
		super(IServerConfigProperty.class, ServerConfigProperty.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.dao.DAO#createEntity()
	 */
	@Override
	public IServerConfigProperty createEntity() throws DataAccessException {
		//ServerConfigProperties are created without returning it.
		//It is not allowed here, to return an unsaved, empty ServerConfigProperty
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.model.daos.IServerConfigPropertyDAO#setConfigProperty(java.lang.String, boolean)
	 */
	@Override
	public void setConfigProperty(String key, boolean val) {

		IServerConfigProperty prop = new ServerConfigProperty(key, Boolean.toString(val));
		updateConfigProperty(prop);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.model.daos.IServerConfigPropertyDAO#setConfigProperty(java.lang.String, int)
	 */
	@Override
	public void setConfigProperty(String key, int val) {
		IServerConfigProperty prop = new ServerConfigProperty(key, Integer.toString(val));
		updateConfigProperty(prop);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.model.daos.IServerConfigPropertyDAO#setConfigProperty(java.lang.String, double)
	 */
	@Override
	public void setConfigProperty(String key, double val) {
		IServerConfigProperty prop = new ServerConfigProperty(key, Double.toString(val));
		updateConfigProperty(prop);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.model.daos.IServerConfigPropertyDAO#setConfigProperty(java.lang.String, java.lang.String)
	 */
	@Override
	public void setConfigProperty(String key, String val) {
		IServerConfigProperty prop = new ServerConfigProperty(key, val);
		updateConfigProperty(prop);
	}

	/**
	 * Checks whether the property already exists and if so updates this property. If the property does not exist, a new property will be
	 * created.
	 *
	 * @param property
	 */
	private void updateConfigProperty(IServerConfigProperty property) {
		IServerConfigProperty existingProperty = getConfigProperty(property.getKey());
		if (existingProperty == null) {
			update(property);
		} else {
			// always reload the property from DB
			existingProperty = getConfigProperty(property.getKey(), true);
			existingProperty.setValue(property.getValue());
			update(existingProperty);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.model.daos.IServerConfigPropertyDAO#getConfigBoolean(java.lang.String)
	 */
	@Override
	public boolean getConfigBoolean(final String key) {

		//not in cache, get from db...
		IServerConfigProperty prop = getConfigProperty(key);
		if (prop == null) {
			return false;
		}

		return Boolean.valueOf(prop.getValue()).booleanValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.model.daos.IServerConfigPropertyDAO#getConfigInteger(java.lang.String)
	 */
	@Override
	public int getConfigInteger(final String key) {
		//look in cache first...
		IServerConfigProperty prop = getConfigProperty(key);
		if (prop == null) {
			return 0;
		}

		return Integer.valueOf(prop.getValue()).intValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.model.daos.IServerConfigPropertyDAO#getConfigInteger(java.lang.String)
	 */
	@Override
	public double getConfigDouble(final String key) {
		//look in cache first...
		IServerConfigProperty prop = getConfigProperty(key);
		if (prop == null) {
			return 0;
		}

		return Double.parseDouble(prop.getValue());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.model.daos.IServerConfigPropertyDAO#getConfigString(java.lang.String)
	 */
	@Override
	public String getConfigString(final String key) {

		IServerConfigProperty prop = getConfigProperty(key);
		if (prop == null) {
			return "";
		}

		return prop.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.model.daos.IServerConfigPropertyDAO#getConfigProperty(java.lang.String)
	 */
	@Override
	public IServerConfigProperty getConfigProperty(final String key, boolean ignoreCache) {
		if (!ignoreCache) {
			//look in cache first...
			IServerConfigProperty prop = allProperties.get(key);
			if (prop != null) {
				return prop;
			}
		}

		//not in cache, get from db...
		return (IServerConfigProperty) provider.execute(new DAOCallback() {

			@Override
			public Object run(DAOProviderAPI api) {
				EntityList list = api.getEntities(ServerConfigProperty.class, null, new ServerConfigPropertyByKeyQuery(key));
				if (list.size() != 0) {
					IServerConfigProperty p = (IServerConfigProperty) list.get(0);
					//register property in cache...
					allProperties.put(key, p);
					return p;
				}
				return null;
			}
		});
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.daos.IServerConfigPropertyDAO#getConfigProperty(java.lang.String)
	 */
	@Override
	public IServerConfigProperty getConfigProperty(String key) {
		return getConfigProperty(key, false);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.daos.IServerConfigPropertyDAO#dropCache()
	 */
	@Override
	public void dropCache() {
		allProperties.clear();
		fireEntityChangeEvent(new DaoEntityEvent(DaoEntityEvent.CACHE_CHANGE, null));
	}
}
