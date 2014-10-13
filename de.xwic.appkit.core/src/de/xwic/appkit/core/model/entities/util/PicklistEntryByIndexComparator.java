/*
 * $Id: PicklistEntryByIndexComparator.java,v 1.1 2008/08/20 18:44:25 ronnyp Exp $
 *
 * Copyright (c) 2007 Network Appliance.
 * All rights reserved.

 * com.netapp.balanceit.tools.picklist.PicklistEntryByIndexComparator.java
 * Created on 26.02.2008
 * 
 * @author Ronny Pfretzschner
 */
package de.xwic.appkit.core.model.entities.util;

import java.util.Comparator;

import de.xwic.appkit.core.model.entities.IPicklistEntry;

/**
 * Index key comparator for picklistentries. Revert is possible, means from bit
 * to small values.
 * 
 * Created on 26.02.2008
 * 
 * @author Ronny Pfretzschner
 */
public class PicklistEntryByIndexComparator implements Comparator<IPicklistEntry> {

	private boolean revert = false;

	public PicklistEntryByIndexComparator(boolean revert) {
		this.revert = revert;
	}

	public PicklistEntryByIndexComparator() {
		this(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(IPicklistEntry o1, IPicklistEntry o2) {

		IPicklistEntry pick1 = (IPicklistEntry) o1;
		IPicklistEntry pick2 = (IPicklistEntry) o2;

		if (!revert) {
			return new Integer(pick1.getSortIndex()).compareTo(new Integer(pick2.getSortIndex()));
		} else {
			return new Integer(pick2.getSortIndex()).compareTo(new Integer(pick1.getSortIndex()));
		}
	}
}
