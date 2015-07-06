/*******************************************************************************
 * Copyright 2015 xWic group (http://www.xwic.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *  
 *******************************************************************************/
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
	public static final Comparator<IPicklistEntry> COMPARE_TEXT_EN = new PicklistByTextEnComparator();

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
	 * @since Oct 21, 2014
	 */
	private static class PicklistByTextEnComparator extends NotNullStringComparator<IPicklistEntry> {

		/*
		 * (non-Javadoc)
		 *
		 * @see de.xwic.appkit.core.model.entities.util.NotNullComparator#extractNotNull(java.lang.Object)
		 */
		@Override
		protected String extractNotNull(final IPicklistEntry possiblyNull) {
			return getTextEn(possiblyNull);
		}

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
