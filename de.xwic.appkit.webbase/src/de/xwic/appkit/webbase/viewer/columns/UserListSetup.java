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
package de.xwic.appkit.webbase.viewer.columns;

import java.util.ArrayList;
import java.util.List;

import de.xwic.appkit.core.config.model.EntityDescriptor;

/**
 * Class for the user's list setup configuration
 * @author Aron Cotrau
 */
public class UserListSetup {
	
	private String listId;
	private String typeClass;
	private List<UserListColumn> columns = new ArrayList<UserListColumn>();
	
	private EntityDescriptor entityDescriptor = null;

	/**
	 * @return the columns
	 */
	public List<UserListColumn> getColumns() {
		return columns;
	}

	/**
	 * @param column
	 *            add the given column to the list
	 */
	public void addColumn(UserListColumn column) {
		columns.add(column);
	}

	/**
	 * @return the entityDescriptor
	 */
	public EntityDescriptor getEntityDescriptor() {
		return entityDescriptor;
	}

	/**
	 * @param entityDescriptor
	 *            the entityDescriptor to set
	 */
	public void setEntityDescriptor(EntityDescriptor entityDescriptor) {
		this.entityDescriptor = entityDescriptor;
	}

	/**
	 * @return the listId
	 */
	public String getListId() {
		return listId;
	}

	/**
	 * @param listId
	 *            the listId to set
	 */
	public void setListId(String listId) {
		this.listId = listId;
	}

	/**
	 * @return the typeClass
	 */
	public String getTypeClass() {
		return typeClass;
	}

	/**
	 * @param typeClass
	 *            the typeClass to set
	 */
	public void setTypeClass(String typeClass) {
		this.typeClass = typeClass;
	}
}
