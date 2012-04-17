/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.pol.isis.web.history.wizard.HistoryCompareTableContentProvider
 * Created on Dec 17, 2008 by Aron Cotrau
 *
 */
package de.xwic.appkit.webbase.history.wizard;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.jwic.ecolib.tableviewer.IContentProvider;
import de.jwic.ecolib.tableviewer.Range;
import de.xwic.appkit.webbase.history.HistorySelectionModel;
import de.xwic.appkit.webbase.history.PropertyVersionHelper;


/**
 * 
 *
 * @author Aron Cotrau
 */
public class HistoryCompareTableContentProvider implements IContentProvider {

	private HistorySelectionModel model = null;
	
	/**
	 * @param model
	 */
	public HistoryCompareTableContentProvider(HistorySelectionModel model) {
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
		try {
			List<PropertyVersionHelper> allProperties = model.generatePropertyVersionsList();
			return allProperties.iterator();
		} catch (Exception e) {
			return new ArrayList<PropertyVersionHelper>().iterator();
		}
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
		return Integer.toString(object.hashCode());
	}

	/* (non-Javadoc)
	 * @see de.jwic.ecolib.tableviewer.IContentProvider#hasChildren(java.lang.Object)
	 */
	public boolean hasChildren(Object object) {
		return false;
	}

}
