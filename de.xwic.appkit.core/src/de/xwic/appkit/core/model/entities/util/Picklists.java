/**
 *
 */
package de.xwic.appkit.core.model.entities.util;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import de.xwic.appkit.core.model.entities.IPicklistEntry;
import de.xwic.appkit.core.util.CollectionUtil;
import de.xwic.appkit.core.util.Function;

/**
 * @author Alexandru Bledea
 * @since Oct 3, 2014
 */
public final class Picklists {

	public static final Function<IPicklistEntry, String> GET_KEY = new PicklistKeyExtractor();
	public static final Function<IPicklistEntry, String> GET_TEXT_EN = new PicklistTextEnExtractor();

	public static final Comparator<IPicklistEntry> COMPARE_INDEX = new PicklistEntryByIndexComparator();

	/**
	 *
	 */
	private Picklists() {
	}

	public static String getTextEn(final IPicklistEntry entry) {
		return CollectionUtil.evaluate(entry, GET_TEXT_EN, "");
	}
	/**
	 * @param entry
	 * @return
	 */
	public static String getKey(final IPicklistEntry entry) {
		return CollectionUtil.evaluate(entry, GET_KEY, null);
	}

	/**
	 * @param entries
	 * @return
	 */
	public static List<String> getKeys(final Collection<IPicklistEntry> entries) {
		return CollectionUtil.createList(entries, GET_KEY);
	}

	/**
	 * @param entries
	 * @return
	 */
	public static List<String> getTextEn(final Collection<IPicklistEntry> entries) {
		return CollectionUtil.createList(entries, GET_TEXT_EN);
	}

	/**
	 * @param entry
	 * @param key
	 * @return
	 */
	public static boolean isPicklistKey(final IPicklistEntry entry, final String key) {
		if (null == entry) {
			return false;
		}
		final String entryKey = entry.getKey();
		if (null == key) {
			return null == entryKey;
		}
		return key.equals(entryKey);
	}

	/**
	 * @param entries
	 * @param key
	 * @return
	 */
	public static boolean containsKey(final Collection<IPicklistEntry> entries, final String key) {
		return getKeys(entries).contains(key);
	}

	/**
	 * @author Alexandru Bledea
	 * @since Oct 3, 2014
	 */
	private static class PicklistKeyExtractor implements Function<IPicklistEntry, String> {

		/* (non-Javadoc)
		 * @see de.xwic.appkit.core.util.Function#evaluate(java.lang.Object)
		 */
		@Override
		public String evaluate(final IPicklistEntry obj) {
			return obj.getKey();
		}

	}

	/**
	 * @author Alexandru Bledea
	 * @since Oct 13, 2014
	 */
	private static class PicklistTextEnExtractor implements Function<IPicklistEntry, String> {

		private final String english = Locale.ENGLISH.getLanguage();

		/* (non-Javadoc)
		 * @see de.xwic.appkit.core.util.Function#evaluate(java.lang.Object)
		 */
		@Override
		public String evaluate(final IPicklistEntry obj) {
			return obj.getBezeichnung(english);
		}


	}
}
