/*
 * Copyright (c) 2009 Network Appliance, Inc.
 * All rights reserved.
 */

package de.xwic.appkit.webbase.toolkit.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import de.jwic.data.IContentProvider;
import de.jwic.data.Range;
import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.dao.Limit;
import de.xwic.appkit.core.model.queries.PropertyQuery;

/**
 * @author lippisch
 */
public class SimpleEntityContentProvider<E extends IEntity> implements IContentProvider<E> {

	private DAO dao;
	private PropertyQuery query = null;
	private Comparator<E> customComparator = null;
	
	private int lastTotalSize = 0;
	
	/**
	 * @param dao
	 */
	public SimpleEntityContentProvider(DAO dao) {
		super();
		this.dao = dao;
	}

	/**
	 * @param dao
	 * @param query
	 */
	public SimpleEntityContentProvider(DAO dao, PropertyQuery query) {
		super();
		this.dao = dao;
		this.query = query;
	}

	/* (non-Javadoc)
	 * @see de.jwic.data.IContentProvider#getChildren(java.lang.Object)
	 */
	@Override
	public Iterator<E> getChildren(E object) {
		return null;
	}

	/* (non-Javadoc)
	 * @see de.jwic.data.IContentProvider#getContentIterator(de.jwic.base.Range)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Iterator<E> getContentIterator(Range range) {
		
		Limit limit = new Limit(range.getStart(), range.getMax());
		List<E> result;
		if (query != null) {
			result = dao.getEntities(limit, query);
		} else {
			result = dao.getEntities(limit);
		}
		
		if (result != null && result instanceof EntityList) {
			lastTotalSize = ((EntityList) result).getTotalSize();
		}
		
		if (customComparator != null) {
			Collections.sort(result, customComparator);
		}
		
		return result.iterator();
	}

	/* (non-Javadoc)
	 * @see de.jwic.data.IContentProvider#getObjectFromKey(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public E getObjectFromKey(String uniqueKey) {
		if (uniqueKey == null || uniqueKey.isEmpty()) {
			return null;
		}
		return (E) dao.getEntity(Integer.parseInt(uniqueKey));
	}

	/* (non-Javadoc)
	 * @see de.jwic.data.IContentProvider#getTotalSize()
	 */
	@Override
	public int getTotalSize() {
		return lastTotalSize;
	}

	/* (non-Javadoc)
	 * @see de.jwic.data.IContentProvider#getUniqueKey(java.lang.Object)
	 */
	@Override
	public String getUniqueKey(E object) {
		return Integer.toString(object.getId());
	}

	/* (non-Javadoc)
	 * @see de.jwic.data.IContentProvider#hasChildren(java.lang.Object)
	 */
	@Override
	public boolean hasChildren(E object) {
		return false;
	}

	/**
	 * @return the query
	 */
	public PropertyQuery getQuery() {
		return query;
	}

	/**
	 * @param query the query to set
	 */
	public void setQuery(PropertyQuery query) {
		this.query = query;
	}

	/**
	 * @return the customComparator
	 */
	public Comparator<E> getCustomComparator() {
		return customComparator;
	}

	/**
	 * @param customComparator the customComparator to set
	 */
	public void setCustomComparator(Comparator<E> customComparator) {
		this.customComparator = customComparator;
	}

}
