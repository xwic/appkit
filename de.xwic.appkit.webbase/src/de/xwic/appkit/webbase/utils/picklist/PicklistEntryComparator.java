/*
 * $Id: PicklistEntryComparator.java,v 1.1 2008/08/20 18:44:25 ronnyp Exp $
 *
 * Copyright (c) 2007 Network Appliance.
 * All rights reserved.

 * com.netapp.balanceit.tools.PicklistEntryComparer.java
 * Created on 19.02.2008
 * 
 * @author Ronny Pfretzschner
 */
package de.xwic.appkit.webbase.utils.picklist;

import java.util.Comparator;

import de.xwic.appkit.core.model.entities.IPicklistEntry;

/**
 * Comparator for picklistentries.
 * 
 * Created on 19.02.2008
 * 
 * @author Ronny Pfretzschner
 */
public class PicklistEntryComparator implements Comparator<IPicklistEntry> {

	/**
	 * 
	 */
	private final String lang;

	/**
	 * @param combo
	 */
	public PicklistEntryComparator(String lang) {
		this.lang = lang;
	}

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(IPicklistEntry o1, IPicklistEntry o2) {
		if (o1 == null && o2 == null) {
			return 0;
		}
		if (o1 == null) {
			return -1;
		}
		if (o2 == null) {
			return 1;
		}
		if (o1.getSortIndex() == o2.getSortIndex()) {
			if (lang == null) {
				if (o1.getKey() != null && o2.getKey() != null) {
					return o1.getKey().compareTo(o2.getKey());
				}
			} else {
				return o1.getBezeichnung(lang).compareTo(o2.getBezeichnung(lang));
			}
		} else {
			if (o1.getSortIndex() < o2.getSortIndex()) {
				return -1;
			}
			return 1;
		}
		return 0;
	}

}
