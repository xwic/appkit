/**
 * 
 */
package de.xwic.appkit.core.registry;

import java.util.Comparator;

/**
 * @author Adrian Ionescu
 */
public class ExtensionSortIndexComparator implements Comparator<IExtension> {

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(IExtension o1, IExtension o2) {
		int si1 = o1.getSortIndex();
		int si2 = o2.getSortIndex();
		
		// sorting ASC
		return si1 < si2 ? -1 : si1 == si2 ? 0 : 1;
	}

}
