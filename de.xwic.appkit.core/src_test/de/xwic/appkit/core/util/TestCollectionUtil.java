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

	private static final List<String> totallyRandomNames = Collections.unmodifiableList(Arrays.asList("Alexandra", "Andreia", "Maria", null, "", "Diana", "Miriam"));

	/**
	 *
	 */
	@Test
	public void testCreateSet() {
		final Set<Character> actual = CollectionUtil.createSet(totallyRandomNames, ExtractFirstLetter.INSTANCE);
		final List<Character> expected = Arrays.asList('A', 'M', 'D');
		Assert.assertEquals(new LinkedHashSet<Character>(expected), actual);
	}

	/**
	 *
	 */
	@Test
	public void testCreateList() {
		final List<Character> actual = CollectionUtil.createList(totallyRandomNames, ExtractFirstLetter.INSTANCE);
		final List<Character> expected = Arrays.asList('A', 'A', 'M', 'D', 'M');
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
