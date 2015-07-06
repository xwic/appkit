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

package de.xwic.appkit.webbase.table;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.jwic.data.IContentProvider;
import de.jwic.data.Range;
import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.dao.Limit;

/**
 * @author lippisch
 */
public class EntityContentProvider implements IContentProvider<RowData> {

	private DAO dao;
	private final EntityTableModel model;
	private int lastTotal = 0;

	/**
	 * @param dao
	 * @param model 
	 */
	public EntityContentProvider(DAO dao, EntityTableModel model) {
		super();
		this.dao = dao;
		this.model = model;
	}
	
	

	/* (non-Javadoc)
	 * @see de.jwic.ecolib.tableviewer.IContentProvider#getChildren(java.lang.Object)
	 */
	@Override
	public Iterator<RowData> getChildren(RowData object) {
		return null;
	}

	/* (non-Javadoc)
	 * @see de.jwic.ecolib.tableviewer.IContentProvider#getContentIterator(de.jwic.ecolib.tableviewer.Range)
	 */
	@Override
	public Iterator<RowData> getContentIterator(Range range) {

		Limit limit = null;
		if (range.getMax() != -1) {
			limit = new Limit();
			limit.startNo = range.getStart();
			limit.maxResults = range.getMax();
		}
		EntityList contentList = dao.getEntities(limit, model.getQuery());
		lastTotal = contentList.getTotalSize();
		
		List<RowData> rd = new ArrayList<RowData>();
		for (Object o : contentList) {
			rd.add(new RowData(model, o));
		}
		return rd.iterator();
	}

	/* (non-Javadoc)
	 * @see de.jwic.ecolib.tableviewer.IContentProvider#getTotalSize()
	 */
	@Override
	public int getTotalSize() {
		return lastTotal;
	}

	/* (non-Javadoc)
	 * @see de.jwic.ecolib.tableviewer.IContentProvider#getUniqueKey(java.lang.Object)
	 */
	@Override
	public String getUniqueKey(RowData object) {
		return object.getUniqueKey();
	}

	/* (non-Javadoc)
	 * @see de.jwic.ecolib.tableviewer.IContentProvider#hasChildren(java.lang.Object)
	 */
	@Override
	public boolean hasChildren(RowData object) {
		return false;
	}



	@Override
	public RowData getObjectFromKey(String uniqueKey) {
		return null;
	}

}
