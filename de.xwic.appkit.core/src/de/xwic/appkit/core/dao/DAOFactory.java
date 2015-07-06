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
/*
 * de.xwic.appkit.core.dao.DAOFactory
 * Created on 07.07.2005
 *
 */
package de.xwic.appkit.core.dao;

import java.util.Map;

/**
 * @author Florian Lippisch
 */
public interface DAOFactory {

	/**
	 * Returns the DAO implementation for the specified DAO interface (class).
	 * If no implementation is found, <code>null</code> is returned.
	 * @param daoClass
	 * @return DAO or null
	 */
	public DAO getDAO(Class<? extends DAO> daoClass);

	/**
	 * Find a DAO that handles the specified entityClass.
	 * @param classname
	 * @return
	 */
	public DAO findDAOforEntity(String classname);
	
	/**
	 * @return the map containing all registered daos.
	 */
	public Map<Class<? extends DAO>, DAO> getDAOMap();
	
	/**
	 * Register an additional DAO to the System. <p>
	 * 
	 * @param daoClass The specific DAO Interface class
	 * @param daoImplementation The implementation of the specific DAO Interface
	 */
	public <D extends DAO<?>> void registerDao(Class<D> daoClass, D daoImplementation);
	
	/**
	 * Register an additional Query to the System. <p>
	 * 
	 * @param queryClass  the class of the Query
	 * @param resolver The Resolver for the query
	 */
	public void registerQuery(Class<? extends EntityQuery> queryClass, IEntityQueryResolver resolver);

}
