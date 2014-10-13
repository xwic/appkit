/**
 *
 */
package de.xwic.appkit.core.model.entities.util;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import de.xwic.appkit.core.model.entities.IPicklistEntry;
import de.xwic.appkit.core.util.CollectionUtil;
import de.xwic.appkit.core.util.Function;

/**
 * @author Alexandru Bledea
 * @since Oct 3, 2014
 */
public final class Picklists {

	public static final Function<IPicklistEntry, String> GET_KEY = new PicklistKeyExtractor();

	public static final Comparator<IPicklistEntry> COMPARE_INDEX = new PicklistEntryByIndexComparator();

	/**
	 *
	 */
	private Picklists() {
	}

	/**
	 * @param entry
	 * @return
	 */
	public static final String getKey(final IPicklistEntry entry) {
		return CollectionUtil.evaluate(entry, GET_KEY, null);
	}

	/**
	 * @param entries
	 * @return
	 */
	public static final List<String> getKeys(final Collection<IPicklistEntry> entries) {
		return CollectionUtil.createList(entries, GET_KEY);
	}

	/**
	 * @param entries
	 * @param key
	 * @return
	 */
	public static final boolean containsKey(final Collection<IPicklistEntry> entries, final String key) {
		return getKeys(entries).contains(key);
	}

	/**
	 * @author Alexandru Bledea
	 * @since Oct 3, 2014
	 */
	private static class PicklistKeyExtractor implements Function<IPicklistEntry, String> {

		/*
		 * (non-Javadoc)
		 *
		 * @see de.xwic.appkit.core.util.Function#evaluate(java.lang.Object)
		 */
		@Override
		public String evaluate(final IPicklistEntry obj) {
			return obj.getKey();
		}

	}

}
