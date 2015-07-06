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
package de.xwic.appkit.core.dao;

/**
 * @author jbornema
 *
 */
public abstract class AbstractHistoryDAO<I extends IEntity, E extends Entity> extends AbstractDAO<I, E> {

	{
		// by default enable history handling
		setHandleHistory(true);
	}

	/**
	 * @param iClass
	 * @param eClass
	 */
	public AbstractHistoryDAO(Class<I> iClass, Class<E> eClass) {
		super(iClass, eClass);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.AbstractDAO#getHistoryImplClass()
	 */
	@Override
	public abstract Class<? extends IHistory> getHistoryImplClass();
}
