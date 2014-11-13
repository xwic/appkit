/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 *
 * de.xwic.appkit.core.util.TestCollectionUtil
 */
package de.xwic.appkit.core.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;


/**
 * @author Alexandru Bledea
 * @since Nov 13, 2014
 */
@SuppressWarnings("static-method")
public final class TestCollectionUtil {

	private static final List<String> totallyRandomNames = Collections.unmodifiableList(Arrays.asList("Ileana", "Alexandra", "Andreia", "Maria", null, "", "Diana", "Miriam"));

	/**
	 *
	 */
	@Test
	public void testCreateSet() {
		final Set<Character> actual = CollectionUtil.createSet(totallyRandomNames, ExtractFirstLetter.INSTANCE);
		final List<Character> expected = Arrays.asList('I', 'A', 'M', 'D');
		Assert.assertEquals(new LinkedHashSet<Character>(expected), actual);
	}

	/**
	 *
	 */
	@Test
	public void testCreateList() {
		final List<Character> actual = CollectionUtil.createList(totallyRandomNames, ExtractFirstLetter.INSTANCE);
		final List<Character> expected = Arrays.asList('I', 'A', 'A', 'M', 'D', 'M');
		Assert.assertEquals(expected, actual);
	}

	/**
	 * @author Alexandru Bledea
	 * @since Nov 13, 2014
	 */
	private enum ExtractFirstLetter implements Function<String, Character> {
		INSTANCE;

		/*
		 * (non-Javadoc)
		 *
		 * @see de.xwic.appkit.core.util.Function#evaluate(java.lang.Object)
		 */
		@Override
		public Character evaluate(final String obj) {
			if (obj.isEmpty()) {
				return null;
			}
			return obj.charAt(0);
		}

	}

}
