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
package de.xwic.appkit.webbase.history.wizard;

import java.util.Iterator;
import java.util.List;

import de.jwic.data.IContentProvider;
import de.jwic.data.Range;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.webbase.history.HistorySelectionModel;


/**
 * 
 *
 * @author Aron Cotrau
 */
public class HistoryContentProvider implements IContentProvider {

	private HistorySelectionModel model = null;
	
	/**
	 * @param model
	 */
	public HistoryContentProvider(HistorySelectionModel model) {
		this.model = model;
	}
	
	/* (non-Javadoc)
	 * @see de.jwic.ecolib.tableviewer.IContentProvider#getChildren(java.lang.Object)
	 */
	public Iterator<?> getChildren(Object object) {
		return null;
	}

	/* (non-Javadoc)
	 * @see de.jwic.ecolib.tableviewer.IContentProvider#getContentIterator(de.jwic.ecolib.tableviewer.Range)
	 */
	public Iterator<?> getContentIterator(Range range) {
		List<IEntity> entities = model.getEntityRelatedHistoryObjects(model.getBaseEntity());
		return entities.iterator();
	}

	/* (non-Javadoc)
	 * @see de.jwic.ecolib.tableviewer.IContentProvider#getTotalSize()
	 */
	public int getTotalSize() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see de.jwic.ecolib.tableviewer.IContentProvider#getUniqueKey(java.lang.Object)
	 */
	public String getUniqueKey(Object object) {
		if (object instanceof IEntity) {
			return Long.toString(((IEntity) object).getId());
		}
		
		return Integer.toString(object.hashCode());
	}

	/* (non-Javadoc)
	 * @see de.jwic.ecolib.tableviewer.IContentProvider#hasChildren(java.lang.Object)
	 */
	public boolean hasChildren(Object object) {
		return false;
	}

	/* (non-Javadoc)
	 * @see de.jwic.data.IContentProvider#getObjectFromKey(java.lang.String)
	 */
	@Override
	public Object getObjectFromKey(String uniqueKey) {
		return null;
	}

}
