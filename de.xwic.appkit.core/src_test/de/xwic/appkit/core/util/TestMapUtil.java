/**
 *
 */
package de.xwic.appkit.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;


/**
 * @author Alexandru Bledea
 * @since Oct 17, 2014
 */
@SuppressWarnings("static-method")
public final class TestMapUtil {

	private static final int KEY_3 = 3;
	private static final int KEY_10 = 10;
	private static final int KEY_14 = 14;

	/**
	 *
	 */
	@Test
	public void testGenerateMap() {
		final List<Entry> entries = new ArrayList<Entry>();
		Collections.addAll(entries, new Entry(KEY_10, "0"), new Entry(KEY_10, ""), new Entry(KEY_14, "04"), new Entry(null, "0"),
				new Entry(KEY_10, null), null, new Entry(null, null), new Entry(KEY_3, ""), new Entry(KEY_10, "0"));

		final Map<Integer, Collection<String>> mapOfLists = MapUtil.generateMapOfList(entries, new IdExtractor(), new ValueExtractor());
		Assert.assertFalse("Map should not be empty", mapOfLists.isEmpty());
		Assert.assertEquals("Expecting exactly 3 entries to match the keys", 3, mapOfLists.size());
		Assert.assertEquals("Key 3 should only have one element", 1, mapOfLists.get(KEY_3).size());
		Assert.assertEquals("Key 10 should only have 3 elements", 3, mapOfLists.get(KEY_10).size());

		final Map<Integer, Collection<String>> mapOfSets = MapUtil.generateMapOfSet(entries, new IdExtractor(), new ValueExtractor());
		Assert.assertFalse("Map should not be empty", mapOfSets.isEmpty());
		Assert.assertEquals("Expecting exactly 3 entries to match the keys", 3, mapOfSets.size());
		Assert.assertEquals("Key 3 should only have one element", 1, mapOfSets.get(KEY_3).size());
		Assert.assertEquals("Key 10 should only have 2 elements", 2, mapOfSets.get(KEY_10).size());

		Assert.assertTrue("Should be empty", MapUtil.generateMapOfSet(null, null, null).isEmpty());
	}

	/**
	 * @author Alexandru Bledea
	 * @since Oct 17, 2014
	 */
	private static final class Entry {

		final Integer id;
		final String value;

		/**
		 * @param id
		 * @param value
		 */
		Entry(final Integer id, final String value) {
			this.id = id;
			this.value = value;
		}

	}

	private static final class ValueExtractor implements Function<Entry, String> {

		/*
		 * (non-Javadoc)
		 *
		 * @see de.xwic.appkit.core.util.Function#evaluate(java.lang.Object)
		 */
		@Override
		public String evaluate(final Entry obj) {
			return obj.value;
		}

	}
	/**
	 * @author Alexandru Bledea
	 * @since Oct 17, 2014
	 */
	private static final class IdExtractor implements Function<Entry, Integer> {

		/*
		 * (non-Javadoc)
		 *
		 * @see de.xwic.appkit.core.util.Function#evaluate(java.lang.Object)
		 */
		@Override
		public Integer evaluate(final Entry obj) {
			return obj.id;
		}

	}
}
