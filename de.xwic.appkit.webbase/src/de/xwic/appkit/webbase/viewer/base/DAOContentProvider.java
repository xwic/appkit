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
package de.xwic.appkit.webbase.viewer.base;

import java.util.Iterator;

import de.jwic.data.IContentProvider;
import de.jwic.data.Range;
import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.dao.Limit;

/**
 * @author Florian Lippisch
 *
 */
@SuppressWarnings("unchecked")
public class DAOContentProvider implements IContentProvider {

	private DAO dao = null;
	private EntityQuery entityQuery = null;
	private EntityList contentList = null;
	
	private int lastTotal = 0;
	
	/**
	 * Constructor.
	 * @param dao
	 */
	public DAOContentProvider(DAO dao) {
		this.dao = dao;
		if (dao == null) {
			throw new NullPointerException("Must specify DAO");
		}
	}
	
	/* (non-Javadoc)
	 * @see de.jwic.ecolib.tableviewer.IContentProvider#getContentIterator(de.jwic.ecolib.tableviewer.Range)
	 */
	public Iterator<?> getContentIterator(Range range) {
		
		Limit limit = null;
		if (range.getMax() != -1) {
			limit = new Limit();
			limit.startNo = range.getStart();
			limit.maxResults = range.getMax();
		}
		contentList = dao.getEntities(limit, entityQuery);
		lastTotal = contentList.getTotalSize();
		return contentList.iterator();
	}

	/* (non-Javadoc)
	 * @see de.jwic.ecolib.tableviewer.IContentProvider#getTotalSize()
	 */
	public int getTotalSize() {
		return lastTotal;
	}

	/* (non-Javadoc)
	 * @see de.jwic.ecolib.tableviewer.IContentProvider#getUniqueKey(java.lang.Object)
	 */
	public String getUniqueKey(Object object) {
		IEntity entity = (IEntity)object;
		if (entity == null) {
			return "";
		}
		return Long.toString(entity.getId());
	}

	/**
	 * @return the entityQuery
	 */
	public EntityQuery getEntityQuery() {
		return entityQuery;
	}

	/**
	 * @param entityQuery the entityQuery to set
	 */
	public void setEntityQuery(EntityQuery entityQuery) {
		this.entityQuery = entityQuery;
	}

	/**
	 * removes given entity from the content list
	 * @param entity
	 */
	public void removeElement(IEntity entity) {
		contentList.remove(entity);
	}
	
	/**
	 * adds given entity to the content list
	 * @param entity
	 */
	public void addElement(IEntity entity) {
		contentList.add(entity);
	}
	
	public Iterator<?> getChildren(Object object) {
		return null;
	}

	public boolean hasChildren(Object object) {
		return false;
	}

	@Override
	public Object getObjectFromKey(String uniqueKey) {		
		return null;
	}

}
