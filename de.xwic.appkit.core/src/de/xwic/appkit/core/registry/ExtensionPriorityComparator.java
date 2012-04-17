/**
 * 
 */
package de.xwic.appkit.core.registry;

import java.util.Comparator;

/**
 * @author Adrian Ionescu
 */
public class ExtensionPriorityComparator implements Comparator<IExtension> {

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(IExtension o1, IExtension o2) {
		int p1 = o1.getPriority();
		int p2 = o2.getPriority();
		
		//sorting DESC		
		return p1 > p2 ? -1 : p1 == p2 ? 0 : 1;
	}

}
