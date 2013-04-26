/*
 * Copyright (c) 2009 Network Appliance, Inc.
 * All rights reserved.
 */

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
