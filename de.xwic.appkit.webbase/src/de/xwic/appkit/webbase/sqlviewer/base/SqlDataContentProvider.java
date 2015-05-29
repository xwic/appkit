/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 * 
 * de.xwic.appkit.webbase.sqlviewer.base.SqlDataProvider 
 */

package de.xwic.appkit.webbase.sqlviewer.base;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.jwic.data.IContentProvider;
import de.jwic.data.Range;


/**
 * @author dotto
 * @date May 29, 2015
 * 
 */
public class SqlDataContentProvider implements IContentProvider  {

	private static Log log = LogFactory.getLog(SqlDataContentProvider.class);
	
	private ISqlDataProviderHandler handler;
	private List<Object[]> result;
	
	 
	/**
	 * 
	 */
	public SqlDataContentProvider(ISqlDataProviderHandler handler) {
		this.handler = handler;
	}
	
	/* (non-Javadoc)
	 * @see de.jwic.data.IContentProvider#getChildren(java.lang.Object)
	 */
	@Override
	public Iterator getChildren(Object arg0) {
		return null;
	}

	/* (non-Javadoc)
	 * @see de.jwic.data.IContentProvider#getContentIterator(de.jwic.data.Range)
	 */
	@Override
	public Iterator getContentIterator(Range arg0) {
		try {
			result = handler.getData(arg0);
		} catch (SQLException e) {
			log.error(e.getMessage(),e);
			throw new RuntimeException(e);
		}
		return result.iterator();
	}
	
	/* (non-Javadoc)
	 * @see de.jwic.data.IContentProvider#getObjectFromKey(java.lang.String)
	 */
	@Override
	public Object getObjectFromKey(String arg0) {
		return result.get(Integer.parseInt(arg0));
	}

	/* (non-Javadoc)
	 * @see de.jwic.data.IContentProvider#getTotalSize()
	 */
	@Override
	public int getTotalSize() {
		try {
			return handler.getTotal();
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see de.jwic.data.IContentProvider#getUniqueKey(java.lang.Object)
	 */
	@Override
	public String getUniqueKey(Object arg0) {
		return Integer.toString(result.indexOf(arg0));
	}

	/* (non-Javadoc)
	 * @see de.jwic.data.IContentProvider#hasChildren(java.lang.Object)
	 */
	@Override
	public boolean hasChildren(Object arg0) {
		return false;
	}

	
	/**
	 * @return the handler
	 */
	public ISqlDataProviderHandler getHandler() {
		return handler;
	}

	
	/**
	 * @param handler the handler to set
	 */
	public void setHandler(ISqlDataProviderHandler handler) {
		this.handler = handler;
	}
	

}
