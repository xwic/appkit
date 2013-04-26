/*
 * Copyright (c) 2009 Network Appliance, Inc.
 * All rights reserved.
 */

package de.xwic.appkit.webbase.trace.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.jwic.data.IContentProvider;
import de.jwic.data.Range;
import de.xwic.appkit.core.trace.ITraceContext;
import de.xwic.appkit.core.trace.ITraceDataManager;
import de.xwic.appkit.core.trace.Trace;

/**
 * @author lippisch
 */
public class TraceHistoryContentProvider implements IContentProvider<ITraceContext> {

	private int totalSize = 0;
	private int minDuration;
	
	/* (non-Javadoc)
	 * @see de.jwic.ecolib.tableviewer.IContentProvider#getChildren(java.lang.Object)
	 */
	@Override
	public Iterator<ITraceContext> getChildren(ITraceContext object) {
		return null;
	}

	/* (non-Javadoc)
	 * @see de.jwic.ecolib.tableviewer.IContentProvider#getContentIterator(de.jwic.ecolib.tableviewer.Range)
	 */
	@Override
	public Iterator<ITraceContext> getContentIterator(Range range) {
		
		List<ITraceContext> list = new ArrayList<ITraceContext>(range.getMax());
		
		ITraceDataManager dataManager = Trace.getDataManager();
		if (dataManager != null) {
			
			List<ITraceContext> traceHistory = dataManager.getTraceHistory();
			int idx = traceHistory.size() - 1 - range.getStart();
			if (idx > 0) {
				
				int cnt = 0;
				do {
					ITraceContext tx = traceHistory.get(idx);
					if (minDuration == 0 || (tx.getDuration() > minDuration)) {
						list.add(tx);
						cnt++;
					}
					idx--;
				} while (idx >= 0 && cnt < range.getMax());
				
			}
			totalSize = traceHistory.size();
		} 
		return list.iterator();
	}

	/* (non-Javadoc)
	 * @see de.jwic.ecolib.tableviewer.IContentProvider#getTotalSize()
	 */
	@Override
	public int getTotalSize() {
		return totalSize;
	}

	/* (non-Javadoc)
	 * @see de.jwic.ecolib.tableviewer.IContentProvider#getUniqueKey(java.lang.Object)
	 */
	@Override
	public String getUniqueKey(ITraceContext object) {
		return Long.toString(object.getStartTime()) + System.currentTimeMillis();
	}

	/* (non-Javadoc)
	 * @see de.jwic.ecolib.tableviewer.IContentProvider#hasChildren(java.lang.Object)
	 */
	@Override
	public boolean hasChildren(ITraceContext object) {
		return false;
	}

	/**
	 * @param parseInt
	 */
	public void setMinDuration(int minDuration) {
		this.minDuration = minDuration;
		
	}

	@Override
	public ITraceContext getObjectFromKey(String uniqueKey) {
		return null;
	}

	
	
}
